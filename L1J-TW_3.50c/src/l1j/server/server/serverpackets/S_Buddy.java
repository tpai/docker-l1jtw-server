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
import l1j.server.server.model.L1Buddy;

public class S_Buddy extends ServerBasePacket {
	private static final String _S_Buddy = "[S] _S_Buddy";
	private static final String _HTMLID = "buddy";

	private byte[] _byte = null;

	public S_Buddy(int objId, L1Buddy buddy) {
		buildPacket(objId, buddy);
	}

	private void buildPacket(int objId, L1Buddy buddy) {
		writeC(Opcodes.S_OPCODE_SHOWHTML);
		writeD(objId);
		writeS(_HTMLID);
		writeH(0x02);
		writeH(0x02);

		writeS(buddy.getBuddyListString());
		writeS(buddy.getOnlineBuddyListString());
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return _S_Buddy;
	}
}
