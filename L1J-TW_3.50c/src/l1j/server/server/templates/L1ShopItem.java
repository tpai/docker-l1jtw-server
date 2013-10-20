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

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.game.L1BugBearRace;

public class L1ShopItem {
	private static final long serialVersionUID = 1L;

	private final int _itemId;

	private L1Item _item;

	private final int _price;

	private final int _packCount;

	public L1ShopItem(int itemId, int price, int packCount) {
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
	}

	public int getItemId() {
		return _itemId;
	}

	public L1Item getItem() {
		return _item;
	}

	public int getPrice() {
		return _price;
	}

	public int getPackCount() {
		return _packCount;
	}

	// 食人妖精賽跑用
	public void setName(int num) {
		int trueNum = L1BugBearRace.getInstance().getRunner(num).getNpcId() - 91350 + 1;
		_item = (L1Item) _item.clone();
		String temp = "" + _item.getIdentifiedNameId() + " "
				+ L1BugBearRace.getInstance().getRound() + "-" + trueNum;
		_item.setName(temp);
		_item.setUnidentifiedNameId(temp);
		_item.setIdentifiedNameId(temp);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
