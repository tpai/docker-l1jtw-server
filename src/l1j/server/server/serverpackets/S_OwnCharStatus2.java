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
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SendInvOnLogin

public class S_OwnCharStatus2 extends ServerBasePacket {

	public S_OwnCharStatus2(L1PcInstance l1pcinstance) {
		if (l1pcinstance == null) {
			return;
		}

		cha = l1pcinstance;

		writeC(Opcodes.S_OPCODE_OWNCHARSTATUS2);
		writeC(cha.getStr());
		writeC(cha.getInt());
		writeC(cha.getWis());
		writeC(cha.getDex());
		writeC(cha.getCon());
		writeC(cha.getCha());
		writeC(cha.getInventory().getWeight240());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "[C] S_OwnCharStatus2";
	}

	private L1PcInstance cha = null;
}
