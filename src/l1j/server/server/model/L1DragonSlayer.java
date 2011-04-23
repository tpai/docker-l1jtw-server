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
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_ServerMessage;
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

	// 判斷龍是否已召喚
	private boolean[] _isSpawnDragon = new boolean[12];

	public boolean[] isSpawnDragon() {
		return _isSpawnDragon;
	}

	public void setSpawnDragon(int number, boolean i) {
		_isSpawnDragon[number] = i;
	}

	// 龍之門扉物件
	private L1NpcInstance[] _mob = new L1NpcInstance[12];

	public L1NpcInstance[] isMob() {
		return _mob;
	}

	public void setMob(int number, L1NpcInstance mob) {
		_mob[number] = mob;
	}

	// 屠龍結束
	public void endDragonSlayer(byte portalNumber) {
		if (portalNumber != -1) {
			endDragonSlayerTimer timer = new endDragonSlayerTimer(portalNumber);
			timer.begin();
		}
	}

	// 屠龍結束 X秒後傳送玩家出地圖
	public class endDragonSlayerTimer extends TimerTask {

		private final byte _portalNumber;

		public endDragonSlayerTimer (byte portalNumber) {
			_portalNumber = portalNumber;
		}

		@Override
		public void run() {
			short mapId = 0;
			switch (_portalNumber) {
				case 0:
					mapId = 1005;
					break;
				case 1:
					mapId = 1006;
					break;
				case 2:
					mapId = 1007;
					break;
				case 3:
					mapId = 1008;
					break;
				case 4:
					mapId = 1009;
					break;
				case 5:
					mapId = 1010;
					break;
				case 6:
					mapId = 1011;
					break;
				case 7:
					mapId = 1012;
					break;
				case 8:
					mapId = 1013;
					break;
				case 9:
					mapId = 1014;
					break;
				case 10:
					mapId = 1015;
					break;
				case 11:
					mapId = 1016;
					break;
			}

			if (mapId != 0) {
				for (Object obj : L1World.getInstance().getVisibleObjects(mapId)
						.values()) {
					// 將玩家傳出副本地圖
					if (obj instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) obj;
						if (pc != null && !pc.isTeleport()) {
							// 傳送至威頓村
							L1Teleport.teleport(pc, 33710, 32521, (short) 4, pc.getHeading(), true);
						}
					} else if (obj instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) obj;
						if (npc.getNpcId() == 97006 || npc.getNpcId() == 97007 || npc.getNpcId() == 97008
								|| npc.getNpcId() == 97044 || npc.getNpcId() == 97045 || npc.getNpcId() == 97046) {
							npc.deleteMe();
						}
					}
				}
				// 刪除龍之門扉
				if (isMob()[_portalNumber] != null) {
					isMob()[_portalNumber].setStatus(ActionCodes.ACTION_Die);
					isMob()[_portalNumber].broadcastPacket(new S_DoActionGFX(isMob()[_portalNumber].getId(), ActionCodes.ACTION_Die));
					isMob()[_portalNumber].deleteMe();
				}
				// 龍之門扉重置
				setPortalNumber(_portalNumber, false);
				setSpawnDragon(_portalNumber, false);
				setMob(_portalNumber, null);
			}
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 15000); // 15秒後
		}
	}

	// 召喚龍
	public void spawnDragon(L1PcInstance pc) {
		byte portalNumber = pc.getPortalNumber();
		switch (portalNumber) {
		// 安塔瑞斯
			case 0:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97006, portalNumber, 32786, 32689, (short) 1005, 20);
					timer.begin();
				}
				break;
			case 1:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97006, portalNumber, 32786, 32689, (short) 1006, 20);
					timer.begin();
				}
				break;
			case 2:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97006, portalNumber, 32786, 32689, (short) 1007, 20);
					timer.begin();
				}
				break;
			case 3:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97006, portalNumber, 32786, 32689, (short) 1008, 20);
					timer.begin();
				}
				break;
			case 4:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97006, portalNumber, 32786, 32689, (short) 1009, 20);
					timer.begin();
				}
				break;
			case 5:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97006, portalNumber, 32786, 32689, (short) 1010, 20);
					timer.begin();
				}
				break;
			case 6:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97044, portalNumber, 32959, 32836, (short) 1011, 20);
					timer.begin();
				}
				break;
			case 7:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97044, portalNumber, 32959, 32836, (short) 1012, 20);
					timer.begin();
				}
				break;
			case 8:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97044, portalNumber, 32959, 32836, (short) 1013, 20);
					timer.begin();
				}
				break;
			case 9:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97044, portalNumber, 32959, 32836, (short) 1014, 20);
					timer.begin();
				}
				break;
			case 10:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97044, portalNumber, 32959, 32836, (short) 1015, 20);
					timer.begin();
				}
				break;
			case 11:
				if (!isSpawnDragon()[portalNumber]) {
					setSpawnDragon(portalNumber, true);
					spawnDragon timer = new spawnDragon(97044, portalNumber, 32959, 32836, (short) 1016, 20);
					timer.begin();
				}
				break;
		}
	}

	// X秒後於龍之棲息地召喚龍
	public class spawnDragon extends TimerTask {

		private final int _npcid;
		private final byte _portalNumber;
		private final int _X;
		private final int _Y;
		private final short _mapId;
		private final int _randomRange;

		public spawnDragon(int npcId, byte portalNumber, int X, int Y, short mapId, int randomRange) {
			_npcid = npcId;
			_portalNumber = portalNumber;
			_X =X;
			_Y =Y;
			_mapId = mapId;
			_randomRange = randomRange;
		}

		@Override
		public void run() {
			spawn(_npcid, _portalNumber, _X, _Y, _mapId, _randomRange, 0);
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 180000); // 3分鐘
		}
	}

	// 召喚用
	public static void spawn(int npcId, byte portalNumber, int X, int Y, short mapId, int randomRange,
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
			for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(npc)) {
				npc.onPerceive(pc);
			}
			if (npc.getGfxId() == 7548 || npc.getGfxId() == 7550 || npc.getGfxId() == 7552
					|| npc.getGfxId() == 7554 || npc.getGfxId() == 7585
					|| npc.getGfxId() == 7539 || npc.getGfxId() == 7557 || npc.getGfxId() == 7558
					|| npc.getGfxId() == 7864 || npc.getGfxId() == 7869 || npc.getGfxId() == 7870) {
				S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), 11);
				npc.broadcastPacket(gfx);
				if (npc.getGfxId() == 7539) {
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 50)) {
						pc.sendPackets(new S_ServerMessage(1570));
					}
				} else if (npc.getGfxId() == 7864) {
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 50)) {
						pc.sendPackets(new S_ServerMessage(1657));
					}
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
