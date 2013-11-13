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
package l1j.server.server.model.item;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.PerformanceTimer;
import l1j.server.server.utils.Random;
import l1j.server.server.utils.collections.Maps;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1TreasureBox {

	private static Logger _log = Logger
			.getLogger(L1TreasureBox.class.getName());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "TreasureBoxList")
	private static class TreasureBoxList implements Iterable<L1TreasureBox> {
		@XmlElement(name = "TreasureBox")
		private List<L1TreasureBox> _list;

		@Override
		public Iterator<L1TreasureBox> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Item {
		@XmlAttribute(name = "ItemId")
		private int _itemId;

		@XmlAttribute(name = "Count")
		private int _count;

		private int _chance;

		@SuppressWarnings("unused")
		@XmlAttribute(name = "Chance")
		private void setChance(double chance) {
			_chance = (int) (chance * 10000);
		}

		public int getItemId() {
			return _itemId;
		}

		public int getCount() {
			return _count;
		}

		public double getChance() {
			return _chance;
		}

		@XmlAttribute(name = "Enchant")
		private int _enchant;

		public int getEnchant() {
			return _enchant;
		}
	}

	private static enum TYPE {
		RANDOM, SPECIFIC
	}

	private static final String PATH = "./data/xml/Item/TreasureBox.xml";

	private static final Map<Integer, L1TreasureBox> _dataMap = Maps.newMap();

	/**
	 * 指定されたIDのTreasureBoxを返す。
	 * 
	 * @param id
	 *            - TreasureBoxのID。普通はアイテムのItemIdになる。
	 * @return 指定されたIDのTreasureBox。見つからなかった場合はnull。
	 */
	public static L1TreasureBox get(int id) {
		return _dataMap.get(id);
	}

	@XmlAttribute(name = "ItemId")
	private int _boxId;

	@XmlAttribute(name = "Type")
	private TYPE _type;

	private int getBoxId() {
		return _boxId;
	}

	private TYPE getType() {
		return _type;
	}

	@XmlElement(name = "Item")
	private CopyOnWriteArrayList<Item> _items;

	private List<Item> getItems() {
		return _items;
	}

	private int _totalChance;

	private int getTotalChance() {
		return _totalChance;
	}

	private void init() {
		for (Item each : getItems()) {
			_totalChance += each.getChance();
			if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
				getItems().remove(each);
				_log.warning("item ID " + each.getItemId() + " is not found。");
			}
		}
		if ((getTotalChance() != 0) && (getTotalChance() != 1000000)) {
			_log.warning("ID " + getBoxId() + " 的總機率不等於100%。");
		}
	}

	public static void load() {
		PerformanceTimer timer = new PerformanceTimer();
		System.out.print("loading TreasureBox...");
		try {
			JAXBContext context = JAXBContext
					.newInstance(L1TreasureBox.TreasureBoxList.class);

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(PATH);
			TreasureBoxList list = (TreasureBoxList) um.unmarshal(file);

			for (L1TreasureBox each : list) {
				each.init();
				_dataMap.put(each.getBoxId(), each);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, PATH + "載入失敗。", e);
			System.exit(0);
		}
		System.out.println("OK! " + timer.get() + "ms");
	}

	/**
	 * TreasureBoxを開けるPCにアイテムを入手させる。PCがアイテムを持ちきれなかった場合は アイテムは地面に落ちる。
	 * 
	 * @param pc
	 *            - TreasureBoxを開けるPC
	 * @return 開封した結果何らかのアイテムが出てきた場合はtrueを返す。 持ちきれず地面に落ちた場合もtrueになる。
	 */
	public boolean open(L1PcInstance pc) {
		L1ItemInstance item = null;

		if (getType().equals(TYPE.SPECIFIC)) {
			// 出るアイテムが決まっているもの
			for (Item each : getItems()) {
				item = ItemTable.getInstance().createItem(each.getItemId());
				item.setEnchantLevel(each.getEnchant()); // Enchant Feature for treasure_box
				if (item != null) {
					item.setCount(each.getCount());
					storeItem(pc, item);
				}
			}

		} else if (getType().equals(TYPE.RANDOM)) {
			// 出るアイテムがランダムに決まるもの
			int chance = 0;

			int r = Random.nextInt(getTotalChance());

			for (Item each : getItems()) {
				chance += each.getChance();

				if (r < chance) {
					item = ItemTable.getInstance().createItem(each.getItemId());
					item.setEnchantLevel(each.getEnchant()); // Enchant Feature for treasure_box
					if (item != null) {
						item.setCount(each.getCount());
						storeItem(pc, item);
					}
					break;
				}
			}
		}

		if (item == null) {
			return false;
		} else {
			int itemId = getBoxId();

			// 魂の結晶の破片、魔族のスクロール、ブラックエントの実
			if ((itemId == 40576) || (itemId == 40577) || (itemId == 40578)
					|| (itemId == 40411) || (itemId == 49013)) {
				pc.death(null); // キャラクターを死亡させる
			}

			// 多魯嘉之袋
			if ((itemId == 46000)) {
				L1ItemInstance box = pc.getInventory().findItemId(itemId);
				box.setChargeCount(box.getChargeCount() - 1);
				pc.getInventory().updateItem(box, L1PcInventory.COL_CHARGE_COUNT);
				if (box.getChargeCount() < 1) {
					pc.getInventory().removeItem(box, 1);
				}
			}

			return true;
		}
	}

	private static void storeItem(L1PcInstance pc, L1ItemInstance item) {
		L1Inventory inventory;

		if (pc.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
			inventory = pc.getInventory();
		} else {
			// 持てない場合は地面に落とす 処理のキャンセルはしない（不正防止）
			inventory = L1World.getInstance().getInventory(pc.getLocation());
		}
		inventory.storeItem(item);
		pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0を手に入れました。
	}
}
