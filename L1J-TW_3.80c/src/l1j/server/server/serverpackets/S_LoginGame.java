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

import static l1j.server.server.Opcodes.S_OPCODE_LOGINTOGAME;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 由3.0的S_Unkown1改名
 * 正式命名為LoginGame
 */
public class S_LoginGame extends ServerBasePacket {
	public S_LoginGame(L1PcInstance pc) {
		/*
		 * 【Server】 id:223 size:8 time:1134908599
		 *  0000:  df 03 c1 55 b5 6e d1 dc
		 */
		writeC(S_OPCODE_LOGINTOGAME);
		writeC(0x03);  // 語系
		if(pc.getClanid() > 0){
			writeD(pc.getClanMemberId());
		} else {
			writeC(0x53);
			writeC(0x01);
			writeC(0x00);
			writeC(0x8b);
		}
		writeC(0x9c);
		writeC(0x1f);
	}
	
	public byte[] getContent() {
		return getBytes();
	}
}
