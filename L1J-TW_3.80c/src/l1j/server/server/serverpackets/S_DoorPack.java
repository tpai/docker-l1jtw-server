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

import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1DoorInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_DoorPack

public class S_DoorPack extends ServerBasePacket {

	private static final String S_DOOR_PACK = "[S] S_DoorPack";

	private static final int STATUS_POISON = 1;

	private byte[] _byte = null;

	public S_DoorPack(L1DoorInstance door) {
		buildPacket(door);
	}

	private void buildPacket(L1DoorInstance door) {
		writeC(Opcodes.S_OPCODE_CHARPACK);
		writeH(door.getX());
		writeH(door.getY());
		writeD(door.getId());
		writeH(door.getGfxId());
		int doorStatus = door.getStatus();
		int openStatus = door.getOpenStatus();
		if (door.isDead()) {
			writeC(doorStatus);
		}
		else if (openStatus == ActionCodes.ACTION_Open) {
			writeC(openStatus);
		}
		else if ((door.getMaxHp() > 1) && (doorStatus != 0)) {
			writeC(doorStatus);
		}
		else {
			writeC(openStatus);
		}
		writeC(0);
		writeC(0);
		writeC(0);
		writeD(1);
		writeH(0);
		writeS(null);
		writeS(null);
		int status = 0;
		if (door.getPoison() != null) { // 毒状態
			if (door.getPoison().getEffectId() == 1) {
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

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}

	@Override
	public String getType() {
		return S_DOOR_PACK;
	}

}
