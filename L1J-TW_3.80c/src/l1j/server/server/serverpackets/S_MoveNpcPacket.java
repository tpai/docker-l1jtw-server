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
import l1j.server.server.model.Instance.L1MonsterInstance;

public class S_MoveNpcPacket extends ServerBasePacket {
	private static final String _S__1F_S_MOVENPCPACKET = "[S] S_MoveNpcPacket";

	public S_MoveNpcPacket(L1MonsterInstance npc, int x, int y, int heading) {
		// npc.set_moving(true);

		writeC(Opcodes.S_OPCODE_MOVEOBJECT);
		writeD(npc.getId());
		writeH(x);
		writeH(y);
		writeC(heading);
		writeC(0x80);       // 3.80C 更動
		writeD(0x00000000);

		// npc.set_moving(false);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S__1F_S_MOVENPCPACKET;
	}
}
