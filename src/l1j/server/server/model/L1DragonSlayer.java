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
package l1j.server.server.model;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_OtherCharPacks;
import l1j.server.server.serverpackets.S_OwnCharPack;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.utils.Random;

public class L1DragonSlayer {
	private static Logger _log = Logger.getLogger(L1DragonSlayer.class.getName());

	private static L1DragonSlayer _instance;

	public static L1DragonSlayer getInstance() {
		if (_instance == null) {
			_instance = new L1DragonSlayer();
		}
		return _instance;
	}

// 屠龍副本內 8人小副本
	// 判斷是否已開啟
	private boolean[][] _extraQuest = new boolean[12][4]; // 12個龍門，1個龍門4個8人小副本

	public boolean[][] checkExtraQuest() {
		return _extraQuest;
	}

	public void setExtraQuest(int portalNum, int number, boolean i) {
		_extraQuest[portalNum][number] = i;
	}

	// 判斷參加人數
	private int[][] _extraQuestPlayer =  new int[12][4];

	public int[][] getExtraQuestPlayer() {
		return _extraQuestPlayer;
	}

	public void addExtraQuestPlayer(int portalNum, int num, int i) {
		_extraQuestPlayer[portalNum][num] = _extraQuestPlayer[portalNum][num] + i;
		if (_extraQuestPlayer[portalNum][num] > 8) {
			_extraQuestPlayer[portalNum][num] = 8;
		} else if (_extraQuestPlayer[portalNum][num] < 0) {
			_extraQuestPlayer[portalNum][num] = 0;
		}
		if (!checkExtraQuest()[portalNum][num]) {
			spawnGuard timer = new spawnGuard(portalNum, num);
			timer.begin();
		}
	}

	// X秒後於守門者出現
	public class spawnGuard extends TimerTask {

		private final int _portalNum;
		private final int _num;

		public spawnGuard(int portalNum, int num) {
			_portalNum = portalNum;
			_num = num;
		}

		@Override
		public void run() {
			// 準備召喚守門者
			if (!checkExtraQuest()[_portalNum][_num]) {
				short mapId = (short) (1005 + _portalNum);
				int[] data = null;
				setExtraQuest(_portalNum, _num, true);
				if (_portalNum >= 0 && _portalNum <= 5) { // 地龍副本
					switch (_num) {
						case 0:
							data = new int[] {97011, 32642, 32799, 97012, 32649, 32850, 97013, 32642, 32922};
							break;
						case 1:
							data = new int[] {97011, 32771, 32799, 97012, 32776, 32850, 97013, 32770, 32922};
							break;
						case 2:
							data = new int[] {97011, 32899, 32799, 97012, 32904, 32850, 97013, 32899, 32922};
							break;
						case 3:
							data = new int[] {97011, 32898, 32607, 97012, 32904, 32658, 97013, 32898, 32730};
							break;
					}
				} else if (_portalNum >= 6 && _portalNum <= 11) { // 水龍副本
					// 修改中
				}
				spawn(data[0], _num, data[1], data[2], mapId, 0, 0); // 守門者1
				spawn(data[3], _num, data[4], data[5], mapId, 0, 0); // 守門者2
				spawn(data[6], _num, data[7], data[8], mapId, 0, 0); // 守門者3
			}
			cancel();
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 180000); // 延遲時間
		}
	}
// 屠龍副本內 8人小副本 end

