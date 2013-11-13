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
import l1j.server.server.utils.IntRange;

public class S_HPUpdate extends ServerBasePacket {
	private static final IntRange hpRange = new IntRange(1, 32767);

	public S_HPUpdate(int currentHp, int maxHp) {
		buildPacket(currentHp, maxHp);
	}

	public S_HPUpdate(L1PcInstance pc) {
		buildPacket(pc.getCurrentHp(), pc.getMaxHp());
	}

	public void buildPacket(int currentHp, int maxHp) {
		writeC(Opcodes.S_OPCODE_HPUPDATE);
		writeH(hpRange.ensure(currentHp));
		writeH(hpRange.ensure(maxHp));
		// writeC(0);
		// writeD(GameTimeController.getInstance().getGameTime());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
