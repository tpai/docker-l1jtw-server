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

import java.util.List;

import l1j.server.Config;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.game.L1BugBearRace;
import l1j.server.server.model.identity.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.Random;
import l1j.server.server.utils.collections.Lists;

public class L1Shop {
	private final int _npcId;

	private final List<L1ShopItem> _sellingItems;

	private final List<L1ShopItem> _purchasingItems;

	public L1Shop(int npcId, List<L1ShopItem> sellingItems,
			List<L1ShopItem> purchasingItems) {
		if ((sellingItems == null) || (purchasingItems == null)) {
			throw new NullPointerException();
		}

		_npcId = npcId;
		_sellingItems = sellingItems;
		_purchasingItems = purchasingItems;
	}

	public int getNpcId() {
		return _npcId;
	}

	public List<L1ShopItem> getSellingItems() {
		return _sellingItems;
	}

	/**
	 * この商店で、指定されたアイテムが買取可能な状態であるかを返す。
	 * 
	 * @param item
	 * @return アイテムが買取可能であればtrue
	 */
	private boolean isPurchaseableItem(L1ItemInstance item) {
		if (item == null) {
			return false;
		}
		if (item.isEquipped()) { // 装備中であれば不可
			return false;
		}
		if (item.getEnchantLevel() != 0) { // 強化(or弱化)されていれば不可
			return false;
		}
		if (item.getBless() >= 128) { // 封印された装備
			return false;
		}

		return true;
	}

	private L1ShopItem getPurchasingItem(int itemId) {
		for (L1ShopItem shopItem : _purchasingItems) {
			if (shopItem.getItemId() == itemId) {
				return shopItem;
			}
		}
		return null;
	}

	public L1AssessedItem assessItem(L1ItemInstance item) {
		L1ShopItem shopItem = getPurchasingItem(item.getItemId());
		if (shopItem == null) {
			return null;
		}
		return new L1AssessedItem(item.getId(), getAssessedPrice(shopItem));
	}

	private int getAssessedPrice(L1ShopItem item) {
		return (int) (item.getPrice() * Config.RATE_SHOP_PURCHASING_PRICE / item
				.getPackCount());
	}

