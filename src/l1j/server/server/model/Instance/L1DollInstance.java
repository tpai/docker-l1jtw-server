/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.server.server.model.Instance;

import java.util.Map;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DollPack;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.Random;

public class L1DollInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public static final int DOLLTYPE_BUGBEAR = 0; // 魔法娃娃：肥肥
	public static final int DOLLTYPE_SUCCUBUS = 1; // 魔法娃娃：小思克巴
	public static final int DOLLTYPE_WAREWOLF = 2; // 魔法娃娃：野狼寶寶
	public static final int DOLLTYPE_ELDER = 3; // 魔法娃娃：長老
	public static final int DOLLTYPE_CRUSTANCEAN = 4; // 魔法娃娃：奎斯坦修
	public static final int DOLLTYPE_GOLEM = 5; // 魔法娃娃：石頭高崙
	public static final int DOLLTYPE_SEADANCER = 6; // 魔法娃娃：希爾黛斯
	public static final int DOLLTYPE_SERPENTWOMAN = 7; // 魔法娃娃：蛇女
	public static final int DOLLTYPE_SNOWMAN = 8; // 魔法娃娃：雪怪
	public static final int DOLLTYPE_COCKATRICE = 9; // 魔法娃娃：亞力安
	public static final int DOLLTYPE_SPARTOI = 10; // 魔法娃娃：史巴托
	public static final int DOLLTYPE_AZURU_HACHIRIN = 11; // 神秘稜鏡：淘氣幼龍
	public static final int DOLLTYPE_CRIMSON_HACHIRIN = 12; // 神秘稜鏡：頑皮幼龍
	public static final int DOLLTYPE_MALE_HI_HACHIRIN = 13; // 神秘稜鏡：高等淘氣幼龍
	public static final int DOLLTYPE_FEMALE_HI_HACHIRIN = 14; // 神秘稜鏡：高等頑皮幼龍

	public static final int DOLL_TIME = 1800000;

	private int _dollType;

	private int _itemObjId;

	private  int run;

	private boolean _isDelete = false;

	// ターゲットがいない場合の処理
	@Override
	public boolean noTarget() {
		if (_master.isDead()) {
			_isDelete = true;
			deleteDoll();
			return true;
		}
		else if ((_master != null) && (_master.getMapId() == getMapId())) {
			if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
				int dir = moveDirection(_master.getX(), _master.getY());
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			} else {
				// 魔法娃娃 - 特殊動作
				dollAction();
			}
		}
		else {
			_isDelete = true;
			deleteDoll();
			return true;
		}
		return false;
	}

	// 時間計測用
	class DollTimer implements Runnable {
		@Override
		public void run() {
			if (_destroyed) { // 既に破棄されていないかチェック
				return;
			}
			deleteDoll();
		}
	}

	public L1DollInstance(L1Npc template, L1PcInstance master, int dollType, int itemObjId) {
		super(template);
		setId(IdFactory.getInstance().nextId());

		setDollType(dollType);
		setItemObjId(itemObjId);
		GeneralThreadPool.getInstance().schedule(new DollTimer(), DOLL_TIME);

		setMaster(master);
		setX(master.getX() + Random.nextInt(5) - 2);
		setY(master.getY() + Random.nextInt(5) - 2);
		setMap(master.getMapId());
		setHeading(5);
		setLightSize(template.getLightSize());
		setMoveSpeed(1);
		setBraveSpeed(1);

		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.addDoll(this);
		if (!isAiRunning()) {
			startAI();
		}
		if (isMpRegeneration()) {
			master.startMpRegenerationByDoll();
		}
	}

	public void deleteDoll() {
		broadcastPacket(new S_SkillSound(getId(), 5936));
		if (_master != null && _isDelete) {
			L1PcInstance pc = (L1PcInstance) _master;
			pc.sendPackets(new S_SkillIconGFX(56, 0));
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
		if (isMpRegeneration()) {
			((L1PcInstance) _master).stopMpRegenerationByDoll();
		}
		_master.getDollList().remove(getId());
		deleteMe();
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		// 判斷旅館內是否使用相同鑰匙
		if (perceivedFrom.getMapId() > 10000
				&& perceivedFrom.getInnKeyId() != _master.getInnKeyId()) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_DollPack(this));
	}

	@Override
	public void onItemUse() {
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
	}

	public int getDollType() {
		return _dollType;
	}

	public void setDollType(int i) {
		_dollType = i;
	}

	public int getItemObjId() {
		return _itemObjId;
	}

	public void setItemObjId(int i) {
		_itemObjId = i;
	}

	public int getDamageByDoll() { // TODO 增加傷害
		int damage = 0;
		if (getDollType() == DOLLTYPE_WAREWOLF
				|| getDollType() == DOLLTYPE_CRUSTANCEAN) {
			int chance = Random.nextInt(100) + 1;
			if (chance <= 3) {
				damage = 15;
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPackets(new S_SkillSound(_master.getId(), 6319));
				}
				_master
						.broadcastPacket(new S_SkillSound(_master.getId(), 6319));
			}
		}
		return damage;
	}

	public boolean isMpRegeneration() { // TODO 回魔
		boolean isMpRegeneration = false;
		if (getDollType() == DOLLTYPE_SUCCUBUS
				|| getDollType() == DOLLTYPE_ELDER) {
			isMpRegeneration = true;
		}
		return isMpRegeneration;
	}

	// 魔力回復量計算
	public static int getMpByDoll(L1Character _master) {
		int s = 0;
		s += getTypeCountByDoll(_master.getDollList(), DOLLTYPE_SUCCUBUS)
				* 15
				+ getTypeCountByDoll(_master.getDollList(), DOLLTYPE_ELDER)
				* 15;
		return s;
	}

	public boolean isHpRegeneration() { // TODO 回血
		boolean isHpRegeneration = false;
		if (getDollType() == DOLLTYPE_SEADANCER) {
			isHpRegeneration = true;
		}
		return isHpRegeneration;
	}

	public boolean isItemMake() { // TODO ItemMake_type
		boolean isItemMake = false;
		if (getDollType() == DOLLTYPE_AZURU_HACHIRIN
				|| getDollType() == DOLLTYPE_CRIMSON_HACHIRIN
				|| getDollType() == DOLLTYPE_MALE_HI_HACHIRIN
				|| getDollType() == DOLLTYPE_FEMALE_HI_HACHIRIN) {
			isItemMake = true;
		}
		return isItemMake;
	}

	public int getDamageReductionByDoll() { // TODO 傷害減少
		int damageReduction = 0;
		if (getDollType() == DOLLTYPE_GOLEM) {
			int chance = Random.nextInt(100) + 1;
			if (chance <= 4) {
				damageReduction = 15;
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPackets(new S_SkillSound(_master.getId(), 6320));
				}
				_master
						.broadcastPacket(new S_SkillSound(_master.getId(), 6320));
			}
		}
		return damageReduction;
	}

	public int getDamageEvasionByDoll() { // TODO 攻擊迴避
		int damageEvasion = 0;
		if (getDollType() == DOLLTYPE_SPARTOI) {
			int chance = Random.nextInt(100) + 1;
			if (chance <= 4) {
				damageEvasion = 1;
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPackets(new S_SkillSound(_master.getId(), 6320)); // TODO
				}
				_master
						.broadcastPacket(new S_SkillSound(_master.getId(), 6320));
			}
		}
		return damageEvasion;
	}

	public int getPoisonByDoll() { // TODO 中毒
		int damagePoison = 0;
		if (getDollType() == DOLLTYPE_SERPENTWOMAN) {
			int chance = Random.nextInt(100) + 1;
			if (chance <= 10) {
				damagePoison = 1;
			}
		}
		return damagePoison;
	}

	private static int getTypeCountByDoll(Map<Integer, L1DollInstance> dolls,
			int type) {
		int s = 0;
		for (Object obj : dolls.values().toArray()) {
			if (((L1DollInstance) obj).getDollType() == type) {
				s++;
			}
		}
		return s;
	}

	// 弓的命中增加
	public static int getBowHitAddByDoll(L1Character _master) {
		int s = 0;
		s += getTypeCountByDoll(_master.getDollList(), DOLLTYPE_COCKATRICE);
		return s;
	}

	// 弓的攻擊力增加
	public static int getBowDamageByDoll(L1Character _master) {
		int s = 0;
		s += getTypeCountByDoll(_master.getDollList(), DOLLTYPE_COCKATRICE);
		return s;
	}

	// 防禦增加
	public static int getAcByDoll(L1Character _master) {
		int s = 0;
		s -= ((getTypeCountByDoll(_master.getDollList(), DOLLTYPE_SNOWMAN)) * 3);
		return s;
	}

	// TODO 寒冰耐性增加
	public static int getRegistFreezeByDoll(L1PcInstance _master) {
		int s = 0;
		s += ((getTypeCountByDoll(_master.getDollList(), DOLLTYPE_SNOWMAN)) * 7);
		return s;
	}

	// TODO 魔力回復量增加
	public static int getMprByDoll(L1Character _master) {
		int s = 0;
		s += (getTypeCountByDoll(_master.getDollList(),
				DOLLTYPE_CRIMSON_HACHIRIN)
				* 10
				+ (getTypeCountByDoll(_master.getDollList(),
						DOLLTYPE_MALE_HI_HACHIRIN) + getTypeCountByDoll(_master
						.getDollList(), DOLLTYPE_FEMALE_HI_HACHIRIN))
				* 5
				+ getTypeCountByDoll(_master.getDollList(),
						DOLLTYPE_SERPENTWOMAN) * 4);
		return s;
	}

	// TODO 體力回復量增加
	public static int getHprByDoll(L1Character _master) {
		int s = 0;
		s += getTypeCountByDoll(_master.getDollList(), DOLLTYPE_AZURU_HACHIRIN) * 20;
		return s;
	}

	// TODO 負重減輕
	public static int getWeightReductionByDoll(L1Character _master) {
		int s = 0;
		s += (getTypeCountByDoll(_master.getDollList(), DOLLTYPE_BUGBEAR)
				+ getTypeCountByDoll(_master.getDollList(),
						DOLLTYPE_MALE_HI_HACHIRIN) + getTypeCountByDoll(_master
				.getDollList(), DOLLTYPE_FEMALE_HI_HACHIRIN))
				* 10
				+ (getTypeCountByDoll(_master.getDollList(),
						DOLLTYPE_CRIMSON_HACHIRIN) + getTypeCountByDoll(_master
						.getDollList(), DOLLTYPE_AZURU_HACHIRIN)) * 15;
		return s;
	}

	// 表情動作
	private void dollAction() {
		run = Random.nextInt(100) + 1;
		if (run <= 10) {
			if (run <= 5) {
				broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Think));
				setSleepTime(calcSleepTime(SprTable.getInstance()
						.getSprSpeed(getTempCharGfx(), ActionCodes.ACTION_Think), MOVE_SPEED)); // 66
			} else {
				broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Aggress));
				setSleepTime(calcSleepTime(SprTable.getInstance()
						.getSprSpeed(getTempCharGfx(), ActionCodes.ACTION_Aggress), MOVE_SPEED)); // 67
			}
		}
	}
}
