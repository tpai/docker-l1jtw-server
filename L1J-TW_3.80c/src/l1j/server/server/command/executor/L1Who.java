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

import java.util.Collection;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_WhoAmount;

public class L1Who implements L1CommandExecutor {
	private L1Who() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Who();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			Collection<L1PcInstance> players = L1World.getInstance().getAllPlayers();
			String amount = String.valueOf(players.size());
			S_WhoAmount s_whoamount = new S_WhoAmount(amount);
			pc.sendPackets(s_whoamount);

			// オンラインのプレイヤーリストを表示
			if (arg.equalsIgnoreCase("all")) {
				pc.sendPackets(new S_SystemMessage("-- 線上玩家 --"));
				StringBuffer buf = new StringBuffer();
				for (L1PcInstance each : players) {
					buf.append(each.getName());
					buf.append(" / ");
					if (buf.length() > 50) {
						pc.sendPackets(new S_SystemMessage(buf.toString()));
						buf.delete(0, buf.length() - 1);
					}
				}
				if (buf.length() > 0) {
					pc.sendPackets(new S_SystemMessage(buf.toString()));
				}
			}
		}
		catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入: .who [all] 。"));
		}
	}
}
