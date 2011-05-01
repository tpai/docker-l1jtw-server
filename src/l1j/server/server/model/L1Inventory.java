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

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import l1j.server.Config;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.InnKeyTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.RaceTicketTable;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.utils.Random;
import l1j.server.server.utils.collections.Lists;

public class L1Inventory extends L1Object {

	private static final long serialVersionUID = 1L;

	protected List<L1ItemInstance> _items = Lists.newConcurrentList();

	public static final int MAX_AMOUNT = 2000000000; // 2G

	public static final int MAX_WEIGHT = 1500;

	public L1Inventory() {
		//
	}

	// インベントリ内のアイテムの総数
	public int getSize() {
		return _items.size();
	}

	// インベントリ内の全てのアイテム
	public List<L1ItemInstance> getItems() {
		return _items;
	}

	// インベントリ内の総重量
	public int getWeight() {
		int weight = 0;

		for (L1ItemInstance item : _items) {
			weight += item.getWeight();
		}

		return weight;
	}

	// 引数のアイテムを追加しても容量と重量が大丈夫か確認
	public static final int OK = 0;

	public static final int SIZE_OVER = 1;

	public static final int WEIGHT_OVER = 2;

	public static final int AMOUNT_OVER = 3;

	public int checkAddItem(L1ItemInstance item, int count) {
		if (item == null) {
			return -1;
		}
		if ((item.getCount() <= 0) || (count <= 0)) {
			return -1;
		}
		if ((getSize() > Config.MAX_NPC_ITEM)
				|| ((getSize() == Config.MAX_NPC_ITEM) && (!item.isStackable() || !checkItem(item
						.getItem().getItemId())))) { // 容量確認
			return SIZE_OVER;
		}

		int weight = getWeight() + item.getItem().getWeight() * count / 1000
				+ 1;
		if ((weight < 0) || ((item.getItem().getWeight() * count / 1000) < 0)) {
			return WEIGHT_OVER;
		}
		if (weight > (MAX_WEIGHT * Config.RATE_WEIGHT_LIMIT_PET)) { // その他の重量確認（主にサモンとペット）
			return WEIGHT_OVER;
		}

		L1ItemInstance itemExist = findItemId(item.getItemId());
		if ((itemExist != null)
				&& ((itemExist.getCount() + count) > MAX_AMOUNT)) {
			return AMOUNT_OVER;
		}

		return OK;
	}

	// 引数のアイテムを追加しても倉庫の容量が大丈夫か確認
	public static final int WAREHOUSE_TYPE_PERSONAL = 0;

	public static final int WAREHOUSE_TYPE_CLAN = 1;

	public int checkAddItemToWarehouse(L1ItemInstance item, int count, int type) {
		if (item == null) {
			return -1;
		}
		if ((item.getCount() <= 0) || (count <= 0)) {
			return -1;
		}

		int maxSize = 100;
		if (type == WAREHOUSE_TYPE_PERSONAL) {
			maxSize = Config.MAX_PERSONAL_WAREHOUSE_ITEM;
		} else if (type == WAREHOUSE_TYPE_CLAN) {
			maxSize = Config.MAX_CLAN_WAREHOUSE_ITEM;
		}
		if ((getSize() > maxSize)
				|| ((getSize() == maxSize) && (!item.isStackable() || !checkItem(item
						.getItem().getItemId())))) { // 容量確認
			return SIZE_OVER;
		}

		return OK;
	}

