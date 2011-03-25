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
package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static l1j.server.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static l1j.server.server.model.skill.L1SkillId.AWAKEN_VALAKAS;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_SPMR;

// Referenced classes of package l1j.server.server.model:
// L1Cooking

public class L1Awake {
	private L1Awake() {
	}

	public static void start(L1PcInstance pc, int skillId) {
		if (skillId == pc.getAwakeSkillId()) { // 再詠唱なら解除
			stop(pc);
		}
		else if (pc.getAwakeSkillId() != 0) { // 他の覚醒スキルの場合はリターン
			return;
		}
		else {
			if (skillId == AWAKEN_ANTHARAS) { // 覚醒：アンタラス
				pc.addMaxHp(127);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) { // パーティー中
					pc.getParty().updateMiniHP(pc);
				}
				pc.addAc(-12);
			}
			else if (skillId == AWAKEN_FAFURION) { // 覚醒：パプリオン
				pc.addMr(30);
				pc.sendPackets(new S_SPMR(pc));
				pc.addWind(30);
				pc.addWater(30);
				pc.addFire(30);
				pc.addEarth(30);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			else if (skillId == AWAKEN_VALAKAS) { // 覚醒：ヴァラカス
				pc.addStr(5);
				pc.addCon(5);
				pc.addDex(5);
				pc.addCha(5);
				pc.addInt(5);
				pc.addWis(5);
			}
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.setAwakeSkillId(skillId);
			doPoly(pc);
			pc.startMpReductionByAwake();
		}
	}

	public static void stop(L1PcInstance pc) {
		int skillId = pc.getAwakeSkillId();
		if (skillId == AWAKEN_ANTHARAS) { // 覚醒：アンタラス
			pc.addMaxHp(-127);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) { // パーティー中
				pc.getParty().updateMiniHP(pc);
			}
			pc.addAc(12);
		}
		else if (skillId == AWAKEN_FAFURION) { // 覚醒：パプリオン
			pc.addMr(-30);
			pc.sendPackets(new S_SPMR(pc));
			pc.addWind(-30);
			pc.addWater(-30);
			pc.addFire(-30);
			pc.addEarth(-30);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
		}
		else if (skillId == AWAKEN_VALAKAS) { // 覚醒：ヴァラカス
			pc.addStr(-5);
			pc.addCon(-5);
			pc.addDex(-5);
			pc.addCha(-5);
			pc.addInt(-5);
			pc.addWis(-5);
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
		pc.setAwakeSkillId(0);
		undoPoly(pc);
		pc.stopMpReductionByAwake();
	}

	public static void doPoly(L1PcInstance pc) {
		int polyId = 6894;
		if (pc.hasSkillEffect(SHAPE_CHANGE)) {
			pc.killSkillEffectTimer(SHAPE_CHANGE);
		}
		pc.setTempCharGfx(polyId);
		pc.sendPackets(new S_ChangeShape(pc.getId(), polyId));
		if (!pc.isGmInvis() && !pc.isInvisble()) {
			pc.broadcastPacket(new S_ChangeShape(pc.getId(), polyId));
		}
		L1ItemInstance weapon = pc.getWeapon();
		if (weapon != null) {
			S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
			pc.sendPackets(charVisual);
			pc.broadcastPacket(charVisual);
		}
	}

	public static void undoPoly(L1PcInstance pc) {
		int classId = pc.getClassId();
		pc.setTempCharGfx(classId);
		pc.sendPackets(new S_ChangeShape(pc.getId(), classId));
		pc.broadcastPacket(new S_ChangeShape(pc.getId(), classId));
		L1ItemInstance weapon = pc.getWeapon();
		if (weapon != null) {
			S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
			pc.sendPackets(charVisual);
			pc.broadcastPacket(charVisual);
		}
	}

}
