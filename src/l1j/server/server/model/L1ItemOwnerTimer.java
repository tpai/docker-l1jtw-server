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

import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.model.Instance.L1ItemInstance;

public class L1ItemOwnerTimer extends TimerTask {
	private static final Logger _log = Logger.getLogger(L1ItemOwnerTimer.class
			.getName());

	public L1ItemOwnerTimer(L1ItemInstance item, int timeMillis) {
		_item = item;
		_timeMillis = timeMillis;
	}

	@Override
	public void run() {
		_item.setItemOwnerId(0);
		this.cancel();
	}

	public void begin() {
		Timer timer = new Timer();
		timer.schedule(this, _timeMillis);
	}

	private final L1ItemInstance _item;
	private final int _timeMillis;
}
