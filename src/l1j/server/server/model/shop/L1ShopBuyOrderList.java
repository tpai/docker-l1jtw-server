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
package l1j.server.server.model.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.templates.L1ShopItem;

class L1ShopBuyOrder {
	private final L1ShopItem _item;
	private final int _count;

	public L1ShopBuyOrder(L1ShopItem item, int count) {
		_item = item;
		_count = count;
	}

	public L1ShopItem getItem() {
		return _item;
	}

	public int getCount() {
		return _count;
	}
}

public class L1ShopBuyOrderList {
	private static Logger _log = Logger.getLogger(L1ShopBuyOrder.class
			.getName());

	private final L1Shop _shop;
	private final List<L1ShopBuyOrder> _list = new ArrayList<L1ShopBuyOrder>();
	private final L1TaxCalculator _taxCalc;

	private int _totalWeight = 0;
	private int _totalPrice = 0;
	private int _totalPriceTaxIncluded = 0;

	L1ShopBuyOrderList(L1Shop shop) {
		_shop = shop;
		_taxCalc = new L1TaxCalculator(shop.getNpcId());
	}

	public void add(int orderNumber, int count) {
		if (_shop.getSellingItems().size() < orderNumber) {
			return;
		}
		L1ShopItem shopItem = _shop.getSellingItems().get(orderNumber);

		int price = (int) (shopItem.getPrice() * Config.RATE_SHOP_SELLING_PRICE);
		// オーバーフローチェック
		for (int j = 0; j < count; j++) {
			if (price * j < 0) {
				return;
			}
		}
		if (_totalPrice < 0) {
			return;
		}
		_totalPrice += price * count;
		_totalPriceTaxIncluded += _taxCalc.layTax(price) * count;
		_totalWeight += shopItem.getItem().getWeight() * count
				* shopItem.getPackCount();

		if (shopItem.getItem().isStackable()) {
			_list.add(new L1ShopBuyOrder(shopItem, count
					* shopItem.getPackCount()));
			return;
		}

		for (int i = 0; i < (count * shopItem.getPackCount()); i++) {
			_list.add(new L1ShopBuyOrder(shopItem, 1));
		}
	}

	List<L1ShopBuyOrder> getList() {
		return _list;
	}

	public int getTotalWeight() {
		return _totalWeight;
	}

	public int getTotalPrice() {
		return _totalPrice;
	}

	public int getTotalPriceTaxIncluded() {
		return _totalPriceTaxIncluded;
	}

	L1TaxCalculator getTaxCalculator() {
		return _taxCalc;
	}
}
