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

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;

// Referenced classes of package l1j.server.server.model:
// L1SkillDelay

public class L1SkillDelay {

	private L1SkillDelay() {
	}

	static class SkillDelayTimer implements Runnable {
		private L1Character _cha;

		public SkillDelayTimer(L1Character cha, int time) {
			_cha = cha;
		}

		@Override
		public void run() {
			stopDelayTimer();
		}

		public void stopDelayTimer() {
			_cha.setSkillDelay(false);
		}
	}

	public static void onSkillUse(L1Character cha, int time) {
		cha.setSkillDelay(true);
		GeneralThreadPool.getInstance().schedule(new SkillDelayTimer(cha, time), time);
	}

}
