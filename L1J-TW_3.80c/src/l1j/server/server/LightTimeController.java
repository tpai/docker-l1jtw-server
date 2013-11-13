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
package l1j.server.server;

import l1j.server.server.datatables.LightSpawnTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.gametime.L1GameTimeClock;

public class LightTimeController implements Runnable {
	private static LightTimeController _instance;

	private boolean isSpawn = false;

	public static LightTimeController getInstance() {
		if (_instance == null) {
			_instance = new LightTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				checkLightTime();
				Thread.sleep(60000);
			}
		} catch (Exception e1) {
		}
	}

	private void checkLightTime() {
		int serverTime = L1GameTimeClock.getInstance().currentTime()
				.getSeconds();
		int nowTime = serverTime % 86400;
		if ((nowTime >= ((5 * 3600) + 3300))
				&& (nowTime < ((17 * 3600) + 3300))) { // 5:55~17:55
			if (isSpawn) {
				isSpawn = false;
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1FieldObjectInstance) {
						L1FieldObjectInstance npc = (L1FieldObjectInstance) object;
						if (((npc.getNpcTemplate().get_npcId() == 81177)
								|| (npc.getNpcTemplate().get_npcId() == 81178)
								|| (npc.getNpcTemplate().get_npcId() == 81179)
								|| (npc.getNpcTemplate().get_npcId() == 81180) || (npc
								.getNpcTemplate().get_npcId() == 81181))
								&& ((npc.getMapId() == 0) || (npc.getMapId() == 4))) {
							npc.deleteMe();
						}
					}
				}
			}
		} else if (((nowTime >= ((17 * 3600) + 3300)) && (nowTime <= 24 * 3600))
				|| ((nowTime >= 0 * 3600) && (nowTime < ((5 * 3600) + 3300)))) { // 17:55~24:00,0:00~5:55
			if (!isSpawn) {
				isSpawn = true;
				LightSpawnTable.getInstance();
			}
		}
	}

}
