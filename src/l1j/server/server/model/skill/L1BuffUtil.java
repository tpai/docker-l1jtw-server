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
package l1j.server.server.model.skill;

import static l1j.server.server.model.skill.L1SkillId.BLOODLUST;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_THIRD_SPEED;
import static l1j.server.server.model.skill.L1SkillId.ENTANGLE;
import static l1j.server.server.model.skill.L1SkillId.GREATER_HASTE;
import static l1j.server.server.model.skill.L1SkillId.HASTE;
import static l1j.server.server.model.skill.L1SkillId.HOLY_WALK;
import static l1j.server.server.model.skill.L1SkillId.MASS_SLOW;
import static l1j.server.server.model.skill.L1SkillId.MOVING_ACCELERATION;
import static l1j.server.server.model.skill.L1SkillId.SLOW;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.server.server.model.skill.L1SkillId.WIND_WALK;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;

public class L1BuffUtil {
	public static void haste(L1PcInstance pc, int timeMillis) {

		int objId = pc.getId();

		/* 已存在加速狀態消除 */
		if (pc.hasSkillEffect(HASTE)
				|| pc.hasSkillEffect(GREATER_HASTE)
				|| pc.hasSkillEffect(STATUS_HASTE)) {
			if (pc.hasSkillEffect(HASTE)) {					// 加速術
				pc.killSkillEffectTimer(HASTE);
			} else if (pc.hasSkillEffect(GREATER_HASTE)) {	// 強力加速術
				pc.killSkillEffectTimer(GREATER_HASTE);
			} else if (pc.hasSkillEffect(STATUS_HASTE)) {	// 自我加速藥水
				pc.killSkillEffectTimer(STATUS_HASTE);
			}
		}
		/* 抵消緩速魔法效果 緩速術 集體緩速術 地面障礙 */
		if (pc.hasSkillEffect(SLOW) || pc.hasSkillEffect(MASS_SLOW) || pc.hasSkillEffect(ENTANGLE)) {
			if (pc.hasSkillEffect(SLOW)) {				// 緩速術
				pc.killSkillEffectTimer(SLOW);
			} else if (pc.hasSkillEffect(MASS_SLOW)) {	// 集體緩速術
				pc.killSkillEffectTimer(MASS_SLOW);
			} else if (pc.hasSkillEffect(ENTANGLE)) {	// 地面障礙
				pc.killSkillEffectTimer(ENTANGLE);
			}
			pc.sendPackets(new S_SkillHaste(objId, 0, 0));
			pc.broadcastPacket(new S_SkillHaste(objId, 0, 0));
		}

		pc.setSkillEffect(STATUS_HASTE, timeMillis);

		pc.sendPackets(new S_SkillSound(objId, 191));
		pc.broadcastPacket(new S_SkillSound(objId, 191));
		pc.sendPackets(new S_SkillHaste(objId, 1, timeMillis / 1000));
		pc.broadcastPacket(new S_SkillHaste(objId, 1, 0));
		pc.sendPackets(new S_ServerMessage(184)); // \f1你的動作突然變快。 */
		pc.setMoveSpeed(1);
	}

	public static void brave(L1PcInstance pc, int timeMillis) {
		// 消除重複狀態
		if (pc.hasSkillEffect(STATUS_BRAVE)) {			// 勇敢藥水	1.33倍
			pc.killSkillEffectTimer(STATUS_BRAVE);
		}
		if (pc.hasSkillEffect(STATUS_ELFBRAVE)) {		// 精靈餅乾	1.15倍
			pc.killSkillEffectTimer(STATUS_ELFBRAVE);
		}
		if (pc.hasSkillEffect(HOLY_WALK)) {				// 神聖疾走	移速1.33倍
			pc.killSkillEffectTimer(HOLY_WALK);
		}
		if (pc.hasSkillEffect(MOVING_ACCELERATION)) {	// 行走加速	移速1.33倍
			pc.killSkillEffectTimer(MOVING_ACCELERATION);
		}
		if (pc.hasSkillEffect(WIND_WALK)) {				// 風之疾走	移速1.33倍
			pc.killSkillEffectTimer(WIND_WALK);
		}
		if (pc.hasSkillEffect(BLOODLUST)) {				// 血之渴望	攻速1.33倍
			pc.killSkillEffectTimer(BLOODLUST);
		}
		if (pc.hasSkillEffect(STATUS_BRAVE2)) {			// 超級加速	2.66倍
			pc.killSkillEffectTimer(STATUS_BRAVE2);
		}

		pc.setSkillEffect(STATUS_BRAVE, timeMillis);

		int objId = pc.getId();
		pc.sendPackets(new S_SkillSound(objId, 751));
		pc.broadcastPacket(new S_SkillSound(objId, 751));
		pc.sendPackets(new S_SkillBrave(objId, 1, timeMillis / 1000));
		pc.broadcastPacket(new S_SkillBrave(objId, 1, 0));
		pc.setBraveSpeed(1);
	}

	public static void thirdSpeed(L1PcInstance pc) {
		if (pc.hasSkillEffect(EFFECT_THIRD_SPEED)) {
			pc.killSkillEffectTimer(EFFECT_THIRD_SPEED);
		}

		pc.setSkillEffect(EFFECT_THIRD_SPEED, 600 * 1000);

		pc.sendPackets(new S_SkillSound(pc.getId(), 751));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 751));
		pc.sendPackets(new S_Liquor(pc.getId(), 8)); // 人物 * 1.15
		pc.broadcastPacket(new S_Liquor(pc.getId(), 8)); // 人物 * 1.15
		pc.sendPackets(new S_ServerMessage(1065)); // 將發生神秘的奇蹟力量。
	}
}
