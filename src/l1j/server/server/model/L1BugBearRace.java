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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.DoorTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.RaceTicketTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.templates.L1ShopItem;

public class L1BugBearRace {
	L1MerchantInstance pory;
	L1MerchantInstance cecile;
	L1MerchantInstance parkin;
	private static final int FIRST_ID = 0x0000000;
	private static final int STATUS_NONE = 0;
	private static final int STATUS_READY = 1;
	private static final int STATUS_PLAYING = 2;
	private static final int STATUS_END = 3;
	private static final int WAIT_TIME = 60;
	private static final int READY_TIME = 9 * 60 - 10;// test 60;//
	private static final int FIRST_NPCID = 91350;// ~20
	private L1NpcInstance[] _runner;
	private int[] _runnerStatus = new int[5];
	private double[] _winning_average = new double[5];
	private double[] _allotment_percentage = new double[5];
	private int[] _condition = new int[5];
	private int _allBet;
	private int[] _betCount = new int[5];

	private int _round;

	public int getRound() {
		return _round;
	}

	private void setRound(int round) {
		this._round = round;
	}

	private static Random _random = new Random();

	private static L1BugBearRace instance;

	public static L1BugBearRace getInstance() {
		if (instance == null) {
			instance = new L1BugBearRace();
		}
		return instance;
	}

