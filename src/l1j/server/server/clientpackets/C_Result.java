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
package l1j.server.server.clientpackets;

import java.util.List;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.model.shop.L1ShopBuyOrderList;
import l1j.server.server.model.shop.L1ShopSellOrderList;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;

/**
 * TODO 翻譯，好多 處理收到由客戶端傳來取得結果的封包
 */
public class C_Result extends ClientBasePacket {

	private static final String C_RESULT = "[C] C_Result";

	public C_Result(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);
		int npcObjectId = readD();
		int resultType = readC();
		int size = readH();

		L1PcInstance pc = clientthread.getActiveChar();
		int level = pc.getLevel();

		int npcId = 0;
		String npcImpl = "";
		boolean isPrivateShop = false;
		boolean tradable = true;
		L1Object findObject = L1World.getInstance().findObject(npcObjectId);
		if (findObject != null) {
			int diffLocX = Math.abs(pc.getX() - findObject.getX());
			int diffLocY = Math.abs(pc.getY() - findObject.getY());
			// 3格以上的距離視為無效要求
			if ((diffLocX > 3) || (diffLocY > 3)) {
				return;
			}
			if (findObject instanceof L1NpcInstance) {
				L1NpcInstance targetNpc = (L1NpcInstance) findObject;
				npcId = targetNpc.getNpcTemplate().get_npcId();
				npcImpl = targetNpc.getNpcTemplate().getImpl();
			}
			else if (findObject instanceof L1PcInstance) {
				isPrivateShop = true;
			}
		}

