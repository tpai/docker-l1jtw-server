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

public class S_BlueMessage extends ServerBasePacket {

	private static final String _S__18_BLUEMESSAGE = "[S] S_BlueMessage";

	private byte[] _byte = null;

	public S_BlueMessage(int type, String msg1) {
		buildPacket(type, msg1, null, null, 1);
	}

	public S_BlueMessage(int type, String msg1, String msg2) {
		buildPacket(type, msg1, msg2, null, 2);
	}

	public S_BlueMessage(int type, String msg1, String msg2, String msg3) {
		buildPacket(type, msg1, msg2, msg3, 3);
	}

	private void buildPacket(int type, String msg1, String msg2, String msg3,
			int check) {
		writeC(Opcodes.S_OPCODE_BLUEMESSAGE);
		writeH(type);
		if (check == 1) {
			if (msg1.length() <= 0) {
				writeC(0);
			} else {
				writeC(1);
				writeS(msg1);
			}
		} else if (check == 2) {
			writeC(2);
			writeS(msg1);
			writeS(msg2);
		} else if (check == 3) {
			writeC(3);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return _S__18_BLUEMESSAGE;
	}
}
