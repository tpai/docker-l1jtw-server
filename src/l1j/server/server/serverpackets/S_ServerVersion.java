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

import l1j.server.Config;
import l1j.server.server.Opcodes;

public class S_ServerVersion extends ServerBasePacket {
	private static final int CLIENT_LANGUAGE = Config.CLIENT_LANGUAGE;
	private static final int uptime = (int) (System.currentTimeMillis() / 1000);

	/*
	 * [來源:Server]<位址:17>{長度:32}(時間:1314150068749) 0000: 11 00 38 32 c7 a8 00 a7
	 * c6 a8 00 ba 6e cf 77 ad ..82........n.w. 0010: cd a8 00 71 23 53 4e 00 00
	 * 03 00 00 00 00 08 00 ...q#SN.........
	 */
	public S_ServerVersion() {
		writeC(Opcodes.S_OPCODE_SERVERVERSION);
		writeC(0x00);
		writeC(0x02);
		writeD(0x00a8c732); // server verion 3.5C Taiwan Server
		writeD(0x00a8c6a7); // cache verion 3.5C Taiwan Server
		writeD(0x77cf6eba); // auth verion 3.5C Taiwan Server
		writeD(0x00a8cdad); // npc verion 3.5C Taiwan Server
		writeD(uptime);
		writeC(0x00); // unknown
		writeC(0x00); // unknown
		writeC(CLIENT_LANGUAGE); // Country: 0.US 3.Taiwan 4.Janpan 5.China
		writeD(0x00000000);
		writeC(0xae); // unknown
		writeC(0xb2); // unknown
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
