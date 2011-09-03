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

import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.PetItemTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetTypeTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PetFood;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PetCtrlMenu;
import l1j.server.server.serverpackets.S_PetMenuPacket;
import l1j.server.server.serverpackets.S_PetPack;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1PetItem;
import l1j.server.server.templates.L1PetType;
import l1j.server.server.utils.Random;

public class L1PetInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	private int _dir;

	// ターゲットがいない場合の処理
	@Override
	public boolean noTarget() {
		switch (_currentPetStatus) {
			case 3: // 休息
				return true;
			case 4: // 散開
				if ((_petMaster != null)
						&& (_petMaster.getMapId() == getMapId())
						&& (getLocation().getTileLineDistance(
								_petMaster.getLocation()) < 5)) {
					_dir = targetReverseDirection(_petMaster.getX(),
							_petMaster.getY());
					_dir = checkObject(getX(), getY(), getMapId(), _dir);
					setDirectionMove(_dir);
					setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
				} else { // 距離主人 5格以上休息
					_currentPetStatus = 3;
					return true;
				}
				return false;
			case 5: // 警戒
				if ((Math.abs(getHomeX() - getX()) > 1)
						|| (Math.abs(getHomeY() - getY()) > 1)) {
					int dir = moveDirection(getHomeX(), getHomeY());
					if (dir == -1) {
						setHomeX(getX());
						setHomeY(getY());
					} else {
						setDirectionMove(dir);
						setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
					}
				}
				return false;
			case 7: // 哨子呼叫
				if ((_petMaster != null)
						&& (_petMaster.getMapId() == getMapId())
						&& (getLocation().getTileLineDistance(
								_petMaster.getLocation()) <= 1)) {
					_currentPetStatus = 3;
					return true;
				}
				int locx = _petMaster.getX() + Random.nextInt(1);
				int locy = _petMaster.getY() + Random.nextInt(1);
				_dir = moveDirection(locx, locy);
				if (_dir == -1) {
					_currentPetStatus = 3;
					return true;
				}
				setDirectionMove(_dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
				return false;
			default:
				if ((_petMaster != null)
						&& (_petMaster.getMapId() == getMapId())) {
					if (getLocation().getTileLineDistance(_petMaster.getLocation()) > 2) {
						_dir = moveDirection(_petMaster.getX(), _petMaster.getY());
						setDirectionMove(_dir);
						setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
					}
				} else { // 與主人走失則休息
					_currentPetStatus = 3;
					return true;
				}
				return false;
		}
	}

	/** 領出寵物 */
	public L1PetInstance(L1Npc template, L1PcInstance master, L1Pet l1pet) {
		super(template);

		_petMaster = master;
		_itemObjId = l1pet.get_itemobjid();
		_type = PetTypeTable.getInstance().get(template.get_npcId());

		// ステータスを上書き
		setId(l1pet.get_objid());
		setName(l1pet.get_name());
		setLevel(l1pet.get_level());
		// HPMPはMAXとする
		setMaxHp(l1pet.get_hp());
		setCurrentHpDirect(l1pet.get_hp());
		setMaxMp(l1pet.get_mp());
		setCurrentMpDirect(l1pet.get_mp());
		setExp(l1pet.get_exp());
		setExpPercent(ExpTable.getExpPercentage(l1pet.get_level(),
				l1pet.get_exp()));
		setLawful(l1pet.get_lawful());
		setTempLawful(l1pet.get_lawful());
		set_food(l1pet.get_food());
		// 執行飽食度計時器
		startFoodTimer(this);

		setMaster(master);
		setX(master.getX() + Random.nextInt(5) - 2);
		setY(master.getY() + Random.nextInt(5) - 2);
		setMap(master.getMapId());
		setHeading(5);
		setLightSize(template.getLightSize());

		_currentPetStatus = 3;

		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.addPet(this);
	}

	/** 馴養寵物 */
	public L1PetInstance(L1NpcInstance target, L1PcInstance master, int itemid) {
		super(null);

		_petMaster = master;
		_itemObjId = itemid;
		_type = PetTypeTable.getInstance().get(
				target.getNpcTemplate().get_npcId());

		// ステータスを上書き
		setId(IdFactory.getInstance().nextId());
		setting_template(target.getNpcTemplate());
		setCurrentHpDirect(target.getCurrentHp());
		setCurrentMpDirect(target.getCurrentMp());
		setExp(750); // Lv.5のEXP
		setExpPercent(0);
		setLawful(0);
		setTempLawful(0);
		set_food(50); // 飽食度：普通
		startFoodTimer(this); // 執行飽食度計時器

		setMaster(master);
		setX(target.getX());
		setY(target.getY());
		setMap(target.getMapId());
		setHeading(target.getHeading());
		setLightSize(target.getLightSize());
		setPetcost(6);
		setInventory(target.getInventory());
		target.setInventory(null);

		_currentPetStatus = 3;
		/* 修正馴養後回血&回魔 */
		stopHpRegeneration();
		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}
		stopMpRegeneration();
		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
		target.deleteMe();
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}

		master.addPet(this);
		PetTable.getInstance().storeNewPet(target, getId(), itemid);
	}

	// 攻撃でＨＰを減らすときはここを使用
	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (getCurrentHp() > 0) {
			if (damage > 0) { // 回復の場合は攻撃しない。
				setHate(attacker, 0); // ペットはヘイト無し
				removeSkillEffect(FOG_OF_SLEEPING);
			}

			if ((attacker instanceof L1PcInstance) && (damage > 0)) {
				L1PcInstance player = (L1PcInstance) attacker;
				player.setPetTarget(this);
			}

			if (attacker instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) attacker;
				// 目標在安區、攻擊者在安區、NOPVP
				if ((getZoneType() == 1) || (pet.getZoneType() == 1)) {
					damage = 0;
				}
			} else if (attacker instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) attacker;
				// 目標在安區、攻擊者在安區、NOPVP
				if ((getZoneType() == 1) || (summon.getZoneType() == 1)) {
					damage = 0;
				}
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0) {
				death(attacker);
			} else {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) { // 念のため
			death(attacker);
		}
	}

	public synchronized void death(L1Character lastAttacker) {
		if (!isDead()) {
			setDead(true);
			// 停止飽食度計時器
			stopFoodTimer(this);
			setStatus(ActionCodes.ACTION_Die);
			setCurrentHp(0);

			getMap().setPassable(getLocation(), true);
			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));
		}
	}

	/** 寵物進化 */
	public void evolvePet(int new_itemobjid) {

		L1Pet l1pet = PetTable.getInstance().getTemplate(_itemObjId);
		if (l1pet == null) {
			return;
		}

		int newNpcId = _type.getNpcIdForEvolving();
		int evolvItem = _type.getEvolvItemId();
		// 取得進化前最大血魔
		int tmpMaxHp = getMaxHp();
		int tmpMaxMp = getMaxMp();

		transform(newNpcId);
		_type = PetTypeTable.getInstance().get(newNpcId);

		setLevel(1);
		// 進化後血魔減半
		setMaxHp(tmpMaxHp / 2);
		setMaxMp(tmpMaxMp / 2);
		setCurrentHpDirect(getMaxHp());
		setCurrentMpDirect(getMaxMp());
		setExp(0);
		setExpPercent(0);
		getInventory().consumeItem(evolvItem, 1); // 吃掉進化道具

		// 將原寵物身上道具移交到進化後的寵物身上
		L1Object obj= L1World.getInstance().findObject(l1pet.get_objid());
		if ((obj != null) && (obj instanceof L1NpcInstance)) {
			L1PetInstance new_pet = (L1PetInstance) obj;
			L1Inventory new_petInventory = new_pet.getInventory();
			List<L1ItemInstance> itemList = getInventory().getItems();
			for (Object itemObject : itemList) {
				L1ItemInstance item = (L1ItemInstance) itemObject;
				if (item == null) {
					continue;
				}
				if (item.isEquipped()) { // 裝備中
					item.setEquipped(false);
					L1PetItem petItem = PetItemTable.getInstance().getTemplate(item.getItemId());
					if (petItem.getUseType() == 1) { // 牙齒
						setWeapon(null);
						new_pet.usePetWeapon(this, item);
					} else if (petItem.getUseType() == 0) { // 盔甲
						setArmor(null);
						new_pet.usePetArmor(this, item);
					}
				}
				if (new_pet.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
					getInventory().tradeItem(item, item.getCount(), new_petInventory);
				} else { // 掉落地面
					new_petInventory = L1World.getInstance().getInventory(getX(),
							getY(), getMapId());
					getInventory().tradeItem(item, item.getCount(), new_petInventory);
				}
			}
			new_pet.broadcastPacket(new S_SkillSound(new_pet.getId(), 2127)); // 升級光芒
		}

		// 刪除原寵物資料
		PetTable.getInstance().deletePet(_itemObjId);

		// 紀錄新寵物資料
		l1pet.set_itemobjid(new_itemobjid);
		l1pet.set_npcid(newNpcId);
		l1pet.set_name(getName());
		l1pet.set_level(getLevel());
		l1pet.set_hp(getMaxHp());
		l1pet.set_mp(getMaxMp());
		l1pet.set_exp(getExp());
		l1pet.set_food(get_food());

		PetTable.getInstance().storeNewPet(this, getId(), new_itemobjid);

		_itemObjId = new_itemobjid;
		// 執行飽食度計時器
		if ((obj != null) && (obj instanceof L1NpcInstance)) {
			L1PetInstance new_pet = (L1PetInstance) obj;
			startFoodTimer(new_pet);
		}
	}

	/** 解放寵物 */
	public void liberate() {
		L1MonsterInstance monster = new L1MonsterInstance(getNpcTemplate());
		monster.setId(IdFactory.getInstance().nextId());

		monster.setX(getX());
		monster.setY(getY());
		monster.setMap(getMapId());
		monster.setHeading(getHeading());
		monster.set_storeDroped(true);
		monster.setInventory(getInventory());
		setInventory(null);
		monster.setLevel(getLevel());
		monster.setMaxHp(getMaxHp());
		monster.setCurrentHpDirect(getCurrentHp());
		monster.setMaxMp(getMaxMp());
		monster.setCurrentMpDirect(getCurrentMp());

		_petMaster.getPetList().remove(getId());
		if (_petMaster.getPetList().isEmpty()) {
			_petMaster.sendPackets(new S_PetCtrlMenu(_master, monster, false));// 關閉寵物控制圖形介面
		}

		deleteMe();

		// DBとPetTableから削除し、ペットアミュも破棄
		_petMaster.getInventory().removeItem(_itemObjId, 1);
		PetTable.getInstance().deletePet(_itemObjId);

		L1World.getInstance().storeObject(monster);
		L1World.getInstance().addVisibleObject(monster);
		for (L1PcInstance pc : L1World.getInstance()
				.getRecognizePlayer(monster)) {
			onPerceive(pc);
		}
	}

	// 收集寵物身上道具
	public void collect(boolean isDepositnpc) {
		L1Inventory targetInventory = _petMaster.getInventory();
		List<L1ItemInstance> itemList = getInventory().getItems();
		for (Object itemObject : itemList) {
			L1ItemInstance item = (L1ItemInstance) itemObject;
			if (item == null) {
				continue;
			}
			if (item.isEquipped()) { // 裝備中
				if (!isDepositnpc) { // 非寄放寵物
					continue;
				} else {
					L1PetItem petItem = PetItemTable.getInstance().getTemplate(item.getItemId());
					if (petItem.getUseType() == 1) { // 牙齒
						setWeapon(null);
					} else if (petItem.getUseType() == 0) { // 盔甲
						setArmor(null);
					}
					item.setEquipped(false);
				}
			}
			if (_petMaster.getInventory().checkAddItem( // 主人可否拿取判斷
					item, item.getCount()) == L1Inventory.OK) {
				getInventory().tradeItem(item, item.getCount(), targetInventory);
				_petMaster.sendPackets(new S_ServerMessage(143, getName(), item.getLogName()));
			} else { // 掉落地面
				targetInventory = L1World.getInstance().getInventory(getX(),
						getY(), getMapId());
				getInventory().tradeItem(item, item.getCount(), targetInventory);
			}
		}
	}

	// 重登時寵物身上道具掉落地面
	public void dropItem() {
		L1Inventory targetInventory = L1World.getInstance().getInventory(
				getX(), getY(), getMapId());
		List<L1ItemInstance> items = _inventory.getItems();
		int size = _inventory.getSize();
		for (int i = 0; i < size; i++) {
			L1ItemInstance item = items.get(0);
			if (item.isEquipped()) { // 裝備中
				L1PetItem petItem = PetItemTable.getInstance().getTemplate(item.getItemId());
				if (petItem.getUseType() == 1) { // 牙齒
					setWeapon(null);
				} else if (petItem.getUseType() == 0) { // 盔甲
					setArmor(null);
				}
				item.setEquipped(false);
			}
			_inventory.tradeItem(item, item.getCount(), targetInventory);
		}
	}

	// 哨子呼叫寵物
	public void call() {
		int id = _type.getMessageId(L1PetType.getMessageNumber(getLevel()));
		if (id != 0 && !isDead()) {
			if (get_food() == 0) {
				id = _type.getDefyMessageId();
			}
			broadcastPacket(new S_NpcChatPacket(this, "$" + id, 0));
		}

		if (get_food() > 0) {
			setCurrentPetStatus(7); // 前往主人身邊並休息
		} else {
			setCurrentPetStatus(3); // 休息
		}
	}

	public void setTarget(L1Character target) {
		if ((target != null)
				&& ((_currentPetStatus == 1) || (_currentPetStatus == 2) || (_currentPetStatus == 5))
				&& (get_food() > 0)) {
			setHate(target, 0);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	public void setMasterTarget(L1Character target) {
		if ((target != null)
				&& ((_currentPetStatus == 1) || (_currentPetStatus == 5))
				&& (get_food() > 0)) {
			setHate(target, 0);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_PetPack(this, perceivedFrom)); // ペット系オブジェクト認識
		if (isDead()) {
			perceivedFrom.sendPackets(new S_DoActionGFX(getId(),
					ActionCodes.ACTION_Die));
		}
	}

	@Override
	public void onAction(L1PcInstance pc) {
		onAction(pc, 0);
	}

	@Override
	public void onAction(L1PcInstance pc, int skillId) {
		L1Character cha = getMaster();
		L1PcInstance master = (L1PcInstance) cha;
		if (master.isTeleport()) { // テレポート処理中
			return;
		}
		if (getZoneType() == 1) { // 攻撃される側がセーフティーゾーン
			L1Attack attack_mortion = new L1Attack(pc, this, skillId); // 攻撃モーション送信
			attack_mortion.action();
			return;
		}

		if (pc.checkNonPvP(pc, this)) {
			return;
		}

		L1Attack attack = new L1Attack(pc, this, skillId);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		if (isDead()) {
			return;
		}
		if (_petMaster.equals(player)) {
			player.sendPackets(new S_PetMenuPacket(this, getExpPercent()));
			L1Pet l1pet = PetTable.getInstance().getTemplate(_itemObjId);
			// XXX ペットに話しかけるたびにDBに書き込む必要はない
			if (l1pet != null) {
				l1pet.set_exp(getExp());
				l1pet.set_level(getLevel());
				l1pet.set_hp(getMaxHp());
				l1pet.set_mp(getMaxMp());
				l1pet.set_food(get_food());
				PetTable.getInstance().storePet(l1pet); // DBに書き込み
			}
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
		int status = actionType(action);
		if (status == 0) {
			return;
		}
		if (status == 6) {
			L1PcInstance petMaster = (L1PcInstance) _master;
			liberate(); // ペットの解放
			// 更新寵物控制介面
			Object[] petList = petMaster.getPetList().values().toArray();
			for (Object petObject : petList) {
				if (petObject instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) petObject;
					petMaster.sendPackets(new S_SummonPack(summon, petMaster));
					return;
				} else if (petObject instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) petObject;
					petMaster.sendPackets(new S_PetPack(pet, petMaster));
					return;
				}
			}
		} else {
			// 同じ主人のペットの状態をすべて更新
			Object[] petList = _petMaster.getPetList().values().toArray();
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) { // 寵物
					L1PetInstance pet = (L1PetInstance) petObject;
					if ((_petMaster != null)
							&& (_petMaster.getLevel() >= pet.getLevel()) && pet.get_food() > 0) {
						pet.setCurrentPetStatus(status);
					} else {
						if (!pet.isDead()) {
							L1PetType type = PetTypeTable.getInstance().get(
									pet.getNpcTemplate().get_npcId());
							int id = type.getDefyMessageId();
							if (id != 0) {
								pet.broadcastPacket(new S_NpcChatPacket(pet, "$" + id, 0));
							}
						}
					}
				} else if (petObject instanceof L1SummonInstance) { // 召喚獸
					L1SummonInstance summon = (L1SummonInstance) petObject;
					summon.set_currentPetStatus(status);
				}
			}
		}
	}

	@Override
	public void onItemUse() {
		if (!isActived()) {
			useItem(USEITEM_HASTE, 100); // １００％の確率でヘイストポーション使用
		}
		if (getCurrentHp() * 100 / getMaxHp() < 40) { // ＨＰが４０％きったら
			useItem(USEITEM_HEAL, 100); // １００％の確率で回復ポーション使用
		}
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		if (getNpcTemplate().get_digestitem() > 0) {
			setDigestItem(item);
		}
		Arrays.sort(healPotions);
		Arrays.sort(haestPotions);
		if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
			if (getCurrentHp() != getMaxHp()) {
				useItem(USEITEM_HEAL, 100);
			}
		} else if (Arrays
				.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
			useItem(USEITEM_HASTE, 100);
		}
	}

	private int actionType(String action) {
		int status = 0;
		if (action.equalsIgnoreCase("aggressive")) { // 攻撃態勢
			status = 1;
		} else if (action.equalsIgnoreCase("defensive")) { // 防御態勢
			status = 2;
		} else if (action.equalsIgnoreCase("stay")) { // 休憩
			status = 3;
		} else if (action.equalsIgnoreCase("extend")) { // 配備
			status = 4;
		} else if (action.equalsIgnoreCase("alert")) { // 警戒
			status = 5;
		} else if (action.equalsIgnoreCase("dismiss")) { // 解散
			status = 6;
		} else if (action.equalsIgnoreCase("getitem")) { // 収集
			collect(false);
		}
		return status;
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

		if (_petMaster != null) {
			int HpRatio = 100 * currentHp / getMaxHp();
			L1PcInstance Master = _petMaster;
			Master.sendPackets(new S_HPMeter(getId(), HpRatio));
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

	public void setCurrentPetStatus(int i) {
		_currentPetStatus = i;
		if (_currentPetStatus == 5) {
			setHomeX(getX());
			setHomeY(getY());
		}
		if (_currentPetStatus == 7) {
			allTargetClear();
		}

		if (_currentPetStatus == 3) {
			allTargetClear();
		} else {
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	public int getCurrentPetStatus() {
		return _currentPetStatus;
	}

	public int getItemObjId() {
		return _itemObjId;
	}

	public void setExpPercent(int expPercent) {
		_expPercent = expPercent;
	}

	public int getExpPercent() {
		return _expPercent;
	}

	private L1ItemInstance _weapon;

	public void setWeapon(L1ItemInstance weapon) {
		_weapon = weapon;
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	private L1ItemInstance _armor;

	public void setArmor(L1ItemInstance armor) {
		_armor = armor;
	}

	public L1ItemInstance getArmor() {
		return _armor;
	}

	private int _hitByWeapon;

	public void setHitByWeapon(int i) {
		_hitByWeapon = i;
	}

	public int getHitByWeapon() {
		return _hitByWeapon;
	}

	private int _damageByWeapon;

	public void setDamageByWeapon(int i) {
		_damageByWeapon = i;
	}

	public int getDamageByWeapon() {
		return _damageByWeapon;
	}

	private int _currentPetStatus;

	private L1PcInstance _petMaster;

	private int _itemObjId;

	private L1PetType _type;

	private int _expPercent;

	public L1PetType getPetType() {
		return _type;
	}

	// 寵物飽食度計時器
	private L1PetFood _petFood;

	public void startFoodTimer(L1PetInstance pet) {
		_petFood = new L1PetFood(pet, _itemObjId);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(_petFood, 1000, 20000); // 每 X秒減少
	}

	public void stopFoodTimer(L1PetInstance pet) {
		if (_petFood != null) {
			_petFood.cancel();
			_petFood = null;
		}
	}

	// 使用寵物裝備
	public void usePetWeapon(L1PetInstance pet, L1ItemInstance weapon) {
		if (pet.getWeapon() == null) {
			setPetWeapon(pet, weapon);
		}
		else { // 既に何かを装備している場合、前の装備をはずす
			if (pet.getWeapon().equals(weapon)) {
				removePetWeapon(pet, pet.getWeapon());
			}
			else {
				removePetWeapon(pet, pet.getWeapon());
				setPetWeapon(pet, weapon);
			}
		}
	}

	public void usePetArmor(L1PetInstance pet, L1ItemInstance armor) {
		if (pet.getArmor() == null) {
			setPetArmor(pet, armor);
		}
		else { // 既に何かを装備している場合、前の装備をはずす
			if (pet.getArmor().equals(armor)) {
				removePetArmor(pet, pet.getArmor());
			}
			else {
				removePetArmor(pet, pet.getArmor());
				setPetArmor(pet, armor);
			}
		}
	}

	private void setPetWeapon(L1PetInstance pet, L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.setHitByWeapon(petItem.getHitModifier());
		pet.setDamageByWeapon(petItem.getDamageModifier());
		pet.addStr(petItem.getAddStr());
		pet.addCon(petItem.getAddCon());
		pet.addDex(petItem.getAddDex());
		pet.addInt(petItem.getAddInt());
		pet.addWis(petItem.getAddWis());
		pet.addMaxHp(petItem.getAddHp());
		pet.addMaxMp(petItem.getAddMp());
		pet.addSp(petItem.getAddSp());
		pet.addMr(petItem.getAddMr());

		pet.setWeapon(weapon);
		weapon.setEquipped(true);
	}

	private void removePetWeapon(L1PetInstance pet, L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.setHitByWeapon(0);
		pet.setDamageByWeapon(0);
		pet.addStr(-petItem.getAddStr());
		pet.addCon(-petItem.getAddCon());
		pet.addDex(-petItem.getAddDex());
		pet.addInt(-petItem.getAddInt());
		pet.addWis(-petItem.getAddWis());
		pet.addMaxHp(-petItem.getAddHp());
		pet.addMaxMp(-petItem.getAddMp());
		pet.addSp(-petItem.getAddSp());
		pet.addMr(-petItem.getAddMr());

		pet.setWeapon(null);
		weapon.setEquipped(false);
	}

	private void setPetArmor(L1PetInstance pet, L1ItemInstance armor) {
		int itemId = armor.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.addAc(petItem.getAddAc());
		pet.addStr(petItem.getAddStr());
		pet.addCon(petItem.getAddCon());
		pet.addDex(petItem.getAddDex());
		pet.addInt(petItem.getAddInt());
		pet.addWis(petItem.getAddWis());
		pet.addMaxHp(petItem.getAddHp());
		pet.addMaxMp(petItem.getAddMp());
		pet.addSp(petItem.getAddSp());
		pet.addMr(petItem.getAddMr());

		pet.setArmor(armor);
		armor.setEquipped(true);
	}

	private void removePetArmor(L1PetInstance pet, L1ItemInstance armor) {
		int itemId = armor.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.addAc(-petItem.getAddAc());
		pet.addStr(-petItem.getAddStr());
		pet.addCon(-petItem.getAddCon());
		pet.addDex(-petItem.getAddDex());
		pet.addInt(-petItem.getAddInt());
		pet.addWis(-petItem.getAddWis());
		pet.addMaxHp(-petItem.getAddHp());
		pet.addMaxMp(-petItem.getAddMp());
		pet.addSp(-petItem.getAddSp());
		pet.addMr(-petItem.getAddMr());

		pet.setArmor(null);
		armor.setEquipped(false);
	}
}
