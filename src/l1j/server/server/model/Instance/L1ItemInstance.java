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

import static l1j.server.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.ENCHANT_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.HOLY_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.SHADOW_FANG;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetItemTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.L1EquipmentTimer;
import l1j.server.server.model.L1ItemOwnerTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.identity.L1ArmorId;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1PetItem;
import l1j.server.server.utils.BinaryOutputStream;

// Referenced classes of package l1j.server.server.model:
// L1Object, L1PcInstance

public class L1ItemInstance extends L1Object {
	private static final long serialVersionUID = 1L;

	private int _count;

	private int _itemId;

	private L1Item _item;

	private boolean _isEquipped = false;

	private int _enchantLevel;

	private boolean _isIdentified = false;

	private int _durability;

	private int _chargeCount;

	private int _remainingTime;

	private Timestamp _lastUsed = null;

	private int _lastWeight;

	private final LastStatus _lastStatus = new LastStatus();

	private L1PcInstance _pc;

	private boolean _isRunning = false;

	private EnchantTimer _timer;

	private int _bless;

	private int _attrEnchantKind;

	private int _attrEnchantLevel;

	public L1ItemInstance() {
		_count = 1;
		_enchantLevel = 0;
	}

	public L1ItemInstance(L1Item item, int count) {
		this();
		setItem(item);
		setCount(count);
	}

	/**
	 * アイテムが確認(鑑定)済みであるかを返す。
	 * 
	 * @return 確認済みならtrue、未確認ならfalse。
	 */
	public boolean isIdentified() {
		return _isIdentified;
	}

	/**
	 * アイテムが確認(鑑定)済みであるかを設定する。
	 * 
	 * @param identified
	 *            確認済みならtrue、未確認ならfalse。
	 */
	public void setIdentified(boolean identified) {
		_isIdentified = identified;
	}

	public String getName() {
		return _item.getName();
	}

	/**
	 * アイテムの個数を返す。
	 * 
	 * @return アイテムの個数
	 */
	public int getCount() {
		return _count;
	}

	/**
	 * アイテムの個数を設定する。
	 * 
	 * @param count
	 *            アイテムの個数
	 */
	public void setCount(int count) {
		_count = count;
	}

	/**
	 * アイテムが装備されているかを返す。
	 * 
	 * @return アイテムが装備されていればtrue、装備されていなければfalse。
	 */
	public boolean isEquipped() {
		return _isEquipped;
	}

	/**
	 * アイテムが装備されているかを設定する。
	 * 
	 * @param equipped
	 *            アイテムが装備されていればtrue,装備されていなければfalse。
	 */
	public void setEquipped(boolean equipped) {
		_isEquipped = equipped;
	}

	public L1Item getItem() {
		return _item;
	}

