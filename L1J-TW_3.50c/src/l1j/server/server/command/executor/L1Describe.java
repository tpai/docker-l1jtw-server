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

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

/**
 * GM指令：描述
 */
public class L1Describe implements L1CommandExecutor {
	private L1Describe() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Describe();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringBuilder msg = new StringBuilder();
			pc.sendPackets(new S_SystemMessage("-- describe: " + pc.getName() + " --"));
			int hpr = pc.getHpr() + pc.getInventory().hpRegenPerTick();
			int mpr = pc.getMpr() + pc.getInventory().mpRegenPerTick();
			msg.append("Dmg: +" + pc.getDmgup() + " / ");
			msg.append("Hit: +" + pc.getHitup() + " / ");
			msg.append("MR: " + pc.getMr() + " / ");
			msg.append("HPR: " + hpr + " / ");
			msg.append("MPR: " + mpr + " / ");
			msg.append("Karma: " + pc.getKarma() + " / ");
			msg.append("Item: " + pc.getInventory().getSize() + " / ");
			pc.sendPackets(new S_SystemMessage(msg.toString()));
		}
		catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " 指令錯誤"));
		}
	}
}
