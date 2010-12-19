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

import java.util.ArrayList;
import java.util.logging.Logger;

import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;


/**
 * GM指令：刪除地上道具
 */
public class L1DeleteGroundItem implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1DeleteGroundItem.class
			.getName());

	private L1DeleteGroundItem() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1DeleteGroundItem();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1ItemInstance) {
				L1ItemInstance l1iteminstance = (L1ItemInstance) l1object;
				if (l1iteminstance.getX() == 0 && l1iteminstance.getY() == 0) { // 地面上のアイテムではなく、誰かの所有物
					continue;
				}

				ArrayList<L1PcInstance> players = L1World.getInstance()
						.getVisiblePlayer(l1iteminstance, 0);
				if (0 == players.size()) {
					L1Inventory groundInventory = L1World.getInstance()
							.getInventory(l1iteminstance.getX(),
									l1iteminstance.getY(),
									l1iteminstance.getMapId());
					int itemId = l1iteminstance.getItem().getItemId();
					if (itemId == 40314 || itemId == 40316) { // ペットのアミュレット
						PetTable.getInstance()
								.deletePet(l1iteminstance.getId());
					} else if (itemId >= 49016 && itemId <= 49025) { // 便箋
						LetterTable lettertable = new LetterTable();
						lettertable.deleteLetter(l1iteminstance.getId());
					} else if (itemId >= 41383 && itemId <= 41400) { // 家具
						if (l1object instanceof L1FurnitureInstance) {
							L1FurnitureInstance furniture = (L1FurnitureInstance) l1object;
							if (furniture.getItemObjId() == l1iteminstance
									.getId()) { // 既に引き出している家具
								FurnitureSpawnTable.getInstance()
										.deleteFurniture(furniture);
							}
						}
					}
					groundInventory.deleteItem(l1iteminstance);
					L1World.getInstance().removeVisibleObject(l1iteminstance);
					L1World.getInstance().removeObject(l1iteminstance);
				}
			}
		}
		L1World.getInstance().broadcastServerMessage(
				"地上的垃圾被GM清除了。");
	}
}
