/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.server.model;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;

public class L1PetMatch {
	public static final int STATUS_NONE = 0;

	public static final int STATUS_READY1 = 1;

	public static final int STATUS_READY2 = 2;

	public static final int STATUS_PLAYING = 3;

	public static final int MAX_PET_MATCH = 1;

	private static final short[] PET_MATCH_MAPID =
	{ 5125, 5131, 5132, 5133, 5134 };

	private String[] _pc1Name = new String[MAX_PET_MATCH];

	private String[] _pc2Name = new String[MAX_PET_MATCH];

	private L1PetInstance[] _pet1 = new L1PetInstance[MAX_PET_MATCH];

	private L1PetInstance[] _pet2 = new L1PetInstance[MAX_PET_MATCH];

	private static L1PetMatch _instance;

	public static L1PetMatch getInstance() {
		if (_instance == null) {
			_instance = new L1PetMatch();
		}
		return _instance;
	}

	public int setPetMatchPc(int petMatchNo, L1PcInstance pc, L1PetInstance pet) {
		int status = getPetMatchStatus(petMatchNo);
		if (status == STATUS_NONE) {
			_pc1Name[petMatchNo] = pc.getName();
			_pet1[petMatchNo] = pet;
			return STATUS_READY1;
		}
		else if (status == STATUS_READY1) {
			_pc2Name[petMatchNo] = pc.getName();
			_pet2[petMatchNo] = pet;
			return STATUS_PLAYING;
		}
		else if (status == STATUS_READY2) {
			_pc1Name[petMatchNo] = pc.getName();
			_pet1[petMatchNo] = pet;
			return STATUS_PLAYING;
		}
		return STATUS_NONE;
	}

	private synchronized int getPetMatchStatus(int petMatchNo) {
		L1PcInstance pc1 = null;
		if (_pc1Name[petMatchNo] != null) {
			pc1 = L1World.getInstance().getPlayer(_pc1Name[petMatchNo]);
		}
		L1PcInstance pc2 = null;
		if (_pc2Name[petMatchNo] != null) {
			pc2 = L1World.getInstance().getPlayer(_pc2Name[petMatchNo]);
		}

		if ((pc1 == null) && (pc2 == null)) {
			return STATUS_NONE;
		}
		if ((pc1 == null) && (pc2 != null)) {
			if (pc2.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
				return STATUS_READY2;
			}
			else {
				_pc2Name[petMatchNo] = null;
				_pet2[petMatchNo] = null;
				return STATUS_NONE;
			}
		}
		if ((pc1 != null) && (pc2 == null)) {
			if (pc1.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
				return STATUS_READY1;
			}
			else {
				_pc1Name[petMatchNo] = null;
				_pet1[petMatchNo] = null;
				return STATUS_NONE;
			}
		}

		// PCが試合場に2人いる場合
		if ((pc1.getMapId() == PET_MATCH_MAPID[petMatchNo]) && (pc2.getMapId() == PET_MATCH_MAPID[petMatchNo])) {
			return STATUS_PLAYING;
		}

		// PCが試合場に1人いる場合
		if (pc1.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
			_pc2Name[petMatchNo] = null;
			_pet2[petMatchNo] = null;
			return STATUS_READY1;
		}
		if (pc2.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
			_pc1Name[petMatchNo] = null;
			_pet1[petMatchNo] = null;
			return STATUS_READY2;
		}
		return STATUS_NONE;
	}

	private int decidePetMatchNo() {
		// 相手が待機中の試合を探す
		for (int i = 0; i < MAX_PET_MATCH; i++) {
			int status = getPetMatchStatus(i);
			if ((status == STATUS_READY1) || (status == STATUS_READY2)) {
				return i;
			}
		}
		// 待機中の試合がなければ空いている試合を探す
		for (int i = 0; i < MAX_PET_MATCH; i++) {
			int status = getPetMatchStatus(i);
			if (status == STATUS_NONE) {
				return i;
			}
		}
		return -1;
	}

	public synchronized boolean enterPetMatch(L1PcInstance pc, int amuletId) {
		int petMatchNo = decidePetMatchNo();
		if (petMatchNo == -1) {
			return false;
		}

		L1PetInstance pet = withdrawPet(pc, amuletId);
		L1Teleport.teleport(pc, 32799, 32868, PET_MATCH_MAPID[petMatchNo], 0, true);

		L1PetMatchReadyTimer timer = new L1PetMatchReadyTimer(petMatchNo, pc, pet);
		timer.begin();
		return true;
	}

	private L1PetInstance withdrawPet(L1PcInstance pc, int amuletId) {
		L1Pet l1pet = PetTable.getInstance().getTemplate(amuletId);
		if (l1pet == null) {
			return null;
		}
		L1Npc npcTemp = NpcTable.getInstance().getTemplate(l1pet.get_npcid());
		L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
		pet.setPetcost(6);
		return pet;
	}

	public void startPetMatch(int petMatchNo) {
		_pet1[petMatchNo].setCurrentPetStatus(1);
		_pet1[petMatchNo].setTarget(_pet2[petMatchNo]);

		_pet2[petMatchNo].setCurrentPetStatus(1);
		_pet2[petMatchNo].setTarget(_pet1[petMatchNo]);

		L1PetMatchTimer timer = new L1PetMatchTimer(_pet1[petMatchNo], _pet2[petMatchNo], petMatchNo);
		timer.begin();
	}

