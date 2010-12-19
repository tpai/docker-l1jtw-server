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

import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來刪除身上道具的封包
 */
public class C_DeleteInventoryItem extends ClientBasePacket {

	private static Logger _log = Logger.getLogger(C_DeleteInventoryItem.class
			.getName());
	private static final String C_DELETE_INVENTORY_ITEM
			= "[C] C_DeleteInventoryItem";

	public C_DeleteInventoryItem(byte[] decrypt, ClientThread client) {
		super(decrypt);
		int itemObjectId = readD();
		L1PcInstance pc = client.getActiveChar();
		L1ItemInstance item = pc.getInventory().getItem(itemObjectId);

		// 沒有要刪除的道具
		if (item == null) {
			return;
		}

		if (item.getItem().isCantDelete()) {
			// \f1你不能夠放棄此樣物品。
			pc.sendPackets(new S_ServerMessage(125));
			return;
		}

		Object[] petlist = pc.getPetList().values().toArray();
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					// \f1%0%d是不可轉移的…
					pc.sendPackets(new S_ServerMessage(210, item.getItem()
							.getName()));
					return;
				}
			}
		}

		if (item.isEquipped()) {
			// \f1削除できないアイテムや装備しているアイテムは捨てられません。
			pc.sendPackets(new S_ServerMessage(125));
			return;
		}
		if (item.getBless() >= 128) { // 封印された装備
			// \f1%0は捨てたりまたは他人に讓ることができません。
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
			return;
		}

		pc.getInventory().removeItem(item, item.getCount());
		pc.turnOnOffLight();
	}

	@Override
	public String getType() {
		return C_DELETE_INVENTORY_ITEM;
	}
}