		if ((resultType == 0) && (size != 0) && npcImpl.equalsIgnoreCase("L1Merchant")) { // 買道具
			L1Shop shop = ShopTable.getInstance().get(npcId);
			L1ShopBuyOrderList orderList = shop.newBuyOrderList();
			for (int i = 0; i < size; i++) {
				orderList.add(readD(), readD());
			}
			shop.sellItems(pc, orderList);
		}
		else if ((resultType == 1) && (size != 0) && npcImpl.equalsIgnoreCase("L1Merchant")) { // 賣道具
			L1Shop shop = ShopTable.getInstance().get(npcId);
			L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
			for (int i = 0; i < size; i++) {
				orderList.add(readD(), readD());
			}
			shop.buyItems(orderList);
		}
		else if ((resultType == 2) && (size != 0) && npcImpl.equalsIgnoreCase("L1Dwarf") && (level >= 5)) { // 自己的倉庫
			int objectId, count;
			for (int i = 0; i < size; i++) {
				tradable = true;
				objectId = readD();
				count = readD();
				L1Object object = pc.getInventory().getItem(objectId);
				L1ItemInstance item = (L1ItemInstance) object;
				if (!item.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0は捨てたりまたは他人に讓ることができません。
				}
				Object[] petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							tradable = false;
							// \f1%0は捨てたりまたは他人に讓ることができません。
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
							break;
						}
					}
				}
				Object[] dolllist = pc.getDollList().values().toArray();
				for (Object dollObject : dolllist) {
					if (dollObject instanceof L1DollInstance) {
						L1DollInstance doll = (L1DollInstance) dollObject;
						if (item.getId() == doll.getItemObjId()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(1181)); // 該当のマジックドールは現在使用中です。
							break;
						}
					}
				}
				if (pc.getDwarfInventory().checkAddItemToWarehouse(item, count, L1Inventory.WAREHOUSE_TYPE_PERSONAL) == L1Inventory.SIZE_OVER) {
					pc.sendPackets(new S_ServerMessage(75)); // \f1これ以上ものを置く場所がありません。
					break;
				}
				if (tradable) {
					pc.getInventory().tradeItem(objectId, count, pc.getDwarfInventory());
					pc.turnOnOffLight();
				}
			}

			// 強制儲存一次身上道具, 避免角色背包內的物品未正常寫入導致物品複製的問題
			pc.saveInventory();
		}
		else if ((resultType == 3) && (size != 0) && npcImpl.equalsIgnoreCase("L1Dwarf") && (level >= 5)) { // 從倉庫取出東西
			int objectId, count;
			L1ItemInstance item;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				item = pc.getDwarfInventory().getItem(objectId);
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) // 檢查重量與容量
				{
					if (pc.getInventory().consumeItem(L1ItemId.ADENA, 30)) {
						pc.getDwarfInventory().tradeItem(item, count, pc.getInventory());
					}
					else {
						pc.sendPackets(new S_ServerMessage(189)); // \f1アデナが不足しています。
						break;
					}
				}
				else {
					pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
					break;
				}
			}
		}
		else if ((resultType == 4) && (size != 0) && npcImpl.equalsIgnoreCase("L1Dwarf") && (level >= 5)) { // 儲存道具到倉庫
			int objectId, count;
			if (pc.getClanid() != 0) { // 有血盟
				for (int i = 0; i < size; i++) {
					tradable = true;
					objectId = readD();
					count = readD();
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
					L1Object object = pc.getInventory().getItem(objectId);
					L1ItemInstance item = (L1ItemInstance) object;
					if (clan != null) {
						if (!item.getItem().isTradable()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0は捨てたりまたは他人に讓ることができません。
						}
						if (item.getBless() >= 128) { // 被封印的裝備
							tradable = false;
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0は捨てたりまたは他人に讓ることができません。
						}
						Object[] petlist = pc.getPetList().values().toArray();
						for (Object petObject : petlist) {
							if (petObject instanceof L1PetInstance) {
								L1PetInstance pet = (L1PetInstance) petObject;
								if (item.getId() == pet.getItemObjId()) {
									tradable = false;
									// \f1%0は捨てたりまたは他人に讓ることができません。
									pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
									break;
								}
							}
						}
						Object[] dolllist = pc.getDollList().values().toArray();
						for (Object dollObject : dolllist) {
							if (dollObject instanceof L1DollInstance) {
								L1DollInstance doll = (L1DollInstance) dollObject;
								if (item.getId() == doll.getItemObjId()) {
									tradable = false;
									pc.sendPackets(new S_ServerMessage(1181)); // 該当のマジックドールは現在使用中です。
									break;
								}
							}
						}
						if (clan.getDwarfForClanInventory().checkAddItemToWarehouse(item, count, L1Inventory.WAREHOUSE_TYPE_CLAN) == L1Inventory.SIZE_OVER) {
							pc.sendPackets(new S_ServerMessage(75)); // \f1これ以上ものを置く場所がありません。
							break;
						}
						if (tradable) {
							pc.getInventory().tradeItem(objectId, count, clan.getDwarfForClanInventory());
							pc.turnOnOffLight();
						}
					}
				}

				// 強制儲存一次身上道具, 避免角色背包內的物品未正常寫入導致物品複製的問題
				pc.saveInventory();
			}
			else {
				pc.sendPackets(new S_ServerMessage(208)); // \f1血盟倉庫を使用するには血盟に加入していなくてはなりません。
			}
		}
		else if ((resultType == 5) && (size != 0) && npcImpl.equalsIgnoreCase("L1Dwarf") && (level >= 5)) { // 從克萊因倉庫中取出道具
			int objectId, count;
			L1ItemInstance item;

			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				for (int i = 0; i < size; i++) {
					objectId = readD();
					count = readD();
					item = clan.getDwarfForClanInventory().getItem(objectId);
					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
						if (pc.getInventory().consumeItem(L1ItemId.ADENA, 30)) {
							clan.getDwarfForClanInventory().tradeItem(item, count, pc.getInventory());
						}
						else {
							pc.sendPackets(new S_ServerMessage(189)); // \f1アデナが不足しています。
							break;
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
						break;
					}
				}
				clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
			}
		}
		else if ((resultType == 5) && (size == 0) && npcImpl.equalsIgnoreCase("L1Dwarf")) { // クラン倉庫から取り出し中にCancel、または、ESCキー
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
			}
		}
		else if ((resultType == 8) && (size != 0) && npcImpl.equalsIgnoreCase("L1Dwarf") && (level >= 5) && pc.isElf()) { // 自分のエルフ倉庫に格納
			int objectId, count;
			for (int i = 0; i < size; i++) {
				tradable = true;
				objectId = readD();
				count = readD();
				L1Object object = pc.getInventory().getItem(objectId);
				L1ItemInstance item = (L1ItemInstance) object;
				if (!item.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0は捨てたりまたは他人に讓ることができません。
				}
				Object[] petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							tradable = false;
							// \f1%0は捨てたりまたは他人に讓ることができません。
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
							break;
						}
					}
				}
				Object[] dolllist = pc.getDollList().values().toArray();
				for (Object dollObject : dolllist) {
					if (dollObject instanceof L1DollInstance) {
						L1DollInstance doll = (L1DollInstance) dollObject;
						if (item.getId() == doll.getItemObjId()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(1181)); // 該当のマジックドールは現在使用中です。
							break;
						}
					}
				}
				if (pc.getDwarfForElfInventory().checkAddItemToWarehouse(item, count, L1Inventory.WAREHOUSE_TYPE_PERSONAL) == L1Inventory.SIZE_OVER) {
					pc.sendPackets(new S_ServerMessage(75)); // \f1これ以上ものを置く場所がありません。
					break;
				}
				if (tradable) {
					pc.getInventory().tradeItem(objectId, count, pc.getDwarfForElfInventory());
					pc.turnOnOffLight();
				}
			}

			// 強制儲存一次身上道具, 避免角色背包內的物品未正常寫入導致物品複製的問題
			pc.saveInventory();
		}
		else if ((resultType == 9) && (size != 0) && npcImpl.equalsIgnoreCase("L1Dwarf") && (level >= 5) && pc.isElf()) { // 自分のエルフ倉庫から取り出し
			int objectId, count;
			L1ItemInstance item;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				item = pc.getDwarfForElfInventory().getItem(objectId);
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
					if (pc.getInventory().consumeItem(40494, 2)) { // ミスリル
						pc.getDwarfForElfInventory().tradeItem(item, count, pc.getInventory());
					}
					else {
						pc.sendPackets(new S_ServerMessage(337, "$767")); // \f1%0が不足しています。
						break;
					}
				}
				else {
					pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
					break;
				}
			}
		}
		else if ((resultType == 0) && (size != 0) && isPrivateShop) { // 個人商店からアイテム購入
			if (findObject == null) {
				return;
			}
			if (!(findObject instanceof L1PcInstance)) {
				return;
			}
			L1PcInstance targetPc = (L1PcInstance) findObject;

			int order;
			int count;
			int price;
			List<L1PrivateShopSellList> sellList;
			L1PrivateShopSellList pssl;
			int itemObjectId;
			int sellPrice;
			int sellTotalCount;
			int sellCount;
			L1ItemInstance item;
			boolean[] isRemoveFromList = new boolean[8];

			if (targetPc.isTradingInPrivateShop()) {
				return;
			}
			sellList = targetPc.getSellList();
			synchronized (sellList) {
				// 売り切れが発生し、閲覧中のアイテム数とリスト数が異なる
				if (pc.getPartnersPrivateShopItemCount() != sellList.size()) {
					return;
				}
				targetPc.setTradingInPrivateShop(true);

				for (int i = 0; i < size; i++) { // 購入予定の商品
					order = readD();
					count = readD();
					pssl = sellList.get(order);
					itemObjectId = pssl.getItemObjectId();
					sellPrice = pssl.getSellPrice();
					sellTotalCount = pssl.getSellTotalCount(); // 売る予定の個数
					sellCount = pssl.getSellCount(); // 売った累計
					item = targetPc.getInventory().getItem(itemObjectId);
					if (item == null) {
						continue;
					}
					if (count > sellTotalCount - sellCount) {
						count = sellTotalCount - sellCount;
					}
					if (count == 0) {
						continue;
					}

					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
						for (int j = 0; j < count; j++) { // オーバーフローをチェック
							if (sellPrice * j > 2000000000) {
								pc.sendPackets(new S_ServerMessage(904, // 総販売価格は%dアデナを超過できません。
										"2000000000"));
								targetPc.setTradingInPrivateShop(false);
								return;
							}
						}
						price = count * sellPrice;
						if (pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
							L1ItemInstance adena = pc.getInventory().findItemId(L1ItemId.ADENA);
							if ((targetPc != null) && (adena != null)) {
								if (targetPc.getInventory().tradeItem(item, count, pc.getInventory()) == null) {
									targetPc.setTradingInPrivateShop(false);
									return;
								}
								pc.getInventory().tradeItem(adena, price, targetPc.getInventory());
								String message = item.getItem().getName() + " (" + String.valueOf(count) + ")";
								targetPc.sendPackets(new S_ServerMessage(877, // %1%o
										// %0に販売しました。
										pc.getName(), message));
								pssl.setSellCount(count + sellCount);
								sellList.set(order, pssl);
								if (pssl.getSellCount() == pssl.getSellTotalCount()) { // 売る予定の個数を売った
									isRemoveFromList[order] = true;
								}
							}
						}
						else {
							pc.sendPackets(new S_ServerMessage(189)); // \f1アデナが不足しています。
							break;
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(270)); // \f1持っているものが重くて取引できません。
						break;
					}
				}
				// 売り切れたアイテムをリストの末尾から削除
				for (int i = 7; i >= 0; i--) {
					if (isRemoveFromList[i]) {
						sellList.remove(i);
					}
				}
				targetPc.setTradingInPrivateShop(false);
			}
		}
		else if ((resultType == 1) && (size != 0) && isPrivateShop) { // 個人商店にアイテム売却
			int count;
			int order;
			List<L1PrivateShopBuyList> buyList;
			L1PrivateShopBuyList psbl;
			int itemObjectId;
			L1ItemInstance item;
			int buyPrice;
			int buyTotalCount;
			int buyCount;
			boolean[] isRemoveFromList = new boolean[8];

			L1PcInstance targetPc = null;
			if (findObject instanceof L1PcInstance) {
				targetPc = (L1PcInstance) findObject;
			}
			if (targetPc.isTradingInPrivateShop()) {
				return;
			}
			targetPc.setTradingInPrivateShop(true);
			buyList = targetPc.getBuyList();

			for (int i = 0; i < size; i++) {
				itemObjectId = readD();
				count = readCH();
				order = readC();
				item = pc.getInventory().getItem(itemObjectId);
				if (item == null) {
					continue;
				}
				psbl = buyList.get(order);
				buyPrice = psbl.getBuyPrice();
				buyTotalCount = psbl.getBuyTotalCount(); // 買う予定の個数
				buyCount = psbl.getBuyCount(); // 買った累計
				if (count > buyTotalCount - buyCount) {
					count = buyTotalCount - buyCount;
				}
				if (item.isEquipped()) {
					pc.sendPackets(new S_ServerMessage(905)); // 装備しているアイテムは販売できません。
					continue;
				}

				if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
					for (int j = 0; j < count; j++) { // オーバーフローをチェック
						if (buyPrice * j > 2000000000) {
							targetPc.sendPackets(new S_ServerMessage(904, // 総販売価格は%dアデナを超過できません。
									"2000000000"));
							return;
						}
					}
					if (targetPc.getInventory().checkItem(L1ItemId.ADENA, count * buyPrice)) {
						L1ItemInstance adena = targetPc.getInventory().findItemId(L1ItemId.ADENA);
						if (adena != null) {
							targetPc.getInventory().tradeItem(adena, count * buyPrice, pc.getInventory());
							pc.getInventory().tradeItem(item, count, targetPc.getInventory());
							psbl.setBuyCount(count + buyCount);
							buyList.set(order, psbl);
							if (psbl.getBuyCount() == psbl.getBuyTotalCount()) { // 買う予定の個数を買った
								isRemoveFromList[order] = true;
							}
						}
					}
					else {
						targetPc.sendPackets(new S_ServerMessage(189)); // \f1アデナが不足しています。
						break;
					}
				}
				else {
					pc.sendPackets(new S_ServerMessage(271)); // \f1相手が物を持ちすぎていて取引できません。
					break;
				}
			}
			// 買い切ったアイテムをリストの末尾から削除
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					buyList.remove(i);
				}
			}
			targetPc.setTradingInPrivateShop(false);
		}
		else if ((resultType == 12) && (size != 0) && npcImpl.equalsIgnoreCase("L1Merchant")) { // 領取寵物
			int petCost, petCount, divisor, itemObjectId, itemCount = 0;
			boolean chackAdena = true;

			for (int i = 0; i < size; i++) {
				petCost = 0;
				petCount = 0;
				divisor = 6;
				itemObjectId = readD();
				itemCount = readD();

				if (itemCount == 0) {
					continue;
				}
				Object[] petList = pc.getPetList().values().toArray();
				for (Object pet : petList) {
					petCost += ((L1NpcInstance) pet).getPetcost();
				}
				int charisma = pc.getCha();
				if (pc.isCrown()) { // 王族
					charisma += 6;
				}
				else if (pc.isElf()) { // 妖精
					charisma += 12;
				}
				else if (pc.isWizard()) { // 法師
					charisma += 6;
				}
				else if (pc.isDarkelf()) { // 黑暗妖精
					charisma += 6;
				}
				else if (pc.isDragonKnight()) { // 龍騎士
					charisma += 6;
				}
				else if (pc.isIllusionist()) { // 幻術師
					charisma += 6;
				}

				if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 115)) {
					chackAdena = false;
				}
				L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
				if (l1pet != null && chackAdena) {
					npcId = l1pet.get_npcid();
					charisma -= petCost;
					if ((npcId == 45313) || (npcId == 45710 // タイガー、バトルタイガー
							) || (npcId == 45711) || (npcId == 45712)) { // 紀州犬の子犬、紀州犬
						divisor = 12;
					}
					else {
						divisor = 6;
					}
					petCount = charisma / divisor;
					if (petCount <= 0) {
						pc.sendPackets(new S_ServerMessage(489)); // 你無法一次控制那麼多寵物。
						return;
					}
					L1Npc npcTemp = NpcTable.getInstance().getTemplate(npcId);
					L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
					pet.setPetcost(divisor);
				}
			}
			if (!chackAdena) {
				pc.sendPackets(new S_ServerMessage(189)); // \f1金幣不足。
			}
		}
	}

	@Override
	public String getType() {
		return C_RESULT;
	}

}
