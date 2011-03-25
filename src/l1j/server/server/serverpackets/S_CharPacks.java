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

public class S_CharPacks extends ServerBasePacket {
	private static final String S_CHAR_PACKS = "[S] S_CharPacks";

	public S_CharPacks(String name, String clanName, int type, int sex, int lawful, int hp, int mp, int ac, int lv, int str, int dex, int con,
			int wis, int cha, int intel, int accessLevel) {
		writeC(Opcodes.S_OPCODE_CHARLIST);
		writeS(name);
		writeS(clanName);
		writeC(type);
		writeC(sex);
		writeH(lawful);
		writeH(hp);
		writeH(mp);
		writeC(ac);
		writeC(lv);
		writeC(str);
		writeC(dex);
		writeC(con);
		writeC(wis);
		writeC(cha);
		writeC(intel);

		// is Administrator
		// 0 = false
		// 1 = true , can't attack
		// > 1 true , can't attack
		// can use Public GameMaster Command
		// if (accessLevel == 200) {
		// writeC(1);
		// } else {
		writeC(0);
		// }
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_CHAR_PACKS;
	}
}
