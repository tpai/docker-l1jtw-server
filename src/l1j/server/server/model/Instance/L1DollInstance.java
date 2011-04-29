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

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.SprTable;
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

	public static final int DOLLTYPE_BUGBEAR = 0;

	public static final int DOLLTYPE_SUCCUBUS = 1;

	public static final int DOLLTYPE_WAREWOLF = 2;

	public static final int DOLLTYPE_ELDER = 3;

	public static final int DOLLTYPE_CRUSTANCEAN = 4;

	public static final int DOLLTYPE_GOLEM = 5;

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
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_DollPack(this, perceivedFrom));
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

	public int getDamageByDoll() {
		int damage = 0;
		int dollType = getDollType();
		if ((dollType == DOLLTYPE_WAREWOLF) || (dollType == DOLLTYPE_CRUSTANCEAN)) {
			int chance = Random.nextInt(100) + 1;
			if (chance <= 3) {
				damage = 15;
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPackets(new S_SkillSound(_master.getId(), 6319));
				}
				_master.broadcastPacket(new S_SkillSound(_master.getId(), 6319));
			}
		}
		return damage;
	}

	public boolean isMpRegeneration() {
		boolean isMpRegeneration = false;
		int dollType = getDollType();
		if ((dollType == DOLLTYPE_SUCCUBUS) || (dollType == DOLLTYPE_ELDER)) {
			isMpRegeneration = true;
		}
		return isMpRegeneration;
	}

	public int getWeightReductionByDoll() {
		int weightReduction = 0;
		int dollType = getDollType();
		if (dollType == DOLLTYPE_BUGBEAR) {
			weightReduction = 20;
		}
		return weightReduction;
	}

	public int getDamageReductionByDoll() {
		int damageReduction = 0;
		int dollType = getDollType();
		if (dollType == DOLLTYPE_GOLEM) {
			int chance = Random.nextInt(100) + 1;
			if (chance <= 4) {
				damageReduction = 15;
			}
		}
		return damageReduction;
	}

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
