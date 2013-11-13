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

// Referenced classes of package l1j.server.server.templates:
// L1PrivateShopSellList

public class L1PrivateShopSellList {
	public L1PrivateShopSellList() {
	}

	private int _itemObjectId;

	public void setItemObjectId(int i) {
		_itemObjectId = i;
	}

	public int getItemObjectId() {
		return _itemObjectId;
	}

	private int _sellTotalCount; // 売る予定の個数

	public void setSellTotalCount(int i) {
		_sellTotalCount = i;
	}

	public int getSellTotalCount() {
		return _sellTotalCount;
	}

	private int _sellPrice;

	public void setSellPrice(int i) {
		_sellPrice = i;
	}

	public int getSellPrice() {
		return _sellPrice;
	}

	private int _sellCount; // 売った累計

	public void setSellCount(int i) {
		_sellCount = i;
	}

	public int getSellCount() {
		return _sellCount;
	}
}