	/**
	 * インベントリ内の買取可能アイテムを査定する。
	 * 
	 * @param inv
	 *            査定対象のインベントリ
	 * @return 査定された買取可能アイテムのリスト
	 */
	public List<L1AssessedItem> assessItems(L1PcInventory inv) {
		List<L1AssessedItem> result = Lists.newList();
		for (L1ShopItem item : _purchasingItems) {
			for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
				if (!isPurchaseableItem(targetItem)) {
					continue;
				}

				result.add(new L1AssessedItem(targetItem.getId(),
						getAssessedPrice(item)));
			}
		}
		return result;
	}

	/**
	 * プレイヤーへアイテムを販売できることを保証する。
	 * 
	 * @return 何らかの理由でアイテムを販売できない場合、false
	 */
	private boolean ensureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPriceTaxIncluded();
		// オーバーフローチェック
		if (!IntRange.includes(price, 0, 2000000000)) {
			// 総販売価格は%dアデナを超過できません。
			pc.sendPackets(new S_ServerMessage(904, "2000000000"));
			return false;
		}
		// 購入できるかチェック
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			System.out.println(price);
			// \f1アデナが不足しています。
			pc.sendPackets(new S_ServerMessage(189));
			return false;
		}
		// 重量チェック
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			// アイテムが重すぎて、これ以上持てません。
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		// 個数チェック
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1一人のキャラクターが持って歩けるアイテムは最大180個までです。
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		return true;
	}

	/**
	 * 地域税納税処理 アデン城・ディアド要塞を除く城はアデン城へ国税として10%納税する
	 * 
	 * @param orderList
	 */
	private void payCastleTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();

		int price = orderList.getTotalPrice();

		int castleId = L1CastleLocation.getCastleIdByNpcid(_npcId);
		int castleTax = calc.calcCastleTaxPrice(price);
		int nationalTax = calc.calcNationalTaxPrice(price);
		// アデン城・ディアド城の場合は国税なし
		if ((castleId == L1CastleLocation.ADEN_CASTLE_ID)
				|| (castleId == L1CastleLocation.DIAD_CASTLE_ID)) {
			castleTax += nationalTax;
			nationalTax = 0;
		}

		if ((castleId != 0) && (castleTax > 0)) {
			L1Castle castle = CastleTable.getInstance()
					.getCastleTable(castleId);

			synchronized (castle) {
				int money = castle.getPublicMoney();
				if (2000000000 > money) {
					money = money + castleTax;
					castle.setPublicMoney(money);
					CastleTable.getInstance().updateCastle(castle);
				}
			}

			if (nationalTax > 0) {
				L1Castle aden = CastleTable.getInstance().getCastleTable(
						L1CastleLocation.ADEN_CASTLE_ID);
				synchronized (aden) {
					int money = aden.getPublicMoney();
					if (2000000000 > money) {
						money = money + nationalTax;
						aden.setPublicMoney(money);
						CastleTable.getInstance().updateCastle(aden);
					}
				}
			}
		}
	}

	/**
	 * ディアド税納税処理 戦争税の10%がディアド要塞の公金となる。
	 * 
	 * @param orderList
	 */
	private void payDiadTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();

		int price = orderList.getTotalPrice();

		// ディアド税
		int diadTax = calc.calcDiadTaxPrice(price);
		if (diadTax <= 0) {
			return;
		}

		L1Castle castle = CastleTable.getInstance().getCastleTable(
				L1CastleLocation.DIAD_CASTLE_ID);
		synchronized (castle) {
			int money = castle.getPublicMoney();
			if (2000000000 > money) {
				money = money + diadTax;
				castle.setPublicMoney(money);
				CastleTable.getInstance().updateCastle(castle);
			}
		}
	}

	/**
	 * 町税納税処理
	 * 
	 * @param orderList
	 */
	private void payTownTax(L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();

		// 町の売上
		if (!L1World.getInstance().isProcessingContributionTotal()) {
			int town_id = L1TownLocation.getTownIdByNpcid(_npcId);
			if ((town_id >= 1) && (town_id <= 10)) {
				TownTable.getInstance().addSalesMoney(town_id, price);
			}
		}
	}

	// XXX 納税処理はこのクラスの責務では無い気がするが、とりあえず
	private void payTax(L1ShopBuyOrderList orderList) {
		payCastleTax(orderList);
		payTownTax(orderList);
		payDiadTax(orderList);
	}

	/**
	 * 販売取引
	 */
	private void sellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(L1ItemId.ADENA,orderList.getTotalPriceTaxIncluded())) {
			throw new IllegalStateException("購入に必要なアデナを消費できませんでした。");
		}
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			L1ItemInstance item = ItemTable.getInstance().createItem(itemId);
			if (item.getItemId() == 40309) {// Race Tickets
				item.setItem(order.getItem().getItem());
				L1BugBearRace.getInstance().setAllBet(
						L1BugBearRace.getInstance().getAllBet()
								+ (amount * order.getItem().getPrice()));
				String[] runNum = item.getItem().getIdentifiedNameId()
						.split("-");
				int trueNum = 0;
				for (int i = 0; i < 5; i++) {
					if (L1BugBearRace.getInstance().getRunner(i).getNpcId() - 91350 == (Integer
							.parseInt(runNum[runNum.length - 1]) - 1)) {
						trueNum = i;
						break;
					}
				}
				L1BugBearRace.getInstance().setBetCount(
						trueNum,
						L1BugBearRace.getInstance().getBetCount(trueNum)
								+ amount);
			}
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			if ((_npcId == 70068) || (_npcId == 70020)) {
				item.setIdentified(false);
				int chance = Random.nextInt(100) + 1;
				if (chance <= 15) {
					item.setEnchantLevel(-2);
				} else if ((chance >= 16) && (chance <= 30)) {
					item.setEnchantLevel(-1);
				} else if ((chance >= 31) && (chance <= 70)) {
					item.setEnchantLevel(0);
				} else if ((chance >= 71) && (chance <= 87)) {
					item.setEnchantLevel(Random.nextInt(2) + 1);
				} else if ((chance >= 88) && (chance <= 97)) {
					item.setEnchantLevel(Random.nextInt(3) + 3);
				} else if ((chance >= 98) && (chance <= 99)) {
					item.setEnchantLevel(6);
				} else if (chance == 100) {
					item.setEnchantLevel(7);
				}
			}
		}
	}

	/**
	 * プレイヤーに、L1ShopBuyOrderListに記載されたアイテムを販売する。
	 * 
	 * @param pc
	 *            販売するプレイヤー
	 * @param orderList
	 *            販売すべきアイテムが記載されたL1ShopBuyOrderList
	 */
	public void sellItems(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		if (!ensureSell(pc, orderList)) {
			return;
		}

		sellItems(pc.getInventory(), orderList);
		payTax(orderList);
	}

	/**
	 * L1ShopSellOrderListに記載されたアイテムを買い取る。
	 * 
	 * @param orderList
	 *            買い取るべきアイテムと価格が記載されたL1ShopSellOrderList
	 */
	public void buyItems(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		for (L1ShopSellOrder order : orderList.getList()) {
			int count = inv.removeItem(order.getItem().getTargetId(),
					order.getCount());
			totalPrice += order.getItem().getAssessedPrice() * count;
		}

		totalPrice = IntRange.ensure(totalPrice, 0, 2000000000);
		if (0 < totalPrice) {
			inv.storeItem(L1ItemId.ADENA, totalPrice);
		}
	}

	public L1ShopBuyOrderList newBuyOrderList() {
		return new L1ShopBuyOrderList(this);
	}

	public L1ShopSellOrderList newSellOrderList(L1PcInstance pc) {
		return new L1ShopSellOrderList(this, pc);
	}
}
