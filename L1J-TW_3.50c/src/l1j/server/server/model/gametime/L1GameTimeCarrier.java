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
package l1j.server.server.model.gametime;

import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.L1GameTimeClock;
import l1j.server.server.serverpackets.S_GameTime;

public class L1GameTimeCarrier extends TimerTask {
	private static final Timer _timer = new Timer();
	private L1PcInstance _pc;

	public L1GameTimeCarrier(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc.getNetConnection() == null) {
				cancel();
				return;
			}

			int serverTime = L1GameTimeClock.getInstance().currentTime()
					.getSeconds();
			if (serverTime % 300 == 0) {
				_pc.sendPackets(new S_GameTime(serverTime));
			}
		} catch (Exception e) {
			// ignore
		}
	}

	public void start() {
		_timer.scheduleAtFixedRate(this, 0, 500);
	}

	public void stop() {
		cancel();
	}
}
