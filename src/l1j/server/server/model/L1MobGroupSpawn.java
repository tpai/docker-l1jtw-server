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

import l1j.server.server.utils.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.MobGroupTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1MobGroupInfo;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1MobGroup;
import l1j.server.server.templates.L1NpcCount;

// Referenced classes of package l1j.server.server.model:
// L1MobGroupSpawn

public class L1MobGroupSpawn {

	private static final Logger _log = Logger.getLogger(L1MobGroupSpawn.class
			.getName());

	private static L1MobGroupSpawn _instance;

	private boolean _isRespawnScreen;

	private boolean _isInitSpawn;

	private L1MobGroupSpawn() {
	}

	public static L1MobGroupSpawn getInstance() {
		if (_instance == null) {
			_instance = new L1MobGroupSpawn();
		}
		return _instance;
	}

	public void doSpawn(L1NpcInstance leader, int groupId,
			boolean isRespawnScreen, boolean isInitSpawn) {

		L1MobGroup mobGroup = MobGroupTable.getInstance().getTemplate(groupId);
		if (mobGroup == null) {
			return;
		}

		L1NpcInstance mob;
		_isRespawnScreen = isRespawnScreen;
		_isInitSpawn = isInitSpawn;

		L1MobGroupInfo mobGroupInfo = new L1MobGroupInfo();

		mobGroupInfo.setRemoveGroup(mobGroup.isRemoveGroupIfLeaderDie());
		mobGroupInfo.addMember(leader);

		for (L1NpcCount minion : mobGroup.getMinions()) {
			if (minion.isZero()) {
				continue;
			}
			for (int i = 0; i < minion.getCount(); i++) {
				mob = spawn(leader, minion.getId());
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
	}

	private L1NpcInstance spawn(L1NpcInstance leader, int npcId) {
		L1NpcInstance mob = null;
		try {
			mob = NpcTable.getInstance().newNpcInstance(npcId);

			mob.setId(IdFactory.getInstance().nextId());

			mob.setHeading(leader.getHeading());
			mob.setMap(leader.getMapId());
			mob.setMovementDistance(leader.getMovementDistance());
			mob.setRest(leader.isRest());

			mob.setX(leader.getX() + Random.nextInt(5) - 2);
			mob.setY(leader.getY() + Random.nextInt(5) - 2);
			// マップ外、障害物上、画面内沸き不可で画面内にPCがいる場合、リーダーと同じ座標
			if (!canSpawn(mob)) {
				mob.setX(leader.getX());
				mob.setY(leader.getY());
			}
			mob.setHomeX(mob.getX());
			mob.setHomeY(mob.getY());

			if (mob instanceof L1MonsterInstance) {
				((L1MonsterInstance) mob).initHideForMinion(leader);
			}

			mob.setSpawn(leader.getSpawn());
			mob.setreSpawn(leader.isReSpawn());
			mob.setSpawnNumber(leader.getSpawnNumber());

			if (mob instanceof L1MonsterInstance) {
				if (mob.getMapId() == 666) {
					((L1MonsterInstance) mob).set_storeDroped(true);
				}
			}

			L1World.getInstance().storeObject(mob);
			L1World.getInstance().addVisibleObject(mob);

			if (mob instanceof L1MonsterInstance) {
				if (!_isInitSpawn && mob.getHiddenStatus() == 0) {
					mob.onNpcAI(); // モンスターのＡＩを開始
				}
			}
			mob.turnOnOffLight();
			mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット開始
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return mob;
	}

	private boolean canSpawn(L1NpcInstance mob) {
		if (mob.getMap().isInMap(mob.getLocation())
				&& mob.getMap().isPassable(mob.getLocation())) {
			if (_isRespawnScreen) {
				return true;
			}
			if (L1World.getInstance().getVisiblePlayer(mob).isEmpty()) {
				return true;
			}
		}
		return false;
	}

}
