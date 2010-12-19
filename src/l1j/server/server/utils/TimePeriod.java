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

import java.sql.Time;
import java.util.logging.Logger;

import l1j.server.server.model.gametime.L1GameTime;

public class TimePeriod {
	private static Logger _log = Logger.getLogger(TimePeriod.class.getName());

	private final Time _timeStart;
	private final Time _timeEnd;

	public TimePeriod(Time timeStart, Time timeEnd) {
		if (timeStart.equals(timeEnd)) {
			throw new IllegalArgumentException(
					"timeBegin must not equals timeEnd");
		}

		_timeStart = timeStart;
		_timeEnd = timeEnd;
	}

	private boolean includes(L1GameTime time, Time timeStart, Time timeEnd) {
		Time when = time.toTime();
		return timeStart.compareTo(when) <= 0 && 0 < timeEnd.compareTo(when);
	}

	public boolean includes(L1GameTime time) {
		/*
		 * 分かりづらいロジック・・・ timeStart after timeEndのとき(例:18:00~06:00)
		 * timeEnd~timeStart(06:00~18:00)の範囲内でなければ、
		 * timeStart~timeEnd(18:00~06:00)の範囲内と見なせる
		 */
		return _timeStart.after(_timeEnd) ? !includes(time, _timeEnd,
				_timeStart) : includes(time, _timeStart, _timeEnd);
	}
}
