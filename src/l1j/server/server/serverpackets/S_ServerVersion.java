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
	private static final String S_SERVER_VERSION = "[S] ServerVersion";

	private static final int CLIENT_LANGUAGE = Config.CLIENT_LANGUAGE;

/*
 * [Server] opcode = 89 0000: 59 00 02/ c9 60 01 00/ 6a 60 01 00/ 01 ee 00 00/
 * db Y...`..j`....... 0010: 3c 01 00/ ec 66 c4 49/ 00 00 03 58 0d 00 00 10 5f
 * <...f.I...X...._
 */
	public S_ServerVersion() {
		writeC(Opcodes.S_OPCODE_SERVERVERSION);
		// Auth Check client Version
		// 1 = Check
		// 0 = no check
		// > 1 no check
		// type : boolean
		writeC(0x00);

		// your server id, first id = 2
		// id = 0, ????
		// id = 1, ????
		writeC(0x02);

		// all version
		// If the user level is a administrator,
		// inputs /ver to be able to print out all version in game
		// If the user level isn't a administrator
		// inputs /ver to be able to print out client version in game
// writeD(0x00009D7C); // server verion // 2.70C
// writeD(0x0000791A); // cache verion // 2.70C
// writeD(0x0000791A); // auth verion // 2.70C
// writeD(0x00009DD1); // npc verion // 2.70C
		writeD(0x000160c9); // server verion
		writeD(0x0001606a); // cache verion
		writeD(0x0000ee01); // auth verion
		writeD(0x00013cdb); // npc verion

		// ↓ unknown
		// ↓ Old 270Lin.bin
		// ↓ New 270Lin.bin
		// ↓ isn't game time
// writeD(0x882a2cc6); // 2.70C
		writeD(0x49c466ec);

		writeC(0x00); // unknown
		writeC(0x00); // unknown

		// Country
		// 0.US 3.Taiwan 4.Janpan 5.China
		writeC(CLIENT_LANGUAGE);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SERVER_VERSION;
	}
}
