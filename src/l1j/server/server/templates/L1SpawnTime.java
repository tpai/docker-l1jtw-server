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
package l1j.server.server.templates;

import java.sql.Time;
import java.sql.Timestamp;

import l1j.server.server.utils.TimePeriod;

public class L1SpawnTime {
	private final int _spawnId;

	private final Time _timeStart;

	private final Time _timeEnd;

	private final TimePeriod _timePeriod;

	private final Timestamp _periodStart;

	private final Timestamp _periodEnd;

	private boolean _isDeleteAtEndTime;

	public boolean isDeleteAtEndTime() {
		return _isDeleteAtEndTime;
	}

	private L1SpawnTime(L1SpawnTimeBuilder builder) {
		_spawnId = builder._spawnId;
		_timeStart = builder._timeStart;
		_timeEnd = builder._timeEnd;
		_timePeriod = new TimePeriod(_timeStart, _timeEnd);
		_periodStart = builder._periodStart;
		_periodEnd = builder._periodEnd;
		_isDeleteAtEndTime = builder._isDeleteAtEndTime;
	}

	public int getSpawnId() {
		return _spawnId;
	}

	public Time getTimeStart() {
		return _timeStart;
	}

	public Time getTimeEnd() {
		return _timeEnd;
	}

	public Timestamp getPeriodStart() {
		return _periodStart;
	}

	public Timestamp getPeriodEnd() {
		return _periodEnd;
	}

	public static class L1SpawnTimeBuilder {
		private final int _spawnId;

		private Time _timeStart;

		private Time _timeEnd;

		private Timestamp _periodStart;

		private Timestamp _periodEnd;

		private boolean _isDeleteAtEndTime;

		public L1SpawnTimeBuilder(int spawnId) {
			_spawnId = spawnId;
		}

		public L1SpawnTime build() {
			return new L1SpawnTime(this);
		}

		public void setTimeStart(Time timeStart) {
			_timeStart = timeStart;
		}

		public void setTimeEnd(Time timeEnd) {
			_timeEnd = timeEnd;
		}

		public void setPeriodStart(Timestamp periodStart) {
			_periodStart = periodStart;
		}

		public void setPeriodEnd(Timestamp periodEnd) {
			_periodEnd = periodEnd;
		}

		public void setDeleteAtEndTime(boolean f) {
			_isDeleteAtEndTime = f;
		}

	}

	public TimePeriod getTimePeriod() {
		return _timePeriod;
	}
}
