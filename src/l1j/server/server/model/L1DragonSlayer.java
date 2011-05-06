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

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
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
import l1j.server.server.utils.collections.Maps;

/** 安塔瑞斯、法利昂副本 */
public class L1DragonSlayer {
	private static Logger _log = Logger.getLogger(L1DragonSlayer.class.getName());

	private static L1DragonSlayer _instance;

	public static final int STATUS_DRAGONSLAYER_NONE = 0;
	public static final int STATUS_DRAGONSLAYER_READY_1RD = 1;
	public static final int STATUS_DRAGONSLAYER_READY_2RD = 2;
	public static final int STATUS_DRAGONSLAYER_READY_3RD = 3;
	public static final int STATUS_DRAGONSLAYER_READY_4RD = 4;
	public static final int STATUS_DRAGONSLAYER_START_1RD = 5;
	public static final int STATUS_DRAGONSLAYER_START_2RD = 6;
	public static final int STATUS_DRAGONSLAYER_START_2RD_1 = 7;
	public static final int STATUS_DRAGONSLAYER_START_2RD_2 = 8;
	public static final int STATUS_DRAGONSLAYER_START_2RD_3 = 9;
	public static final int STATUS_DRAGONSLAYER_START_2RD_4 = 10;
	public static final int STATUS_DRAGONSLAYER_START_3RD = 11;
	public static final int STATUS_DRAGONSLAYER_START_3RD_1 = 12;
	public static final int STATUS_DRAGONSLAYER_START_3RD_2 = 13;
	public static final int STATUS_DRAGONSLAYER_START_3RD_3 = 14;
	public static final int STATUS_DRAGONSLAYER_END_1 = 15;
	public static final int STATUS_DRAGONSLAYER_END_2 = 16;
	public static final int STATUS_DRAGONSLAYER_END_3 = 17;
	public static final int STATUS_DRAGONSLAYER_END_4 = 18;
	public static final int STATUS_DRAGONSLAYER_END_5 = 19;
	public static final int STATUS_DRAGONSLAYER_END = 20;

	public static final int STATUS_NONE = 0;
	public static final int STATUS_READY_SPAWN = 1;
	public static final int STATUS_SPAWN = 2;

	private static class DragonSlayer {
		private ArrayList<L1PcInstance> _members = new ArrayList<L1PcInstance>();
	}

	private static final Map<Integer, DragonSlayer> _dataMap = Maps.newMap();

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

	// 龍之門扉物件
	private L1NpcInstance[] _portal = new L1NpcInstance[12];

	public L1NpcInstance[] portalPack() {
		return _portal;
	}

	public void setPortalPack(int number, L1NpcInstance portal) {
		_portal[number] = portal;
	}

	// 副本目前狀態
	private int[] _DragonSlayerStatus = new int[12];

	public int[] getDragonSlayerStatus() {
		return _DragonSlayerStatus;
	}

	public void setDragonSlayerStatus(int portalNum, int i) {
		_DragonSlayerStatus[portalNum] = i;
	}

	// 判斷隱匿的巨龍谷入口是否已出現
	private int _hiddenDragonValleyStstus = 0;

	public int checkHiddenDragonValleyStstus() {
		return _hiddenDragonValleyStstus;
	}

	public void setHiddenDragonValleyStstus(int i) {
		_hiddenDragonValleyStstus = i;
	}

	// 加入玩家
	public void addPlayerList(L1PcInstance pc, int portalNum) {
		if (_dataMap.containsKey(portalNum)) {
			if (!_dataMap.get(portalNum)._members.contains(pc)) {
				_dataMap.get(portalNum)._members.add(pc);
			}
		}
	}

	// 移除玩家
	public void removePlayer(L1PcInstance pc, int portalNum) {
		if (_dataMap.containsKey(portalNum)) {
			if (_dataMap.get(portalNum)._members.contains(pc)) {
				_dataMap.get(portalNum)._members.remove(pc);
			}
		}
	}

	// 清除玩家
	private void clearPlayerList(int portalNum) {
		if (_dataMap.containsKey(portalNum)) {
			_dataMap.get(portalNum)._members.clear();
		}
	}

	// 取得參加人數
	public int getPlayersCount(int num) {
		DragonSlayer _DragonSlayer = null;
		if (!_dataMap.containsKey(num)) {
			_DragonSlayer = new DragonSlayer();
			_dataMap.put(num, _DragonSlayer);
		}
		return _dataMap.get(num)._members.size();
	}

	private L1PcInstance[] getPlayersArray(int num) {
		return _dataMap.get(num)._members.toArray(new L1PcInstance[_dataMap.get(num)._members.size()]);
	}

