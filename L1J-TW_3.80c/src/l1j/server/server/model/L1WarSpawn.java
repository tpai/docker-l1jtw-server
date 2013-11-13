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

import java.lang.reflect.Constructor;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.templates.L1Npc;

// Referenced classes of package l1j.server.server.model:
// L1WarSpawn

public class L1WarSpawn {
	private static L1WarSpawn _instance;

	private Constructor<?> _constructor;

	public L1WarSpawn() {
	}

	public static L1WarSpawn getInstance() {
		if (_instance == null) {
			_instance = new L1WarSpawn();
		}
		return _instance;
	}

	public void SpawnTower(int castleId) {
		int npcId = 81111;
		if (castleId == L1CastleLocation.ADEN_CASTLE_ID) {
			npcId = 81189;
		}
		L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId); // ガーディアンタワー
		int[] loc = new int[3];
		loc = L1CastleLocation.getTowerLoc(castleId);
		SpawnWarObject(l1npc, loc[0], loc[1], (short) (loc[2]));
		if (castleId == L1CastleLocation.ADEN_CASTLE_ID) {
			spawnSubTower();
		}
	}

	private void spawnSubTower() {
		L1Npc l1npc;
		int[] loc = new int[3];
		for (int i = 1; i <= 4; i++) {
			l1npc = NpcTable.getInstance().getTemplate(81189 + i); // サブタワー
			loc = L1CastleLocation.getSubTowerLoc(i);
			SpawnWarObject(l1npc, loc[0], loc[1], (short) (loc[2]));
		}
	}

	public void SpawnCrown(int castleId) {
		L1Npc l1npc = NpcTable.getInstance().getTemplate(81125); // クラウン
		int[] loc = new int[3];
		loc = L1CastleLocation.getTowerLoc(castleId);
		SpawnWarObject(l1npc, loc[0], loc[1], (short) (loc[2]));
	}

	public void SpawnFlag(int castleId) {
		L1Npc l1npc = NpcTable.getInstance().getTemplate(81122); // 旗
		int[] loc = new int[5];
		loc = L1CastleLocation.getWarArea(castleId);
		int x = 0;
		int y = 0;
		int locx1 = loc[0];
		int locx2 = loc[1];
		int locy1 = loc[2];
		int locy2 = loc[3];
		short mapid = (short) loc[4];

		for (x = locx1, y = locy1; x <= locx2; x += 8) {
			SpawnWarObject(l1npc, x, y, mapid);
		}
		for (x = locx2, y = locy1; y <= locy2; y += 8) {
			SpawnWarObject(l1npc, x, y, mapid);
		}
		for (x = locx2, y = locy2; x >= locx1; x -= 8) {
			SpawnWarObject(l1npc, x, y, mapid);
		}
		for (x = locx1, y = locy2; y >= locy1; y -= 8) {
			SpawnWarObject(l1npc, x, y, mapid);
		}
	}

	private void SpawnWarObject(L1Npc l1npc, int locx, int locy, short mapid) {
		try {
			if (l1npc != null) {
				String s = l1npc.getImpl();
				_constructor = Class.forName(
						(new StringBuilder()).append("l1j.server.server.model.Instance.").append(s).append("Instance").toString()).getConstructors()[0];
				Object aobj[] =
				{ l1npc };
				L1NpcInstance npc = (L1NpcInstance) _constructor.newInstance(aobj);
				npc.setId(IdFactory.getInstance().nextId());
				npc.setX(locx);
				npc.setY(locy);
				npc.setHomeX(locx);
				npc.setHomeY(locy);
				npc.setHeading(0);
				npc.setMap(mapid);
				L1World.getInstance().storeObject(npc);
				L1World.getInstance().addVisibleObject(npc);

				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					npc.addKnownObject(pc);
					pc.addKnownObject(npc);
					pc.sendPackets(new S_NPCPack(npc));
					pc.broadcastPacket(new S_NPCPack(npc));
				}
			}
		}
		catch (Exception exception) {}
	}
}