	// 新しいアイテムの格納
	public synchronized L1ItemInstance storeItem(int id, int count) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}

		if (id == 40312) {
			L1ItemInstance item = new L1ItemInstance(temp, count);

			if (findKeyId(id) == null) { // 新しく生成する必要がある場合のみIDの発行とL1Worldへの登録を行う
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}

			return storeItem(item);
		}
		else if (temp.isStackable()) {
			L1ItemInstance item = new L1ItemInstance(temp, count);

			if (findItemId(id) == null) { // 新しく生成する必要がある場合のみIDの発行とL1Worldへの登録を行う
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}

			return storeItem(item);
		}

		// スタックできないアイテムの場合
		L1ItemInstance result = null;
		for (int i = 0; i < count; i++) {
			L1ItemInstance item = new L1ItemInstance(temp, 1);
			item.setId(IdFactory.getInstance().nextId());
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		// 最後に作ったアイテムを返す。配列を戻すようにメソッド定義を変更したほうが良いかもしれない。
		return result;
	}

	// DROP、購入、GMコマンドで入手した新しいアイテムの格納
	public synchronized L1ItemInstance storeItem(L1ItemInstance item) {
		if (item.getCount() <= 0) {
			return null;
		}
		int itemId = item.getItem().getItemId();
		if (item.isStackable()) {
			L1ItemInstance findItem = findItemId(itemId);
			if (itemId == 40309) { // Race Tickets
				findItem = findItemNameId(item.getItem().getIdentifiedNameId());
			} else if (itemId == 40312) { // 旅館鑰匙
				findItem = findKeyId(itemId);
			} else {
				findItem = findItemId(itemId);
			}
			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}

		if (itemId == 40309) {// Race Tickets
			String[] temp = item.getItem().getIdentifiedNameId().split(" ");
			temp=temp[temp.length-1].split("-");
			L1RaceTicket ticket = new L1RaceTicket();
			ticket.set_itemobjid(item.getId());
			ticket.set_round(Integer.parseInt(temp[0]));
			ticket.set_allotment_percentage(0.0);
			ticket.set_victory(0);
			ticket.set_runner_num(Integer.parseInt(temp[1]));
			RaceTicketTable.getInstance().storeNewTiket(ticket);
		}
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		int chargeCount = item.getItem().getMaxChargeCount();
		if ((itemId == 40006) || (itemId == 40007) || (itemId == 40008)
				|| (itemId == 140006) || (itemId == 140008)
				|| (itemId == 41401)) {
			chargeCount -= Random.nextInt(5);
		}
		if (itemId == 20383) {
			chargeCount = 50;
		}
		item.setChargeCount(chargeCount);
		if ((item.getItem().getType2() == 0) && (item.getItem().getType() == 2)) { // light系アイテム
			item.setRemainingTime(item.getItem().getLightFuel());
		} else {
			item.setRemainingTime(item.getItem().getMaxUseTime());
		}
		item.setBless(item.getItem().getBless());
		// 登入鑰匙紀錄
		if (item.getItem().getItemId() == 40312) {
			if (!InnKeyTable.checkey(item)) {
				InnKeyTable.StoreKey(item);
			}
		}
		_items.add(item);
		insertItem(item);
		return item;
	}

	// /trade、倉庫から入手したアイテムの格納
	public synchronized L1ItemInstance storeTradeItem(L1ItemInstance item) {
		if (item.getItem().getItemId() == 40312) { // 旅館鑰匙
			L1ItemInstance findItem = findKeyId(item.getKeyId()); // 檢查鑰匙編號是否相同
			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		} else if (item.isStackable()) {
			L1ItemInstance findItem = findItemId(item.getItem().getItemId());
			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		// 登入鑰匙紀錄
		if (item.getItem().getItemId() == 40312) {
			if (!InnKeyTable.checkey(item)) {
				InnKeyTable.StoreKey(item);
			}
		}
		_items.add(item);
		insertItem(item);
		return item;
	}

	/**
	 * インベントリから指定されたアイテムIDのアイテムを削除する。L1ItemInstanceへの参照
	 * がある場合はremoveItemの方を使用するのがよい。 （こちらは矢とか魔石とか特定のアイテムを消費させるときに使う）
	 * 
	 * @param itemid
	 *            - 削除するアイテムのitemid(objidではない)
	 * @param count
	 *            - 削除する個数
	 * @return 実際に削除された場合はtrueを返す。
	 */
	public boolean consumeItem(int itemid, int count) {
		if (count <= 0) {
			return false;
		}
		if (ItemTable.getInstance().getTemplate(itemid).isStackable()) {
			L1ItemInstance item = findItemId(itemid);
			if ((item != null) && (item.getCount() >= count)) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId(itemid);
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			} else if (itemList.length > count) { // 指定個数より多く所持している場合
				DataComparator<L1ItemInstance> dc = new DataComparator<L1ItemInstance>();
				Arrays.sort(itemList, dc); // エンチャント順にソートし、エンチャント数の少ないものから消費させる
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}

	public class DataComparator<T> implements Comparator<L1ItemInstance> {
		@Override
		public int compare(L1ItemInstance item1, L1ItemInstance item2) {
			return item1.getEnchantLevel() - item2.getEnchantLevel();
		}
	}

	// 指定したアイテムから指定個数を削除（使ったりゴミ箱に捨てられたとき）戻り値：実際に削除した数
	public int removeItem(int objectId, int count) {
		L1ItemInstance item = getItem(objectId);
		return removeItem(item, count);
	}

	public int removeItem(L1ItemInstance item) {
		return removeItem(item, item.getCount());
	}

	public int removeItem(L1ItemInstance item, int count) {
		if (item == null) {
			return 0;
		}
		if ((item.getCount() <= 0) || (count <= 0)) {
			return 0;
		}
		if (item.getCount() < count) {
			count = item.getCount();
		}
		if (item.getCount() == count) {
			int itemId = item.getItem().getItemId();
			if ((itemId == 40314) || (itemId == 40316)) { // ペットのアミュレット
				PetTable.getInstance().deletePet(item.getId());
			} else if ((itemId >= 49016) && (itemId <= 49025)) { // 便箋
				LetterTable lettertable = new LetterTable();
				lettertable.deleteLetter(item.getId());
			} else if ((itemId >= 41383) && (itemId <= 41400)) { // 家具
				for (L1Object l1object : L1World.getInstance().getObject()) {
					if (l1object instanceof L1FurnitureInstance) {
						L1FurnitureInstance furniture = (L1FurnitureInstance) l1object;
						if (furniture.getItemObjId() == item.getId()) { // 既に引き出している家具
							FurnitureSpawnTable.getInstance().deleteFurniture(
									furniture);
						}
					}
				}
			} else if (item.getItemId() == 40309) {// Race Tickets
				RaceTicketTable.getInstance().deleteTicket(item.getId());
			}
			deleteItem(item);
			L1World.getInstance().removeObject(item);
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
		}
		return count;
	}

	// _itemsから指定オブジェクトを削除(L1PcInstance、L1DwarfInstance、L1GroundInstanceでこの部分をオーバライドする)
	public void deleteItem(L1ItemInstance item) {
		// 刪除鑰匙紀錄
		if (item.getItem().getItemId() == 40312) {
			InnKeyTable.DeleteKey(item);
		}
		_items.remove(item);
	}

	// 引数のインベントリにアイテムを移譲
	public synchronized L1ItemInstance tradeItem(int objectId, int count,
			L1Inventory inventory) {
		L1ItemInstance item = getItem(objectId);
		return tradeItem(item, count, inventory);
	}

	public synchronized L1ItemInstance tradeItem(L1ItemInstance item,
			int count, L1Inventory inventory) {
		if (item == null) {
			return null;
		}
		if ((item.getCount() <= 0) || (count <= 0)) {
			return null;
		}
		if (item.isEquipped()) {
			return null;
		}
		if (!checkItem(item.getItem().getItemId(), count)) {
			return null;
		}
		L1ItemInstance carryItem;
		if (item.getCount() <= count) {
			deleteItem(item);
			carryItem = item;
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
			carryItem = ItemTable.getInstance().createItem(
					item.getItem().getItemId());
			carryItem.setCount(count);
			carryItem.setEnchantLevel(item.getEnchantLevel());
			carryItem.setIdentified(item.isIdentified());
			carryItem.set_durability(item.get_durability());
			carryItem.setChargeCount(item.getChargeCount());
			carryItem.setRemainingTime(item.getRemainingTime());
			carryItem.setLastUsed(item.getLastUsed());
			carryItem.setBless(item.getBless());
			// 旅館鑰匙
			if (carryItem.getItem().getItemId() == 40312) {
				carryItem.setInnNpcId(item.getInnNpcId()); // 旅館NPC
				carryItem.setKeyId(item.getKeyId()); // 鑰匙編號
				carryItem.setHall(item.checkRoomOrHall()); // 房間或會議室
				carryItem.setDueTime(item.getDueTime()); // 租用時間
			}
		}
		return inventory.storeTradeItem(carryItem);
	}

	/*
	 * アイテムを損傷・損耗させる（武器・防具も含む） アイテムの場合、損耗なのでマイナスするが 武器・防具は損傷度を表すのでプラスにする。
	 */
	public L1ItemInstance receiveDamage(int objectId) {
		L1ItemInstance item = getItem(objectId);
		return receiveDamage(item);
	}

	public L1ItemInstance receiveDamage(L1ItemInstance item) {
		return receiveDamage(item, 1);
	}

	public L1ItemInstance receiveDamage(L1ItemInstance item, int count) {
		int itemType = item.getItem().getType2();
		int currentDurability = item.get_durability();

		if (((currentDurability == 0) && (itemType == 0))
				|| (currentDurability < 0)) {
			item.set_durability(0);
			return null;
		}

		// 武器・防具のみ損傷度をプラス
		if (itemType == 0) {
			int minDurability = (item.getEnchantLevel() + 5) * -1;
			int durability = currentDurability - count;
			if (durability < minDurability) {
				durability = minDurability;
			}
			if (currentDurability > durability) {
				item.set_durability(durability);
			}
		} else {
			int maxDurability = item.getEnchantLevel() + 5;
			int durability = currentDurability + count;
			if (durability > maxDurability) {
				durability = maxDurability;
			}
			if (currentDurability < durability) {
				item.set_durability(durability);
			}
		}

		updateItem(item, L1PcInventory.COL_DURABILITY);
		return item;
	}

	public L1ItemInstance recoveryDamage(L1ItemInstance item) {
		if (item == null) {
			return null;
		}

		int itemType = item.getItem().getType2();
		int durability = item.get_durability();

		if (((durability == 0) && (itemType != 0)) || (durability < 0)) {
			item.set_durability(0);
			return null;
		}

		if (itemType == 0) {
			// 耐久度をプラスしている。
			item.set_durability(durability + 1);
		} else {
			// 損傷度をマイナスしている。
			item.set_durability(durability - 1);
		}

		updateItem(item, L1PcInventory.COL_DURABILITY);
		return item;
	}

	// アイテムＩＤから検索
	public L1ItemInstance findItemId(int id) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemId() == id) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance findKeyId(int id) {
		for (L1ItemInstance item : _items) {
			if (item.getKeyId() == id) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance[] findItemsId(int id) {
		List<L1ItemInstance> itemList = Lists.newList();
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[itemList.size()]);
	}

	public L1ItemInstance[] findItemsIdNotEquipped(int id) {
		List<L1ItemInstance> itemList = Lists.newList();
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id) {
				if (!item.isEquipped()) {
					itemList.add(item);
				}
			}
		}
		return itemList.toArray(new L1ItemInstance[itemList.size()]);
	}

	// オブジェクトＩＤから検索
	public L1ItemInstance getItem(int objectId) {
		for (Object itemObject : _items) {
			L1ItemInstance item = (L1ItemInstance) itemObject;
			if (item.getId() == objectId) {
				return item;
			}
		}
		return null;
	}

	// 特定のアイテムを指定された個数以上所持しているか確認（矢とか魔石の確認）
	public boolean checkItem(int id) {
		return checkItem(id, 1);
	}

	public boolean checkItem(int id, int count) {
		if (count == 0) {
			return true;
		}
		if (ItemTable.getInstance().getTemplate(id).isStackable()) {
			L1ItemInstance item = findItemId(id);
			if ((item != null) && (item.getCount() >= count)) {
				return true;
			}
		} else {
			Object[] itemList = findItemsId(id);
			if (itemList.length >= count) {
				return true;
			}
		}
		return false;
	}

	// 強化された特定のアイテムを指定された個数以上所持しているか確認
	// 装備中のアイテムは所持していないと判別する
	public boolean checkEnchantItem(int id, int enchant, int count) {
		int num = 0;
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) { // 装備しているものは該当しない
				continue;
			}
			if ((item.getItemId() == id) && (item.getEnchantLevel() == enchant)) {
				num++;
				if (num == count) {
					return true;
				}
			}
		}
		return false;
	}

	// 強化された特定のアイテムを消費する
	// 装備中のアイテムは所持していないと判別する
	public boolean consumeEnchantItem(int id, int enchant, int count) {
		for (L1ItemInstance item : _items) {
			if (item.isEquipped()) { // 装備しているものは該当しない
				continue;
			}
			if ((item.getItemId() == id) && (item.getEnchantLevel() == enchant)) {
				removeItem(item);
				return true;
			}
		}
		return false;
	}

	// 特定のアイテムを指定された個数以上所持しているか確認
	// 装備中のアイテムは所持していないと判別する
	public boolean checkItemNotEquipped(int id, int count) {
		if (count == 0) {
			return true;
		}
		return count <= countItems(id);
	}

	// 特定のアイテムを全て必要な個数所持しているか確認（イベントとかで複数のアイテムを所持しているか確認するため）
	public boolean checkItem(int[] ids) {
		int len = ids.length;
		int[] counts = new int[len];
		for (int i = 0; i < len; i++) {
			counts[i] = 1;
		}
		return checkItem(ids, counts);
	}

	public boolean checkItem(int[] ids, int[] counts) {
		for (int i = 0; i < ids.length; i++) {
			if (!checkItem(ids[i], counts[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * このインベントリ内にある、指定されたIDのアイテムの数を数える。
	 * 
	 * @return
	 */
	public int countItems(int id) {
		if (ItemTable.getInstance().getTemplate(id).isStackable()) {
			L1ItemInstance item = findItemId(id);
			if (item != null) {
				return item.getCount();
			}
		} else {
			Object[] itemList = findItemsIdNotEquipped(id);
			return itemList.length;
		}
		return 0;
	}

	public void shuffle() {
		Collections.shuffle(_items);
	}

	// インベントリ内の全てのアイテムを消す（所有者を消すときなど）
	public void clearItems() {
		for (Object itemObject : _items) {
			L1ItemInstance item = (L1ItemInstance) itemObject;
			L1World.getInstance().removeObject(item);
		}
		_items.clear();
	}

	/**
	 * スタック可能なアイテムリストからnameIdと同じ値を持つitemを返す
	 * 
	 * @param nameId
	 * @return item null 如果沒有找到。
	 */
	public L1ItemInstance findItemNameId(String nameId) {
		for (L1ItemInstance item : _items) {
			if (nameId.equals(item.getItem().getIdentifiedNameId())) {
				return item;
			}
		}
		return null;
	}

	// オーバーライド用
	public void loadItems() {
	}

	public void insertItem(L1ItemInstance item) {
	}

	public void updateItem(L1ItemInstance item) {
	}

	public void updateItem(L1ItemInstance item, int colmn) {
	}

}
