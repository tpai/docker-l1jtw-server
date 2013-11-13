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
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillIconWisdomPotion;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.Random;

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

		if (pc.hasSkillEffect(STATUS_THIRD_SPEED)) {
			pc.killSkillEffectTimer(STATUS_THIRD_SPEED);
		}

		pc.setSkillEffect(STATUS_THIRD_SPEED, time * 1000);

		pc.sendPackets(new S_Liquor(pc.getId(), 8)); // 人物 * 1.15
		pc.broadcastPacket(new S_Liquor(pc.getId(), 8)); // 人物 * 1.15
		pc.sendPackets(new S_SkillSound(pc.getId(), 7976));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 7976));
		pc.sendPackets(new S_ServerMessage(1065)); // 將發生神秘的奇蹟力量。
		pc.getInventory().removeItem(item, 1);
	}

	public static void UseHeallingPotion(L1PcInstance pc, L1ItemInstance item, int healHp, int gfxid) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
		pc.sendPackets(new S_ServerMessage(77)); // \f1你覺得舒服多了。
		healHp *= ((new java.util.Random()).nextGaussian() / 5.0D) + 1.0D;
		if (pc.hasSkillEffect(POLLUTE_WATER)) { // 汙濁之水 - 效果減半
			healHp /= 2;
		}
		pc.setCurrentHp(pc.getCurrentHp() + healHp);
		pc.getInventory().removeItem(item, 1);
	}

	public static void UseMpPotion(L1PcInstance pc, L1ItemInstance item, int mp, int i) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), 190));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		pc.sendPackets(new S_ServerMessage(338, "$1084")); // 你的 魔力 漸漸恢復。
		int newMp = 0;
		if (i > 0) {
			newMp = Random.nextInt(i, mp);
		} else {
			newMp = mp;
		}
		pc.setCurrentMp(pc.getCurrentMp() + newMp);
		pc.getInventory().removeItem(item, 1);
	}

	public static void useGreenPotion(L1PcInstance pc, L1ItemInstance item, int itemId) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		int time = 0;
		if (itemId == L1ItemId.POTION_OF_HASTE_SELF || itemId == 40030) { // 自我加速藥水、象牙塔加速藥水
			time = 300;
		} else if (itemId == L1ItemId.B_POTION_OF_HASTE_SELF) { // 受祝福的 自我加速藥水
			time = 350;
		} else if ((itemId == 40018) || (itemId == 41338)
				|| (itemId == 41342) || (itemId == 49140)) { // 強化 自我加速藥水、受祝福的葡萄酒、梅杜莎之血、綠茶蛋糕卷
			time = 1800;
		} else if (itemId == 140018) { // 受祝福的 強化 自我加速藥水
			time = 2100;
		} else if (itemId == 40039) { // 紅酒
			time = 600;
		} else if (itemId == 40040) { // 威士忌
			time = 900;
		} else if (itemId == 49302) { // 福利加速藥水
			time = 1200;
		} else if ((itemId == 41261) || (itemId == 41262) || (itemId == 41268)
				|| (itemId == 41269) || (itemId == 41271) || (itemId == 41272)
				|| (itemId == 41273)) { // 商店料理
			time = 30;
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), 191));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 191));
		// XXX:ヘイストアイテム装備時、酔った状態が解除されるのか不明
		if (pc.getHasteItemEquipped() > 0) {
			return;
		}
		// 酔った状態を解除
		pc.setDrink(false);

		// ヘイスト、グレーターヘイストとは重複しない
		if (pc.hasSkillEffect(HASTE)) {
			pc.killSkillEffectTimer(HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		}
		else if (pc.hasSkillEffect(GREATER_HASTE)) {
			pc.killSkillEffectTimer(GREATER_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		}
		else if (pc.hasSkillEffect(STATUS_HASTE)) {
			pc.killSkillEffectTimer(STATUS_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		}

		// スロー、マス スロー、エンタングル中はスロー状態を解除するだけ
		if (pc.hasSkillEffect(SLOW)) { // スロー
			pc.killSkillEffectTimer(SLOW);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		}
		else if (pc.hasSkillEffect(MASS_SLOW)) { // マス スロー
			pc.killSkillEffectTimer(MASS_SLOW);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		}
		else if (pc.hasSkillEffect(ENTANGLE)) { // エンタングル
			pc.killSkillEffectTimer(ENTANGLE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		}
		else {
			pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
			pc.setMoveSpeed(1);
			pc.setSkillEffect(STATUS_HASTE, time * 1000);
		}
		pc.getInventory().removeItem(item, 1);
	}

	public static void useBluePotion(L1PcInstance pc, L1ItemInstance item, int item_id) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		int time = 0;
		if ((item_id == 40015) || (item_id == 40736)) { // 藍色藥水、智慧貨幣
			time = 600;
		} else if (item_id == 140015) { // 受祝福的 藍色藥水
			time = 700;
		} else if (item_id == 49306) { // 福利藍色藥水
			time = 2400;
		}

		if (pc.hasSkillEffect(STATUS_BLUE_POTION)) {
			pc.killSkillEffectTimer(STATUS_BLUE_POTION);
		}
		pc.sendPackets(new S_SkillIconGFX(34, time)); // 狀態圖示
		pc.sendPackets(new S_SkillSound(pc.getId(), 190));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		pc.sendPackets(new S_ServerMessage(1007)); // 你感覺到魔力恢復速度加快。

		pc.setSkillEffect(STATUS_BLUE_POTION, time * 1000);
		pc.getInventory().removeItem(item, 1);
	}

	public static void useWisdomPotion(L1PcInstance pc, L1ItemInstance item, int item_id) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		int time = 0;
		if (item_id == L1ItemId.POTION_OF_EMOTION_WISDOM) { // 慎重藥水
			time = 300;
		} else if (item_id == L1ItemId.B_POTION_OF_EMOTION_WISDOM) { // 受祝福的 慎重藥水
			time = 360;
		} else if (item_id == 49307) { // 福利慎重藥水
			time = 1000;
		}

		if (!pc.hasSkillEffect(STATUS_WISDOM_POTION)) {
			pc.addSp(2);
		} else {
			pc.killSkillEffectTimer(STATUS_WISDOM_POTION);
		}

		pc.sendPackets(new S_SkillIconWisdomPotion((time / 4))); // 狀態圖示
		pc.sendPackets(new S_SkillSound(pc.getId(), 750));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));

		pc.setSkillEffect(STATUS_WISDOM_POTION, time * 1000);
		pc.getInventory().removeItem(item, 1);
	}

	public static void useBlessOfEva(L1PcInstance pc, L1ItemInstance item, int item_id) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		int time = 0;
		if (item_id == 40032) { // 伊娃的祝福
			time = 1800;
		} else if (item_id == 40041) { // 人魚之鱗
			time = 300;
		} else if (item_id == 41344) { // 水中的水
			time = 2100;
		} else if (item_id == 49303) { // 福利呼吸藥水
			time = 7200;
		}

		if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) { // 持續時間可累加
			int timeSec = pc.getSkillEffectTimeSec(STATUS_UNDERWATER_BREATH);
			time += timeSec;
			if (time > 7200) {
				time = 7200;
			}
			pc.killSkillEffectTimer(STATUS_UNDERWATER_BREATH);
		}
		pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), time)); // 狀態圖示
		pc.sendPackets(new S_SkillSound(pc.getId(), 190));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		pc.setSkillEffect(STATUS_UNDERWATER_BREATH, time * 1000);
		pc.getInventory().removeItem(item, 1);
	}

	public static void useBlindPotion(L1PcInstance pc, L1ItemInstance item) {
		if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
			pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
			return;
		}

		int time = 16;
		if (pc.hasSkillEffect(CURSE_BLIND)) {
			pc.killSkillEffectTimer(CURSE_BLIND);
		} else if (pc.hasSkillEffect(DARKNESS)) {
			pc.killSkillEffectTimer(DARKNESS);
		}

		if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
			pc.sendPackets(new S_CurseBlind(2));
		} else {
			pc.sendPackets(new S_CurseBlind(1));
		}

		pc.setSkillEffect(CURSE_BLIND, time * 1000);
		pc.getInventory().removeItem(item, 1);
	}
}
