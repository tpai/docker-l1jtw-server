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
import l1j.server.server.model.Instance.L1NpcInstance;

public class S_NpcChatPacket extends ServerBasePacket {
	private static final String S_NPC_CHAT_PACKET = "[S] S_NpcChatPacket";

	private byte[] _byte = null;

	public S_NpcChatPacket(L1NpcInstance npc, String chat, int type) {
		buildPacket(npc, chat, type);
	}

	private void buildPacket(L1NpcInstance npc, String chat, int type) {
		switch (type) {
			case 0: // normal chat
				writeC(Opcodes.S_OPCODE_NPCSHOUT); // Key is 16 , can use
													// desc-?.tbl
				writeC(type); // Color
				writeD(npc.getId());
				writeS(npc.getName() + ": " + chat);
				break;

			case 2: // shout
				writeC(Opcodes.S_OPCODE_NPCSHOUT); // Key is 16 , can use
													// desc-?.tbl
				writeC(type); // Color
				writeD(npc.getId());
				writeS("<" + npc.getName() + "> " + chat);
				break;

			case 3: // world chat
				writeC(Opcodes.S_OPCODE_NPCSHOUT);
				writeC(type); // XXX 白色になる
				writeD(npc.getId());
				writeS("[" + npc.getName() + "] " + chat);
				break;

			default:
				break;
		}
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
		return S_NPC_CHAT_PACKET;
	}
}
