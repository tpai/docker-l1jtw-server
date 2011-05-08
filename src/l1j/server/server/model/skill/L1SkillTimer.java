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

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillIconBloodstain;
import l1j.server.server.serverpackets.S_SkillIconShield;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SkillIconWisdomPotion;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.templates.L1Skills;

public interface L1SkillTimer {
	public int getRemainingTime();

	public void begin();

	public void end();

	public void kill();
}

/*
 * XXX 2008/02/13 vala 本来、このクラスはあるべきではないが暫定処置。
 */
class L1SkillStop {
	public static void stopSkill(L1Character cha, int skillId) {
		if (skillId == LIGHT) { // ライト
			if (cha instanceof L1PcInstance) {
				if (!cha.isInvisble()) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.turnOnOffLight();
				}
			}
		}
		else if (skillId == GLOWING_AURA) { // グローウィング オーラ
			cha.addHitup(-5);
			cha.addBowHitup(-5);
			cha.addMr(-20);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_SkillIconAura(113, 0));
			}
		}
		else if (skillId == SHINING_AURA) { // シャイニング オーラ
			cha.addAc(8);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(114, 0));
			}
		}
		else if (skillId == BRAVE_AURA) { // ブレイブ オーラ
			cha.addDmgup(-5);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(116, 0));
			}
		}
		else if (skillId == SHIELD) { // シールド
			cha.addAc(2);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(5, 0));
			}
		}
		else if (skillId == BLIND_HIDING) { // ブラインドハイディング
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.delBlindHiding();
			}
		}
		else if (skillId == SHADOW_ARMOR) { // シャドウ アーマー
			cha.addAc(3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(3, 0));
			}
		}
		else if (skillId == DRESS_DEXTERITY) { // ドレス デクスタリティー
			cha.addDex((byte) -2);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Dexup(pc, 2, 0));
			}
		}
		else if (skillId == DRESS_MIGHTY) { // ドレス マイティー
			cha.addStr((byte) -2);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Strup(pc, 2, 0));
			}
		}
		else if (skillId == SHADOW_FANG) { // シャドウ ファング
			cha.addDmgup(-5);
		}
		else if (skillId == ENCHANT_WEAPON) { // エンチャント ウェポン
			cha.addDmgup(-2);
		}
		else if (skillId == BLESSED_ARMOR) { // ブレスド アーマー
			cha.addAc(3);
		}
		else if (skillId == EARTH_BLESS) { // アース ブレス
			cha.addAc(7);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(7, 0));
			}
		}
		else if (skillId == RESIST_MAGIC) { // レジスト マジック
			cha.addMr(-10);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SPMR(pc));
			}
		}
		else if (skillId == CLEAR_MIND) { // クリアー マインド
			cha.addWis((byte) -3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.resetBaseMr();
			}
		}
		else if (skillId == RESIST_ELEMENTAL) { // レジスト エレメント
			cha.addWind(-10);
			cha.addWater(-10);
			cha.addFire(-10);
			cha.addEarth(-10);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
		else if (skillId == ELEMENTAL_PROTECTION) { // エレメンタルプロテクション
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int attr = pc.getElfAttr();
				if (attr == 1) {
					cha.addEarth(-50);
				} else if (attr == 2) {
					cha.addFire(-50);
				} else if (attr == 4) {
					cha.addWater(-50);
				} else if (attr == 8) {
					cha.addWind(-50);
				}
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
		else if (skillId == ELEMENTAL_FALL_DOWN) { // 弱化屬性
			int attr = cha.getAddAttrKind();
			int i = 50;
			switch (attr) {
				case 1:
					cha.addEarth(i);
					break;
				case 2:
					cha.addFire(i);
					break;
				case 4:
					cha.addWater(i);
					break;
				case 8:
					cha.addWind(i);
					break;
				default:
					break;
			}
			cha.setAddAttrKind(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
		else if (skillId == IRON_SKIN) { // アイアン スキン
			cha.addAc(10);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(10, 0));
			}
		}
		else if (skillId == EARTH_SKIN) { // アース スキン
			cha.addAc(6);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(6, 0));
			}
		}
		else if (skillId == PHYSICAL_ENCHANT_STR) { // フィジカル エンチャント：STR
			cha.addStr((byte) -5);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Strup(pc, 5, 0));
			}
		}
		else if (skillId == PHYSICAL_ENCHANT_DEX) { // フィジカル エンチャント：DEX
			cha.addDex((byte) -5);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Dexup(pc, 5, 0));
			}
		}
		else if (skillId == FIRE_WEAPON) { // ファイアー ウェポン
			cha.addDmgup(-4);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(147, 0));
			}
		}
		else if (skillId == FIRE_BLESS) { // ファイアー ブレス
			cha.addDmgup(-4);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(154, 0));
			}
		}
		else if (skillId == BURNING_WEAPON) { // バーニング ウェポン
			cha.addDmgup(-6);
			cha.addHitup(-3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(162, 0));
			}
		}
		else if (skillId == BLESS_WEAPON) { // ブレス ウェポン
			cha.addDmgup(-2);
			cha.addHitup(-2);
			cha.addBowHitup(-2);
		}
		else if (skillId == WIND_SHOT) { // ウィンド ショット
			cha.addBowHitup(-6);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(148, 0));
			}
		}
		else if (skillId == STORM_EYE) { // ストーム アイ
			cha.addBowHitup(-2);
			cha.addBowDmgup(-3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(155, 0));
			}
		}
		else if (skillId == STORM_SHOT) { // ストーム ショット
			cha.addBowDmgup(-5);
			cha.addBowHitup(1);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(165, 0));
			}
		}
		else if (skillId == BERSERKERS) { // バーサーカー
			cha.addAc(-10);
			cha.addDmgup(-5);
			cha.addHitup(-2);
		}
		else if (skillId == SHAPE_CHANGE) { // シェイプ チェンジ
			L1PolyMorph.undoPoly(cha);
		}
		else if (skillId == ADVANCE_SPIRIT) { // アドバンスド スピリッツ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-pc.getAdvenHp());
				pc.addMaxMp(-pc.getAdvenMp());
				pc.setAdvenHp(0);
				pc.setAdvenMp(0);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // パーティー中
					pc.getParty().updateMiniHP(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
		}
		else if ((skillId == HASTE) || (skillId == GREATER_HASTE)) { // ヘイスト、グレーターヘイスト
			cha.setMoveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			}
		}
		else if ((skillId == HOLY_WALK) || (skillId == MOVING_ACCELERATION) || (skillId == WIND_WALK) || (skillId == BLOODLUST)) { // ホーリーウォーク、ムービングアクセレーション、ウィンドウォーク、ブラッドラスト
			cha.setBraveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
			}
		}
		else if (skillId == ILLUSION_OGRE) { // 幻覺：歐吉
			cha.addDmgup(-4);
			cha.addHitup(-4);
			cha.addBowDmgup(-4);
			cha.addBowHitup(-4);
		}
		else if (skillId == ILLUSION_LICH) { // イリュージョン：リッチ
			cha.addSp(-2);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SPMR(pc));
			}
		}
		else if (skillId == ILLUSION_DIA_GOLEM) { // イリュージョン：ダイアモンドゴーレム
			cha.addAc(20);
		}
		else if (skillId == ILLUSION_AVATAR) { // イリュージョン：アバター
			cha.addDmgup(-10);
			cha.addBowDmgup(-10);
		}
		else if (skillId == INSIGHT) { // 洞察
			cha.addStr((byte) -1);
			cha.addCon((byte) -1);
			cha.addDex((byte) -1);
			cha.addWis((byte) -1);
			cha.addInt((byte) -1);
		}
		else if (skillId == PANIC) { // 恐慌
			cha.addStr((byte) 1);
			cha.addCon((byte) 1);
			cha.addDex((byte) 1);
			cha.addWis((byte) 1);
			cha.addInt((byte) 1);
		}

		// ****** 状態変化が解けた場合
		else if ((skillId == CURSE_BLIND) || (skillId == DARKNESS)) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_CurseBlind(0));
			}
		}
		else if (skillId == CURSE_PARALYZE) { // カーズ パラライズ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Poison(pc.getId(), 0));
				pc.broadcastPacket(new S_Poison(pc.getId(), 0));
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, false));
			}
		}
		else if (skillId == WEAKNESS) { // 弱化術
			cha.addDmgup(5);
			cha.addHitup(1);
		}
		else if (skillId == DISEASE) { // 疾病術
			cha.addDmgup(6);
			cha.addAc(-12);
		}
		else if ((skillId == ICE_LANCE // アイスランス
				)
				|| (skillId == FREEZING_BLIZZARD // フリージングブリザード
				) || (skillId == FREEZING_BREATH)) { // フリージングブレス
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Poison(pc.getId(), 0));
				pc.broadcastPacket(new S_Poison(pc.getId(), 0));
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
			}
			else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.broadcastPacket(new S_Poison(npc.getId(), 0));
				npc.setParalyzed(false);
			}
		}
		else if (skillId == EARTH_BIND) { // アースバインド
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Poison(pc.getId(), 0));
				pc.broadcastPacket(new S_Poison(pc.getId(), 0));
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
			}
			else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.broadcastPacket(new S_Poison(npc.getId(), 0));
				npc.setParalyzed(false);
			}
		}
		else if (skillId == SHOCK_STUN) { // 衝擊之暈
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
			} else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
		}
		else if (skillId == BONE_BREAK_START) { // 骷髏毀壞 (發動)
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
				pc.setSkillEffect(BONE_BREAK_END, 1 * 1000);
			} else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(true);
				npc.setSkillEffect(BONE_BREAK_END, 1 * 1000);
			}
		}
		else if (skillId == BONE_BREAK_END) { // 骷髏毀壞 (結束)
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
			} else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
		}
		else if (skillId == FOG_OF_SLEEPING) { // フォグ オブ スリーピング
			cha.setSleeped(false);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, false));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
		}
		else if (skillId == ABSOLUTE_BARRIER) { // アブソルート バリア
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.startHpRegeneration();
				pc.startMpRegeneration();
				pc.startMpRegenerationByDoll();
			}
		}
		else if (skillId == WIND_SHACKLE) { // 風之枷鎖
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), 0));
				pc.broadcastPacket(new S_SkillIconWindShackle(pc.getId(), 0));
			}
		}
		else if ((skillId == SLOW) || (skillId == ENTANGLE) || (skillId == MASS_SLOW)) { // スロー、エンタングル、マススロー
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			}
			cha.setMoveSpeed(0);
		}
		else if (skillId == STATUS_FREEZE) { // 束縛
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
			}
			else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
		}
		else if (skillId == THUNDER_GRAB_START) {
			L1Skills _skill = SkillsTable.getInstance().getTemplate(THUNDER_GRAB); // 奪命之雷
			int _fetterDuration = _skill.getBuffDuration() * 1000;
			cha.setSkillEffect(STATUS_FREEZE, _fetterDuration);
			L1EffectSpawn.getInstance().spawnEffect(81182, _fetterDuration, cha.getX(), cha.getY(), cha.getMapId());
		}
		else if (skillId == GUARD_BRAKE) { // 護衛毀滅
			cha.addAc(-15);
		}
		else if (skillId == HORROR_OF_DEATH) { // 驚悚死神
			cha.addStr(5);
			cha.addInt(5);
		}
		else if (skillId == STATUS_CUBE_IGNITION_TO_ALLY) { // キューブ[イグニション]：味方
			cha.addFire(-30);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
		else if (skillId == STATUS_CUBE_QUAKE_TO_ALLY) { // キューブ[クエイク]：味方
			cha.addEarth(-30);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
		else if (skillId == STATUS_CUBE_SHOCK_TO_ALLY) { // キューブ[ショック]：味方
			cha.addWind(-30);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
		else if (skillId == STATUS_CUBE_IGNITION_TO_ENEMY) { // キューブ[イグニション]：敵
		}
		else if (skillId == STATUS_CUBE_QUAKE_TO_ENEMY) { // キューブ[クエイク]：敵
		}
		else if (skillId == STATUS_CUBE_SHOCK_TO_ENEMY) { // キューブ[ショック]：敵
		}
		else if (skillId == STATUS_MR_REDUCTION_BY_CUBE_SHOCK) { // キューブ[ショック]によるMR減少
			// cha.addMr(10);
			// if (cha instanceof L1PcInstance) {
			// L1PcInstance pc = (L1PcInstance) cha;
			// pc.sendPackets(new S_SPMR(pc));
			// }
		}
		else if (skillId == STATUS_CUBE_BALANCE) { // キューブ[バランス]
		}

		// ****** アイテム関係
		else if ((skillId == STATUS_BRAVE)
				|| (skillId == STATUS_ELFBRAVE)
				|| (skillId == STATUS_BRAVE2)) { // 二段加速
			cha.setBraveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
			}
		}
		else if (skillId == EFFECT_THIRD_SPEED) { // 三段加速
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Liquor(pc.getId(), 0)); // 人物 * 1.15
				pc.broadcastPacket(new S_Liquor(pc.getId(), 0)); // 人物 * 1.15
			}
		}
		/** 生命之樹果實 */
		/*else if (skillId == STATUS_RIBRAVE) { // ユグドラの実
			// XXX ユグドラの実のアイコンを消す方法が不明
			cha.setBraveSpeed(0);
		}*/
		else if (skillId == STATUS_HASTE) { // グリーン ポーション
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			}
			cha.setMoveSpeed(0);
		}
		else if (skillId == STATUS_BLUE_POTION) { // ブルー ポーション
		}
		else if (skillId == STATUS_UNDERWATER_BREATH) { // エヴァの祝福＆マーメイドの鱗
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0));
			}
		}
		else if (skillId == STATUS_WISDOM_POTION) { // ウィズダム ポーション
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				cha.addSp(-2);
				pc.sendPackets(new S_SkillIconWisdomPotion(0));
			}
		}
		else if (skillId == STATUS_CHAT_PROHIBITED) { // チャット禁止
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_ServerMessage(288)); // チャットができるようになりました。
			}
		}

		// ****** 毒関係
		else if (skillId == STATUS_POISON) { // ダメージ毒
			cha.curePoison();
		}

		// ****** 料理関係
		else if ((skillId == COOKING_1_0_N) || (skillId == COOKING_1_0_S)) { // フローティングアイステーキ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addWind(-10);
				pc.addWater(-10);
				pc.addFire(-10);
				pc.addEarth(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_PacketBox(53, 0, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_1_1_N) || (skillId == COOKING_1_1_S)) { // ベアーステーキ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // パーティー中
					pc.getParty().updateMiniHP(pc);
				}
				pc.sendPackets(new S_PacketBox(53, 1, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_1_2_N) || (skillId == COOKING_1_2_S)) { // ナッツ餅
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 2, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_1_3_N) || (skillId == COOKING_1_3_S)) { // 蟻脚のチーズ焼き
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addAc(1);
				pc.sendPackets(new S_PacketBox(53, 3, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_1_4_N) || (skillId == COOKING_1_4_S)) { // フルーツサラダ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-20);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_PacketBox(53, 4, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_1_5_N) || (skillId == COOKING_1_5_S)) { // フルーツ甘酢あんかけ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 5, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_1_6_N) || (skillId == COOKING_1_6_S)) { // 猪肉の串焼き
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-5);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 6, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_1_7_N) || (skillId == COOKING_1_7_S)) { // キノコスープ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 7, 0));
				pc.setDessertId(0);
			}
		}
		else if ((skillId == COOKING_2_0_N) || (skillId == COOKING_2_0_S)) { // キャビアカナッペ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 8, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_2_1_N) || (skillId == COOKING_2_1_S)) { // アリゲーターステーキ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // パーティー中
					pc.getParty().updateMiniHP(pc);
				}
				pc.addMaxMp(-30);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_PacketBox(53, 9, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_2_2_N) || (skillId == COOKING_2_2_S)) { // タートルドラゴンの菓子
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addAc(2);
				pc.sendPackets(new S_PacketBox(53, 10, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_2_3_N) || (skillId == COOKING_2_3_S)) { // キウィパロット焼き
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 11, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_2_4_N) || (skillId == COOKING_2_4_S)) { // スコーピオン焼き
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 12, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_2_5_N) || (skillId == COOKING_2_5_S)) { // イレッカドムシチュー
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-10);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 13, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_2_6_N) || (skillId == COOKING_2_6_S)) { // クモ脚の串焼き
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSp(-1);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 14, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_2_7_N) || (skillId == COOKING_2_7_S)) { // クラブスープ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 15, 0));
				pc.setDessertId(0);
			}
		}
		else if ((skillId == COOKING_3_0_N) || (skillId == COOKING_3_0_S)) { // クラスタシアンのハサミ焼き
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 16, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_3_1_N) || (skillId == COOKING_3_1_S)) { // グリフォン焼き
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // パーティー中
					pc.getParty().updateMiniHP(pc);
				}
				pc.addMaxMp(-50);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_PacketBox(53, 17, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_3_2_N) || (skillId == COOKING_3_2_S)) { // コカトリスステーキ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 18, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_3_3_N) || (skillId == COOKING_3_3_S)) { // タートルドラゴン焼き
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addAc(3);
				pc.sendPackets(new S_PacketBox(53, 19, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_3_4_N) || (skillId == COOKING_3_4_S)) { // レッサードラゴンの手羽先
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-15);
				pc.sendPackets(new S_SPMR(pc));
				pc.addWind(-10);
				pc.addWater(-10);
				pc.addFire(-10);
				pc.addEarth(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_PacketBox(53, 20, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_3_5_N) || (skillId == COOKING_3_5_S)) { // ドレイク焼き
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSp(-2);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 21, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_3_6_N) || (skillId == COOKING_3_6_S)) { // 深海魚のシチュー
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // パーティー中
					pc.getParty().updateMiniHP(pc);
				}
				pc.sendPackets(new S_PacketBox(53, 22, 0));
				pc.setCookingId(0);
			}
		}
		else if ((skillId == COOKING_3_7_N) || (skillId == COOKING_3_7_S)) { // バシリスクの卵スープ
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 23, 0));
				pc.setDessertId(0);
			}
		}
		// ****** 
		else if (skillId == EFFECT_BLESS_OF_MAZU) { // 媽祖的祝福
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-3);
				pc.addDmgup(-3);
			}
		}
		else if (skillId == EFFECT_STRENGTHENING_HP) { // 體力增強卷軸
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
			}
		}
		else if (skillId == EFFECT_STRENGTHENING_MP) { // 魔力增強卷軸
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-40);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
		}
		else if (skillId == EFFECT_ENCHANTING_BATTLE) { // 強化戰鬥卷軸
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-3);
				pc.addDmgup(-3);
				pc.addBowHitup(-3);
				pc.addBowDmgup(-3);
				pc.addSp(-3);
				pc.sendPackets(new S_SPMR(pc));
			}
		}
		else if (skillId == MIRROR_IMAGE || skillId == UNCANNY_DODGE) { // 鏡像、暗影閃避
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDodge((byte) -5); // 閃避率 - 50%
				// 更新閃避率顯示
				pc.sendPackets(new S_PacketBox(88, pc.getDodge()));
			}
		}
		else if (skillId == RESIST_FEAR) { // 恐懼無助
			cha.addNdodge((byte) -5); // 閃避率 + 50%
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// 更新閃避率顯示
				pc.sendPackets(new S_PacketBox(101, pc.getNdodge()));
			}
		}
		else if (skillId == EFFECT_BLOODSTAIN_OF_ANTHARAS) { // 安塔瑞斯的血痕
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addAc(2);
				pc.addWater(-50);
				pc.sendPackets(new S_SkillIconBloodstain(82, 0));
			}
		}
		else if (skillId == EFFECT_BLOODSTAIN_OF_FAFURION) { // 法利昂的血痕
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addWind(-50);
				pc.sendPackets(new S_SkillIconBloodstain(85, 0));
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_A_1) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-10);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_A_2) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-20);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_A_3) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_A_4) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-40);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_A_5) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.addHpr(-1);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_A_6) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-60);
				pc.addHpr(-2);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_A_7) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-70);
				pc.addHpr(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_A_8) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-80);
				pc.addHpr(-4);
				pc.addHitup(-1);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_A_9) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-100);
				pc.addHpr(-5);
				pc.addHitup(-2);
				pc.addDmgup(-2);
				pc.addStr((byte) -1);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_B_1) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-5);
				pc.addMaxMp(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_B_2) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-10);
				pc.addMaxMp(-6);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_B_3) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-15);
				pc.addMaxMp(-10);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_B_4) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-20);
				pc.addMaxMp(-15);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_B_5) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-25);
				pc.addMaxMp(-20);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_B_6) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-30);
				pc.addMaxMp(-20);
				pc.addHpr(-1);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_B_7) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-35);
				pc.addMaxMp(-20);
				pc.addHpr(-1);
				pc.addMpr(-1);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_B_8) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-40);
				pc.addMaxMp(-25);
				pc.addHpr(-2);
				pc.addMpr(-1);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_B_9) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.addMaxMp(-30);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.addBowDmgup(-2);
				pc.addBowHitup(-2);
				pc.addDex((byte) -1);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				if (pc.isInParty()) { // 組隊中
					pc.getParty().updateMiniHP(pc);
				}
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_C_1) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-5);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_C_2) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-10);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_C_3) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-15);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_C_4) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-20);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_C_5) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-25);
				pc.addMpr(-1);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_C_6) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-30);
				pc.addMpr(-2);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_C_7) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-35);
				pc.addMpr(-3);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_C_8) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-40);
				pc.addMpr(-4);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_C_9) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-50);
				pc.addMpr(-5);
				pc.addInt((byte)-1);
				pc.addSp(-1);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_D_1) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-2);
				pc.sendPackets(new S_SPMR(pc));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_D_2) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-4);
				pc.sendPackets(new S_SPMR(pc));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_D_3) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-6);
				pc.sendPackets(new S_SPMR(pc));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_D_4) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-8);
				pc.sendPackets(new S_SPMR(pc));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_D_5) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-10);
				pc.addAc(1);
				pc.sendPackets(new S_SPMR(pc));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_D_6) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-10);
				pc.addAc(2);
				pc.sendPackets(new S_SPMR(pc));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_D_7) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-10);
				pc.addAc(3);
				pc.sendPackets(new S_SPMR(pc));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_D_8) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-15);
				pc.addAc(4);
				pc.addDamageReductionByArmor(-1);
				pc.sendPackets(new S_SPMR(pc));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_STONE_D_9) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMr(-20);
				pc.addAc(5);
				pc.addCon((byte) -1);
				pc.addDamageReductionByArmor(-3);
				pc.sendPackets(new S_SPMR(pc));
				pc.setMagicStoneLevel((byte) 0);
			}
		}
		else if (skillId == EFFECT_MAGIC_EYE_OF_AHTHARTS) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addRegistStone(-3); // 石化耐性

				pc.addDodge((byte) -1); // 閃避率 - 10%
				// 更新閃避率顯示
				pc.sendPackets(new S_PacketBox(88, pc.getDodge()));
			}
		}
		else if (skillId == EFFECT_MAGIC_EYE_OF_FAFURION) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_regist_freeze(-3); // 寒冰耐性
				// 魔法傷害減免
			}
		}
		else if (skillId == EFFECT_MAGIC_EYE_OF_LINDVIOR) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addRegistSleep(-3); // 睡眠耐性
				// 魔法暴擊率
			}
		}
		else if (skillId == EFFECT_MAGIC_EYE_OF_VALAKAS) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addRegistStun(-3); // 昏迷耐性
				pc.addDmgup(-2); // 額外攻擊點數
			}
		}
		else if (skillId == EFFECT_MAGIC_EYE_OF_BIRTH) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addRegistBlind(-3); // 闇黑耐性
				// 魔法傷害減免

				pc.addDodge((byte) -1); // 閃避率 - 10%
				// 更新閃避率顯示
				pc.sendPackets(new S_PacketBox(88, pc.getDodge()));
			}
		}
		else if (skillId == EFFECT_MAGIC_EYE_OF_FIGURE) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addRegistSustain(-3); // 支撐耐性
				// 魔法傷害減免
				// 魔法暴擊率

				pc.addDodge((byte) -1); // 閃避率 - 10%
				// 更新閃避率顯示
				pc.sendPackets(new S_PacketBox(88, pc.getDodge()));
			}
		}
		else if (skillId == EFFECT_MAGIC_EYE_OF_LIFE) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(2); // 額外攻擊點數
				// 魔法傷害減免
				// 魔法暴擊率
				// 防護中毒狀態

				pc.addDodge((byte) -1); // 閃避率 - 10%
				// 更新閃避率顯示
				pc.sendPackets(new S_PacketBox(88, pc.getDodge()));
			}
		}
		else if (skillId == EFFECT_BLESS_OF_CRAY) { // 卡瑞的祝福
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-100);
				pc.addMaxMp(-50);
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.addEarth(-30);
				pc.addDmgup(-1);
				pc.addHitup(-5);
				pc.addWeightReduction(-40);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					pc.getParty().updateMiniHP(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
		}
		else if (skillId == EFFECT_BLESS_OF_SAELL) { // 莎爾的祝福
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-80);
				pc.addMaxMp(-10);
				pc.addWater(-30);
				pc.addAc(8);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					pc.getParty().updateMiniHP(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
		}

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			sendStopMessage(pc, skillId);
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
	}

	// メッセージの表示（終了するとき）
	private static void sendStopMessage(L1PcInstance charaPc, int skillid) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillid);
		if ((l1skills == null) || (charaPc == null)) {
			return;
		}

		int msgID = l1skills.getSysmsgIdStop();
		if (msgID > 0) {
			charaPc.sendPackets(new S_ServerMessage(msgID));
		}
	}
}

