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

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.identity.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;

import static l1j.server.server.model.skill.L1SkillId.*;

public class Potion {
	/** 2段加速效果 **/
	public static void Brave(L1PcInstance pc, L1ItemInstance item, int item_id) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		int time = 0;

		// 判斷持續時間 && 使用類型
		/* 勇敢藥水類 */
		if (item_id == L1ItemId.POTION_OF_EMOTION_BRAVERY
				|| item_id == L1ItemId.B_POTION_OF_EMOTION_BRAVERY
				|| item_id == L1ItemId.POTION_OF_REINFORCED_CASE
				|| item_id == L1ItemId.W_POTION_OF_EMOTION_BRAVERY
				|| item_id == L1ItemId.DEVILS_BLOOD
				|| item_id == L1ItemId.COIN_OF_REPUTATION) {
			if (item_id == L1ItemId.POTION_OF_EMOTION_BRAVERY) { // 勇敢藥水
				time = 300;
			} else if (item_id == L1ItemId.B_POTION_OF_EMOTION_BRAVERY) { // 受祝福的勇敢藥水
				time = 350;
			} else if (item_id == L1ItemId.POTION_OF_REINFORCED_CASE) { // 強化勇氣的藥水
				time = 1800;
			} else if (item_id == L1ItemId.DEVILS_BLOOD) { // 惡魔之血
				time = 600;
			} else if (item_id == L1ItemId.COIN_OF_REPUTATION) { // 名譽貨幣
				time = 600;
			} else if (item_id == L1ItemId.W_POTION_OF_EMOTION_BRAVERY) { // 福利勇敢藥水
				time = 1200;
			}
			buff_brave(pc, STATUS_BRAVE, (byte) 1, time); // 給予勇敢藥水效果
			pc.getInventory().removeItem(item, 1);
			/* 精靈餅乾 & 祝福的精靈餅乾 */
		} else if (item_id == L1ItemId.ELVEN_WAFER
				|| item_id == L1ItemId.B_ELVEN_WAFER
				|| item_id == L1ItemId.W_POTION_OF_FOREST) {
			if (item_id == L1ItemId.ELVEN_WAFER) { // 精靈餅乾
				time = 480;
			} else if (item_id == L1ItemId.B_ELVEN_WAFER) { // 祝福的精靈餅乾
				time = 700;
			} else if (item_id == L1ItemId.W_POTION_OF_FOREST) { // 福利森林藥水
				time = 1920;
			}
			buff_brave(pc, STATUS_ELFBRAVE, (byte) 3, time); // 給予精靈餅乾效果
			pc.getInventory().removeItem(item, 1);
			/* 生命之樹果實 */
		} else if (item_id == L1ItemId.FORBIDDEN_FRUIT) { // 生命之樹果實
			time = 480;
			pc.setSkillEffect(STATUS_RIBRAVE, time * 1000);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7110));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7110));
			pc.getInventory().removeItem(item, 1);
		}
	}

	private static void buff_brave(L1PcInstance pc, int skillId, byte type,
			int timeMillis) {
		// 消除重複狀態
		if (pc.hasSkillEffect(STATUS_BRAVE)) { // 勇敢藥水類 1.33倍
			pc.killSkillEffectTimer(STATUS_BRAVE);
		}
		if (pc.hasSkillEffect(STATUS_ELFBRAVE)) { // 精靈餅乾 1.15倍
			pc.killSkillEffectTimer(STATUS_ELFBRAVE);
		}
		if (pc.hasSkillEffect(HOLY_WALK)) { // 神聖疾走 移速1.33倍
			pc.killSkillEffectTimer(HOLY_WALK);
		}
		if (pc.hasSkillEffect(MOVING_ACCELERATION)) { // 行走加速 移速1.33倍
			pc.killSkillEffectTimer(MOVING_ACCELERATION);
		}
		if (pc.hasSkillEffect(WIND_WALK)) { // 風之疾走 移速1.33倍
			pc.killSkillEffectTimer(WIND_WALK);
		}
		if (pc.hasSkillEffect(BLOODLUST)) { // 血之渴望 攻速1.33倍
			pc.killSkillEffectTimer(BLOODLUST);
		}
		if (pc.hasSkillEffect(STATUS_BRAVE2)) { // 超級加速 2.66倍
			pc.killSkillEffectTimer(STATUS_BRAVE2);
		}
		// 給予狀態 && 效果
		pc.setSkillEffect(skillId, timeMillis * 1000);
		pc.sendPackets(new S_SkillSound(pc.getId(), 751));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 751));
		pc.sendPackets(new S_SkillBrave(pc.getId(), type, timeMillis));
		pc.broadcastPacket(new S_SkillBrave(pc.getId(), type, 0));
		pc.setBraveSpeed(type);
	}

	/** 3段加速效果 **/
	public static void ThirdSpeed(L1PcInstance pc, L1ItemInstance item, int time) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		if (pc.hasSkillEffect(EFFECT_THIRD_SPEED)) {
			pc.killSkillEffectTimer(EFFECT_THIRD_SPEED);
		}

		pc.setSkillEffect(EFFECT_THIRD_SPEED, time * 1000);

		pc.sendPackets(new S_SkillSound(pc.getId(), 8031));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 8031));
		pc.sendPackets(new S_Liquor(pc.getId(), 8)); // 人物 * 1.15
		pc.broadcastPacket(new S_Liquor(pc.getId(), 8)); // 人物 * 1.15
		pc.sendPackets(new S_ServerMessage(1065)); // 將發生神秘的奇蹟力量。
		pc.getInventory().removeItem(item, 1);
	}
}
