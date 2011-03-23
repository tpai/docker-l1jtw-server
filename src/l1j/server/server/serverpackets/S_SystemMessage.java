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

public class S_SystemMessage extends ServerBasePacket {
	private static final String S_SYSTEM_MESSAGE = "[S] S_SystemMessage";

	private byte[] _byte = null;

	private final String _msg;

	/**
	 * クライアントにデータの存在しないオリジナルのメッセージを表示する。
	 * メッセージにnameid($xxx)が含まれている場合はオーバーロードされたもう一方を使用する。
	 * 
	 * @param msg
	 *            - 表示する文字列
	 */
	public S_SystemMessage(String msg) {
		_msg = msg;
		writeC(Opcodes.S_OPCODE_SYSMSG);
		writeC(0x09);
		writeS(msg);
	}

	/**
	 * クライアントにデータの存在しないオリジナルのメッセージを表示する。
	 * 
	 * @param msg
	 *            - 表示する文字列
	 * @param nameid
	 *            - 文字列にnameid($xxx)が含まれている場合trueにする。
	 */
	public S_SystemMessage(String msg, boolean nameid) {
		_msg = msg;
		writeC(Opcodes.S_OPCODE_NPCSHOUT);
		writeC(2);
		writeD(0);
		writeS(msg);
		// NPCチャットパケットであればnameidが解釈されるためこれを利用する
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", S_SYSTEM_MESSAGE, _msg);
	}

	@Override
	public String getType() {
		return S_SYSTEM_MESSAGE;
	}
}
