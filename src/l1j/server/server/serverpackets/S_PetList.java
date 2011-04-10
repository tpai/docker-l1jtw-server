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
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.utils.collections.Lists;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_PetList extends ServerBasePacket {

	private static final String S_PETLIST = "[S] S_PetList";

	private byte[] _byte = null;

	public S_PetList(int npcObjId, L1PcInstance pc) {
		buildPacket(npcObjId, pc);
	}

	private void buildPacket(int npcObjId, L1PcInstance pc) {
		List<L1ItemInstance> amuletList = Lists.newList();
		// 判斷身上是否有寵物項圈！
		for (Object itemObject : pc.getInventory().getItems()) {
			L1ItemInstance item = (L1ItemInstance) itemObject;
			if ((item.getItem().getItemId() == 40314) || (item.getItem().getItemId() == 40316)) {
				if (!isWithdraw(pc, item)) {
					amuletList.add(item);
				}
			}
		}

		if (amuletList.size() != 0) {
			writeC(Opcodes.S_OPCODE_SHOWRETRIEVELIST);
			writeD(npcObjId);
			writeH(amuletList.size());
			writeC(0x0c);
			for (L1ItemInstance item : amuletList) {
				writeD(item.getId());
				writeC(0x00);
				writeH(item.get_gfxid());
				writeC(item.getBless());
				writeD(item.getCount());
				writeC(item.isIdentified() ? 1 : 0);
				writeS(item.getViewName());
			}
		}
		writeD(0x00000073); // Price
	}

	private boolean isWithdraw(L1PcInstance pc, L1ItemInstance item) {
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_PETLIST;
	}
}