// 屠龍副本
	// 判斷龍之門扉是否開啟 ,最多12個龍門
	private boolean[] _portalNumber = new boolean[12];

	public boolean[] getPortalNumber() {
		return _portalNumber;
	}

	public void setPortalNumber(int number, boolean i) {
		_portalNumber[number] = i;
	}

	// 判斷龍之鑰匙顯示可開啟的龍門
	private boolean[] _checkDragonPortal = new boolean[4];

	public boolean[] checkDragonPortal() {
		_checkDragonPortal[0] = false; // 安塔瑞斯
		_checkDragonPortal[1] = false; // 法利昂
		_checkDragonPortal[2] = false; // 林德拜爾
		_checkDragonPortal[3] = false; // 巴拉卡斯

		for (int i = 0; i < 12; i++) {
			if (!getPortalNumber()[i]) {
				if (i < 6) { // 前6個安塔瑞斯
					_checkDragonPortal[0] = true;
				} else { // 後6個法利昂
					_checkDragonPortal[1] = true;
				}
			}
		}
		return _checkDragonPortal;
	}

	// 判斷龍是否準備召喚
	private boolean[] _readySpawnDragon = new boolean[12];

	public boolean[] isReadySpawnDragon() {
		return _readySpawnDragon;
	}

	public void setReadySpawnDragon(int number, boolean i) {
		_readySpawnDragon[number] = i;
	}

	// 判斷龍是否已召喚
	private boolean[] _isSpawnDragon = new boolean[12];

	public boolean[] isSpawnDragon() {
		return _isSpawnDragon;
	}

	public void setSpawnDragon(int number, boolean i) {
		_isSpawnDragon[number] = i;
	}

	// 判斷是否要進行召喚第二、三階段的龍
	private boolean[] _nextSpawnDragon = new boolean[12];

	public boolean[] startNextSpawnDragon() {
		return _nextSpawnDragon;
	}

	public void setNextSpawnDragon(int number, boolean i) {
		_nextSpawnDragon[number] = i;
	}

	// 判斷隱匿的巨龍谷入口是否已出現
	private byte _hiddenDragonValley = 0;

	public byte checkHiddenDragonValley() {
		return _hiddenDragonValley;
	}

	public void setHiddenDragonValley(byte i) {
		_hiddenDragonValley = i;
	}

	// 龍之門扉物件
	private L1NpcInstance[] _portal = new L1NpcInstance[12];

	public L1NpcInstance[] portalPack() {
		return _portal;
	}

	public void setPortalPack(int number, L1NpcInstance portal) {
		_portal[number] = portal;
	}

	// 龍門持續時間結束
	public void endDragonPortal(int portalNumber) {
		if (portalNumber != -1) {
			endDragonSlayerTimer timer = new endDragonSlayerTimer(portalNumber);
			timer.begin();
		}
	}

	// 屠龍任務完成
	public void endDragonSlayer(int portalNumber) {
		if (portalNumber != -1) {
			if (checkHiddenDragonValley() == 0) { // 判斷隱匿的巨龍谷入口是否已開啟
				setHiddenDragonValley((byte) 1);
			}
			endDragonSlayerTimer timer = new endDragonSlayerTimer(portalNumber);
			timer.begin();
		}
	}

	// 屠龍結束 X秒後傳送玩家出地圖
	public class endDragonSlayerTimer extends TimerTask {

		private final int _portalNum;

		public endDragonSlayerTimer (int portalNum) {
			_portalNum = portalNum;
		}

		@Override
		public void run() {
			// 刪除龍之門扉
			if (portalPack()[_portalNum] != null) {
				portalPack()[_portalNum].setStatus(ActionCodes.ACTION_Die);
				portalPack()[_portalNum].broadcastPacket(new S_DoActionGFX(portalPack()[_portalNum].getId(), ActionCodes.ACTION_Die));
				portalPack()[_portalNum].deleteMe();
			}
			// 龍之門扉重置
			resetDragonSlayer(_portalNum);
			setPortalPack(_portalNum, null);
			// 召喚隱匿的巨龍谷入口
			if (checkHiddenDragonValley() == 1) {
				setHiddenDragonValley((byte) 2);
				spawn(81277, -1, 33726, 32506, (short) 4, 0, 86400000); // 24小時
			}
			nextCanStartTimer timer = new nextCanStartTimer(_portalNum);
			timer.begin();
			cancel();
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 15000); // 15秒後
		}
	}

	// 下次可重新開啟此編號的龍門計時器
	public class nextCanStartTimer extends TimerTask {

		private final int _portalNum;

		public nextCanStartTimer (int portalNum) {
			_portalNum = portalNum;
		}

		@Override
		public void run() {
			setPortalNumber(_portalNum, false);
			cancel();
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 300000); // 5分鐘後
		}
	}

	// 召喚龍 - 階段一、二、三
	public void spawnDragon(L1Character cha, int round) {
		int portalNumber = cha.getPortalNumber();
		int readyTime = 60000;
		int npcId = 97006; // 地龍 - 第一階段
		int X = 32786;
		int Y = 32689;
		if (portalNumber >= 0 && portalNumber <= 5) { // 地龍 - 安塔瑞斯
			if (round == 1) { // 第一階段
				readyTime =180000;
				setNextSpawnDragon(portalNumber, false);
			} else if (round == 2) { // 第二階段
				npcId = 97007;
				setNextSpawnDragon(portalNumber, true);
			} else { // 最終階段
				npcId = 97008;
				setNextSpawnDragon(portalNumber, true);
			}
		}
		else if (portalNumber >= 6 && portalNumber <= 11) { // 水龍 - 法利昂
			if (round == 1) { // 第一階段
				npcId = 97044;
				readyTime = 180000;
				setNextSpawnDragon(portalNumber, false);
			} else if (round == 2) { // 第二階段
				npcId = 97045;
				setNextSpawnDragon(portalNumber, true);
			} else { // 最終階段
				npcId = 97046;
				setNextSpawnDragon(portalNumber, true);
			}
			X = 32959;
			Y = 32836;
		}
		short MapId = (short) (1005 + portalNumber);

		// 確定是否未召喚
		if ((!isReadySpawnDragon()[portalNumber] && !isSpawnDragon()[portalNumber])
			|| startNextSpawnDragon()[portalNumber]) {
			setReadySpawnDragon(portalNumber, true); // 準備召喚
			spawnDragon _spawnDragon = new spawnDragon(npcId, portalNumber, X, Y, MapId, 10, readyTime);
			_spawnDragon.begin();
		}
	}

	// X秒後於龍之棲息地召喚龍
	public class spawnDragon extends TimerTask {

		private final int _npcid;
		private final int _portalNumber;
		private final int _X;
		private final int _Y;
		private final short _mapId;
		private final int _randomRange;
		private final int _readyTime;

		public spawnDragon(int npcId, int portalNumber, int X, int Y, short mapId, int randomRange, int readyTime) {
			_npcid = npcId;
			_portalNumber = portalNumber;
			_X =X;
			_Y =Y;
			_mapId = mapId;
			_randomRange = randomRange;
			_readyTime = readyTime;
		}

		@Override
		public void run() {
			// 準備召喚階段一
			if (isReadySpawnDragon()[_portalNumber] && !isSpawnDragon()[_portalNumber]) {
				setSpawnDragon(_portalNumber, true);
				spawn(_npcid, _portalNumber, _X, _Y, _mapId, _randomRange, 0);
			}
			// 召喚階段二、三
			else if (startNextSpawnDragon()[_portalNumber] && isSpawnDragon()[_portalNumber]) {
				spawn(_npcid, _portalNumber, _X, _Y, _mapId, _randomRange, 0);
			}
			cancel();
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, _readyTime); // 延遲時間
		}
	}

	// 重置副本
	public void resetDragonSlayer(int portalNumber) {
		short mapId = (short) (1005 + portalNumber); // MapId 判斷

		for (Object obj : L1World.getInstance().getVisibleObjects(mapId).values()) {
			// 將玩家傳出副本地圖
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (pc != null) {
					if (pc.isDead()) {
						reStartPlayer(pc);
					} else {
						// 傳送至威頓村
						L1Teleport.teleport(pc, 33710, 32521, (short) 4, pc.getHeading(), true);
					}
				}
			}
			// 門關閉
			else if (obj instanceof L1DoorInstance) {
				L1DoorInstance door = (L1DoorInstance) obj;
				door.close();
			}
			// 刪除副本內的怪物
			else if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (npc.getNpcId() == 97006 || npc.getNpcId() == 97007 || npc.getNpcId() == 97008
						|| npc.getNpcId() == 97044 || npc.getNpcId() == 97045 || npc.getNpcId() == 97046
						|| npc.getNpcId() ==  97011 || npc.getNpcId() == 97012 || npc.getNpcId() == 97013) {
					npc.deleteMe();
				}
			}
			// 刪除副本內的物品
			else if (obj instanceof L1Inventory) {
				L1Inventory inventory = (L1Inventory) obj;
				inventory.clearItems();
			}
		}
		setSpawnDragon(portalNumber, false);
		setReadySpawnDragon(portalNumber, false);
		setNextSpawnDragon(portalNumber, false);
		// 喀瑪族、阿爾波斯任務
		for (int i = 0; i < 4; i++) {
			setExtraQuest(portalNumber, i, false);
		}
	}

	// 副本內死亡的玩家重新開始
	private void reStartPlayer(L1PcInstance pc) {
		pc.stopPcDeleteTimer();

		int[] loc = Getback.GetBack_Location(pc, true);

		pc.removeAllKnownObjects();
		pc.broadcastPacket(new S_RemoveObject(pc));

		pc.setCurrentHp(pc.getLevel());
		pc.set_food(40);
		pc.setDead(false);
		pc.setStatus(0);
		L1World.getInstance().moveVisibleObject(pc, loc[2]);
		pc.setX(loc[0]);
		pc.setY(loc[1]);
		pc.setMap((short) loc[2]);
		pc.sendPackets(new S_MapID(pc.getMapId(), pc.getMap().isUnderwater()));
		pc.broadcastPacket(new S_OtherCharPacks(pc));
		pc.sendPackets(new S_OwnCharPack(pc));
		pc.sendPackets(new S_CharVisualUpdate(pc));
		pc.startHpRegeneration();
		pc.startMpRegeneration();
		pc.sendPackets(new S_Weather(L1World.getInstance().getWeather()));
		if (pc.getHellTime() > 0) {
			pc.beginHell(false);
		}
	}

	// 召喚用
	public static void spawn(int npcId, int portalNumber, int X, int Y, short mapId, int randomRange,
			int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(mapId);

			if (randomRange == 0) {
				npc.setX(X);
				npc.setY(Y);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(X + Random.nextInt(randomRange) - Random.nextInt(randomRange));
					npc.setY(Y + Random.nextInt(randomRange) - Random.nextInt(randomRange));
					if (npc.getMap().isInMap(npc.getLocation())
							&& npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.setX(X);
					npc.setY(Y);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(Random.nextInt(8));
			npc.setPortalNumber(portalNumber);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			if (npc.getGfxId() == 7548 || npc.getGfxId() == 7550 || npc.getGfxId() == 7552
					|| npc.getGfxId() == 7554 || npc.getGfxId() == 7585) {
				npc.npcSleepTime(ActionCodes.ACTION_AxeWalk, npc.ATTACK_SPEED);
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc)) {
					npc.onPerceive(pc);
					S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_AxeWalk);
					pc.sendPackets(gfx);
				}
			} else if (npc.getGfxId() == 7539 || npc.getGfxId() == 7557 || npc.getGfxId() == 7558
					|| npc.getGfxId() == 7864 || npc.getGfxId() == 7869 || npc.getGfxId() == 7870) {
				npc.npcSleepTime(ActionCodes.ACTION_AxeWalk, npc.ATTACK_SPEED);
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 50)) {
					if (npc.getGfxId() == 7539) {
						pc.sendPackets(new S_ServerMessage(1570));
					} else if (npc.getGfxId() == 7864) {
						pc.sendPackets(new S_ServerMessage(1657));
					}
					npc.onPerceive(pc);
					S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_AxeWalk);
					pc.sendPackets(gfx);
				}
			}

			npc.turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット開始
			
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc,
						timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