	// 開始第一階段
	public void startDragonSlayer(int portalNum) {
		if (getDragonSlayerStatus()[portalNum] == STATUS_DRAGONSLAYER_NONE) {
			setDragonSlayerStatus(portalNum, STATUS_DRAGONSLAYER_READY_1RD);
			DragonSlayerTimer timer = new DragonSlayerTimer(portalNum, STATUS_DRAGONSLAYER_READY_1RD, 150000);
			timer.begin();
		}
	}

	// 開始第二階段
	public void startDragonSlayer2rd(int portalNum) {
		if (getDragonSlayerStatus()[portalNum] == STATUS_DRAGONSLAYER_START_1RD) {
			if (portalNum >= 6 && portalNum <= 11) {
				sendMessage(portalNum, 1661, null); // 法利昂：可憐啊！他們就是和你一樣，註定要當我的祭品！
			} else {
				sendMessage(portalNum, 1573, null); // 安塔瑞斯：你這頑固的傢伙！你又激起我的憤怒了！
			}
			setDragonSlayerStatus(portalNum, STATUS_DRAGONSLAYER_START_2RD);
			DragonSlayerTimer timer = new DragonSlayerTimer(portalNum, STATUS_DRAGONSLAYER_START_2RD, 10000);
			timer.begin();
		}
	}

	// 開始第三階段
	public void startDragonSlayer3rd(int portalNum) {
		if (getDragonSlayerStatus()[portalNum] == STATUS_DRAGONSLAYER_START_2RD_4) {
			if (portalNum >= 6 && portalNum <= 11) {
				sendMessage(portalNum, 1665, null); // 巫女莎爾：法利昂的力量好像削弱了不少！ 勇士們啊，再接再厲吧！
			} else {
				sendMessage(portalNum, 1577, null); // 卡瑞：嗚啊！你有聽到那些冤魂的慘叫聲嗎！受死吧！！
			}
			setDragonSlayerStatus(portalNum, STATUS_DRAGONSLAYER_START_3RD);
			DragonSlayerTimer timer = new DragonSlayerTimer(portalNum, STATUS_DRAGONSLAYER_START_3RD, 10000);
			timer.begin();
		}
	}

	// 副本完成
	public void endDragonSlayer(int portalNum) {
		if (getDragonSlayerStatus()[portalNum] == STATUS_DRAGONSLAYER_START_3RD_3) {
			setDragonSlayerStatus(portalNum, STATUS_DRAGONSLAYER_END_1);
			DragonSlayerTimer timer = new DragonSlayerTimer(portalNum, STATUS_DRAGONSLAYER_END_1, 10000);
			timer.begin();
		}
	}

	// 門扉存在時間結束
	public void endDragonPortal(int portalNum) {
		if (getDragonSlayerStatus()[portalNum] != STATUS_DRAGONSLAYER_END_5) {
			setDragonSlayerStatus(portalNum, STATUS_DRAGONSLAYER_END_5);
			DragonSlayerTimer timer = new DragonSlayerTimer(portalNum, STATUS_DRAGONSLAYER_END_5, 5000);
			timer.begin();
		}
	}

	// 計時器
	public class DragonSlayerTimer extends TimerTask {

		private final int _num;
		private final int _status;
		private final int _time;

		public DragonSlayerTimer(int num, int status, int time) {
			_num = num;
			_status = status;
			_time = time;
		}

