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

import static l1j.server.server.model.skill.L1SkillId.DRESS_EVASION;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_150;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_175;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_200;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_225;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_EXP_250;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_BLESS_OF_MAZU;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_POTION_OF_BATTLE;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_STRENGTHENING_HP;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_STRENGTHENING_MP;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_ENCHANTING_BATTLE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_RIBRAVE;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.Random;

public class S_ActiveSpells extends ServerBasePacket {

	private byte[] _byte = null;

	public S_ActiveSpells(L1PcInstance pc) {
		byte[] randBox = new byte[2];
		randBox[0] = Random.nextByte();
		randBox[1] = Random.nextByte();

		// 取得技能剩餘時間
		CharBuffTable.buffRemainingTime(pc);

		writeC(Opcodes.S_OPCODE_ACTIVESPELLS);
		writeC(0x14);

		for (int i : activeSpells(pc)) {
			if (i != 72) {
				writeC(i);
			} else {
				writeD(0x00000000); // 時間???
			}
		}
		writeByte(randBox);
	}

	// 登入時給于角色狀態剩餘時間
	private int[] activeSpells(L1PcInstance pc) {
		int[] data = new int[101];
		 // 生命之樹果實
		if (pc.hasSkillEffect(STATUS_RIBRAVE)) {
			data[61] = pc.getSkillEffectTimeSec(STATUS_RIBRAVE) / 4;
		}
		// 迴避提升
		if (pc.hasSkillEffect(DRESS_EVASION)) {
			data[17] = pc.getSkillEffectTimeSec(DRESS_EVASION) / 4;
		}
		// 戰鬥藥水、神力藥水
		if (pc.hasSkillEffect(EFFECT_POTION_OF_BATTLE)) {
			data[45] = pc.getSkillEffectTimeSec(EFFECT_POTION_OF_BATTLE) / 16;
			if (data[45] != 0) {
				data[62] = 20; // 經驗值加成20%。
			}
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_250)) {
			data[42] = pc.getSkillEffectTimeSec(EFFECT_POTION_OF_EXP_250) / 4;
			if (data[42] != 0) {
				data[43] = 36; // 狩獵經驗值將會增加。
			}
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_225)) {
			data[42] = pc.getSkillEffectTimeSec(EFFECT_POTION_OF_EXP_225) / 4;
			if (data[42] != 0) {
				data[43] = 35; // 狩獵經驗值將會增加。
			}
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_200)) {
			data[42] = pc.getSkillEffectTimeSec(EFFECT_POTION_OF_EXP_200) / 4;
			if (data[42] != 0) {
				data[43] = 34; // 狩獵經驗值將會增加。
			}
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_175)) {
			data[42] = pc.getSkillEffectTimeSec(EFFECT_POTION_OF_EXP_175) / 4;
			if (data[42] != 0) {
				data[43] = 33; // 狩獵經驗值將會增加。
			}
		} else if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_150)) {
			data[42] = pc.getSkillEffectTimeSec(EFFECT_POTION_OF_EXP_150) / 4;
			if (data[42] != 0) {
				data[43] = 32; // 狩獵經驗值將會增加。
			}
		}
		// 媽祖的祝福
		if (pc.hasSkillEffect(EFFECT_BLESS_OF_MAZU)) {
			data[48] = pc.getSkillEffectTimeSec(EFFECT_BLESS_OF_MAZU) / 16;
			if (data[48] != 0) {
				data[49] = 44; // 感受到媽祖的祝福。
			}
		}
		// 體力增強卷軸、魔力增強卷軸、強化戰鬥卷軸
		if (pc.hasSkillEffect(EFFECT_STRENGTHENING_HP)) {
			data[46] = pc.getSkillEffectTimeSec(EFFECT_STRENGTHENING_HP) / 16;
			if (data[46] != 0) {
				data[47] = 0; // 體力上限+50，體力回復+4。
			}
		} else if (pc.hasSkillEffect(EFFECT_STRENGTHENING_MP)) {
			data[46] = pc.getSkillEffectTimeSec(EFFECT_STRENGTHENING_MP) / 16;
			if (data[46] != 0) {
				data[47] = 1; // 魔力上限+40，魔力回復+4。
			}
		} else if (pc.hasSkillEffect(EFFECT_ENCHANTING_BATTLE)) {
			data[46] = pc.getSkillEffectTimeSec(EFFECT_ENCHANTING_BATTLE) / 16;
			if (data[46] != 0) {
				data[47] = 2; // 攻擊成功及攻擊力+3,魔攻+3,遠距離攻擊及命中率+3。
			}
		}
		return data;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}
}
