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

public class S_Disconnect extends ServerBasePacket {
	public S_Disconnect() {
		int content = 500;
		writeC(Opcodes.S_OPCODE_DISCONNECT);
		writeH(content);
		writeD(0x00000000);
	}

	/**
	 * 0~21, 連線中斷 22, 有人以同樣的帳號登入，請注意，您的密碼可能已經外洩
	 */
	public S_Disconnect(int id) {
		writeC(Opcodes.S_OPCODE_DISCONNECT);
		writeC(id);
		writeD(0x00000000);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
