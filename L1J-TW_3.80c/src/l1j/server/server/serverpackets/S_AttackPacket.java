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
import l1j.server.server.model.L1Character;

public class S_AttackPacket extends ServerBasePacket {
	private static final String S_ATTACK_PACKET = "[S] S_AttackPacket";

	private byte[] _byte = null;

	public S_AttackPacket(L1Character atk, int objid, int[] data) {
		buildpacket(atk, objid, data);
	}

	public S_AttackPacket(L1Character atk, int objid, int actid) {
		int[] data = {actid, 0, 0};
		buildpacket(atk, objid, data);
	}

	private void buildpacket(L1Character atk, int objid, int[] data) { // data = {actid, dmg, effect}
		writeC(Opcodes.S_OPCODE_ATTACKPACKET);
		writeC(data[0]); // actid
		writeD(atk.getId());
		writeD(objid);
		writeH(data[1]); // dmg
		writeC(atk.getHeading());
		writeD(0x00000000);
		writeC(data[2]); // effect 0:none 2:爪痕 4:雙擊 8:鏡返射
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
		return S_ATTACK_PACKET;
	}
}
