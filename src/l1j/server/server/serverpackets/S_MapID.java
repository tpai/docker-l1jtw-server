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

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_MapID extends ServerBasePacket {

	public S_MapID(int mapid, boolean isUnderwater) {
		writeC(Opcodes.S_OPCODE_MAPID);
		writeH(mapid);
		writeC(isUnderwater ? 1 : 0); // 水底:1
		writeC(0);
		writeH(0);
		writeC(0);
		writeD(0); // 正服值 2
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
