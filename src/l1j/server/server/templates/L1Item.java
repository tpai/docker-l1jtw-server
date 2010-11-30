/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.templates;

import java.io.Serializable;

public abstract class L1Item implements Serializable {

	private static final long serialVersionUID = 1L;

	public L1Item() {
	}

	// ■■■■■■ L1EtcItem,L1Weapon,L1Armor に共通する項目 ■■■■■■

	private int _type2; // ● 0=L1EtcItem, 1=L1Weapon, 2=L1Armor

	/**
	 * @return 0 if L1EtcItem, 1 if L1Weapon, 2 if L1Armor
	 */
	public int getType2() {
		return _type2;
	}

	public void setType2(int type) {
		_type2 = type;
	}

	private int _itemId; // ● アイテムＩＤ

	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int itemId) {
		_itemId = itemId;
	}

	private String _name; // ● アイテム名

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	private String _unidentifiedNameId; // ● 未鑑定アイテムのネームＩＤ

	public String getUnidentifiedNameId() {
		return _unidentifiedNameId;
	}

	public void setUnidentifiedNameId(String unidentifiedNameId) {
		_unidentifiedNameId = unidentifiedNameId;
	}

	private String _identifiedNameId; // ● 鑑定済みアイテムのネームＩＤ

	public String getIdentifiedNameId() {
		return _identifiedNameId;
	}

	public void setIdentifiedNameId(String identifiedNameId) {
		_identifiedNameId = identifiedNameId;
	}

	private int _type; // ● 詳細なタイプ

	/**
	 * アイテムの種類を返す。<br>
	 * 
	 * @return
	 * <p>
	 * [etcitem]<br>
	 * 0:arrow, 1:wand, 2:light, 3:gem, 4:totem, 5:firecracker, 6:potion,
	 * 7:food, 8:scroll, 9:questitem, 10:spellbook, 11:petitem, 12:other,
	 * 13:material, 14:event, 15:sting
	 * </p>
	 * <p>
	 * [weapon]<br>
	 * 1:sword, 2:dagger, 3:tohandsword, 4:bow, 5:spear, 6:blunt, 7:staff,
	 * 8:throwingknife, 9:arrow, 10:gauntlet, 11:claw, 12:edoryu, 13:singlebow,
	 * 14:singlespear, 15:tohandblunt, 16:tohandstaff, 17:kiringku 18chainsword
	 * </p>
	 * <p>
	 * [armor]<br>
	 * 1:helm, 2:armor, 3:T, 4:cloak, 5:glove, 6:boots, 7:shield, 8:amulet,
	 * 9:ring, 10:belt, 11:ring2, 12:earring
	 */
	public int getType() {
		return _type;
	}

	public void setType(int type) {
		_type = type;
	}

	private int _type1; // ● タイプ

	/**
	 * アイテムの種類を返す。<br>
	 * 
	 * @return
	 * <p>
	 * [weapon]<br>
	 * sword:4, dagger:46, tohandsword:50, bow:20, blunt:11, spear:24, staff:40,
	 * throwingknife:2922, arrow:66, gauntlet:62, claw:58, edoryu:54,
	 * singlebow:20, singlespear:24, tohandblunt:11, tohandstaff:40,
	 * kiringku:58, chainsword:24
	 * </p>
	 */
	public int getType1() {
		return _type1;
	}

	public void setType1(int type1) {
		_type1 = type1;
	}

	private int _material; // ● 素材

	/**
	 * アイテムの素材を返す
	 * 
	 * @return 0:none 1:液体 2:web 3:植物性 4:動物性 5:紙 6:布 7:皮 8:木 9:骨 10:竜の鱗 11:鉄
	 *         12:鋼鉄 13:銅 14:銀 15:金 16:プラチナ 17:ミスリル 18:ブラックミスリル 19:ガラス 20:宝石
	 *         21:鉱物 22:オリハルコン
	 */
	public int getMaterial() {
		return _material;
	}

	public void setMaterial(int material) {
		_material = material;
	}

	private int _weight; // ● 重量

	public int getWeight() {
		return _weight;
	}

	public void setWeight(int weight) {
		_weight = weight;
	}

	private int _gfxId; // ● インベントリ内のグラフィックＩＤ

	public int getGfxId() {
		return _gfxId;
	}

	public void setGfxId(int gfxId) {
		_gfxId = gfxId;
	}

	private int _groundGfxId; // ● 地面に置いた時のグラフィックＩＤ

	public int getGroundGfxId() {
		return _groundGfxId;
	}

	public void setGroundGfxId(int groundGfxId) {
		_groundGfxId = groundGfxId;
	}

	private int _minLevel; // ● 使用、装備可能最小ＬＶ

	private int _itemDescId;

	/**
	 * 鑑定時に表示されるItemDesc.tblのメッセージIDを返す。
	 */
	public int getItemDescId() {
		return _itemDescId;
	}

	public void setItemDescId(int descId) {
		_itemDescId = descId;
	}

	public int getMinLevel() {
		return _minLevel;
	}

	public void setMinLevel(int level) {
		_minLevel = level;
	}

	private int _maxLevel; // ● 使用、装備可能最大ＬＶ

	public int getMaxLevel() {
		return _maxLevel;
	}

	public void setMaxLevel(int maxlvl) {
		_maxLevel = maxlvl;
	}

	private int _bless; // ● 祝福状態

	public int getBless() {
		return _bless;
	}

	public void setBless(int i) {
		_bless = i;
	}

	private boolean _tradable; // ● トレード可／不可

	public boolean isTradable() {
		return _tradable;
	}

	public void setTradable(boolean flag) {
		_tradable = flag;
	}

	private boolean _cantDelete; // ● 削除不可

	public boolean isCantDelete() {
		return _cantDelete;
	}

	public void setCantDelete(boolean flag) {
		_cantDelete = flag;
	}

	private boolean _save_at_once;

	/**
	 * アイテムの個数が変化した際にすぐにDBに書き込むべきかを返す。
	 */
	public boolean isToBeSavedAtOnce() {
		return _save_at_once;
	}

	public void setToBeSavedAtOnce(boolean flag) {
		_save_at_once = flag;
	}

	// ■■■■■■ L1EtcItem,L1Weapon に共通する項目 ■■■■■■

	private int _dmgSmall = 0; // ● 最小ダメージ

	public int getDmgSmall() {
		return _dmgSmall;
	}

	public void setDmgSmall(int dmgSmall) {
		_dmgSmall = dmgSmall;
	}

	private int _dmgLarge = 0; // ● 最大ダメージ

	public int getDmgLarge() {
		return _dmgLarge;
	}

	public void setDmgLarge(int dmgLarge) {
		_dmgLarge = dmgLarge;
	}

	// ■■■■■■ L1EtcItem,L1Armor に共通する項目 ■■■■■■

	// ■■■■■■ L1Weapon,L1Armor に共通する項目 ■■■■■■

	private int _safeEnchant = 0; // ● ＯＥ安全圏

	public int get_safeenchant() {
		return _safeEnchant;
	}

	public void set_safeenchant(int safeenchant) {
		_safeEnchant = safeenchant;
	}

	private boolean _useRoyal = false; // ● ロイヤルクラスが装備できるか

	public boolean isUseRoyal() {
		return _useRoyal;
	}

	public void setUseRoyal(boolean flag) {
		_useRoyal = flag;
	}

	private boolean _useKnight = false; // ● ナイトクラスが装備できるか

	public boolean isUseKnight() {
		return _useKnight;
	}

	public void setUseKnight(boolean flag) {
		_useKnight = flag;
	}

	private boolean _useElf = false; // ● エルフクラスが装備できるか

	public boolean isUseElf() {
		return _useElf;
	}

	public void setUseElf(boolean flag) {
		_useElf = flag;
	}

	private boolean _useMage = false; // ● メイジクラスが装備できるか

	public boolean isUseMage() {
		return _useMage;
	}

	public void setUseMage(boolean flag) {
		_useMage = flag;
	}

	private boolean _useDarkelf = false; // ● ダークエルフクラスが装備できるか

	public boolean isUseDarkelf() {
		return _useDarkelf;
	}

	public void setUseDarkelf(boolean flag) {
		_useDarkelf = flag;
	}

	private boolean _useDragonknight = false; // ● ドラゴンナイト裝備できるか

	public boolean isUseDragonknight() {
		return _useDragonknight;
	}

	public void setUseDragonknight(boolean flag) {
		_useDragonknight = flag;
	}

	private boolean _useIllusionist = false; // ● イリュージョニスト裝備できるか

	public boolean isUseIllusionist() {
		return _useIllusionist;
	}

	public void setUseIllusionist(boolean flag) {
		_useIllusionist = flag;
	}

	private byte _addstr = 0; // ● ＳＴＲ補正

	public byte get_addstr() {
		return _addstr;
	}

	public void set_addstr(byte addstr) {
		_addstr = addstr;
	}

	private byte _adddex = 0; // ● ＤＥＸ補正

	public byte get_adddex() {
		return _adddex;
	}

	public void set_adddex(byte adddex) {
		_adddex = adddex;
	}

	private byte _addcon = 0; // ● ＣＯＮ補正

	public byte get_addcon() {
		return _addcon;
	}

	public void set_addcon(byte addcon) {
		_addcon = addcon;
	}

	private byte _addint = 0; // ● ＩＮＴ補正

	public byte get_addint() {
		return _addint;
	}

	public void set_addint(byte addint) {
		_addint = addint;
	}

	private byte _addwis = 0; // ● ＷＩＳ補正

	public byte get_addwis() {
		return _addwis;
	}

	public void set_addwis(byte addwis) {
		_addwis = addwis;
	}

	private byte _addcha = 0; // ● ＣＨＡ補正

	public byte get_addcha() {
		return _addcha;
	}

	public void set_addcha(byte addcha) {
		_addcha = addcha;
	}

	private int _addhp = 0; // ● ＨＰ補正

	public int get_addhp() {
		return _addhp;
	}

	public void set_addhp(int addhp) {
		_addhp = addhp;
	}

	private int _addmp = 0; // ● ＭＰ補正

	public int get_addmp() {
		return _addmp;
	}

	public void set_addmp(int addmp) {
		_addmp = addmp;
	}

	private int _addhpr = 0; // ● ＨＰＲ補正

	public int get_addhpr() {
		return _addhpr;
	}

	public void set_addhpr(int addhpr) {
		_addhpr = addhpr;
	}

	private int _addmpr = 0; // ● ＭＰＲ補正

	public int get_addmpr() {
		return _addmpr;
	}

	public void set_addmpr(int addmpr) {
		_addmpr = addmpr;
	}

	private int _addsp = 0; // ● ＳＰ補正

	public int get_addsp() {
		return _addsp;
	}

	public void set_addsp(int addsp) {
		_addsp = addsp;
	}

	private int _mdef = 0; // ● ＭＲ

	public int get_mdef() {
		return _mdef;
	}

	public void set_mdef(int i) {
		this._mdef = i;
	}

	private boolean _isHasteItem = false; // ● ヘイスト効果の有無

	public boolean isHasteItem() {
		return _isHasteItem;
	}

	public void setHasteItem(boolean flag) {
		_isHasteItem = flag;
	}

	private int _maxUseTime = 0; // ● 使用可能な時間

	public int getMaxUseTime() {
		return _maxUseTime;
	}

	public void setMaxUseTime(int i) {
		_maxUseTime = i;
	}

	private int _useType;

	/**
	 * 使用したときのリアクションを決定するタイプを返す。
	 */
	public int getUseType() {
		return _useType;
	}

	public void setUseType(int useType) {
		_useType = useType;
	}

	private int _foodVolume;

	/**
	 * 肉などのアイテムに設定されている満腹度を返す。
	 */
	public int getFoodVolume() {
		return _foodVolume;
	}

	public void setFoodVolume(int volume) {
		_foodVolume = volume;
	}

	/**
	 * ランプなどのアイテムに設定されている明るさを返す。
	 */
	public int getLightRange() {
		if (_itemId == 40001) { // ランプ
			return 11;
		} else if (_itemId == 40002) { // ランタン
			return 14;
		} else if (_itemId == 40004) { // マジックランタン
			return 14;
		} else if (_itemId == 40005) { // キャンドル
			return 8;
		} else {
			return 0;
		}
	}

	/**
	 * ランプなどの燃料の量を返す。
	 */
	public int getLightFuel() {
		if (_itemId == 40001) { // ランプ
			return 6000;
		} else if (_itemId == 40002) { // ランタン
			return 12000;
		} else if (_itemId == 40003) { // ランタンオイル
			return 12000;
		} else if (_itemId == 40004) { // マジックランタン
			return 0;
		} else if (_itemId == 40005) { // キャンドル
			return 600;
		} else {
			return 0;
		}
	}

	// ■■■■■■ L1EtcItem でオーバーライドする項目 ■■■■■■
	public boolean isStackable() {
		return false;
	}

	public int get_locx() {
		return 0;
	}

	public int get_locy() {
		return 0;
	}

	public short get_mapid() {
		return 0;
	}

	public int get_delayid() {
		return 0;
	}

	public int get_delaytime() {
		return 0;
	}

	public int getMaxChargeCount() {
		return 0;
	}

	public boolean isCanSeal() {
		return false;
	}

	// ■■■■■■ L1Weapon でオーバーライドする項目 ■■■■■■
	public int getRange() {
		return 0;
	}

	public int getHitModifier() {
		return 0;
	}

	public int getDmgModifier() {
		return 0;
	}

	public int getDoubleDmgChance() {
		return 0;
	}

	public int getMagicDmgModifier() {
		return 0;
	}

	public int get_canbedmg() {
		return 0;
	}

	public boolean isTwohandedWeapon() {
		return false;
	}

	// ■■■■■■ L1Armor でオーバーライドする項目 ■■■■■■
	public int get_ac() {
		return 0;
	}

	public int getDamageReduction() {
		return 0;
	}

	public int getWeightReduction() {
		return 0;
	}

	public int getHitModifierByArmor() {
		return 0;
	}

	public int getDmgModifierByArmor() {
		return 0;
	}

	public int getBowHitModifierByArmor() {
		return 0;
	}

	public int getBowDmgModifierByArmor() {
		return 0;
	}

	public int get_defense_water() {
		return 0;
	}

	public int get_defense_fire() {
		return 0;
	}

	public int get_defense_earth() {
		return 0;
	}

	public int get_defense_wind() {
		return 0;
	}

	public int get_regist_stun() {
		return 0;
	}

	public int get_regist_stone() {
		return 0;
	}

	public int get_regist_sleep() {
		return 0;
	}

	public int get_regist_freeze() {
		return 0;
	}

	public int get_regist_sustain() {
		return 0;
	}

	public int get_regist_blind() {
		return 0;
	}

}
