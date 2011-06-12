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

import java.util.EnumMap;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.LogRecorder;

/**
 * 檢查加速器使用的類別。
 */
public class AcceleratorChecker {

	private static final Logger _log = Logger.getLogger(AcceleratorChecker.class.getName());

	private final L1PcInstance _pc;

	private int _injusticeCount;

	private int _justiceCount;

	private static final int INJUSTICE_COUNT_LIMIT = Config.INJUSTICE_COUNT;

	private static final int JUSTICE_COUNT_LIMIT = Config.JUSTICE_COUNT;

	// 実際には移動と攻撃のパケット間隔はsprの理論値より5%ほど遅い。
	// それを考慮して-5としている。
	private static final double CHECK_STRICTNESS = (Config.CHECK_STRICTNESS - 5) / 100D;

	private static final double HASTE_RATE = 0.75; // 速度 * 1.33

	private static final double WAFFLE_RATE = 0.87; // 速度 * 1.15

	private static final double DOUBLE_HASTE_RATE = 0.375; // 速度 * 2.66

	private final EnumMap<ACT_TYPE, Long> _actTimers = new EnumMap<ACT_TYPE, Long>(
			ACT_TYPE.class);

	private final EnumMap<ACT_TYPE, Long> _checkTimers = new EnumMap<ACT_TYPE, Long>(
			ACT_TYPE.class);

	public static enum ACT_TYPE {
		MOVE, ATTACK, SPELL_DIR, SPELL_NODIR
	}

	// 檢查結果
	public static final int R_OK = 0;

	public static final int R_DETECTED = 1;

	public static final int R_DISPOSED = 2;

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
	 * @param type
	 *            - チェックするアクションのタイプ
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
				doPunishment(Config.ILLEGAL_SPEEDUP_PUNISHMENT);
				return R_DISPOSED;
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

	/**
	 * 加速檢測處罰
	 * @param punishmaent 處罰模式
	 */
	private void doPunishment(int punishmaent) {
		if (!_pc.isGm()) {// 如果不是GM才執行處罰
			int x = _pc.getX() ,y = _pc.getY() ,mapid = _pc.getMapId();// 紀錄座標
			switch (punishmaent) {
			case 0:// 剔除
				_pc.sendPackets(new S_ServerMessage(945));
				_pc.sendPackets(new S_Disconnect());
				_log.info(String.format("因為檢測到%s正在使用加速器，強制切斷其連線。",_pc.getName()));
				break;
			case 1:// 鎖定人物10秒
				_pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
				try {
					Thread.sleep(10000);// 暫停十秒
				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
				}
				_pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
				break;
			case 2:// 傳到地域
				L1Teleport.teleport(_pc, 32737, 32796, (short) 99, 5, false);
				_pc.sendPackets(new S_SystemMessage("因為你使用加速器，被傳送到了地域，10秒後傳回。"));
				try {
					Thread.sleep(10000);// 暫停十秒
				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
				}
				L1Teleport.teleport(_pc, x, y, (short) mapid, 5, false);
				break;
			case 3:// 傳到GM房，30秒後傳回
				L1Teleport.teleport(_pc, 32737, 32796, (short) 99, 5, false);
				_pc.sendPackets(new S_SystemMessage("因為你使用加速器，被傳送到了GM房，30秒後傳回。"));
				try {
					Thread.sleep(30000);// 暫停30秒
				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
				}
				L1Teleport.teleport(_pc, x, y, (short) mapid, 5, false);
				break;
			}
		} else {
			// GM不需要斷線
			_pc.sendPackets(new S_SystemMessage("遊戲管理員在遊戲中使用加速器檢測中。"));
			_injusticeCount = 0;
		}
		if (Config.writeRobotsLog) {
			LogRecorder.writeRobotsLog(_pc);// 加速器紀錄
		}
	}

	/**
	 * PCの状態から指定された種類のアクションの正しいインターバル(ms)を計算し、返す。
	 * 
	 * @param type
	 *            - アクションの種類
	 * @param _pc
	 *            - 調べるPC
	 * @return 正しいインターバル(ms)
	 */
	private int getRightInterval(ACT_TYPE type) {
		int interval;

		// 動作判斷
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

		// 一段加速
		switch (_pc.getMoveSpeed()) {
		case 1: // 加速術
			interval *= HASTE_RATE;
			break;
		case 2: // 緩速術
			interval /= HASTE_RATE;
			break;
		default:
			break;
		}

		// 二段加速
		switch (_pc.getBraveSpeed()) {
		case 1: // 勇水
			interval *= HASTE_RATE; // 攻速、移速 * 1.33倍
			break;
		case 3: // 精餅
			interval *= WAFFLE_RATE; // 攻速、移速 * 1.15倍
			break;
		case 4: // 神疾、風走、行走
			if (type.equals(ACT_TYPE.MOVE)) {
				interval *= HASTE_RATE; // 移速 * 1.33倍
			}
			break;
		case 5: // 超級加速
			interval *= DOUBLE_HASTE_RATE; // 攻速、移速 * 2.66倍
			break;
		case 6: // 血之渴望
			if (type.equals(ACT_TYPE.ATTACK)) {
				interval *= HASTE_RATE; // 攻速 * 1.33倍
			}
			break;
		default:
			break;
		}

		// 生命之樹果實
		if (_pc.isRibrave() && type.equals(ACT_TYPE.MOVE)) { // 移速 * 1.15倍
			interval *= WAFFLE_RATE;
		}
		// 三段加速
		if (_pc.isThirdSpeed()) { // 攻速、移速 * 1.15倍
			interval *= WAFFLE_RATE;
		}
		// 風之枷鎖
		if (_pc.isWindShackle()) { // 攻速 / 2倍
			interval *= 2;
		}
		if (_pc.getMapId() == 5143) { // 寵物競速例外
			interval *= 0.1;
		}
		return interval;
	}
}
