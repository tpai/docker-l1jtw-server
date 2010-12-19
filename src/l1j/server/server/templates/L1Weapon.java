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
package l1j.server.server.templates;

public class L1Weapon extends L1Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1Weapon() {
	}

	private int _range = 0; // ● 射程範囲

	@Override
	public int getRange() {
		return _range;
	}

	public void setRange(int i) {
		_range = i;
	}

	private int _hitModifier = 0; // ● 命中率補正

	@Override
	public int getHitModifier() {
		return _hitModifier;
	}

	public void setHitModifier(int i) {
		_hitModifier = i;
	}

	private int _dmgModifier = 0; // ● ダメージ補正

	@Override
	public int getDmgModifier() {
		return _dmgModifier;
	}

	public void setDmgModifier(int i) {
		_dmgModifier = i;
	}

	private int _doubleDmgChance; // ● DB、クロウの発動確率

	@Override
	public int getDoubleDmgChance() {
		return _doubleDmgChance;
	}

	public void setDoubleDmgChance(int i) {
		_doubleDmgChance = i;
	}

	private int _magicDmgModifier = 0; // ● 攻撃魔法のダメージ補正

	@Override
	public int getMagicDmgModifier() {
		return _magicDmgModifier;
	}

	public void setMagicDmgModifier(int i) {
		_magicDmgModifier = i;
	}

	private int _canbedmg = 0; // ● 損傷の有無

	@Override
	public int get_canbedmg() {
		return _canbedmg;
	}

	public void set_canbedmg(int i) {
		_canbedmg = i;
	}

	@Override
	public boolean isTwohandedWeapon() {
		int weapon_type = getType();
		
		boolean bool = (weapon_type == 3 || weapon_type == 4
				|| weapon_type == 5 || weapon_type == 11
				|| weapon_type == 12 || weapon_type == 15
				|| weapon_type == 16 || weapon_type == 18);

		return bool;
	}
}
