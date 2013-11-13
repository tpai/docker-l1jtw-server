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

import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_ChatPacket extends ServerBasePacket {

	private static final String _S__1F_NORMALCHATPACK = "[S] S_ChatPacket";

	private byte[] _byte = null;

	public S_ChatPacket(L1PcInstance pc, String chat, int opcode, int type) {

		if (type == 0) { // 一般頻道
			writeC(opcode);
			writeC(type);
			if (pc.isInvisble()) {
				writeD(0);
			}
			else {
				writeD(pc.getId());
			}
			writeS(pc.getName() + ": " + chat);
		}
		else if (type == 2) { // 大喊
			writeC(opcode);
			writeC(type);
			if (pc.isInvisble()) {
				writeD(0);
			}
			else {
				writeD(pc.getId());
			}
			writeS("<" + pc.getName() + "> " + chat);
			writeH(pc.getX());
			writeH(pc.getY());
		}
		else if (type == 3) { // 世界頻道
			writeC(opcode);
			writeC(type);
			if (pc.isGm() == true) {
				writeS("[******] " + chat);
			}
			else {
				writeS("[" + pc.getName() + "] " + chat);
			}
		}
		else if (type == 4) { // 血盟騎士頻道
			writeC(opcode);
			writeC(type);
			writeS("{" + pc.getName() + "} " + chat);
		}
		else if (type == 9) { // 密語頻道
			writeC(opcode);
			writeC(type);
			writeS("-> (" + pc.getName() + ") " + chat);
		}
		else if (type == 11) { // 血盟頻道
			writeC(opcode);
			writeC(type);
			writeS("(" + pc.getName() + ") " + chat);
		}
		else if (type == 12) { // 交易頻道
			writeC(opcode);
			writeC(type);
			writeS("[" + pc.getName() + "] " + chat);
		}
		else if (type == 13) { // 聯盟頻道
			writeC(opcode);
			writeC(0x04);
			writeS("{{" + pc.getName() + "}} " + chat);
		}
		else if (type == 14) { // 隊伍頻道
			writeC(opcode);
			writeC(type);
			if (pc.isInvisble()) {
				writeD(0);
			}
			else {
				writeD(pc.getId());
			}
			writeS("(" + pc.getName() + ") " + chat);
		}
		else if (type == 16) { // 密語頻道
			writeC(opcode);
			writeS(pc.getName());
			writeS(chat);
		}
		else if(type == 17) { // 血盟王族公告頻道
			writeC(opcode);
			writeC(type);
			writeS("{" + pc.getName() + "}" + chat);
		}
	}

	@Override
	public byte[] getContent() {
		if (null == _byte) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return _S__1F_NORMALCHATPACK;
	}

}