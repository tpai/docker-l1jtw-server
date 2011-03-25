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

import java.util.StringTokenizer;

import l1j.server.server.HomeTownTimeController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1HomeTown implements L1CommandExecutor {
	private L1HomeTown() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1HomeTown();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String para1 = st.nextToken();
			if (para1.equalsIgnoreCase("daily")) {
				HomeTownTimeController.getInstance().dailyProc();
			}
			else if (para1.equalsIgnoreCase("monthly")) {
				HomeTownTimeController.getInstance().monthlyProc();
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("請輸入 .hometown daily|monthly 。"));
		}
	}
}
