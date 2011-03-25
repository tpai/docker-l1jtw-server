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

import static l1j.server.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static l1j.server.server.model.skill.L1SkillId.CONCENTRATION;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_2_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_2_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_2_4_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_2_4_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_3_5_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_3_5_S;
import static l1j.server.server.model.skill.L1SkillId.EXOTIC_VITALIZE;
import static l1j.server.server.model.skill.L1SkillId.MEDITATION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.types.Point;

public class MpRegeneration extends TimerTask {
	private static Logger _log = Logger.getLogger(MpRegeneration.class.getName());

	private final L1PcInstance _pc;

	private int _regenPoint = 0;

	private int _curPoint = 4;

	public MpRegeneration(L1PcInstance pc) {
		_pc = pc;
	}

	public void setState(int state) {
		if (_curPoint < state) {
			return;
		}

		_curPoint = state;
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead()) {
				return;
			}

			_regenPoint += _curPoint;
			_curPoint = 4;

			if (64 <= _regenPoint) {
				_regenPoint = 0;
				regenMp();
			}
		}
		catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void regenMp() {
		int baseMpr = 1;
		int wis = _pc.getWis();
		if ((wis == 15) || (wis == 16)) {
			baseMpr = 2;
		}
		else if (wis >= 17) {
			baseMpr = 3;
		}

		if (_pc.hasSkillEffect(STATUS_BLUE_POTION)) { // ブルーポーション使用中
			if (wis < 11) { // WIS11未満でもMPR+1
				wis = 11;
			}
			baseMpr += wis - 10;
		}
		if (_pc.hasSkillEffect(MEDITATION)) { // メディテーション中
			baseMpr += 5;
		}
		if (_pc.hasSkillEffect(CONCENTRATION)) { // コンセントレーション中
			baseMpr += 2;
		}
		if (L1HouseLocation.isInHouse(_pc.getX(), _pc.getY(), _pc.getMapId())) {
			baseMpr += 3;
		}
		if ((_pc.getMapId() == 16384) || (_pc.getMapId() == 16896) || (_pc.getMapId() == 17408) || (_pc.getMapId() == 17920)
				|| (_pc.getMapId() == 18432) || (_pc.getMapId() == 18944) || (_pc.getMapId() == 19968) || (_pc.getMapId() == 19456)
				|| (_pc.getMapId() == 20480) || (_pc.getMapId() == 20992) || (_pc.getMapId() == 21504) || (_pc.getMapId() == 22016)
				|| (_pc.getMapId() == 22528) || (_pc.getMapId() == 23040) || (_pc.getMapId() == 23552) || (_pc.getMapId() == 24064)
				|| (_pc.getMapId() == 24576) || (_pc.getMapId() == 25088)) { // 宿屋
			baseMpr += 3;
		}
		if ((_pc.getLocation().isInScreen(new Point(33055, 32336)) && (_pc.getMapId() == 4) && _pc.isElf())) {
			baseMpr += 3;
		}
		if (_pc.hasSkillEffect(COOKING_1_2_N) || _pc.hasSkillEffect(COOKING_1_2_S)) {
			baseMpr += 3;
		}
		if (_pc.hasSkillEffect(COOKING_2_4_N) || _pc.hasSkillEffect(COOKING_2_4_S) || _pc.hasSkillEffect(COOKING_3_5_N)
				|| _pc.hasSkillEffect(COOKING_3_5_S)) {
			baseMpr += 2;
		}
		if (_pc.getOriginalMpr() > 0) { // オリジナルWIS MPR補正
			baseMpr += _pc.getOriginalMpr();
		}

		int itemMpr = _pc.getInventory().mpRegenPerTick();
		itemMpr += _pc.getMpr();

		if ((_pc.get_food() < 3) || isOverWeight(_pc)) {
			baseMpr = 0;
			if (itemMpr > 0) {
				itemMpr = 0;
			}
		}
		int mpr = baseMpr + itemMpr;
		int newMp = _pc.getCurrentMp() + mpr;
		if (newMp < 0) {
			newMp = 0;
		}
		_pc.setCurrentMp(newMp);
	}

	private boolean isOverWeight(L1PcInstance pc) {
		// エキゾチックバイタライズ状態、アディショナルファイアー状態であれば、
		// 重量オーバーでは無いとみなす。
		if (pc.hasSkillEffect(EXOTIC_VITALIZE) || pc.hasSkillEffect(ADDITIONAL_FIRE)) {
			return false;
		}

		return (120 <= pc.getInventory().getWeight240()) ? true : false;
	}
}
