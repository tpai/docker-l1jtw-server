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
package l1j.server.server.model.item.action;

import static l1j.server.server.model.skill.L1SkillId.*;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;

public class Effect {

	public static void useEffectItem(L1PcInstance pc, L1ItemInstance item) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		int itemId = item.getItem().getItemId();
		int skillId = 0;
		int time = 0;
		int gfxid = 0;
		boolean _effect = true;
		switch(itemId) {
			case L1ItemId.EFFECT_POTION_OF_EXP_150: // 150%神力藥水
			case L1ItemId.EFFECT_POTION_OF_EXP_175: // 175%神力藥水
			case L1ItemId.EFFECT_POTION_OF_EXP_200: // 200%神力藥水
			case L1ItemId.EFFECT_POTION_OF_EXP_225: // 225%神力藥水
			case L1ItemId.EFFECT_POTION_OF_EXP_250: // 250%神力藥水
				skillId = itemId - 42999;
				time = 900;
				gfxid = itemId - 39699;
				deleteExpEffect(pc);
				pc.sendPackets(new S_ServerMessage(1292)); // 狩獵的經驗值將會增加。
				break;
			case L1ItemId.BLESS_OF_MAZU: // 媽祖祝福平安符
				skillId = EFFECT_BLESS_OF_MAZU;
				time = 2400;
				gfxid = 7321;
				deleteElfEffect(pc); // 與妖精屬性魔法相衝！
				break;
			default:
				pc.sendPackets(new S_ServerMessage(79)); // 沒有任何事情發生。
				return;
		}
		// 狀態不重複判斷
		if (pc.hasSkillEffect(skillId)) {
			_effect = false;
			pc.killSkillEffectTimer(skillId);
		}
		pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
		useEffect(pc, skillId, time, _effect);
		pc.getInventory().removeItem(item, 1);
	}

	public static void useEffect(L1PcInstance pc, int skillId, int time) {
		useEffect(pc, skillId, time, true);
	}

	private static void useEffect(L1PcInstance pc, int skillId, int time, boolean buff_effect) {
		if (buff_effect) {
			switch(skillId) {
				case EFFECT_BLESS_OF_MAZU: // 媽祖的祝福
					pc.addHitup(3); // 攻擊成功 +3
					pc.addDmgup(3); // 額外攻擊點數 +3
					break;
				default:
					break;
			}
		}
		pc.setSkillEffect(skillId, time * 1000);
	}

	private static void deleteExpEffect(L1PcInstance pc) {
		if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_150)) {
			pc.killSkillEffectTimer(EFFECT_POTION_OF_EXP_150);
		}
		if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_175)) {
			pc.killSkillEffectTimer(EFFECT_POTION_OF_EXP_175);
		}
		if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_200)) {
			pc.killSkillEffectTimer(EFFECT_POTION_OF_EXP_200);
		}
		if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_225)) {
			pc.killSkillEffectTimer(EFFECT_POTION_OF_EXP_225);
		}
		if (pc.hasSkillEffect(EFFECT_POTION_OF_EXP_250)) {
			pc.killSkillEffectTimer(EFFECT_POTION_OF_EXP_250);
		}
	}

	private static void deleteElfEffect(L1PcInstance pc) {
		if (pc.hasSkillEffect(FIRE_WEAPON)) {
			pc.removeSkillEffect(FIRE_WEAPON);
		}
		if (pc.hasSkillEffect(WIND_SHOT)) {
			pc.removeSkillEffect(WIND_SHOT);
		}
		if (pc.hasSkillEffect(FIRE_BLESS)) {
			pc.removeSkillEffect(FIRE_BLESS);
		}
		if (pc.hasSkillEffect(STORM_EYE)) {
			pc.removeSkillEffect(STORM_EYE);
		}
		if (pc.hasSkillEffect(BURNING_WEAPON)) {
			pc.removeSkillEffect(BURNING_WEAPON);
		}
		if (pc.hasSkillEffect(STORM_SHOT)) {
			pc.removeSkillEffect(STORM_SHOT);
		}
	}
}
