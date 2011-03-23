/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ServerMessage extends ServerBasePacket {
	private static final String S_SERVER_MESSAGE = "[S] S_ServerMessage";

	public static final int NO_PLEDGE = 208;

	public static final int CANNOT_GLOBAL = 195;

	public static final int CANNOT_BOOKMARK_LOCATION = 214;

	public static final int USER_NOT_ON = 73;

	public static final int NOT_ENOUGH_MP = 278;

	public static final int YOU_FEEL_BETTER = 77;

	public static final int YOUR_WEAPON_BLESSING = 693;

	public static final int YOUR_Are_Slowed = 29;

	private byte[] _byte = null;

	public S_ServerMessage(int type) {
		buildPacket(type, null, null, null, null, null, 0);
	}

	public S_ServerMessage(int type, String msg1) {
		buildPacket(type, msg1, null, null, null, null, 1);
	}

	public S_ServerMessage(int type, String msg1, String msg2) {
		buildPacket(type, msg1, msg2, null, null, null, 2);
	}

	public S_ServerMessage(int type, String msg1, String msg2, String msg3) {
		buildPacket(type, msg1, msg2, msg3, null, null, 3);
	}

	public S_ServerMessage(int type, String msg1, String msg2, String msg3, String msg4) {
		buildPacket(type, msg1, msg2, msg3, msg4, null, 4);
	}

	public S_ServerMessage(int type, String msg1, String msg2, String msg3, String msg4, String msg5) {

		buildPacket(type, msg1, msg2, msg3, msg4, msg5, 5);
	}

	private void buildPacket(int type, String msg1, String msg2, String msg3, String msg4, String msg5, int check) {

		writeC(Opcodes.S_OPCODE_SERVERMSG);
		writeH(type);

		if (check == 0) {
			writeC(0);
		}
		else if (check == 1) {
			writeC(1);
			writeS(msg1);
		}
		else if (check == 2) {
			writeC(2);
			writeS(msg1);
			writeS(msg2);
		}
		else if (check == 3) {
			writeC(3);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
		}
		else if (check == 4) {
			writeC(4);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
			writeS(msg4);
		}
		else {
			writeC(5);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
			writeS(msg4);
			writeS(msg5);
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
		return S_SERVER_MESSAGE;
	}
}