class L1SkillTimerThreadImpl extends Thread implements L1SkillTimer {
	public L1SkillTimerThreadImpl(L1Character cha, int skillId, int timeMillis) {
		_cha = cha;
		_skillId = skillId;
		_timeMillis = timeMillis;
	}

	@Override
	public void run() {
		for (int timeCount = _timeMillis / 1000; timeCount > 0; timeCount--) {
			try {
				Thread.sleep(1000);
				_remainingTime = timeCount;
			}
			catch (InterruptedException e) {
				return;
			}
		}
		_cha.removeSkillEffect(_skillId);
	}

	@Override
	public int getRemainingTime() {
		return _remainingTime;
	}

	@Override
	public void begin() {
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void end() {
		super.interrupt();
		L1SkillStop.stopSkill(_cha, _skillId);
	}

	@Override
	public void kill() {
		if (Thread.currentThread().getId() == super.getId()) {
			return; // 呼び出し元スレッドが自分であれば止めない
		}
		super.interrupt();
	}

	private final L1Character _cha;

	private final int _timeMillis;

	private final int _skillId;

	private int _remainingTime;
}

class L1SkillTimerTimerImpl implements L1SkillTimer, Runnable {
	private static Logger _log = Logger.getLogger(L1SkillTimerTimerImpl.class.getName());

	private ScheduledFuture<?> _future = null;

	public L1SkillTimerTimerImpl(L1Character cha, int skillId, int timeMillis) {
		_cha = cha;
		_skillId = skillId;
		_timeMillis = timeMillis;

		_remainingTime = _timeMillis / 1000;
	}

	@Override
	public void run() {
		_remainingTime--;
		if (_remainingTime <= 0) {
			_cha.removeSkillEffect(_skillId);
		}
	}

	@Override
	public void begin() {
		_future = GeneralThreadPool.getInstance().scheduleAtFixedRate(this, 1000, 1000);
	}

	@Override
	public void end() {
		kill();
		try {
			L1SkillStop.stopSkill(_cha, _skillId);
		}
		catch (Throwable e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void kill() {
		if (_future != null) {
			_future.cancel(false);
		}
	}

	@Override
	public int getRemainingTime() {
		return _remainingTime;
	}

	private final L1Character _cha;

	private final int _timeMillis;

	private final int _skillId;

	private int _remainingTime;
}