		@Override
		public void run() {
			short mapId = (short) (1005 + _num);
			int[] msg = { 1570, 1571, 1572, 1574, 1575, 1576, 1578, 1579, 1581 };
			if (_num >= 6 && _num <= 11) {
				msg = new int[] { 1657, 1658, 1659, 1662, 1663, 1664, 1666, 1667, 1669};
			}
			switch (_status) {
				// 階段一
				case STATUS_DRAGONSLAYER_READY_1RD:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_READY_2RD);
					sendMessage(_num, msg[0], null); // 安塔瑞斯：到底是誰把我吵醒了？
					// 法利昂：竟敢闖入我的領域...勇氣可嘉啊...
					DragonSlayerTimer timer_1rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_READY_2RD, 10000);
					timer_1rd.begin();
					break;
				case STATUS_DRAGONSLAYER_READY_2RD:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_READY_3RD);
					sendMessage(_num, msg[1], null); // 卡瑞：安塔瑞斯！我不停追逐你，結果追到這黑暗的地方來！
					// 巫女莎爾：你這卑劣的法利昂！你會付出欺騙我的代價！
					DragonSlayerTimer timer_2rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_READY_3RD, 10000);
					timer_2rd.begin();
					break;
				case STATUS_DRAGONSLAYER_READY_3RD:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_READY_4RD);
					sendMessage(_num, msg[2], null); // 安塔瑞斯：真可憐，就讓我把你解決掉，受死吧！卡瑞！
					// 法利昂：雖然在解除封印時你幫了很大的忙...但現在我不會再仁慈了！！
					DragonSlayerTimer timer_3rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_READY_4RD, 10000);
					timer_3rd.begin();
					break;
				case STATUS_DRAGONSLAYER_READY_4RD:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_START_1RD);
					// 召喚龍
					if (_num >= 0 && _num <= 5) {
						spawn(97006, _num, 32783, 32693, mapId, 10, 0); // 地龍 - 階段一
					} else {
						spawn(97044, _num, 32955, 32839, mapId, 10, 0); // 水龍 - 階段一
					}
					break;
				// 階段二
				case STATUS_DRAGONSLAYER_START_2RD:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_START_2RD_1);
					sendMessage(_num, msg[3], null); // 卡瑞：勇士們！亞丁的命運就掌握在你們的武器上了， 能夠讓安塔瑞斯窒息的人就是你們了！
					// 巫女莎爾：勇士們！請消滅邪惡的法利昂，解除伊娃王國的血之詛咒吧！
					DragonSlayerTimer timer_4rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_START_2RD_1, 10000);
					timer_4rd.begin();
					break;
				case STATUS_DRAGONSLAYER_START_2RD_1:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_START_2RD_2);
					sendMessage(_num, msg[4], null); // 安塔瑞斯：像這種蝦兵蟹將也想要贏我！噗哈哈哈…
					// 法利昂：你們只夠格當我的玩具！！
					DragonSlayerTimer timer_5rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_START_2RD_2, 30000);
					timer_5rd.begin();
					break;
				case STATUS_DRAGONSLAYER_START_2RD_2:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_START_2RD_3);
					sendMessage(_num, msg[5], null); // 安塔瑞斯：我今天又可以飽餐一頓了？你們的血激起我的鬥志。
					// 法利昂：刻骨的恐懼到底是什麼，就讓你們嘗一下吧！
					DragonSlayerTimer timer_6rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_START_2RD_3, 10000);
					timer_6rd.begin();
					break;
				case STATUS_DRAGONSLAYER_START_2RD_3:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_START_2RD_4);
					// 召喚龍
					if (_num >= 0 && _num <= 5) {
						spawn(97007, _num, 32783, 32693, mapId, 10, 0); // 地龍 - 階段二
					} else {
						spawn(97045, _num, 32955, 32839, mapId, 10, 0); // 水龍 - 階段二
					}
					break;
				// 階段三
				case STATUS_DRAGONSLAYER_START_3RD:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_START_3RD_1);
					sendMessage(_num, msg[6], null); // 安塔瑞斯：你竟然敢對付我...我看你們是不想活了？
					// 法利昂：我要讓你們知道你們所謂的希望，只不過是妄想！
					DragonSlayerTimer timer_7rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_START_3RD_1, 40000);
					timer_7rd.begin();
					break;
				case STATUS_DRAGONSLAYER_START_3RD_1:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_START_3RD_2);
					sendMessage(_num, msg[7], null); // 安塔瑞斯：我的憤怒值已經衝上天了，我的父親格蘭肯將會賜我力量。
					// 法利昂：你們會後悔跟了莎爾！ 可笑的愚民…
					DragonSlayerTimer timer_8rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_START_3RD_2, 10000);
					timer_8rd.begin();
					break;
				case STATUS_DRAGONSLAYER_START_3RD_2:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_START_3RD_3);
					// 召喚龍
					if (_num >= 0 && _num <= 5) {
						spawn(97008, _num, 32783, 32693, mapId, 10, 0); // 地龍 - 階段三
					} else {
						spawn(97046, _num, 32955, 32839, mapId, 10, 0); // 水龍 - 階段三
					}
					break;
				case STATUS_DRAGONSLAYER_END_1:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_END_2);
					sendMessage(_num, msg[8], null); // 卡瑞：喔... 頂尖的勇士們！你們經歷了多少的失敗才有今天的成就，你們擊敗了安塔瑞斯！
													//我終於復仇了嗚哈哈哈！！ 謝謝你們，你們是最頂尖的戰士！
					if (checkHiddenDragonValleyStstus() == STATUS_NONE) { // 準備開啟隱匿的巨龍谷入口
						setHiddenDragonValleyStstus(STATUS_READY_SPAWN);
						DragonSlayerTimer timer_9rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_END_2, 10000);
						timer_9rd.begin();
					} else { // 直接結束
						if (getDragonSlayerStatus()[_num] != STATUS_DRAGONSLAYER_END_5) {
							setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_END_5);
							DragonSlayerTimer timer = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_END_5, 5000);
							timer.begin();
						}
					}
					break;
				case STATUS_DRAGONSLAYER_END_2:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_END_3);
					sendMessage(_num, 1582, null); // 侏儒的呼喚：威頓村莊出現了通往隱匿的巨龍谷入口。
					if (checkHiddenDragonValleyStstus() == STATUS_READY_SPAWN) { // 開啟隱匿的巨龍谷入口
						setHiddenDragonValleyStstus(STATUS_SPAWN);
						spawn(81277, -1, 33726, 32506, (short) 4, 0, 86400000); // 24小時
					}
					DragonSlayerTimer timer_10rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_END_3, 5000);
					timer_10rd.begin();
					break;
				case STATUS_DRAGONSLAYER_END_3:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_END_4);
					sendMessage(_num, 1583, null); // 侏儒的呼喚：威頓村莊通往隱匿的巨龍谷入口已經開啟了。
					DragonSlayerTimer timer_11rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_END_4, 5000);
					timer_11rd.begin();
					break;
				case STATUS_DRAGONSLAYER_END_4:
					setDragonSlayerStatus(_num, STATUS_DRAGONSLAYER_END_5);
					sendMessage(_num, 1584, null); // 侏儒的呼喚：快離開這裡吧，門就快要關了。
					DragonSlayerTimer timer_12rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_END_5, 5000);
					timer_12rd.begin();
					break;
				case STATUS_DRAGONSLAYER_END_5:
					// 刪除龍之門扉
					if (portalPack()[_num] != null) {
						portalPack()[_num].setStatus(ActionCodes.ACTION_Die);
						portalPack()[_num].broadcastPacket(new S_DoActionGFX(portalPack()[_num].getId(), ActionCodes.ACTION_Die));
						portalPack()[_num].deleteMe();
					}
					// 龍之門扉重置
					resetDragonSlayer(_num);
					DragonSlayerTimer timer_13rd = new DragonSlayerTimer(_num, STATUS_DRAGONSLAYER_END, 300000); // 下次可重新開啟同編號龍門的等候時間
					timer_13rd.begin();
					break;
				case STATUS_DRAGONSLAYER_END:
					setPortalNumber(_num, false);
					break;
			}
			cancel();
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, _time); // 延遲時間
		}
	}

	// 訊息發送
	public void sendMessage(int portalNum, int type, String msg) {
		short mapId = (short) (1005 + portalNum);
		L1PcInstance[] temp = getPlayersArray(portalNum);
		for (L1PcInstance element : temp) {
			if ((element.getMapId() == mapId)
					&& (element.getX() >= 32740 && element.getX() <= 32827)
					&& (element.getY() >= 32652 && element.getY() <= 32727)
					&& (portalNum >= 0 && portalNum <= 5)) { // 安塔瑞斯棲息地
				element.sendPackets(new S_ServerMessage(type, msg));
			} else if ((element.getMapId() == mapId)
					&& (element.getX() >= 32921 && element.getX() <= 33009)
					&& (element.getY() >= 32799 && element.getY() <= 32869)
					&& (portalNum >= 6 && portalNum <= 11)) { // 法利昂棲息地
				element.sendPackets(new S_ServerMessage(type, msg));
			}
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
						pc.setPortalNumber(-1);
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
				if ((npc.getMaster() == null)
						&& (npc.getNpcTemplate().get_npcId() < 81301 && npc.getNpcTemplate().get_npcId() > 81306)) {
					npc.deleteMe();
				}
			}
			// 刪除副本內的物品
			else if (obj instanceof L1Inventory) {
				L1Inventory inventory = (L1Inventory) obj;
				inventory.clearItems();
			}
		}
		setPortalPack(portalNumber, null);
		setDragonSlayerStatus(portalNumber, STATUS_DRAGONSLAYER_NONE);
		clearPlayerList(portalNumber);
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
	public void spawn(int npcId, int portalNumber, int X, int Y, short mapId, int randomRange,
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
					|| npc.getGfxId() == 7554 || npc.getGfxId() == 7585
					|| npc.getGfxId() == 7539 || npc.getGfxId() == 7557 || npc.getGfxId() == 7558
					|| npc.getGfxId() == 7864 || npc.getGfxId() == 7869 || npc.getGfxId() == 7870) {
				npc.npcSleepTime(ActionCodes.ACTION_AxeWalk, npc.ATTACK_SPEED);
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc)) {
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
