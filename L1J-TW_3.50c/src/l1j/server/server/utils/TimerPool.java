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
package l1j.server.server.utils;

import java.util.Timer;

public class TimerPool {
	private Timer _timers[];
	private int _numOfTimers;
	private int _pointer = 0;

	public TimerPool(int numOfTimers) {
		_timers = new Timer[numOfTimers];
		for (int i = 0; i < numOfTimers; i++) {
			_timers[i] = new Timer();
		}
		_numOfTimers = numOfTimers;
	}

	public synchronized Timer getTimer() {
		if (_numOfTimers <= _pointer) {
			_pointer = 0;
		}
		return _timers[_pointer++];
	}
}
