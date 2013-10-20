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

package l1j.server.server.templates;


public class L1Drop {
	int _mobId;

	int _itemId;

	int _min;

	int _max;

	int _chance;

	public L1Drop(int mobId, int itemId, int min, int max, int chance) {
		_mobId = mobId;
		_itemId = itemId;
		_min = min;
		_max = max;
		_chance = chance;
	}

	public int getChance() {
		return _chance;
	}

	public int getItemid() {
		return _itemId;
	}

	public int getMax() {
		return _max;
	}

	public int getMin() {
		return _min;
	}

	public int getMobid() {
		return _mobId;
	}
}
