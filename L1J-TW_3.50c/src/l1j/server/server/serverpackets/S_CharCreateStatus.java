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

public class S_CharCreateStatus extends ServerBasePacket {
	private static final String S_CHAR_CREATE_STATUS = "[S] S_CharCreateStatus";

	public static final int REASON_OK = 0x02;

	public static final int REASON_ALREADY_EXSISTS = 0x06;

	public static final int REASON_INVALID_NAME = 0x09;

	public static final int REASON_WRONG_AMOUNT = 0x15;

	public S_CharCreateStatus(int reason) {
		writeC(Opcodes.S_OPCODE_NEWCHARWRONG);
		writeC(reason);
		writeD(0x00000000);
		writeD(0x0000);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_CHAR_CREATE_STATUS;
	}
}
