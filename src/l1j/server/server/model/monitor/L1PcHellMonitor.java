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
package l1j.server.server.model.monitor;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1PcHellMonitor extends L1PcMonitor {

	public L1PcHellMonitor(int oId) {
		super(oId);
	}

	@Override
	public void execTask(L1PcInstance pc) {
		if (pc.isDead()) { // 死んでいたらカウントダウンしない
			return;
		}
		pc.setHellTime(pc.getHellTime() - 1);
		if (pc.getHellTime() <= 0) {
			// endHellの実行時間が影響ないように
			Runnable r = new L1PcMonitor(pc.getId()) {
				@Override
				public void execTask(L1PcInstance pc) {
					pc.endHell();
				}
			};
			GeneralThreadPool.getInstance().execute(r);
		}
	}
}
