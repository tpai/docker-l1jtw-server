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

import static l1j.server.server.model.skill.L1SkillId.*;

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
			writeC(i);
		}
		writeByte(randBox);
	}

	// 登入時給于角色狀態剩餘時間
	private int[] activeSpells(L1PcInstance pc) {
		int[] data = new int[104];
		 // 生命之樹果實
		if (pc.hasSkillEffect(STATUS_RIBRAVE)) {
			data[61] = pc.getSkillEffectTimeSec(STATUS_RIBRAVE) / 4;
		}
		// 迴避提升
		if (pc.hasSkillEffect(DRESS_EVASION)) {
			data[17] = pc.getSkillEffectTimeSec(DRESS_EVASION) / 4;
		}
		// 恐懼無助
		if (pc.hasSkillEffect(RESIST_FEAR)) {
			data[57] = pc.getSkillEffectTimeSec(RESIST_FEAR) / 4;
		}
		// 象牙塔妙藥
		if (pc.hasSkillEffect(COOKING_WONDER_DRUG)) {
			data[42] = pc.getSkillEffectTimeSec(COOKING_WONDER_DRUG) / 4;
			if (data[42] != 0) {
				data[43] = 54; // 因為妙藥，身心都很輕鬆。提升體力回復量和魔力回復量。
			}
		}
		// 戰鬥藥水
		if (pc.hasSkillEffect(EFFECT_POTION_OF_BATTLE)) {
			data[45] = pc.getSkillEffectTimeSec(EFFECT_POTION_OF_BATTLE) / 16;
			if (data[45] != 0) {
				data[62] = 20; // 經驗值加成20%。
			}
		}
		// 150% ~ 250% 神力藥水
		for (int i = 0; i < 5; i++) {
			if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_150 + i)) {
				data[45] = pc.getSkillEffectTimeSec(EFFECT_POTION_OF_EXP_150 + i) / 16;
				if (data[45] != 0) {
					data[62] = 50; // 狩獵經驗值將會增加。
				}
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
		for (int i = 0; i < 3; i++) {
			if (pc.hasSkillEffect(EFFECT_STRENGTHENING_HP + i)) {
				data[46] = pc.getSkillEffectTimeSec(EFFECT_STRENGTHENING_HP + i) / 16;
				if (data[46] != 0) {
					data[47] = i; // 體力上限+50，體力回復+4。
				}
			} 
		}
		// 附魔石
		if (pc.getMagicStoneLevel() != 0) {
			int skillId = pc.getMagicStoneLevel() + 3929; // skillId = 4013 ~ 4048
			data[102] = pc.getSkillEffectTimeSec(skillId) / 32;
			if (data[102] != 0) {
				data[103] = pc.getMagicStoneLevel() ;
			}
		}
		// 龍之魔眼
		for (int i = 0; i < 7; i++) {
			if (pc.hasSkillEffect(EFFECT_MAGIC_EYE_OF_AHTHARTS + i)) {
				data[78] = pc.getSkillEffectTimeSec(EFFECT_MAGIC_EYE_OF_AHTHARTS + i) / 32;
				if (data[78] != 0) {
					data[79] = 46 + i;
				}
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