	public void endPetMatch(int petMatchNo, int winNo) {
		L1PcInstance pc1 = L1World.getInstance().getPlayer(_pc1Name[petMatchNo]);
		L1PcInstance pc2 = L1World.getInstance().getPlayer(_pc2Name[petMatchNo]);
		if (winNo == 1) {
			giveMedal(pc1, petMatchNo, true);
			giveMedal(pc2, petMatchNo, false);
		}
		else if (winNo == 2) {
			giveMedal(pc1, petMatchNo, false);
			giveMedal(pc2, petMatchNo, true);
		}
		else if (winNo == 3) { // 引き分け
			giveMedal(pc1, petMatchNo, false);
			giveMedal(pc2, petMatchNo, false);
		}
		qiutPetMatch(petMatchNo);
	}

	private void giveMedal(L1PcInstance pc, int petMatchNo, boolean isWin) {
		if (pc == null) {
			return;
		}
		if (pc.getMapId() != PET_MATCH_MAPID[petMatchNo]) {
			return;
		}
		if (isWin) {
			pc.sendPackets(new S_ServerMessage(1166, pc.getName())); // %0%sペットマッチで勝利を収めました。
			L1ItemInstance item = ItemTable.getInstance().createItem(41309);
			int count = 3;
			if (item != null) {
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					item.setCount(count);
					pc.getInventory().storeItem(item);
					pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0を手に入れました。
				}
			}
		}
		else {
			L1ItemInstance item = ItemTable.getInstance().createItem(41309);
			int count = 1;
			if (item != null) {
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					item.setCount(count);
					pc.getInventory().storeItem(item);
					pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0を手に入れました。
				}
			}
		}
	}

	private void qiutPetMatch(int petMatchNo) {
		L1PcInstance pc1 = L1World.getInstance().getPlayer(_pc1Name[petMatchNo]);
		if ((pc1 != null) && (pc1.getMapId() == PET_MATCH_MAPID[petMatchNo])) {
			for (Object object : pc1.getPetList().values().toArray()) {
				if (object instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) object;
					pet.dropItem();
					pc1.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
			}
			L1Teleport.teleport(pc1, 32630, 32744, (short) 4, 4, true);
		}
		_pc1Name[petMatchNo] = null;
		_pet1[petMatchNo] = null;

		L1PcInstance pc2 = L1World.getInstance().getPlayer(_pc2Name[petMatchNo]);
		if ((pc2 != null) && (pc2.getMapId() == PET_MATCH_MAPID[petMatchNo])) {
			for (Object object : pc2.getPetList().values().toArray()) {
				if (object instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) object;
					pet.dropItem();
					pc2.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
			}
			L1Teleport.teleport(pc2, 32630, 32744, (short) 4, 4, true);
		}
		_pc2Name[petMatchNo] = null;
		_pet2[petMatchNo] = null;
	}

	public class L1PetMatchReadyTimer extends TimerTask {
		private Logger _log = Logger.getLogger(L1PetMatchReadyTimer.class.getName());

		private final int _petMatchNo;

		private final L1PcInstance _pc;

		private final L1PetInstance _pet;

		public L1PetMatchReadyTimer(int petMatchNo, L1PcInstance pc, L1PetInstance pet) {
			_petMatchNo = petMatchNo;
			_pc = pc;
			_pet = pet;
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 3000);
		}

		@Override
		public void run() {
			try {
				for (;;) {
					Thread.sleep(1000);
					if ((_pc == null) || (_pet == null)) {
						cancel();
						return;
					}

					if (_pc.isTeleport()) {
						continue;
					}
					if (L1PetMatch.getInstance().setPetMatchPc(_petMatchNo, _pc, _pet) == L1PetMatch.STATUS_PLAYING) {
						L1PetMatch.getInstance().startPetMatch(_petMatchNo);
					}
					cancel();
					return;
				}
			}
			catch (Throwable e) {
				_log.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
		}

	}

	public class L1PetMatchTimer extends TimerTask {
		private Logger _log = Logger.getLogger(L1PetMatchTimer.class.getName());

		private final L1PetInstance _pet1;

		private final L1PetInstance _pet2;

		private final int _petMatchNo;

		private int _counter = 0;

		public L1PetMatchTimer(L1PetInstance pet1, L1PetInstance pet2, int petMatchNo) {
			_pet1 = pet1;
			_pet2 = pet2;
			_petMatchNo = petMatchNo;
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 0);
		}

		@Override
		public void run() {
			try {
				for (;;) {
					Thread.sleep(3000);
					_counter++;
					if ((_pet1 == null) || (_pet2 == null)) {
						cancel();
						return;
					}

					if (_pet1.isDead() || _pet2.isDead()) {
						int winner = 0;
						if (!_pet1.isDead() && _pet2.isDead()) {
							winner = 1;
						}
						else if (_pet1.isDead() && !_pet2.isDead()) {
							winner = 2;
						}
						else {
							winner = 3;
						}
						L1PetMatch.getInstance().endPetMatch(_petMatchNo, winner);
						cancel();
						return;
					}

					if (_counter == 100) { // 5分経っても終わらない場合は引き分け
						L1PetMatch.getInstance().endPetMatch(_petMatchNo, 3);
						cancel();
						return;
					}
				}
			}
			catch (Throwable e) {
				_log.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
		}

	}

}
