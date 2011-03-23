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
import l1j.server.server.model.Instance.L1SignboardInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SignboardPack

public class S_SignboardPack extends ServerBasePacket {

	private static final String S_SIGNBOARD_PACK = "[S] S_SignboardPack";

	private static final int STATUS_POISON = 1;

	private byte[] _byte = null;

	public S_SignboardPack(L1SignboardInstance signboard) {
		writeC(Opcodes.S_OPCODE_CHARPACK);
		writeH(signboard.getX());
		writeH(signboard.getY());
		writeD(signboard.getId());
		writeH(signboard.getGfxId());
		writeC(0);
		writeC(getDirection(signboard.getHeading()));
		writeC(0);
		writeC(0);
		writeD(0);
		writeH(0);
		writeS(null);
		writeS(signboard.getName());
		int status = 0;
		if (signboard.getPoison() != null) { // 毒状態
			if (signboard.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
			}
		}
		writeC(status);
		writeD(0);
		writeS(null);
		writeS(null);
		writeC(0);
		writeC(0xFF);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0xFF);
		writeC(0xFF);
	}

	private int getDirection(int heading) {
		int dir = 0;
		switch (heading) {
			case 2:
				dir = 1;
				break;
			case 3:
				dir = 2;
				break;
			case 4:
				dir = 3;
				break;
			case 6:
				dir = 4;
				break;
			case 7:
				dir = 5;
				break;
		}
		return dir;
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
		return S_SIGNBOARD_PACK;
	}

}
