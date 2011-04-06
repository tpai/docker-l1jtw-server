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

import static l1j.server.server.model.skill.L1SkillId.STATUS_RIBRAVE;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.Random;

public class S_ActiveSpells extends ServerBasePacket {

	private byte[] _byte = null;

	public S_ActiveSpells(L1PcInstance pc) {
		int[] UByte8 = new int[101];
		int[] type = new int[101];
		int[] time = new int[101];
		byte[] randBox = new byte[2];
		randBox[0] = Random.nextByte();
		randBox[1] = Random.nextByte();

		// 取得技能剩餘時間
		CharBuffTable.buffRemainingTime(pc);

		// 登入時給于角色狀態
		if (pc.hasSkillEffect(STATUS_RIBRAVE)) { // 生命之樹果實
			time[61] = pc.getSkillEffectTimeSec(STATUS_RIBRAVE) / 4;
			UByte8[61] = time[61]; // 圖示時間  = 時間 * 4 - 2
			if (time[61] != 0) {
				pc.setSkillEffect(STATUS_RIBRAVE, time[61] * 4 * 1000);
			}
		}

		writeC(Opcodes.S_OPCODE_ACTIVESPELLS);
		writeC(0x14);

		for (int i : UByte8) {
			if (i != 72) {
				writeC(i);
			} else {
				writeD(0x00000000); // 時間???
			}
		}
		writeByte(randBox);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}
}
