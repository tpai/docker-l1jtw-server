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
import l1j.server.server.model.identity.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_SPMR;

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
		switch(itemId) {
			case L1ItemId.POTION_OF_EXP_150: // 150%神力藥水
			case L1ItemId.POTION_OF_EXP_175: // 175%神力藥水
			case L1ItemId.POTION_OF_EXP_200: // 200%神力藥水
			case L1ItemId.POTION_OF_EXP_225: // 225%神力藥水
			case L1ItemId.POTION_OF_EXP_250: // 250%神力藥水
				skillId = itemId - 42999;
				time = 900;
				gfxid = itemId - 39699;
				deleteRepeatedSkills(pc, skillId); // 與戰鬥藥水等相衝
				pc.sendPackets(new S_ServerMessage(1292)); // 狩獵的經驗值將會增加。
				break;
			case L1ItemId.BLESS_OF_MAZU: // 媽祖祝福平安符
				skillId = EFFECT_BLESS_OF_MAZU;
				time = 2400;
				gfxid = 7321;
				deleteRepeatedSkills(pc, skillId); // 與妖精屬性魔法相衝！
				break;
			case L1ItemId.POTION_OF_BATTLE: // 戰鬥藥水
				skillId = EFFECT_POTION_OF_BATTLE;
				time = 3600;
				gfxid = 7013;
				deleteRepeatedSkills(pc, skillId); // 與神力藥水等相衝
				break;
			case L1ItemId.SCROLL_FOR_STRENGTHENING_HP: // 體力增強卷軸
			case L1ItemId.SCROLL_FOR_STRENGTHENING_MP: // 魔力增強卷軸
			case L1ItemId.SCROLL_FOR_ENCHANTING_BATTLE: // 強化戰鬥卷軸
				skillId = itemId - 42999;
				time = 3600;
				gfxid = itemId - 40014;
				deleteRepeatedSkills(pc, skillId);
				break;
			default:
				pc.sendPackets(new S_ServerMessage(79)); // 沒有任何事情發生。
				return;
		}
		if (pc.hasSkillEffect(skillId)) {
			pc.removeSkillEffect(skillId);
		}
		pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
		useEffect(pc, skillId, time);
		pc.getInventory().removeItem(item, 1);
	}

	public static void useEffect(L1PcInstance pc, int skillId, int time) {
		if (!pc.hasSkillEffect(skillId)) {
			switch(skillId) {
			case EFFECT_BLESS_OF_MAZU: // 媽祖的祝福
				pc.addHitup(3); // 攻擊成功 +3
				pc.addDmgup(3); // 額外攻擊點數 +3
				break;
			case EFFECT_ENCHANTING_BATTLE: // 強化戰鬥卷軸
				pc.addHitup(3); // 攻擊成功 +3
				pc.addDmgup(3); // 額外攻擊點數 +3
				pc.addBowHitup(3); // 遠距離命中率 +3
				pc.addBowDmgup(3); // 遠距離攻擊力 +3
				pc.addSp(3); // 魔攻 +3
				pc.sendPackets(new S_SPMR(pc));
				break;
			case EFFECT_STRENGTHENING_HP: // 體力增強卷軸
				pc.addMaxHp(50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				break;
			case EFFECT_STRENGTHENING_MP: // 魔力增強卷軸
				pc.addMaxMp(40);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				break;
			default:
				break;
		}
		}
		pc.setSkillEffect(skillId, time * 1000);
	}

	// 設定不可重複的魔法狀態 
	public static void deleteRepeatedSkills(L1PcInstance pc, int skillId) {
		final int[][] repeatedSkills =
		{
				// 經驗加成狀態
				{ EFFECT_POTION_OF_EXP_150, EFFECT_POTION_OF_EXP_175, EFFECT_POTION_OF_EXP_200,
					EFFECT_POTION_OF_EXP_225, EFFECT_POTION_OF_EXP_250, EFFECT_POTION_OF_BATTLE },
				// 體力增強卷軸、魔力增強卷軸、強化戰鬥卷
				{ EFFECT_STRENGTHENING_HP, EFFECT_STRENGTHENING_MP, EFFECT_ENCHANTING_BATTLE },
				// 火焰武器、風之神射、烈炎氣息、暴風之眼、烈炎武器、暴風神射、媽祖的祝福
				{ FIRE_WEAPON, WIND_SHOT, FIRE_BLESS, STORM_EYE, BURNING_WEAPON, STORM_SHOT, EFFECT_BLESS_OF_MAZU }
		};

		for (int[] skills : repeatedSkills) {
			for (int id : skills) {
				if (id == skillId) {
					stopSkillList(pc, skillId, skills);
				}
			}
		}
	}

	// 將重複的狀態刪除
	private static void stopSkillList(L1PcInstance pc, int _skillId, int[] repeat_skill) {
		for (int skillId : repeat_skill) {
			if (skillId != _skillId) {
				pc.removeSkillEffect(skillId);
			}
		}
	}

}