	public void setItem(L1Item item) {
		_item = item;
		_itemId = item.getItemId();
	}

	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int itemId) {
		_itemId = itemId;
	}

	public boolean isStackable() {
		return _item.isStackable();
	}

	@Override
	public void onAction(L1PcInstance player) {
	}

	public int getEnchantLevel() {
		return _enchantLevel;
	}

	public void setEnchantLevel(int enchantLevel) {
		_enchantLevel = enchantLevel;
	}

	public int get_gfxid() {
		return _item.getGfxId();
	}

	public int get_durability() {
		return _durability;
	}

	public int getChargeCount() {
		return _chargeCount;
	}

	public void setChargeCount(int i) {
		_chargeCount = i;
	}

	public int getRemainingTime() {
		return _remainingTime;
	}

	public void setRemainingTime(int i) {
		_remainingTime = i;
	}

	public void setLastUsed(Timestamp t) {
		_lastUsed = t;
	}

	public Timestamp getLastUsed() {
		return _lastUsed;
	}

	public int getLastWeight() {
		return _lastWeight;
	}

	public void setLastWeight(int weight) {
		_lastWeight = weight;
	}

	public void setBless(int i) {
		_bless = i;
	}

	public int getBless() {
		return _bless;
	}

	public void setAttrEnchantKind(int i) {
		_attrEnchantKind = i;
	}

	public int getAttrEnchantKind() {
		return _attrEnchantKind;
	}

	public void setAttrEnchantLevel(int i) {
		_attrEnchantLevel = i;
	}

	public int getAttrEnchantLevel() {
		return _attrEnchantLevel;
	}

	public int getMr() {
		int mr = _item.get_mdef();
		if ((getItemId() == L1ArmorId.HELMET_OF_MAGIC_RESISTANCE) || (getItemId() == L1ArmorId.CHAIN_MAIL_OF_MAGIC_RESISTANCE) // 抗魔法頭盔、抗魔法鏈甲
				|| (getItemId() >= L1ArmorId.ELITE_PLATE_MAIL_OF_LINDVIOR && getItemId() <= L1ArmorId.ELITE_SCALE_MAIL_OF_LINDVIOR) // 林德拜爾的力量、林德拜爾的魅惑、林德拜爾的泉源、林德拜爾的霸氣
				|| (getItemId() == L1ArmorId.B_HELMET_OF_MAGIC_RESISTANCE)) { // 受祝福的 抗魔法頭盔
			mr += getEnchantLevel();
		}
		if ((getItemId() == L1ArmorId.CLOAK_OF_MAGIC_RESISTANCE) || (getItemId() == L1ArmorId.B_CLOAK_OF_MAGIC_RESISTANCE) // 抗魔法斗篷、受祝福的 抗魔法斗篷
				|| (getItemId() == L1ArmorId.C_CLOAK_OF_MAGIC_RESISTANCE)) { // 受咀咒的 抗魔法斗篷
			mr += getEnchantLevel() * 2;
		}
		// 飾品強化效果
		if (getM_Def() != 0) {
			mr += getM_Def();
		}
		return mr;
	}

	/*
	 * 耐久性、0~127まで -の値は許可しない。
	 */
	public void set_durability(int i) {
		if (i < 0) {
			i = 0;
		}

		if (i > 127) {
			i = 127;
		}
		_durability = i;
	}

	public int getWeight() {
		if (getItem().getWeight() == 0) {
			return 0;
		} else {
			return Math.max(getCount() * getItem().getWeight() / 1000, 1);
		}
	}

	/**
	 * 前回DBへ保存した際のアイテムのステータスを格納するクラス
	 */
	public class LastStatus {
		public int count;

		public int itemId;

		public boolean isEquipped = false;

		public int enchantLevel;

		public boolean isIdentified = true;

		public int durability;

		public int chargeCount;

		public int remainingTime;

		public Timestamp lastUsed = null;

		public int bless;

		public int attrEnchantKind;

		public int attrEnchantLevel;

		public int firemr; // Scroll of Enchant Accessory

		public int watermr;

		public int earthmr;

		public int windmr;

		public int addhp;

		public int addmp;

		public int hpr;

		public int mpr;

		public int addsp;

		public int m_def;

		public void updateAll() {
			count = getCount();
			itemId = getItemId();
			isEquipped = isEquipped();
			isIdentified = isIdentified();
			enchantLevel = getEnchantLevel();
			durability = get_durability();
			chargeCount = getChargeCount();
			remainingTime = getRemainingTime();
			lastUsed = getLastUsed();
			bless = getBless();
			attrEnchantKind = getAttrEnchantKind();
			attrEnchantLevel = getAttrEnchantLevel();
			firemr = getFireMr();
			watermr = getWaterMr();
			earthmr = getEarthMr();
			windmr = getWindMr();
			addhp = getaddHp();
			addmp = getaddMp();
			addsp = getaddSp();
			hpr = getHpr();
			mpr = getMpr();
			m_def = getM_Def();
		}

		public void updateCount() {
			count = getCount();
		}

		public void updateItemId() {
			itemId = getItemId();
		}

		public void updateEquipped() {
			isEquipped = isEquipped();
		}

		public void updateIdentified() {
			isIdentified = isIdentified();
		}

		public void updateEnchantLevel() {
			enchantLevel = getEnchantLevel();
		}

		public void updateDuraility() {
			durability = get_durability();
		}

		public void updateChargeCount() {
			chargeCount = getChargeCount();
		}

		public void updateRemainingTime() {
			remainingTime = getRemainingTime();
		}

		public void updateLastUsed() {
			lastUsed = getLastUsed();
		}

		public void updateBless() {
			bless = getBless();
		}

		public void updateAttrEnchantKind() {
			attrEnchantKind = getAttrEnchantKind();
		}

		public void updateAttrEnchantLevel() {
			attrEnchantLevel = getAttrEnchantLevel();
		}

		public void updateFireMr() {
			firemr = getFireMr();
		}

		public void updateWaterMr() {
			watermr = getWaterMr();
		}

		public void updateEarthMr() {
			earthmr = getEarthMr();
		}

		public void updateWindMr() {
			windmr = getWindMr();
		}

		public void updateSp() {
			addsp = getaddSp();
		}

		public void updateaddHp() {
			addhp = getaddHp();
		}

		public void updateaddMp() {
			addmp = getaddMp();
		}

		public void updateHpr() {
			hpr = getHpr();
		}

		public void updateMpr() {
			mpr = getMpr();
		}

		public void updateM_Def() {
			m_def = getM_Def();
		}
	}

	public LastStatus getLastStatus() {
		return _lastStatus;
	}

	/**
	 * 前回DBに保存した時から変化しているカラムをビット集合として返す。
	 */
	public int getRecordingColumns() {
		int column = 0;

		if (getCount() != _lastStatus.count) {
			column += L1PcInventory.COL_COUNT;
		}
		if (getItemId() != _lastStatus.itemId) {
			column += L1PcInventory.COL_ITEMID;
		}
		if (isEquipped() != _lastStatus.isEquipped) {
			column += L1PcInventory.COL_EQUIPPED;
		}
		if (getEnchantLevel() != _lastStatus.enchantLevel) {
			column += L1PcInventory.COL_ENCHANTLVL;
		}
		if (get_durability() != _lastStatus.durability) {
			column += L1PcInventory.COL_DURABILITY;
		}
		if (getChargeCount() != _lastStatus.chargeCount) {
			column += L1PcInventory.COL_CHARGE_COUNT;
		}
		if (getLastUsed() != _lastStatus.lastUsed) {
			column += L1PcInventory.COL_DELAY_EFFECT;
		}
		if (isIdentified() != _lastStatus.isIdentified) {
			column += L1PcInventory.COL_IS_ID;
		}
		if (getRemainingTime() != _lastStatus.remainingTime) {
			column += L1PcInventory.COL_REMAINING_TIME;
		}
		if (getBless() != _lastStatus.bless) {
			column += L1PcInventory.COL_BLESS;
		}
		if (getAttrEnchantKind() != _lastStatus.attrEnchantKind) {
			column += L1PcInventory.COL_ATTR_ENCHANT_KIND;
		}
		if (getAttrEnchantLevel() != _lastStatus.attrEnchantLevel) {
			column += L1PcInventory.COL_ATTR_ENCHANT_LEVEL;
		}

		return column;
	}

	public int getRecordingColumnsEnchantAccessory() {
		int column = 0;

		if (getaddHp() != _lastStatus.addhp) {
			column += L1PcInventory.COL_ADDHP;
		}
		if (getaddMp() != _lastStatus.addmp) {
			column += L1PcInventory.COL_ADDMP;
		}
		if (getHpr() != _lastStatus.hpr) {
			column += L1PcInventory.COL_HPR;
		}
		if (getMpr() != _lastStatus.mpr) {
			column += L1PcInventory.COL_MPR;
		}
		if (getaddSp() != _lastStatus.addsp) {
			column += L1PcInventory.COL_ADDSP;
		}
		if (getM_Def() != _lastStatus.m_def) {
			column += L1PcInventory.COL_M_DEF;
		}
		if (getEarthMr() != _lastStatus.earthmr) {
			column += L1PcInventory.COL_EARTHMR;
		}
		if (getFireMr() != _lastStatus.firemr) {
			column += L1PcInventory.COL_FIREMR;
		}
		if (getWaterMr() != _lastStatus.watermr) {
			column += L1PcInventory.COL_WATERMR;
		}
		if (getWindMr() != _lastStatus.windmr) {
			column += L1PcInventory.COL_WINDMR;
		}

		return column;
	}

	/**
	 * 鞄や倉庫で表示される形式の名前を個数を指定して取得する。<br>
	 */
	public String getNumberedViewName(int count) {
		StringBuilder name = new StringBuilder(getNumberedName(count));
		int itemType2 = getItem().getType2();
		int itemId = getItem().getItemId();

		if ((itemId == 40314) || (itemId == 40316)) { // ペットのアミュレット
			L1Pet pet = PetTable.getInstance().getTemplate(getId());
			if (pet != null) {
				L1Npc npc = NpcTable.getInstance().getTemplate(pet.get_npcid());
				// name.append("[Lv." + pet.get_level() + " "
				// + npc.get_nameid() + "]");
				name.append("[Lv." + pet.get_level() + " " + pet.get_name()
						+ "]HP" + pet.get_hp() + " " + npc.get_nameid());
			}
		}

		if ((getItem().getType2() == 0) && (getItem().getType() == 2)) { // light系アイテム
			if (isNowLighting()) {
				name.append(" ($10)");
			}
			if ((itemId == 40001) || (itemId == 40002)) { // ランプorランタン
				if (getRemainingTime() <= 0) {
					name.append(" ($11)");
				}
			}
		}

		if (isEquipped()) {
			if (itemType2 == 1) {
				name.append(" ($9)"); // 装備(Armed)
			} else if (itemType2 == 2) {
				name.append(" ($117)"); // 装備(Worn)
			}
		}
		return name.toString();
	}

	/**
	 * 鞄や倉庫で表示される形式の名前を返す。<br>
	 * 例:+10 カタナ (装備)
	 */
	public String getViewName() {
		return getNumberedViewName(_count);
	}

	/**
	 * ログに表示される形式の名前を返す。<br>
	 * 例:アデナ(250) / +6 ダガー
	 */
	public String getLogName() {
		return getNumberedName(_count);
	}

	/**
	 * ログに表示される形式の名前を、個数を指定して取得する。
	 */
	public String getNumberedName(int count) {
		StringBuilder name = new StringBuilder();

		if (isIdentified()) {
			if (getItem().getType2() == 1) { // 武器
				int attrEnchantLevel = getAttrEnchantLevel();
				if (attrEnchantLevel > 0) {
					String attrStr = null;
					switch (getAttrEnchantKind()) {
					case 1: // 地
						if (attrEnchantLevel == 1) {
							attrStr = "$6124";
						} else if (attrEnchantLevel == 2) {
							attrStr = "$6125";
						} else if (attrEnchantLevel == 3) {
							attrStr = "$6126";
						}
						break;
					case 2: // 火
						if (attrEnchantLevel == 1) {
							attrStr = "$6115";
						} else if (attrEnchantLevel == 2) {
							attrStr = "$6116";
						} else if (attrEnchantLevel == 3) {
							attrStr = "$6117";
						}
						break;
					case 4: // 水
						if (attrEnchantLevel == 1) {
							attrStr = "$6118";
						} else if (attrEnchantLevel == 2) {
							attrStr = "$6119";
						} else if (attrEnchantLevel == 3) {
							attrStr = "$6120";
						}
						break;
					case 8: // 風
						if (attrEnchantLevel == 1) {
							attrStr = "$6121";
						} else if (attrEnchantLevel == 2) {
							attrStr = "$6122";
						} else if (attrEnchantLevel == 3) {
							attrStr = "$6123";
						}
						break;
					default:
						break;
					}
					name.append(attrStr + " ");
				}
			}
			if ((getItem().getType2() == 1) || (getItem().getType2() == 2)) { // 武器・防具
				if (getEnchantLevel() >= 0) {
					name.append("+" + getEnchantLevel() + " ");
				} else if (getEnchantLevel() < 0) {
					name.append(String.valueOf(getEnchantLevel()) + " ");
				}
			}
		}
		if (isIdentified()) {
			name.append(_item.getIdentifiedNameId());
		} else {
			name.append(_item.getUnidentifiedNameId());
		}
		if (isIdentified()) {
			if (getItem().getMaxChargeCount() > 0) {
				name.append(" (" + getChargeCount() + ")");
			}
			if (getItem().getItemId() == 20383) { // 騎馬用ヘルム
				name.append(" (" + getChargeCount() + ")");
			}
			if ((getItem().getMaxUseTime() > 0) && (getItem().getType2() != 0)) { // 武器防具で使用時間制限あり
				name.append(" [" + getRemainingTime() + "]");
			}
		}

		// 旅館鑰匙
		if (getItem().getItemId() == 40312 && getKeyId() != 0) {
			name.append(getInnKeyName());
		}

		if (count > 1) {
			name.append(" (" + count + ")");
		}

		return name.toString();
	}

	// 旅館鑰匙
	public String getInnKeyName() {
		StringBuilder name = new StringBuilder();
		name.append(" #");
		String chatText = String.valueOf(getKeyId());
		String s1 = "";
		String s2 = "";
		for (int i = 0 ; i < chatText.length(); i++) {
			if (i >= 5) {
				break;
			}
			s1 =  s1 + String.valueOf(chatText.charAt(i));
		}
		name.append(s1);
		for (int i = 0 ; i < chatText.length(); i++) {
			if ((i % 2) == 0) {
				s1 =  String.valueOf(chatText.charAt(i));
			} else {
				s2 = s1 + String.valueOf(chatText.charAt(i));
				name.append(Integer.toHexString(Integer.valueOf(s2)).toLowerCase()); // 轉成16進位
			}
		}
		return name.toString();
	}

	/**
	 * アイテムの状態からサーバーパケットで利用する形式のバイト列を生成し、返す。
	 */
	public byte[] getStatusBytes() {
		int itemType2 = getItem().getType2();
		int itemId = getItemId();
		BinaryOutputStream os = new BinaryOutputStream();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);

		if (petItem != null) { // 寵物裝備
			if (petItem.getUseType() == 1) { // 牙齒
				os.writeC(7); // 可使用職業：
				os.writeC(128); // [高等寵物]
				os.writeC(23); // 材質
				os.writeC(getItem().getMaterial());
				os.writeD(getWeight());
			} else { // 盔甲
				// AC
				os.writeC(19);
				int ac = petItem.getAddAc();
				if (ac < 0) {
					ac = ac - ac - ac;
				}
				os.writeC(ac);
				os.writeC(getItem().getMaterial());
				os.writeC(-1); // 飾品級別 - 0:上等 1:中等 2:初級 3:特等
				os.writeD(getWeight());

				os.writeC(7); // 可使用職業：
				os.writeC(128); // [高等寵物]

				// STR~CHA
				if (petItem.getAddStr() != 0) {
					os.writeC(8);
					os.writeC(petItem.getAddStr());
				}
				if (petItem.getAddDex() != 0) {
					os.writeC(9);
					os.writeC(petItem.getAddDex());
				}
				if (petItem.getAddCon() != 0) {
					os.writeC(10);
					os.writeC(petItem.getAddCon());
				}
				if (petItem.getAddWis() != 0) {
					os.writeC(11);
					os.writeC(petItem.getAddWis());
				}
				if (petItem.getAddInt() != 0) {
					os.writeC(12);
					os.writeC(petItem.getAddInt());
				}
				// HP, MP
				if (petItem.getAddHp() != 0) {
					os.writeC(14);
					os.writeH(petItem.getAddHp());
				}
				if (petItem.getAddMp() != 0) {
					os.writeC(32);
					os.writeC(petItem.getAddMp());
				}
				// MR
				if (petItem.getAddMr() != 0) {
					os.writeC(15);
					os.writeH(petItem.getAddMr());
				}
				// SP(魔力)
				if (petItem.getAddSp() != 0) {
					os.writeC(17);
					os.writeC(petItem.getAddSp());
				}
			}
		} else if (itemType2 == 0) { // etcitem
			switch (getItem().getType()) {
			case 2: // light
				os.writeC(22); // 明るさ
				os.writeH(getItem().getLightRange());
				break;
			case 7: // food
				os.writeC(21);
				// 栄養
				os.writeH(getItem().getFoodVolume());
				break;
			case 0: // arrow
			case 15: // sting
				os.writeC(1); // 打撃値
				os.writeC(getItem().getDmgSmall());
				os.writeC(getItem().getDmgLarge());
				break;
			default:
				os.writeC(23); // 材質
				break;
			}
			os.writeC(getItem().getMaterial());
			os.writeD(getWeight());
		} else if ((itemType2 == 1) || (itemType2 == 2)) { // weapon | armor
			if (itemType2 == 1) { // weapon
				// 打撃値
				os.writeC(1);
				os.writeC(getItem().getDmgSmall());
				os.writeC(getItem().getDmgLarge());
				os.writeC(getItem().getMaterial());
				os.writeD(getWeight());
			} else if (itemType2 == 2) { // armor
				// AC
				os.writeC(19);
				int ac = ((L1Armor) getItem()).get_ac();
				if (ac < 0) {
					ac = ac - ac - ac;
				}
				os.writeC(ac);
				os.writeC(getItem().getMaterial());
				os.writeC(getItem().getGrade()); // 飾品級別 - 0:上等 1:中等 2:初級 3:特等
				os.writeD(getWeight());
			}
			/** 強化數判斷 */
			if (getEnchantLevel() != 0) {
				os.writeC(2);
				/** 飾品強化卷軸 */
				if (getItem().getType2() == 2 && getItem().getType() >= 8
						&& getItem().getType() <= 12) { // 8:項鍊 9:戒指1 10:腰帶
														// 11:戒指2 12:耳環
					os.writeC(0);
				} else {
					os.writeC(getEnchantLevel());
				}
			}
			// 損傷度
			if (get_durability() != 0) {
				os.writeC(3);
				os.writeC(get_durability());
			}
			// 両手武器
			if (getItem().isTwohandedWeapon()) {
				os.writeC(4);
			}
			// 攻撃成功
			if (itemType2 == 1) { // weapon
				if (getItem().getHitModifier() != 0) {
					os.writeC(5);
					os.writeC(getItem().getHitModifier());
				}
			} else if (itemType2 == 2) { // armor
				if (getItem().getHitModifierByArmor() != 0) {
					os.writeC(5);
					os.writeC(getItem().getHitModifierByArmor());
				}
			}
			// 追加打撃
			if (itemType2 == 1) { // weapon
				if (getItem().getDmgModifier() != 0) {
					os.writeC(6);
					os.writeC(getItem().getDmgModifier());
				}
			} else if (itemType2 == 2) { // armor
				if (getItem().getDmgModifierByArmor() != 0) {
					os.writeC(6);
					os.writeC(getItem().getDmgModifierByArmor());
				}
			}
			// 使用可能
			int bit = 0;
			bit |= getItem().isUseRoyal() ? 1 : 0;
			bit |= getItem().isUseKnight() ? 2 : 0;
			bit |= getItem().isUseElf() ? 4 : 0;
			bit |= getItem().isUseMage() ? 8 : 0;
			bit |= getItem().isUseDarkelf() ? 16 : 0;
			bit |= getItem().isUseDragonknight() ? 32 : 0;
			bit |= getItem().isUseIllusionist() ? 64 : 0;
			// bit |= getItem().isUseHiPet() ? 64 : 0; // ハイペット
			os.writeC(7);
			os.writeC(bit);
			// 弓の命中率補正
			if (getItem().getBowHitModifierByArmor() != 0) {
				os.writeC(24);
				os.writeC(getItem().getBowHitModifierByArmor());
			}
			// 弓のダメージ補正
			if (getItem().getBowDmgModifierByArmor() != 0) {
				os.writeC(35);
				os.writeC(getItem().getBowDmgModifierByArmor());
			}
			// MP吸収
			if ((itemId == 126) || (itemId == 127)) { // マナスタッフ、鋼鉄のマナスタッフ
				os.writeC(16);
			}
			// HP吸収
			if (itemId == 262) { // ディストラクション
				os.writeC(34);
			}
			// STR~CHA
			if (getItem().get_addstr() != 0) {
				os.writeC(8);
				os.writeC(getItem().get_addstr());
			}
			if (getItem().get_adddex() != 0) {
				os.writeC(9);
				os.writeC(getItem().get_adddex());
			}
			if (getItem().get_addcon() != 0) {
				os.writeC(10);
				os.writeC(getItem().get_addcon());
			}
			if (getItem().get_addwis() != 0) {
				os.writeC(11);
				os.writeC(getItem().get_addwis());
			}
			if (getItem().get_addint() != 0) {
				os.writeC(12);
				os.writeC(getItem().get_addint());
			}
			if (getItem().get_addcha() != 0) {
				os.writeC(13);
				os.writeC(getItem().get_addcha());
			}
			// HP, MP
			if (getItem().get_addhp() != 0 || getaddHp() != 0) {
				os.writeC(14);
				os.writeH(getItem().get_addhp() + getaddHp());
			}
			if (getItem().get_addmp() != 0 || getaddMp() != 0) {
				os.writeC(32);
				os.writeC(getItem().get_addmp() + getaddMp());
			}
			// MR
			if (getMr() != 0) {
				os.writeC(15);
				os.writeH(getMr());
			}
			// SP(魔力)
			if (getItem().get_addsp() != 0 || getaddSp() != 0) {
				os.writeC(17);
				os.writeC(getItem().get_addsp() + getaddSp());
			}
			// ヘイスト
			if (getItem().isHasteItem()) {
				os.writeC(18);
			}
			// 火の属性
			if (getItem().get_defense_fire() != 0 || getFireMr() != 0) {
				os.writeC(27);
				os.writeC(getItem().get_defense_fire() + getFireMr());
			}
			// 水の属性
			if (getItem().get_defense_water() != 0 || getWaterMr() != 0) {
				os.writeC(28);
				os.writeC(getItem().get_defense_water() + getWaterMr());
			}
			// 風の属性
			if (getItem().get_defense_wind() != 0 || getWindMr() != 0) {
				os.writeC(29);
				os.writeC(getItem().get_defense_wind() + getWindMr());
			}
			// 地の属性
			if (getItem().get_defense_earth() != 0 || getEarthMr() != 0) {
				os.writeC(30);
				os.writeC(getItem().get_defense_earth() + getEarthMr());
			}
			// 凍結耐性
			if (getItem().get_regist_freeze() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_freeze());
				os.writeC(33);
				os.writeC(1);
			}
			// 石化耐性
			if (getItem().get_regist_stone() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_stone());
				os.writeC(33);
				os.writeC(2);
			}
			// 睡眠耐性
			if (getItem().get_regist_sleep() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_sleep());
				os.writeC(33);
				os.writeC(3);
			}
			// 暗闇耐性
			if (getItem().get_regist_blind() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_blind());
				os.writeC(33);
				os.writeC(4);
			}
			// スタン耐性
			if (getItem().get_regist_stun() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_stun());
				os.writeC(33);
				os.writeC(5);
			}
			// ホールド耐性
			if (getItem().get_regist_sustain() != 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_sustain());
				os.writeC(33);
				os.writeC(6);
			}
			// 體力回復率
			if (getItem().get_addhpr() != 0 || getHpr() != 0) {
				os.writeC(37);
				os.writeC(getItem().get_addhpr() + getHpr());
			}
			// 魔力回復率
			if (getItem().get_addmpr() != 0 || getMpr() != 0) {
				os.writeC(38);
				os.writeC(getItem().get_addmpr() + getMpr());
			}
			// 幸運
			// if (getItem.getLuck() != 0) {
			// os.writeC(20);
			// os.writeC(val);
			// }
			// 種類
			// if (getItem.getDesc() != 0) {
			// os.writeC(25);
			// os.writeH(val); // desc.tbl ID
			// }
			// レベル
			// if (getItem.getLevel() != 0) {
			// os.writeC(26);
			// os.writeH(val);
			// }
		}
		return os.getBytes();
	}

	class EnchantTimer extends TimerTask {

		public EnchantTimer() {
		}

		@Override
		public void run() {
			try {
				int type = getItem().getType();
				int type2 = getItem().getType2();
				int itemId = getItem().getItemId();
				if ((_pc != null) && _pc.getInventory().checkItem(itemId)) {
					if ((type == 2) && (type2 == 2) && isEquipped()) {
						_pc.addAc(3);
						_pc.sendPackets(new S_OwnCharStatus(_pc));
					}
				}
				setAcByMagic(0);
				setDmgByMagic(0);
				setHolyDmgByMagic(0);
				setHitByMagic(0);
				_pc.sendPackets(new S_ServerMessage(308, getLogName()));
				_isRunning = false;
				_timer = null;
			} catch (Exception e) {
			}
		}
	}

	private int _acByMagic = 0;

	public int getAcByMagic() {
		return _acByMagic;
	}

	public void setAcByMagic(int i) {
		_acByMagic = i;
	}

	private int _dmgByMagic = 0;

	public int getDmgByMagic() {
		return _dmgByMagic;
	}

	public void setDmgByMagic(int i) {
		_dmgByMagic = i;
	}

	private int _holyDmgByMagic = 0;

	public int getHolyDmgByMagic() {
		return _holyDmgByMagic;
	}

	public void setHolyDmgByMagic(int i) {
		_holyDmgByMagic = i;
	}

	private int _hitByMagic = 0;

	public int getHitByMagic() {
		return _hitByMagic;
	}

	public void setHitByMagic(int i) {
		_hitByMagic = i;
	}

	private int _FireMr = 0;

	public int getFireMr() {
		return _FireMr;
	}

	public void setFireMr(int i) {
		_FireMr = i;
	}

	private int _WaterMr = 0;

	public int getWaterMr() {
		return _WaterMr;
	}

	public void setWaterMr(int i) {
		_WaterMr = i;
	}

	private int _EarthMr = 0;

	public int getEarthMr() {
		return _EarthMr;
	}

	public void setEarthMr(int i) {
		_EarthMr = i;
	}

	private int _WindMr = 0;

	public int getWindMr() {
		return _WindMr;
	}

	public void setWindMr(int i) {
		_WindMr = i;
	}

	private int _M_Def = 0;

	public int getM_Def() {
		return _M_Def;
	}

	public void setM_Def(int i) {
		_M_Def = i;
	}

	private int _Mpr = 0;

	public int getMpr() {
		return _Mpr;
	}

	public void setMpr(int i) {
		_Mpr = i;
	}

	private int _Hpr = 0;

	public int getHpr() {
		return _Hpr;
	}

	public void setHpr(int i) {
		_Hpr = i;
	}

	private int _addHp = 0;

	public int getaddHp() {
		return _addHp;
	}

	public void setaddHp(int i) {
		_addHp = i;
	}

	private int _addMp = 0;

	public int getaddMp() {
		return _addMp;
	}

	public void setaddMp(int i) {
		_addMp = i;
	}

	private int _addSp = 0;

	public int getaddSp() {
		return _addSp;
	}

	public void setaddSp(int i) {
		_addSp = i;
	}

	public void setSkillArmorEnchant(L1PcInstance pc, int skillId, int skillTime) {
		int type = getItem().getType();
		int type2 = getItem().getType2();
		if (_isRunning) {
			_timer.cancel();
			int itemId = getItem().getItemId();
			if ((pc != null) && pc.getInventory().checkItem(itemId)) {
				if ((type == 2) && (type2 == 2) && isEquipped()) {
					pc.addAc(3);
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
			}
			setAcByMagic(0);
			_isRunning = false;
			_timer = null;
		}

		if ((type == 2) && (type2 == 2) && isEquipped()) {
			pc.addAc(-3);
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
		setAcByMagic(3);
		_pc = pc;
		_timer = new EnchantTimer();
		(new Timer()).schedule(_timer, skillTime);
		_isRunning = true;
	}

	public void setSkillWeaponEnchant(L1PcInstance pc, int skillId,
			int skillTime) {
		if (getItem().getType2() != 1) {
			return;
		}
		if (_isRunning) {
			_timer.cancel();
			setDmgByMagic(0);
			setHolyDmgByMagic(0);
			setHitByMagic(0);
			_isRunning = false;
			_timer = null;
		}

		switch (skillId) {
		case HOLY_WEAPON:
			setHolyDmgByMagic(1);
			setHitByMagic(1);
			break;

		case ENCHANT_WEAPON:
			setDmgByMagic(2);
			break;

		case BLESS_WEAPON:
			setDmgByMagic(2);
			setHitByMagic(2);
			break;

		case SHADOW_FANG:
			setDmgByMagic(5);
			break;

		default:
			break;
		}

		_pc = pc;
		_timer = new EnchantTimer();
		(new Timer()).schedule(_timer, skillTime);
		_isRunning = true;
	}

	private int _itemOwnerId = 0;

	public int getItemOwnerId() {
		return _itemOwnerId;
	}

	public void setItemOwnerId(int i) {
		_itemOwnerId = i;
	}

	public void startItemOwnerTimer(L1PcInstance pc) {
		setItemOwnerId(pc.getId());
		L1ItemOwnerTimer timer = new L1ItemOwnerTimer(this, 10000);
		timer.begin();
	}

	private L1EquipmentTimer _equipmentTimer;

	public void startEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer = new L1EquipmentTimer(pc, this);
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(_equipmentTimer, 1000, 1000);
		}
	}

	public void stopEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer.cancel();
			_equipmentTimer = null;
		}
	}

	private boolean _isNowLighting = false;

	public boolean isNowLighting() {
		return _isNowLighting;
	}

	public void setNowLighting(boolean flag) {
		_isNowLighting = flag;
	}

	// 旅館鑰匙
	private int _keyId = 0;

	public int getKeyId() {
		return _keyId;
	}

	public void setKeyId(int i) {
		_keyId = i;
	}

	private int _innNpcId = 0;

	public int getInnNpcId() {
		return _innNpcId;
	}

	public void setInnNpcId(int i) {
		_innNpcId = i;
	}

	private boolean _isHall;

	public boolean checkRoomOrHall() {
		return _isHall;
	}

	public void setHall(boolean i) {
		_isHall = i;
	}

	private Timestamp _dueTime;

	public Timestamp getDueTime() {
		return _dueTime;
	}

	public void setDueTime(Timestamp i) {
		_dueTime = i;
	}
}
