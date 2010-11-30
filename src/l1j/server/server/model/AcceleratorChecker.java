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

import java.util.EnumMap;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

/**
 * 加速器の使用をチェックするクラス。
 */
public class AcceleratorChecker {

	private static final Logger _log =
			Logger.getLogger(AcceleratorChecker.class.getName());

	private final L1PcInstance _pc;

	private int _injusticeCount;

	private int _justiceCount;

	private static final int INJUSTICE_COUNT_LIMIT = Config.INJUSTICE_COUNT;

	private static final int JUSTICE_COUNT_LIMIT = Config.JUSTICE_COUNT;

	// 実際には移動と攻撃のパケット間隔はsprの理論値より5%ほど遅い。
	// それを考慮して-5としている。
	private static final double CHECK_STRICTNESS = (Config.CHECK_STRICTNESS - 5) / 100D;

	private static final double HASTE_RATE = 0.745;

	private static final double WAFFLE_RATE = 0.874;

	private final EnumMap<ACT_TYPE, Long> _actTimers =
			new EnumMap<ACT_TYPE, Long>(ACT_TYPE.class);

	private final EnumMap<ACT_TYPE, Long> _checkTimers =
			new EnumMap<ACT_TYPE, Long>(ACT_TYPE.class);

	public static enum ACT_TYPE {
		MOVE, ATTACK, SPELL_DIR, SPELL_NODIR
	}

	// チェックの結果
	public static final int R_OK = 0;

	public static final int R_DETECTED = 1;

	public static final int R_DISCONNECTED = 2;

	public AcceleratorChecker(L1PcInstance pc) {
		_pc = pc;
		_injusticeCount = 0;
		_justiceCount = 0;
		long now = System.currentTimeMillis();
		for (ACT_TYPE each : ACT_TYPE.values()) {
			_actTimers.put(each, now);
			_checkTimers.put(each, now);
		}
	}

	/**
	 * アクションの間隔が不正でないかチェックし、適宜処理を行う。
	 * 
	 * @param type -
	 *            チェックするアクションのタイプ
	 * @return 問題がなかった場合は0、不正であった場合は1、不正動作が一定回数に達した ためプレイヤーを切断した場合は2を返す。
	 */
	public int checkInterval(ACT_TYPE type) {
		int result = R_OK;
		long now = System.currentTimeMillis();
		long interval = now - _actTimers.get(type);
		int rightInterval = getRightInterval(type);

		interval *= CHECK_STRICTNESS;

		if (0 < interval && interval < rightInterval) {
			_injusticeCount++;
			_justiceCount = 0;
			if (_injusticeCount >= INJUSTICE_COUNT_LIMIT) {
				doDisconnect();
				return R_DISCONNECTED;
			}
			result = R_DETECTED;
		} else if (interval >= rightInterval) {
			_justiceCount++;
			if (_justiceCount >= JUSTICE_COUNT_LIMIT) {
				_injusticeCount = 0;
				_justiceCount = 0;
			}
		}

		// 検証用
// double rate = (double) interval / rightInterval;
// System.out.println(String.format("%s: %d / %d = %.2f (o-%d x-%d)",
// type.toString(), interval, rightInterval, rate,
// _justiceCount, _injusticeCount));

		_actTimers.put(type, now);
		return result;
	}

	private void doDisconnect() {
		if (!_pc.isGm()) {
			_pc.sendPackets(new S_ServerMessage(945)); // 違法プログラムが見つかったので、終了します。
			_pc.sendPackets(new S_Disconnect());
			_log.info(String.format(
					"加速器検知のため%sを強制切断しました。", _pc.getName()));
		} else {
			// GMは切断しない
			_pc.sendPackets(new S_SystemMessage(
					"加速器検知にひっかかっています。"));
			_injusticeCount = 0;
		}
	}

	/**
	 * PCの状態から指定された種類のアクションの正しいインターバル(ms)を計算し、返す。
	 * 
	 * @param type -
	 *            アクションの種類
	 * @param _pc -
	 *            調べるPC
	 * @return 正しいインターバル(ms)
	 */
	private int getRightInterval(ACT_TYPE type) {
		int interval;

		switch (type) {
		case ATTACK:
			interval = SprTable.getInstance().getAttackSpeed(
					_pc.getTempCharGfx(), _pc.getCurrentWeapon() + 1);
			break;
		case MOVE:
			interval = SprTable.getInstance().getMoveSpeed(
					_pc.getTempCharGfx(), _pc.getCurrentWeapon());
			break;
		case SPELL_DIR:
			interval = SprTable.getInstance().getDirSpellSpeed(
							_pc.getTempCharGfx());
			break;
		case SPELL_NODIR:
			interval = SprTable.getInstance().getNodirSpellSpeed(
							_pc.getTempCharGfx());
			break;
		default:
			return 0;
		}

		if (_pc.isHaste()) {
			interval *= HASTE_RATE;
		}
		if (type.equals(ACT_TYPE.MOVE) && _pc.isFastMovable()) {
			interval *= HASTE_RATE;
		}
		if (type.equals(ACT_TYPE.ATTACK) && _pc.isFastAttackable()) {
			interval *= HASTE_RATE;
		}
		if (_pc.isBrave()) {
			interval *= HASTE_RATE;
		}
		if (_pc.isElfBrave()) {
			interval *= WAFFLE_RATE;
		}

		return interval;
	}
}
