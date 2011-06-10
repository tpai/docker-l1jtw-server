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
package l1j.server.server.command.executor;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

/**
 * GM指令：創立道具
 */
public class L1CreateItem implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1CreateItem.class.getName());

	private L1CreateItem() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1CreateItem();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String nameid = st.nextToken();
			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			int isId = 0;
			if (st.hasMoreTokens()) {
				isId = Integer.parseInt(st.nextToken());
			}
			String attr = "";
			if (st.hasMoreTokens()) {
				attr = st.nextToken();
			}
			int attLevel = 0;
			if (st.hasMoreTokens()) {
				attLevel = Integer.parseInt(st.nextToken());
			}
			int itemid = 0;
			try {
				itemid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(
						nameid);
				if (itemid == 0) {
					pc.sendPackets(new S_SystemMessage("找不到符合條件項目。"));
					return;
				}
			}
			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp != null) {
				if (temp.isStackable()) {
					L1ItemInstance item = ItemTable.getInstance().createItem(
							itemid);
					item.setEnchantLevel(0);
					item.setCount(count);
					if (isId == 1) {
						item.setIdentified(true);
					}
					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(new S_ServerMessage(403, // %0を手に入れました。
								item.getLogName() + "(ID:" + itemid + ")"));
					}
				} else {
					L1ItemInstance item = null;
					int createCount;
					for (createCount = 0; createCount < count; createCount++) {
						item = ItemTable.getInstance().createItem(itemid);
						item.setEnchantLevel(enchant);
						if ((item.getItem().getType2() == 2)
								&& (item.getItem().getType() >= 8 && item.getItem().getType() <= 12)) { // 飾品類
							boolean award = false;
							for (int i = 0; i < enchant; i++) {
								if (i == 5) {
									award = true;
								} else {
									award = false;
								}
								switch (item.getItem().getGrade()) {
									case 0: // 上等
										// 四屬性 +1
										item.setFireMr(item.getFireMr() + 1);
										item.setWaterMr(item.getWaterMr() + 1);
										item.setEarthMr(item.getEarthMr() + 1);
										item.setWindMr(item.getWindMr() + 1);
										// LV6 額外獎勵：體力與魔力回復量 +1
										if (award) {
											item.setHpr(item.getHpr() + 1);
											item.setMpr(item.getMpr() + 1);
										}
										break;
									case 1: // 中等
										// HP +2
										item.setaddHp(item.getaddHp() + 2);
										// LV6 額外獎勵：魔防 +1
										if (award) {
											item.setM_Def(item.getM_Def() + 1);
										}
										break;
									case 2: // 初等
										// MP +1
										item.setaddMp(item.getaddMp() + 1);
										// LV6 額外獎勵：魔攻 +1
										if (award) {
											item.setaddSp(item.getaddSp() + 1);
										}
										break;
									case 3: // 特等
										// 功能台版未實裝。
										break;
									default:
										break;
								}
							}
						} else if (item.getItem().getType2() == 1) { // 武器類
							if (attr.equalsIgnoreCase("地") || attr.equalsIgnoreCase("1")) {
								if (attLevel > 0 && attLevel <= 3) {
									item.setAttrEnchantKind(1);
									item.setAttrEnchantLevel(attLevel);
								}
							} else if (attr.equalsIgnoreCase("火") || attr.equalsIgnoreCase("2")) {
								if (attLevel > 0 && attLevel <= 3) {
									item.setAttrEnchantKind(2);
									item.setAttrEnchantLevel(attLevel);
								}
							} else if (attr.equalsIgnoreCase("水") || attr.equalsIgnoreCase("4")) {
								if (attLevel > 0 && attLevel <= 3) {
									item.setAttrEnchantKind(4);
									item.setAttrEnchantLevel(attLevel);
								}
							} else if (attr.equalsIgnoreCase("風") || attr.equalsIgnoreCase("8")) {
								if (attLevel > 0 && attLevel <= 3) {
									item.setAttrEnchantKind(8);
									item.setAttrEnchantLevel(attLevel);
								}
							}
						}
						if (isId == 1) {
							item.setIdentified(true);
						}
						if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
							pc.getInventory().storeItem(item);
						} else {
							break;
						}
					}
					if (createCount > 0) {
						pc.sendPackets(new S_ServerMessage(403, // %0を手に入れました。
								item.getLogName() + "(ID:" + itemid + ")"));
					}
				}
			} else {
				pc.sendPackets(new S_SystemMessage("指定的道具編號不存在"));
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			pc.sendPackets(new S_SystemMessage(
					"請輸入 .item itemid|name [數量] [強化等級] [鑑定狀態] [武器屬性] [屬性等級]。"));
		}
	}
}
