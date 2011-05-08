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
	public void onAction(L1PcInstance pc) {
		onAction(pc, 0);
	}

	@Override
	public void onAction(L1PcInstance pc, int skillId) {
		L1Attack attack = new L1Attack(pc, this, skillId);
		if (attack.calcHit()) {
			attack.calcDamage();
			attack.calcStaffOfMana();
			attack.addPcPoisonAttack(pc, this);
			attack.addChaserAttack();
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if ((getCurrentHp() > 0) && !isDead()) {
			if (damage > 0) {
				if (getHeading() < 7) {
					setHeading(getHeading() + 1);
				}
				else {
					setHeading(0);
				}
				broadcastPacket(new S_ChangeHeading(this));

				if ((attacker instanceof L1PcInstance)) {
					L1PcInstance pc = (L1PcInstance) attacker;
					pc.setPetTarget(this);

					if (pc.getLevel() < 5) {
						List<L1Character> targetList = Lists.newList();
						targetList.add(pc);
						List<Integer> hateList = Lists.newList();
						hateList.add(1);
						CalcExp.calcExp(pc, getId(), targetList, hateList, getExp());
					}
				}
			}
		}
	}

	@Override
	public void onTalkAction(L1PcInstance l1pcinstance) {

	}

	public void onFinalAction() {

	}

	public void doFinalAction() {
	}
}
