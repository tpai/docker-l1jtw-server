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
package l1j.server.server.model.Instance;

import java.util.List;

import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CalcExp;
import l1j.server.server.utils.collections.Lists;

public class L1ScarecrowInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	public L1ScarecrowInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		if (attack.calcHit()) {
			if (player.getLevel() < 5) { // ＬＶ制限もうける場合はここを変更
				List<L1Character> targetList = Lists.newList();

				targetList.add(player);
				List<Integer> hateList = Lists.newList();
				hateList.add(1);
				CalcExp.calcExp(player, getId(), targetList, hateList, getExp());
			}
			if (getHeading() < 7) { // 今の向きを取得
				setHeading(getHeading() + 1); // 今の向きを設定
			}
			else {
				setHeading(0); // 今の向きが7 以上になると今の向きを0に戻す
			}
			broadcastPacket(new S_ChangeHeading(this)); // 向きの変更
		}
		attack.action();
	}

	@Override
	public void onTalkAction(L1PcInstance l1pcinstance) {

	}

	public void onFinalAction() {

	}

	public void doFinalAction() {
	}
}
