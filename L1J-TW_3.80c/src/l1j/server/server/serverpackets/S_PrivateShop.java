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
package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_PrivateShop extends ServerBasePacket {

	public S_PrivateShop(L1PcInstance pc, int objectId, int type) {
		L1PcInstance shopPc = (L1PcInstance) L1World.getInstance().findObject(objectId);

		if (shopPc == null) {
			return;
		}

		writeC(Opcodes.S_OPCODE_PRIVATESHOPLIST);
		writeC(type);
		writeD(objectId);

		if (type == 0) {
			List<L1PrivateShopSellList> list = shopPc.getSellList();
			int size = list.size();
			pc.setPartnersPrivateShopItemCount(size);
			writeH(size);
			for (int i = 0; i < size; i++) {
				L1PrivateShopSellList pssl = list.get(i);
				int itemObjectId = pssl.getItemObjectId();
				int count = pssl.getSellTotalCount() - pssl.getSellCount();
				int price = pssl.getSellPrice();
				L1ItemInstance item = shopPc.getInventory().getItem(itemObjectId);
				if (item != null) {
					writeC(i);
					writeC(item.getBless());
					writeH(item.getItem().getGfxId());
					writeD(count);
					writeD(price);
					writeS(item.getNumberedViewName(count));
					writeC(0);
				}
			}
		}
		else if (type == 1) {
			List<L1PrivateShopBuyList> list = shopPc.getBuyList();
			int size = list.size();
			writeH(size);
			for (int i = 0; i < size; i++) {
				L1PrivateShopBuyList psbl = list.get(i);
				int itemObjectId = psbl.getItemObjectId();
				int count = psbl.getBuyTotalCount();
				int price = psbl.getBuyPrice();
				L1ItemInstance item = shopPc.getInventory().getItem(itemObjectId);
				for (L1ItemInstance pcItem : pc.getInventory().getItems()) {
					if ((item.getItemId() == pcItem.getItemId()) && (item.getEnchantLevel() == pcItem.getEnchantLevel())) {
						writeC(i);
						writeD(pcItem.getId());
						writeD(count);
						writeD(price);
					}
				}
			}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
