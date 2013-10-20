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

import l1j.server.server.datatables.IpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1PowerKick implements L1CommandExecutor {
	private L1PowerKick() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1PowerKick();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(arg);

			IpTable iptable = IpTable.getInstance();
			if (target != null) {
				iptable.banIp(target.getNetConnection().getIp()); // 加入IP至BAN名單
				pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append("被您強制踢除遊戲並封鎖IP。").toString()));
				target.sendPackets(new S_Disconnect());
			}
			else {
				pc.sendPackets(new S_SystemMessage("您指定的腳色名稱不存在。"));
			}
		}
		catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 : " + cmdName + " 玩家名稱。"));
		}
	}
}
