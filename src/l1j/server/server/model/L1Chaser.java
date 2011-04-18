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

import l1j.server.server.utils.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import static l1j.server.server.model.skill.L1SkillId.*;

public class L1Chaser extends TimerTask {
	private static Logger _log = Logger.getLogger(L1Chaser.class.getName());

	private ScheduledFuture<?> _future = null;
	private int _timeCounter = 0;
	private final int _attr;
	private final int _gfxid;
	private final L1PcInstance _pc;
	private final L1Character _cha;

	public L1Chaser(L1PcInstance pc, L1Character cha, int attr, int gfxid) {
		_cha = cha;
		_pc = pc;
		_attr = attr;
		_gfxid = gfxid;
	}

	@Override
	public void run() {
		try {
			if (_cha == null || _cha.isDead()) {
				stop();
				return;
			}
			attack();
			_timeCounter++;
			if (_timeCounter >= 3) {
				stop();
				return;
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void begin() {
		// 効果時間が8秒のため、4秒毎のスキルの場合処理時間を考慮すると実際には1回しか効果が現れない
		// よって開始時間を0.9秒後に設定しておく
		_future = GeneralThreadPool.getInstance().scheduleAtFixedRate(this, 0,
				1000);
	}

	public void stop() {
		if (_future != null) {
			_future.cancel(false);
		}
	}

	public void attack() {
		double damage = getDamage(_pc, _cha);
		if (_cha.getCurrentHp() - (int) damage <= 0 && _cha.getCurrentHp() != 1) {
			damage = _cha.getCurrentHp() - 1;
		} else if (_cha.getCurrentHp() == 1) {
			damage = 0;
		}
		S_EffectLocation packet = new S_EffectLocation(_cha.getX(),
				_cha.getY(), _gfxid);
		if (_pc.getWeapon().getItem().getItemId() == 265
				|| _pc.getWeapon().getItem().getItemId() == 266
				|| _pc.getWeapon().getItem().getItemId() == 267
				|| _pc.getWeapon().getItem().getItemId() == 268) {
			packet = new S_EffectLocation(_cha.getX(), _cha.getY(), 7025);
		} else if (_pc.getWeapon().getItem().getItemId() == 280
				|| _pc.getWeapon().getItem().getItemId() == 281) {
			packet = new S_EffectLocation(_cha.getX(), _cha.getY(), 7224);
		} else {
			packet = new S_EffectLocation(_cha.getX(), _cha.getY(), 7025);
		}
		_pc.sendPackets(packet);
		_pc.broadcastPacket(packet);
		if (_cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _cha;
			pc.sendPackets(new S_DoActionGFX(pc.getId(),
					ActionCodes.ACTION_Damage));
			pc.broadcastPacket(new S_DoActionGFX(pc.getId(),
					ActionCodes.ACTION_Damage));
			pc.receiveDamage(_pc, damage, false);
		} else if (_cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) _cha;
			npc.broadcastPacket(new S_DoActionGFX(npc.getId(),
					ActionCodes.ACTION_Damage));
			npc.receiveDamage(_pc, (int) damage);
		}
	}

	public double getDamage(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int spByItem = pc.getSp() - pc.getTrueSp();
		int intel = pc.getInt();
		int charaIntelligence = pc.getInt() + spByItem - 12;
		double coefficientA = 1.0 + 3.0 / 32.0 * charaIntelligence;
		if (coefficientA < 1) {
			coefficientA = 1.0;
		}
		double coefficientB = 0;
		if (intel > 18) {
			coefficientB = (intel + 2.0) / intel;
		} else if (intel <= 12) {
			coefficientB = 12.0 * 0.065;
		} else {
			coefficientB = intel * 0.065;
		}
		double coefficientC = 0;
		if (intel <= 12) {
			coefficientC = 12;
		} else {
			coefficientC = intel;
		}
		dmg = (Random.nextInt(6) + 1 + 7) * coefficientA * coefficientB / 10.5
				* coefficientC * 2.0;
		dmg = L1WeaponSkill.calcDamageReduction(pc, cha, dmg, _attr);
		if (cha.hasSkillEffect(IMMUNE_TO_HARM)) {
			dmg /= 2.0;
		}
		return dmg;
	}

}
