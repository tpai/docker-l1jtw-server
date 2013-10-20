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
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.collections.Lists;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SystemMessage

public class S_FixWeaponList extends ServerBasePacket {

	private static final String S_FIX_WEAPON_LIST = "[S] S_FixWeaponList";

	public S_FixWeaponList(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_SELECTLIST);
		writeD(0x000000c8); // Price

		List<L1ItemInstance> weaponList = Lists.newList();
		List<L1ItemInstance> itemList = pc.getInventory().getItems();
		for (L1ItemInstance item : itemList) {

			// Find Weapon
			switch (item.getItem().getType2()) {
				case 1:
					if (item.get_durability() > 0) {
						weaponList.add(item);
					}
					break;
			}
		}

		writeH(weaponList.size()); // Weapon Amount

		for (L1ItemInstance weapon : weaponList) {

			writeD(weapon.getId()); // Item ID
			writeC(weapon.get_durability()); // Fix Level
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_FIX_WEAPON_LIST;
	}
}