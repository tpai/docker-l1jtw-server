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
package l1j.server.server.command.executor;

import static l1j.server.server.model.skill.L1SkillId.GMSTATUS_FINDINVIS;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1FindInvis implements L1CommandExecutor {
	private L1FindInvis() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1FindInvis();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		if (arg.equalsIgnoreCase("on")) {
			pc.setSkillEffect(GMSTATUS_FINDINVIS, 0);
			pc.removeAllKnownObjects();
			pc.updateObject();
		}
		else if (arg.equalsIgnoreCase("off")) {
			pc.removeSkillEffect(GMSTATUS_FINDINVIS);
			for (L1PcInstance visible : L1World.getInstance().getVisiblePlayer(pc)) {
				if (visible.isInvisble()) {
					pc.sendPackets(new S_RemoveObject(visible));
				}
			}
		}
		else {
			pc.sendPackets(new S_SystemMessage(cmdName + "請輸入  on|off 。"));
		}
	}

}
