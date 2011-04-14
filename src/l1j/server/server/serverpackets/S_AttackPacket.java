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
import l1j.server.server.model.Instance.L1PcInstance;

public class S_AttackPacket extends ServerBasePacket {
	private static final String S_ATTACK_PACKET = "[S] S_AttackPacket";

	private byte[] _byte = null;

	public S_AttackPacket(L1PcInstance pc, int objid, int actId) {
		buildpacket(pc, objid, actId, 1, 0 );
	}

	public S_AttackPacket(L1PcInstance pc, int objid, int actId, int dmg) {
		buildpacket(pc, objid, actId, dmg, 0);
	}

	private void buildpacket(L1PcInstance pc, int objid, int actId, int dmg, int type) {
		writeC(Opcodes.S_OPCODE_ATTACKPACKET);
		writeC(actId);
		writeD(pc.getId());
		writeD(objid);
		writeH(dmg); // 3.3C damage
		writeC(pc.getHeading());
		writeD(0x00000000);
		writeC(type); // 0x00:none 0x04:Claw 0x08:CounterMirror
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
