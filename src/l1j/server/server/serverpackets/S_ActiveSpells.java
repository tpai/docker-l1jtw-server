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
import l1j.server.server.utils.Random;

public class S_ActiveSpells extends ServerBasePacket {

	private byte[] _byte = null;

	public S_ActiveSpells(L1PcInstance pc) {
		int[] UByte8 = new int[101];
		byte[] randBox = new byte[2];
		randBox[0] = Random.nextByte();
		randBox[1] = Random.nextByte();

		int[] time = new int[101];

		writeC(Opcodes.S_OPCODE_ACTIVESPELLS);
		writeC(0x14);

		if (pc.hasSkillEffect(4001)) { // 神力藥水150%
			time[42] = 900 / 4; // 該技能最後登出時間
			UByte8[42] = time[42]; // 圖示時間 = 時間 * 4 - 2
			UByte8[43] = 32; // type 32: 狩獵的經驗值將會增加。
			pc.setSkillEffect(4001, time[42] - 2);
		} else if (pc.hasSkillEffect(4002)) { // 神力藥水175%
			time[42] = 900 / 4; // 該技能最後登出時間
			UByte8[42] = time[42]; // 圖示時間 = 時間 * 4 - 2
			UByte8[43] = 33; // type 33: 狩獵的經驗值將會增加。
			pc.setSkillEffect(4002, time[42] - 2);
		} else if (pc.hasSkillEffect(4003)) { // 神力藥水200%
			time[42] = 900 / 4; // 該技能最後登出時間
			UByte8[42] = time[42]; // 圖示時間 = 時間 * 4 - 2
			UByte8[43] = 34; // type 34: 狩獵的經驗值將會增加。
			pc.setSkillEffect(4003, time[42] - 2);
		} else if (pc.hasSkillEffect(4004)) { // 神力藥水225%
			time[42] = 900 / 4; // 該技能最後登出時間
			UByte8[42] = time[42]; // 圖示時間 = 時間 * 4 - 2
			UByte8[43] = 35; // type 35: 狩獵的經驗值將會增加。
			pc.setSkillEffect(4004, time[42] - 2);
		} else if (pc.hasSkillEffect(4005)) { // 神力藥水250%
			time[42] = 900 / 4; // 該技能最後登出時間
			UByte8[42] = time[42]; // 圖示時間 = 時間 * 4 - 2
			UByte8[43] = 36; // type 36: 狩獵的經驗值將會增加。
			pc.setSkillEffect(4005, time[42] - 2);
		} else if (pc.hasSkillEffect(4006)) { // 象牙塔妙藥
			time[42] = 900 / 4; // 該技能最後登出時間
			UByte8[42] = time[42]; // 圖示時間 = 時間 * 4 - 2
			UByte8[43] = 54; // type 54:因為妙藥，身心都很輕鬆。提升體力回復量和魔力回復量。
			pc.setSkillEffect(4006, time[42] - 2);
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
