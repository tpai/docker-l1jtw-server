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

public class S_War extends ServerBasePacket {

	private static final String S_WAR = "[S] S_War";

	private byte[] _byte = null;

	public S_War(int type, String clan_name1, String clan_name2) {
		buildPacket(type, clan_name1, clan_name2);
	}

	private void buildPacket(int type, String clan_name1, String clan_name2) {
		// 1 : _血盟が_血盟に宣戦布告しました。
		// 2 : _血盟が_血盟に降伏しました。
		// 3 : _血盟と_血盟との戦争が終結しました。
		// 4 : _血盟が_血盟との戦争で勝利しました。
		// 6 : _血盟と_血盟が同盟を結びました。
		// 7 : _血盟と_血盟との同盟関係が解除されました。
		// 8 : あなたの血盟が現在_血盟と交戦中です。

		writeC(Opcodes.S_OPCODE_WAR);
		writeC(type);
		writeS(clan_name1);
		writeS(clan_name2);
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
		return S_WAR;
	}
}
