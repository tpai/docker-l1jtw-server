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

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1NpcInstance;

public class S_PetCtrlMenu extends ServerBasePacket {
	public S_PetCtrlMenu(L1Character cha, L1NpcInstance npc, boolean open) {
		int index = open ? 1 : cha.getPetList().size() - 1;
		writeC(Opcodes.S_OPCODE_PETCTRL);
		writeC(0x0c);

		for (L1NpcInstance temp : cha.getPetList().values()) {
			if (npc.equals(temp)) {
				writeH(index * 3);
				writeD(!open ? 0x00000001 : 0x00000000);
				writeD(npc.getId());

				if (open) {
					writeD(0x00000000);
					writeH(npc.getX());
					writeH(npc.getY());
					writeS(npc.getName());
				} else {
					writeS(null);
				}

				break;
			}

			index = open ? ++index : --index;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
