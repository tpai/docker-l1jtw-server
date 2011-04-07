/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.server.model.monitor;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_Lawful;

public class L1PcExpMonitor extends L1PcMonitor {

	private int _old_lawful;

	private int _old_exp;

	private int _old_karma;

	// 上一次判斷時的戰鬥特化類別
	private int _oldFight;

	public L1PcExpMonitor(int oId) {
		super(oId);
	}

	@Override
	public void execTask(L1PcInstance pc) {

		// ロウフルが変わった場合はS_Lawfulを送信
		// // ただし色が変わらない場合は送信しない
		// if (_old_lawful != pc.getLawful()
		// && !((IntRange.includes(_old_lawful, 9000, 32767) && IntRange
		// .includes(pc.getLawful(), 9000, 32767)) || (IntRange
		// .includes(_old_lawful, -32768, -2000) && IntRange
		// .includes(pc.getLawful(), -32768, -2000)))) {
		if (_old_lawful != pc.getLawful()) {
			_old_lawful = pc.getLawful();
			S_Lawful s_lawful = new S_Lawful(pc.getId(), _old_lawful);
			pc.sendPackets(s_lawful);
			pc.broadcastPacket(s_lawful);

			// 處理戰鬥特化系統
			if (Config.FIGHT_IS_ACTIVE) {
				// 計算目前的戰鬥特化組別
				int fightType = _old_lawful / 10000;

				// 判斷戰鬥特化組別是否有所變更
				if (_oldFight != fightType) {
					// 進行戰鬥特化組別的變更
					pc.changeFightType(_oldFight, fightType);

					_oldFight = fightType;
				}
			}

		}

		if (_old_karma != pc.getKarma()) {
			_old_karma = pc.getKarma();
			pc.sendPackets(new S_Karma(pc));
		}

		if (_old_exp != pc.getExp()) {
			_old_exp = pc.getExp();
			pc.onChangeExp();
		}
	}
}
