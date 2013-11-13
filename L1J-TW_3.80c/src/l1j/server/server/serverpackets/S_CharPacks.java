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

	public S_CharPacks(String name, String clanName, int type, int sex,
			int lawful, int hp, int mp, int ac, int lv, int str, int dex,
			int con, int wis, int cha, int intel, int accessLevel, int birthday) {
		writeC(Opcodes.S_OPCODE_CHARLIST);
		writeS(name);     	// 角色名稱
		writeS(clanName); 	// 血盟
		writeC(type);     	// 職業種類
		writeC(sex);      	// 性別
		writeH(lawful);   	// 相性
		writeH(hp);       	// 體力
		writeH(mp);       	// 魔力
		writeC(ac);       	// 防禦力
		writeC(lv);       	// 等級
		writeC(str);      	// 力量
		writeC(dex);      	// 敏捷
		writeC(con);      	// 體質
		writeC(wis);      	// 精力
		writeC(cha);      	// 魅力
		writeC(intel);    	// 智力
		writeC(0);        	// 是否為管理員
		writeD(birthday); 	// 創造日
		writeC((lv ^ str ^ dex ^ con ^ wis ^ cha ^ intel) & 0xff);  // XOR 驗證
		
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
