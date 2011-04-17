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

import static l1j.server.server.model.skill.L1SkillId.FOG_OF_SLEEPING;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.identity.L1SystemMessageId;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CalcExp;
import l1j.server.server.utils.Random;

public class L1GuardianInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1GuardianInstance.class
			.getName());

	private L1GuardianInstance _npc = this;

	private int GDROPITEM_TIME = Config.GDROPITEM_TIME;

	/**
	 * @param template
	 */
	public L1GuardianInstance(L1Npc template) {
		super(template);
		if (!isDropitems()) {
			doGDropItem(0);
		}
	}

	@Override
	public void searchTarget() {
		// ターゲット検索
		L1PcInstance targetPlayer = null;

		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if ((pc.getCurrentHp() <= 0) || pc.isDead() || pc.isGm()
					|| pc.isGhost()) {
				continue;
			}
			if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) { // インビジチェック
				if (!pc.isElf()) { // エルフ以外
					targetPlayer = pc;
					wideBroadcastPacket(new S_NpcChatPacket(this, "$804", 2)); // エルフ以外の者よ、命が惜しければ早くここから去れ。ここは神聖な場所だ。
					break;
				} else if (pc.isElf() && pc.isWantedForElf()) {
					targetPlayer = pc;
					wideBroadcastPacket(new S_NpcChatPacket(this, "$815", 1)); // 同族を殺したものは、己の血でその罪をあがなうことになるだろう。
					break;
				}
			}
		}
		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
	}

	// リンクの設定
	@Override
	public void setLink(L1Character cha) {
		if ((cha != null) && _hateList.isEmpty()) { // ターゲットがいない場合のみ追加
			_hateList.add(cha, 0);
			checkTarget();
		}
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		setActived(false);
		startAI();
	}

	@Override
	public void onAction(L1PcInstance pc) {
		onAction(pc, 0);
	}

	public void doGDropItem(int timer) {
		GDropItemTask task = new GDropItemTask();
		GeneralThreadPool.getInstance().schedule(task, timer * 60000);
	}

	private class GDropItemTask implements Runnable {
		int npcId = getNpcTemplate().get_npcId();

		private GDropItemTask() {
		}

		@Override
		public void run() {
			try {
				if (GDROPITEM_TIME > 0 && !isDropitems()) {
					if (npcId == 70848) { // 安特
						if (!_inventory.checkItem(40505)
								&& !_inventory.checkItem(40506)
								&& !_inventory.checkItem(40507)) {
							_inventory.storeItem(40506, 1);
							_inventory.storeItem(40507, 66);
							_inventory.storeItem(40505, 8);
						}
					}
					if (npcId == 70850) { // 潘
						if (!_inventory.checkItem(40519)) {
							_inventory.storeItem(40519, 30);
						}
					}
					setDropItems(true);
					giveDropItems(true);
					doGDropItem(GDROPITEM_TIME);
				} else {
					giveDropItems(false);
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, "資料載入錯誤", e);
			}
		}
	}

	@Override
	public void onAction(L1PcInstance pc, int skillId) {
		if ((pc.getType() == 2) && (pc.getCurrentWeapon() == 0) && pc.isElf()) {
			L1Attack attack = new L1Attack(pc, this, skillId);

			if (attack.calcHit()) {
				try {
					int chance = 0;
					int npcId = getNpcTemplate().get_npcId();
					String npcName = getNpcTemplate().get_name();
					String itemName = "";
					int itemCount = 0;
					L1Item item40499 = ItemTable.getInstance().getTemplate(
							40499);
					L1Item item40503 = ItemTable.getInstance().getTemplate(
							40503);
					L1Item item40505 = ItemTable.getInstance().getTemplate(
							40505);
					L1Item item40506 = ItemTable.getInstance().getTemplate(
							40506);
					L1Item item40507 = ItemTable.getInstance().getTemplate(
							40507);
					L1Item item40519 = ItemTable.getInstance().getTemplate(
							40519);
					if (npcId == 70848) { // 安特
						if (_inventory.checkItem(40499)
								&& !_inventory.checkItem(40505)) { // 蘑菇汁 換
																	// 安特之樹皮
							itemName = item40505.getName();
							itemCount = _inventory.countItems(40499);
							if (itemCount > 1) {
								itemName += " (" + itemCount + ")";
							}
							_inventory.consumeItem(40499, itemCount);
							pc.getInventory().storeItem(40505, itemCount);
							pc.sendPackets(new S_ServerMessage(
									L1SystemMessageId.$143, npcName, itemName));
							if (!isDropitems()) {
								doGDropItem(3);
							}
						}
						if (_inventory.checkItem(40505)) { // 安特之樹皮
							chance = Random.nextInt(100) + 1;
							if (chance <= 60 && chance >= 50) {
								itemName = item40505.getName();
								_inventory.consumeItem(40505, 1);
								pc.getInventory().storeItem(40505, 1);
								pc.sendPackets(new S_ServerMessage(
										L1SystemMessageId.$143, npcName,
										itemName));
							} else {
								itemName = item40499.getName();
								pc.sendPackets(new S_ServerMessage(
										L1SystemMessageId.$337, itemName));
							}
						} else if (_inventory.checkItem(40507)
								&& !_inventory.checkItem(40505)) { // 安特之樹枝
							chance = Random.nextInt(100) + 1;
							if (chance <= 40 && chance >= 25) {
								itemName = item40507.getName();
								itemName += " (6)";
								_inventory.consumeItem(40507, 6);
								pc.getInventory().storeItem(40507, 6);
								pc.sendPackets(new S_ServerMessage(
										L1SystemMessageId.$143, npcName,
										itemName));
							} else {
								itemName = item40499.getName();
								pc.sendPackets(new S_ServerMessage(
										L1SystemMessageId.$337, itemName));
							}
						} else if (_inventory.checkItem(40506)
								&& !_inventory.checkItem(40507)) { // 安特的水果
							chance = Random.nextInt(100) + 1;
							if (chance <= 90 && chance >= 85) {
								itemName = item40506.getName();
								_inventory.consumeItem(40506, 1);
								pc.getInventory().storeItem(40506, 1);
								pc.sendPackets(new S_ServerMessage(
										L1SystemMessageId.$143, npcName,
										itemName));
							} else {
								itemName = item40499.getName();
								pc.sendPackets(new S_ServerMessage(
										L1SystemMessageId.$337, itemName));
							}
						} else {
							if (!forDropitems()) {
								setDropItems(false);
								doGDropItem(GDROPITEM_TIME);
							}
							chance = Random.nextInt(100) + 1;
							if (chance <= 80 && chance >= 40) {
								broadcastPacket(new S_NpcChatPacket(_npc,
										"$822", 0));
							} else {
								itemName = item40499.getName();
								pc.sendPackets(new S_ServerMessage(
										L1SystemMessageId.$337, itemName));
							}
						}
					}
					if (npcId == 70850) { // 潘
						if (_inventory.checkItem(40519)) { // 潘的鬃毛
							chance = Random.nextInt(100) + 1;
							if (chance <= 25) {
								itemName = item40519.getName();
								itemName += " (5)";
								_inventory.consumeItem(40519, 5);
								pc.getInventory().storeItem(40519, 5);
								pc.sendPackets(new S_ServerMessage(
										L1SystemMessageId.$143, npcName,
										itemName));
							}
						} else {
							if (!forDropitems()) {
								setDropItems(false);
								doGDropItem(GDROPITEM_TIME);
							}
							chance = Random.nextInt(100) + 1;
							if (chance <= 80 && chance >= 40) {
								broadcastPacket(new S_NpcChatPacket(_npc,
										"$824", 0));
							}
						}
					}
					if (npcId == 70846) { // 芮克妮
						if (_inventory.checkItem(40507)) { // 安特之樹枝 換 芮克妮的網
							itemName = item40503.getName();
							itemCount = _inventory.countItems(40507);
							if (itemCount > 1) {
								itemName += " (" + itemCount + ")";
							}
							_inventory.consumeItem(40507, itemCount);
							pc.getInventory().storeItem(40503, itemCount);
							pc.sendPackets(new S_ServerMessage(
									L1SystemMessageId.$143, npcName, itemName));
						} else {
							itemName = item40507.getName();
							pc.sendPackets(new S_ServerMessage(
									L1SystemMessageId.$337, itemName)); // \\f1%0不足%s。
						}
					}
				} catch (Exception e) {
					_log.log(Level.SEVERE, "發生錯誤", e);
				}
				attack.calcDamage();
				attack.calcStaffOfMana();
				attack.addPcPoisonAttack(pc, this);
				attack.addChaserAttack();
			}
			attack.action();
			attack.commit();
		} else if ((getCurrentHp() > 0) && !isDead()) {
			L1Attack attack = new L1Attack(pc, this, skillId);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.calcStaffOfMana();
				attack.addPcPoisonAttack(pc, this);
				attack.addChaserAttack();
			}
			attack.action();
			attack.commit();
		}
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(
				getNpcTemplate().get_npcId());
		L1Object object = L1World.getInstance().findObject(getId());
		L1NpcInstance target = (L1NpcInstance) object;

		if (talking != null) {
			int pcx = player.getX(); // PCのX座標
			int pcy = player.getY(); // PCのY座標
			int npcx = target.getX(); // NPCのX座標
			int npcy = target.getY(); // NPCのY座標

			if ((pcx == npcx) && (pcy < npcy)) {
				setHeading(0);
			} else if ((pcx > npcx) && (pcy < npcy)) {
				setHeading(1);
			} else if ((pcx > npcx) && (pcy == npcy)) {
				setHeading(2);
			} else if ((pcx > npcx) && (pcy > npcy)) {
				setHeading(3);
			} else if ((pcx == npcx) && (pcy > npcy)) {
				setHeading(4);
			} else if ((pcx < npcx) && (pcy > npcy)) {
				setHeading(5);
			} else if ((pcx < npcx) && (pcy == npcy)) {
				setHeading(6);
			} else if ((pcx < npcx) && (pcy < npcy)) {
				setHeading(7);
			}
			broadcastPacket(new S_ChangeHeading(this));

			// html表示パケット送信
			if (player.getLawful() < -1000) { // プレイヤーがカオティック
				player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
			} else {
				player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
			}

			// 動かないようにする
			synchronized (this) {
				if (_monitor != null) {
					_monitor.cancel();
				}
				setRest(true);
				_monitor = new RestMonitor();
				_restTimer.schedule(_monitor, REST_MILLISEC);
			}
		}
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) { // 攻撃でＨＰを減らすときはここを使用
		if ((attacker instanceof L1PcInstance) && (damage > 0)) {
			L1PcInstance pc = (L1PcInstance) attacker;
			if ((pc.getType() == 2) && // 素手ならダメージなし
					(pc.getCurrentWeapon() == 0)) {
			} else {
				if ((getCurrentHp() > 0) && !isDead()) {
					if (damage >= 0) {
						setHate(attacker, damage);
					}
					if (damage > 0) {
						removeSkillEffect(FOG_OF_SLEEPING);
					}
					onNpcAI();
					// 仲間意識をもつモンスターのターゲットに設定
					serchLink(pc, getNpcTemplate().get_family());
					if (damage > 0) {
						pc.setPetTarget(this);
					}

					int newHp = getCurrentHp() - damage;
					if ((newHp <= 0) && !isDead()) {
						setCurrentHpDirect(0);
						setDead(true);
						setStatus(ActionCodes.ACTION_Die);
						_lastattacker = attacker;
						Death death = new Death();
						GeneralThreadPool.getInstance().execute(death);
					}
					if (newHp > 0) {
						setCurrentHp(newHp);
					}
				} else if (!isDead()) { // 念のため
					setDead(true);
					setStatus(ActionCodes.ACTION_Die);
					_lastattacker = attacker;
					Death death = new Death();
					GeneralThreadPool.getInstance().execute(death);
				}
			}
		}
	}

	@Override
	public void setCurrentHp(int i) {
		int currentHp = i;
		if (currentHp >= getMaxHp()) {
			currentHp = getMaxHp();
		}
		setCurrentHpDirect(currentHp);

		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}
	}

	@Override
	public void setCurrentMp(int i) {
		int currentMp = i;
		if (currentMp >= getMaxMp()) {
			currentMp = getMaxMp();
		}
		setCurrentMpDirect(currentMp);

		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
	}

	private L1Character _lastattacker;

	class Death implements Runnable {
		L1Character lastAttacker = _lastattacker;

		@Override
		public void run() {
			setDeathProcessing(true);
			setCurrentHpDirect(0);
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			int targetobjid = getId();
			getMap().setPassable(getLocation(), true);
			broadcastPacket(new S_DoActionGFX(targetobjid,
					ActionCodes.ACTION_Die));

			L1PcInstance player = null;
			if (lastAttacker instanceof L1PcInstance) {
				player = (L1PcInstance) lastAttacker;
			} else if (lastAttacker instanceof L1PetInstance) {
				player = (L1PcInstance) ((L1PetInstance) lastAttacker)
						.getMaster();
			} else if (lastAttacker instanceof L1SummonInstance) {
				player = (L1PcInstance) ((L1SummonInstance) lastAttacker)
						.getMaster();
			}
			if (player != null) {
				List<L1Character> targetList = _hateList.toTargetArrayList();
				List<Integer> hateList = _hateList.toHateArrayList();
				int exp = getExp();
				CalcExp.calcExp(player, targetobjid, targetList, hateList, exp);

				List<L1Character> dropTargetList = _dropHateList
						.toTargetArrayList();
				List<Integer> dropHateList = _dropHateList.toHateArrayList();
				try {
					DropTable.getInstance().dropShare(_npc, dropTargetList,
							dropHateList);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				// カルマは止めを刺したプレイヤーに設定。ペットorサモンで倒した場合も入る。
				player.addKarma((int) (getKarma() * Config.RATE_KARMA));
			}
			setDeathProcessing(false);

			setKarma(0);
			setExp(0);
			allTargetClear();

			startDeleteTimer();
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
	}

	public void doFinalAction(L1PcInstance player) {
	}

	private static final long REST_MILLISEC = 10000;

	private static final Timer _restTimer = new Timer(true);

	private RestMonitor _monitor;

	public class RestMonitor extends TimerTask {
		@Override
		public void run() {
			setRest(false);
		}
	}
}
