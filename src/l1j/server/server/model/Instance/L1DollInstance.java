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
import l1j.server.server.templates.L1MagicDoll;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.Random;

public class L1DollInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public static final int DOLL_TIME = 1800000;
	private int _itemId;
	private int _itemObjId;
	private int run;
	private boolean _isDelete = false;

	// ターゲットがいない場合の処理
	@Override
	public boolean noTarget() {
		if ((_master != null) && !_master.isDead()
				&& (_master.getMapId() == getMapId())) {
			if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
				int dir = moveDirection(_master.getX(), _master.getY());
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			} else {
				// 魔法娃娃 - 特殊動作
				dollAction();
			}
		} else {
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

	public L1DollInstance(L1Npc template, L1PcInstance master, int itemId,
			int itemObjId) {
		super(template);
		setId(IdFactory.getInstance().nextId());

		setItemId(itemId);
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
		if (L1MagicDoll.isHpRegeneration(_master)) {
			master.startHpRegenerationByDoll();
		}
		if (L1MagicDoll.isMpRegeneration(_master)) {
			master.startMpRegenerationByDoll();
		}
		if (L1MagicDoll.isItemMake(_master)) {
			master.startItemMakeByDoll();
		}
	}

	public void deleteDoll() {
		broadcastPacket(new S_SkillSound(getId(), 5936));
		if (_master != null && _isDelete) {
			L1PcInstance pc = (L1PcInstance) _master;
			pc.sendPackets(new S_SkillIconGFX(56, 0));
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
		if (L1MagicDoll.isHpRegeneration(_master)) {
			((L1PcInstance) _master).stopHpRegenerationByDoll();
		}
		if (L1MagicDoll.isMpRegeneration(_master)) {
			((L1PcInstance) _master).stopMpRegenerationByDoll();
		}
		if (L1MagicDoll.isItemMake(_master)) {
			((L1PcInstance) _master).stopItemMakeByDoll();
		}
		_master.getDollList().remove(getId());
		deleteMe();
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		// 判斷旅館內是否使用相同鑰匙
		if (perceivedFrom.getMapId() >= 16384 && perceivedFrom.getMapId() <= 25088 // 旅館內判斷
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

	public int getItemObjId() {
		return _itemObjId;
	}

	public void setItemObjId(int i) {
		_itemObjId = i;
	}

	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int i) {
		_itemId = i;
	}

	// 表情動作
	private void dollAction() {
		run = Random.nextInt(100) + 1;
		if (run <= 10) {
			int actionCode = ActionCodes.ACTION_Aggress; // 67
			if (run <= 5) 
				actionCode = ActionCodes.ACTION_Think; // 66

			broadcastPacket(new S_DoActionGFX(getId(), actionCode));
			setSleepTime(calcSleepTime(SprTable.getInstance().getSprSpeed(getTempCharGfx(),
							actionCode), MOVE_SPEED)); //
		}
	}
}
