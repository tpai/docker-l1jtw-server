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

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;

public class S_HowManyKey extends ServerBasePacket {

	/*
	 * 『來源:伺服器』<位址:136>{長度:40}(時間:-473598622)
	 *  0000: 88 09 3e 00 00 dc 00 00 00 01 00 00 00 01 00 00 ..>.............
	 *  0010: 00 08 00 00 00 00 00 69 6e 6e 32 00 00 02 00 24 .......inn2....$
	 *  0020: 34 35 30 00 32 32 30 00 450.220.
	 */
	public S_HowManyKey(L1NpcInstance npc, int price, int min, int max, String htmlId) {
		writeC(Opcodes.S_OPCODE_INPUTAMOUNT);
		writeD(npc.getId());
		writeD(price); // 價錢
		writeD(min); // 起始數量
		writeD(min); // 起始數量
		writeD(max); // 購買上限
		writeH(0); // ?
		writeS(htmlId); // 對話檔檔名
		writeC(0); // ?
		writeH(0x02); // writeS 數量
		writeS(npc.getName()); // 顯示NPC名稱
		writeS(String.valueOf(price)); // 顯示價錢
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
