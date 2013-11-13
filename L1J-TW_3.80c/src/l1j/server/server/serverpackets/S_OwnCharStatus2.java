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
	
	/**
	 * 角色六大素質+負重更新<br>
	 * @param pc 
	 * @param type 0:不檢查重複的屬性  1:檢查重複的屬性次數
	 */
	public S_OwnCharStatus2(L1PcInstance pc, int type) {
		if (type == 0) {
			buildPacket(pc);
		} else if (type == 1) {
			int status[] = { pc.getStr(), pc.getInt(), pc.getWis(),pc.getDex(), pc.getCon(), pc.getCha() };
			for (int i = 0; i <= status.length; i++) {
				for (int j = i + 1; j <= status.length; j++) {
					buildPacket(pc);
				}
			}
		}

	}

	/** 更新六項能力值以及負重 */
	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_OWNCHARSTATUS2);
		writeC(pc.getStr());
		writeC(pc.getInt());
		writeC(pc.getWis());
		writeC(pc.getDex());
		writeC(pc.getCon());
		writeC(pc.getCha());
		writeC(pc.getInventory().getWeight242());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "[C] S_OwnCharStatus2";
	}
}
