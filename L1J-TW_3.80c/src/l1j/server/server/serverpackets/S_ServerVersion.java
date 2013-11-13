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
import l1j.server.server.GameServer;
import l1j.server.server.Opcodes;

public class S_ServerVersion extends ServerBasePacket {
	private static final int SERVER_NO = 0x01;
	private static final int CLIENT_LANGUAGE = Config.CLIENT_LANGUAGE;
	private static final int uptime = (int) (System.currentTimeMillis() / 1000);
	
	/*
	 * [Server] opcode = 139
	 * 0000: 8b 00 1c ed 84 c7 07 e8 84 c7 07 2d 69 fc 77 e8 ...........-i.w.
	 * 0010: 84 c7 07 4c d9 f9 51 00 00 03 c2 7d 7f 08 8f 3b ...L..Q....}..;
	 * 0020: fa 51 01 00 97 82 64 56 .Q....dV
	 */
	public S_ServerVersion() {
		writeC(Opcodes.S_OPCODE_SERVERVERSION);
		writeC(0x00);       // Auth ok?
		writeC(SERVER_NO);  // Server Id
		writeD(0x07cbf4dd); // server version 3.80C Taiwan Server
		writeD(0x07cbf4dd); // cache version 3.80C Taiwan Server
		writeD(0x77fc692d); // auth version 3.80C Taiwan Server
		writeD(0x07cbf4d9); // npc version 3.80C Taiwan Server
		writeD(GameServer.getInstance().startTime);
		writeC(0x00); // unknown
		writeC(0x00); // unknown
		writeC(CLIENT_LANGUAGE); // Country: 0.US 3.Taiwan 4.Janpan 5.China
		writeD(0x087f7dc2);      // Server Type
		writeD(uptime);          // Uptime
		writeH(0x01);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
