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

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_Fishing extends ServerBasePacket {

	private static final String S_FISHING = "[S] S_Fishing";

	private byte[] _byte = null;

	public S_Fishing() {
		buildPacket();
	}

	public S_Fishing(int objectId, int motionNum, int x, int y) {
		buildPacket(objectId, motionNum, x, y);
	}

	private void buildPacket() {
		writeC(Opcodes.S_OPCODE_DOACTIONGFX);
		writeC(0x37); // ?
		writeD(0x76002822); // ?
		writeH(0x8AC3); // ?
	}

	private void buildPacket(int objectId, int motionNum, int x, int y) {
		writeC(Opcodes.S_OPCODE_DOACTIONGFX);
		writeD(objectId);
		writeC(motionNum);
		writeH(x);
		writeH(y);
		writeD(0);
		writeH(0);
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
		return S_FISHING;
	}
}