	L1BugBearRace() {
		setRound(RaceTicketTable.getInstance().getRoundNumOfMax());
		_runner = new L1NpcInstance[5];
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (obj instanceof L1MerchantInstance) {
				if (((L1MerchantInstance) obj).getNpcId() == 70041) {
					parkin = (L1MerchantInstance) obj;
				}
			}
		}
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (obj instanceof L1MerchantInstance) {
				if (((L1MerchantInstance) obj).getNpcId() == 70035) {
					cecile = (L1MerchantInstance) obj;
				}
			}
		}
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (obj instanceof L1MerchantInstance) {
				if (((L1MerchantInstance) obj).getNpcId() == 70042) {
					pory = (L1MerchantInstance) obj;
				}
			}
		}
		new RaceTimer(0).begin();
	}

	private void setRandomRunner() {
		for (int i = 0; i < 5; i++) {
			int npcid = FIRST_NPCID + _random.nextInt(20);
			while (checkDuplicate(npcid)) {
				npcid = FIRST_NPCID + _random.nextInt(20);
			}
			L1Location loc = new L1Location(33522 - (i * 2), 32861 + (i * 2), 4);
			_runner[i] = spawnOne(loc, npcid, 6);

		}
	}

	private boolean checkDuplicate(int npcid) {
		boolean flag = false;
		for (int i = 0; i < 5; i++) {
			if (_runner[i] != null) {
				if (_runner[i].getNpcId() == npcid) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	private void clearRunner() {
		for (int i = 0; i < 5; i++) {
			if (_runner[i] != null) {
				_runner[i].deleteMe();
				if (_runner[i].getMap().isInMap(_runner[i].getX(),
						_runner[i].getY())) {
					_runner[i].getMap().setPassable(_runner[i].getX(),
							_runner[i].getY(), true);
				}
			}
			_runner[i] = null;
			_runnerStatus[i] = 0;
			_condition[i] = 0;
			_winning_average[i] = 0;
			_allotment_percentage[i] = 0.0;
			setBetCount(i, 0);
		}
		setAllBet(0);
		for (L1DoorInstance door : DoorTable.getInstance()
				.getDoorList()) {
			if (door.getDoorId() <= 812 && door.getDoorId() >= 808) {
				door.close();
			}
		}
	}

	public boolean checkPosition(int runnerNumber) {// 現在のポジションを確認
		final int[] defaultHead = { 6, 7, 0, 1, 2, 2 };
		if (getGameStatus() != STATUS_PLAYING) {
			return false;
		}
		boolean flag = false;// ゴールするまではfalseを返す
		L1NpcInstance npc = _runner[runnerNumber];
		int x = npc.getX();
		int y = npc.getY();
		if (_runnerStatus[runnerNumber] == 0) {// スタート　直線
			if (// x==33476+(runnerNumber*2)&&y==32861+(runnerNumber*2)
			(x >= 33476 && x <= 33476 + 8) && (y >= 32861 && y <= 32861 + 8)) {
				_runnerStatus[runnerNumber] = _runnerStatus[runnerNumber] + 1;
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを変更
			} else {
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを復元
			}
		} else if (_runnerStatus[runnerNumber] == 1) {//
			if ((x <= 33473 && x >= 33473 - 9) && y == 32858) {
				_runnerStatus[runnerNumber] = _runnerStatus[runnerNumber] + 1;
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを変更
			} else {
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを復元
			}
		} else if (_runnerStatus[runnerNumber] == 2) {//
			if ((x <= 33473 && x >= 33473 - 9) && y == 32852) {
				_runnerStatus[runnerNumber] = _runnerStatus[runnerNumber] + 1;
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを変更
			} else {
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを復元
			}
		} else if (_runnerStatus[runnerNumber] == 3) {//
			if ((x == 33478 && (y <= 32847 && y >= 32847 - 9))) {
				_runnerStatus[runnerNumber] = _runnerStatus[runnerNumber] + 1;
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを変更
			} else {
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを復元
			}
		} else if (_runnerStatus[runnerNumber] == 4) {//
			if (x == 33523 && (y >= 32847 - 9 && y <= 32847)) {
				_runnerStatus[runnerNumber] = _runnerStatus[runnerNumber] + 1;
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを変更
				// goal
				goal(runnerNumber);
			} else {
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを復元
			}
		} else if (_runnerStatus[runnerNumber] == 5) {//
			if (x == 33527 && (y >= 32847 - 8 && y <= 32847)) {
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを変更
				finish();
				flag = true;
			} else {
				npc.setHeading(defaultHead[_runnerStatus[runnerNumber]]);// ヘッジを復元
			}
		}
		return flag;
	}

	private void finish() {
		int cnt = 0;
		for (int i = 0; i < _runnerStatus.length; i++) {
			if (_runnerStatus[i] == 5) {
				cnt++;
			}
		}
		if (cnt == 5) {
			setGameStatus(STATUS_END);
			new RaceTimer(30).begin();
			/* SHOP格納処理 */

			/**/
		}
	}

	private void goal(int runnberNumber) {
		int cnt = 0;
		for (int i = 0; i < _runnerStatus.length; i++) {
			if (_runnerStatus[i] == 5) {
				cnt++;
			}
		}
		if (cnt == 1) {
			cecile.wideBroadcastPacket(new S_NpcChatPacket(cecile, "第 "
					+ getRound()
					+ " $366 "
					+ NpcTable.getInstance()
							.getTemplate(_runner[runnberNumber].getNpcId())
							.get_nameid() + " $367", 2));// 5>3バイト
			/* DB格納処理 */
			RaceTicketTable.getInstance().updateTicket(getRound(),
					_runner[runnberNumber].getNpcId() - FIRST_NPCID + 1,
					_allotment_percentage[runnberNumber]);
			/**/
		}
	}

	// TODO 周回数判定処理　end

	private int _status = 0;

	public void setGameStatus(int i) {
		_status = i;
	}

	public int getGameStatus() {
		return _status;
	}

	private void sendMessage(String id) {
		parkin.wideBroadcastPacket(new S_NpcChatPacket(parkin, id, 2));
		// cecile.broadcastPacket(new S_NpcChatPacket(cecile,id, 2));
		pory.wideBroadcastPacket(new S_NpcChatPacket(pory, id, 2));
	}

	private class RaceTimer extends TimerTask {
		int _startTime;

		RaceTimer(int startTime) {
			_startTime = startTime;
		}

		@Override
		public void run() {

			try {
				// ゲームのステータスをNONEに（10分前）
				setGameStatus(STATUS_NONE);
				sendMessage("$376 10 $377");
				for (int loop = 0; loop < WAIT_TIME; loop++) {
					Thread.sleep(1000);
				}
				clearRunner();
				setRound(getRound() + 1);
				/* レース回数-記録処理 */
				L1RaceTicket ticket = new L1RaceTicket();
				ticket.set_itemobjid(FIRST_ID);// 重複可能
				ticket.set_allotment_percentage(0);
				ticket.set_round(getRound());
				ticket.set_runner_num(0);
				ticket.set_victory(0);
				RaceTicketTable.getInstance().storeNewTiket(ticket);// 記録用
				RaceTicketTable.getInstance().oldTicketDelete(getRound());// 古い記録を削除
				/**/
				setRandomRunner();// ランナー準備
				setRandomCondition();
				/* SHOP BuyList格納処理 */
				L1Shop shop1 = ShopTable.getInstance().get(70035);
				L1Shop shop2 = ShopTable.getInstance().get(70041);
				L1Shop shop3 = ShopTable.getInstance().get(70042);
				for (int i = 0; i < 5; i++) {
					L1ShopItem shopItem1 = new L1ShopItem(40309, 500, 1);
					shopItem1.setName(i);
					L1ShopItem shopItem2 = new L1ShopItem(40309, 500, 1);
					shopItem2.setName(i);
					L1ShopItem shopItem3 = new L1ShopItem(40309, 500, 1);
					shopItem3.setName(i);
					shop1.getSellingItems().add(shopItem1);
					shop2.getSellingItems().add(shopItem2);
					shop3.getSellingItems().add(shopItem3);
				}
				/**/
				setWinnigAverage();
				setGameStatus(STATUS_READY);
				for (int loop = 0; loop < READY_TIME - 1; loop++) {
					if (loop % 60 == 0) {
						sendMessage("$376 " + (1 + (READY_TIME - loop) / 60)
								+ " $377");
					}
					Thread.sleep(1000);
				}
				sendMessage("$363");// 363 レディー！
				Thread.sleep(1000);
				for (int loop = 10; loop > 0; loop--) {
					sendMessage("" + loop);
					Thread.sleep(1000);
				}
				sendMessage("$364");// 364 ゴー！
				setGameStatus(STATUS_PLAYING);
				/* SHOP BuyListから削除 */
				shop1.getSellingItems().clear();
				shop2.getSellingItems().clear();
				shop3.getSellingItems().clear();
				/**/
				for (L1DoorInstance door : DoorTable.getInstance()
						.getDoorList()) {
					if (door.getDoorId() <= 812 && door.getDoorId() >= 808) {
						door.open();
					}
				}
				for (int i = 0; i < _runner.length; i++) {
					new BugBearRunning(i).begin(0);
				}

				new StartBuffTimer().begin();

				for (int i = 0; i < _runner.length; i++) {
					if (getBetCount(i) > 0) {
						_allotment_percentage[i] = (double) (getAllBet()
								/ (getBetCount(i)) / 500);
					} else {
						_allotment_percentage[i] = 0.0;
					}
				}
				for (int i = 0; i < _runner.length; i++) {
					Thread.sleep(1000);
					sendMessage(NpcTable.getInstance()
							.getTemplate(_runner[i].getNpcId()).get_nameid()
							+ " $402 "// 一文字3バイトだが面倒なのでネームIDを復元しない・・・
							+ String.valueOf(_allotment_percentage[i]));// 402
																		// の配当率は
				}
				this.cancel();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, _startTime * 1000);
		}
	}

	private class BugBearRunning extends TimerTask {
		L1NpcInstance _bugBear;
		int _runnerNumber;

		BugBearRunning(int runnerNumber) {
			_bugBear = _runner[runnerNumber];
			_runnerNumber = runnerNumber;
		}

		@Override
		public void run() {
			int sleepTime = 0;
			while (getGameStatus() == STATUS_PLAYING) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while (!_bugBear.getMap().isPassable(_bugBear.getX(),
						_bugBear.getY(), _bugBear.getHeading())) {
					if (_bugBear.getMap().isPassable(_bugBear.getX(),
							_bugBear.getY(), _bugBear.getHeading() + 1)) {
						_bugBear.setHeading(rePressHeading(_bugBear
								.getHeading() + 1));
						break;
					} else {
						_bugBear.setHeading(rePressHeading(_bugBear
								.getHeading() - 1));
						if (_bugBear.getMap().isPassable(_bugBear.getX(),
								_bugBear.getY(), _bugBear.getHeading())) {
							break;
						}
					}
				}
				_bugBear.setDirectionMove(_bugBear.getHeading());// ヘッジ方向
				if (checkPosition(_runnerNumber)) {
					_bugBear = null;
					return;
				} else {
					// new BugBearRunning(_runnerNumber).
					// インスタンスを生成しないでください　メモリリークが発生します
					sleepTime = calcSleepTime(_bugBear.getPassispeed(),
							_runnerNumber);
				}
			}
		}

		public void begin(int startTime) {
			Timer _timer = new Timer();
			_timer.schedule(this, startTime);
		}
	}

	private int rePressHeading(int heading) {
		if (0 > heading) {// 0未満ならば7
			heading = 7;
		}
		if (7 < heading) {// 7より大きいなら0
			heading = 0;
		}
		return heading;
	}

	/**
	 * 指定されたロケーションに任意のNpcを一匹生成する。
	 * 
	 * @param loc
	 *            出現位置
	 * @param npcid
	 *            任意のNpcId
	 * @param heading
	 *            向き
	 * @return L1NpcInstance 戻り値 : 成功=生成したインスタンス 失敗=null
	 */
	@SuppressWarnings("unused")
	private L1NpcInstance spawnOne(L1Location loc, int npcid, int heading) {
		final L1NpcInstance mob = new L1NpcInstance(NpcTable.getInstance()
				.getTemplate(npcid));
		if (mob == null) {
			return mob;
		}

		mob.setNameId("#" + (mob.getNpcId() - FIRST_NPCID + 1) + " "
				+ mob.getNameId());
		mob.setId(IdFactory.getInstance().nextId());
		mob.setHeading(heading);
		mob.setX(loc.getX());
		mob.setHomeX(loc.getX());
		mob.setY(loc.getY());
		mob.setHomeY(loc.getY());
		mob.setMap((short) loc.getMapId());
		mob.setPassispeed(mob.getPassispeed() * 2);
		L1World.getInstance().storeObject(mob);
		L1World.getInstance().addVisibleObject(mob);

		final S_NPCPack s_npcPack = new S_NPCPack(mob);
		for (final L1PcInstance pc : L1World.getInstance().getRecognizePlayer(
				mob)) {
			pc.addKnownObject(mob);
			mob.addKnownObject(pc);
			pc.sendPackets(s_npcPack);
		}
		// モンスターのＡＩを開始
		mob.onNpcAI();
		mob.turnOnOffLight();
		mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット開始
		return mob;
	}

	public void setAllBet(int allBet) {// allbetは3桁の整数
		this._allBet = (int) (allBet * 0.9);// 1割は管理人が取得
	}

	public int getAllBet() {
		return _allBet;
	}

	public int getBetCount(int i) {
		return _betCount[i];
	}

	public void setBetCount(int i, int count) {
		_betCount[i] = count;
	}

	private int calcSleepTime(int sleepTime, int runnerNumber) {
		L1NpcInstance npc = _runner[runnerNumber];
		if (npc.getBraveSpeed() == 1) {
			sleepTime -= (sleepTime * 0.25);
		}

		return sleepTime;
	}

	private class StartBuffTimer extends TimerTask {
		StartBuffTimer() {
		}

		@Override
		public void run() {
			if (getGameStatus() == STATUS_PLAYING) {
				for (int i = 0; i < _runner.length; i++) {
					if (((_random.nextInt(10000) + 1) / 100) <= _winning_average[i]
							* (1 + (0.2 * getCondition(i)))) {
						_runner[i].setBraveSpeed(1);
					} else {
						_runner[i].setBraveSpeed(0);
					}
				}
			} else {
				this.cancel();
			}
		}

		public void begin() {
			Timer _timer = new Timer();
			_timer.scheduleAtFixedRate(this, 1000, 1000);
		}
	}

	private void setWinnigAverage() {
		int max_percentage = 10000;
		for (int i = 0; i < _winning_average.length - 1; i++) {
			double winningAverage = (_random.nextInt(max_percentage) + 1) / 100;
			while (checkDuplicateAverage(winningAverage)) {
				winningAverage = (_random.nextInt(max_percentage) + 1) / 100;
			}
			max_percentage -= winningAverage * 100;
			_winning_average[i] = winningAverage;
		}
		_winning_average[_winning_average.length - 1] = max_percentage / 100;
	}

	private boolean checkDuplicateAverage(double winning_average) {
		boolean flag = false;
		if (winning_average == 0) {
			return flag;
		}
		for (int i = 0; i < 5; i++) {
			if (_winning_average[i] == winning_average) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/*
	 * private void setCondition(int num, int condition) { this._condition[num]
	 * = condition; }
	 */

	public int getCondition(int num) {
		return _condition[num];
	}

	private void setRandomCondition() {
		for (int i = 0; i < _condition.length; i++) {
			_condition[i] = -1 + _random.nextInt(3);
		}
	}

	public L1NpcInstance getRunner(int num) {
		return _runner[num];
	}

	public double getWinningAverage(int num) {
		return _winning_average[num];
	}
}
