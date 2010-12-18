/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import l1j.server.server.utils.Random;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.templates.L1Npc;

public class L1BossSpawn extends L1Spawn {
	private static Logger _log = Logger.getLogger(L1BossSpawn.class.getName());

	private class SpawnTask implements Runnable {
		private int _spawnNumber;
		private int _objectId;

		private SpawnTask(int spawnNumber, int objectId) {
			_spawnNumber = spawnNumber;
			_objectId = objectId;
		}

		@Override
		public void run() {
			doSpawn(_spawnNumber, _objectId);
		}
	}

	public L1BossSpawn(L1Npc mobTemplate) {
		super(mobTemplate);
	}

	/**
	 * SpawnTaskを起動する。
	 * 
	 * @param spawnNumber
	 *            L1Spawnで管理されている番号。ホームポイントが無ければ何を指定しても良い。
	 */
	@Override
	public void executeSpawnTask(int spawnNumber, int objectId) {
		// countをデクリメントして全部死んだかチェック
		if (subAndGetCount() != 0) {
			return; // 全部死んでいない
		}
		// 前回出現時間に対して、次の出現時間を算出
		Calendar spawnTime;
		Calendar now = Calendar.getInstance(); // 現時刻
		Calendar latestStart = _cycle.getLatestStartTime(now); // 現時刻に対する最近の周期の開始時間

		Calendar activeStart = _cycle.getSpawnStartTime(_activeSpawnTime); // アクティブだった周期の開始時間
		// アクティブだった周期の開始時間 >= 最近の周期開始時間の場合、次の出現
		if (!activeStart.before(latestStart)) {
			spawnTime = calcNextSpawnTime(activeStart);
		} else {
			// アクティブだった周期の開始時間 < 最近の周期開始時間の場合は、最近の周期で出現
			// わかりづらいが確率計算する為に、無理やりcalcNextSpawnTimeを通している。
			latestStart.add(Calendar.SECOND, -1);
			spawnTime = calcNextSpawnTime(_cycle
					.getLatestStartTime(latestStart));
		}
		spawnBoss(spawnTime, objectId);
	}

	private int _spawnCount;

	private synchronized int subAndGetCount() {
		return --_spawnCount;
	}

	private String _cycleType;

	public void setCycleType(String type) {
		_cycleType = type;
	}

	private int _percentage;

	public void setPercentage(int percentage) {
		_percentage = percentage;
	}

	private L1BossCycle _cycle;

	private Calendar _activeSpawnTime;

	@Override
	public void init() {
		if (_percentage <= 0) {
			return;
		}
		_cycle = L1BossCycle.getBossCycle(_cycleType);
		if (_cycle == null) {
			throw new RuntimeException(_cycleType + " not found");
		}
		Calendar now = Calendar.getInstance();
		// 出現時間
		Calendar spawnTime;
		if (Config.INIT_BOSS_SPAWN && _percentage > Random.nextInt(100)) {
			spawnTime = _cycle.calcSpawnTime(now);

		} else {
			spawnTime = calcNextSpawnTime(now);
		}
		spawnBoss(spawnTime, 0);
	}

	// 確率計算して次の出現時間を算出
	private Calendar calcNextSpawnTime(Calendar cal) {
		do {
			cal = _cycle.nextSpawnTime(cal);
		} while (!(_percentage > Random.nextInt(100)));
		return cal;
	}

	// 指定された時間でボス出現をスケジュール
	private void spawnBoss(Calendar spawnTime, int objectId) {
		// 今回の出現時間を保存しておく。再出現時に使用。
		_activeSpawnTime = spawnTime;
		long delay = spawnTime.getTimeInMillis() - System.currentTimeMillis();

		int cnt = _spawnCount;
		_spawnCount = getAmount();
		while (cnt < getAmount()) {
			cnt++;
			GeneralThreadPool.getInstance().schedule(
					new SpawnTask(0, objectId), delay);
		}
		_log.log(Level.FINE, toString());
	}

	/**
	 * 現在アクティブなボスに対する周期と出現時間を表す。
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[MOB]npcid:" + getNpcId());
		builder.append(" name:" + getName());
		builder.append("[Type]" + _cycle.getName());
		builder.append("[現在の周期]");
		builder.append(_cycle.getSpawnStartTime(_activeSpawnTime).getTime());
		builder.append(" - ");
		builder.append(_cycle.getSpawnEndTime(_activeSpawnTime).getTime());
		builder.append("[出現時間]");
		builder.append(_activeSpawnTime.getTime());
		return builder.toString();
	}
}
