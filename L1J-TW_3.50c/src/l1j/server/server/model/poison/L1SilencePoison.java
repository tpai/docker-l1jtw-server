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
package l1j.server.server.model.poison;

import static l1j.server.server.model.skill.L1SkillId.STATUS_POISON_SILENCE;
import l1j.server.server.model.L1Character;

public class L1SilencePoison extends L1Poison {
	private final L1Character _target;

	public static boolean doInfection(L1Character cha) {
		if (!L1Poison.isValidTarget(cha)) {
			return false;
		}

		cha.setPoison(new L1SilencePoison(cha));
		return true;
	}

	private L1SilencePoison(L1Character cha) {
		_target = cha;

		doInfection();
	}

	private void doInfection() {
		_target.setPoisonEffect(1);
		sendMessageIfPlayer(_target, 310);

		_target.setSkillEffect(STATUS_POISON_SILENCE, 0);
	}

	@Override
	public int getEffectId() {
		return 1;
	}

	@Override
	public void cure() {
		_target.setPoisonEffect(0);
		sendMessageIfPlayer(_target, 311);

		_target.killSkillEffectTimer(STATUS_POISON_SILENCE);
		_target.setPoison(null);
	}
}
