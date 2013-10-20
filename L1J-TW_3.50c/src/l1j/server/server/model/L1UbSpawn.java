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

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NPCPack;

public class L1UbSpawn implements Comparable<L1UbSpawn> {
	private int _id;

	private int _ubId;

	private int _pattern;

	private int _group;

	private int _npcTemplateId;

	private int _amount;

	private int _spawnDelay;

	private int _sealCount;

	private String _name;

	// --------------------start getter/setter--------------------
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public int getUbId() {
		return _ubId;
	}

	public void setUbId(int ubId) {
		_ubId = ubId;
	}

	public int getPattern() {
		return _pattern;
	}

	public void setPattern(int pattern) {
		_pattern = pattern;
	}

	public int getGroup() {
		return _group;
	}

	public void setGroup(int group) {
		_group = group;
	}

	public int getNpcTemplateId() {
		return _npcTemplateId;
	}

	public void setNpcTemplateId(int npcTemplateId) {
		_npcTemplateId = npcTemplateId;
	}

	public int getAmount() {
		return _amount;
	}

	public void setAmount(int amount) {
		_amount = amount;
	}

	public int getSpawnDelay() {
		return _spawnDelay;
	}

	public void setSpawnDelay(int spawnDelay) {
		_spawnDelay = spawnDelay;
	}

	public int getSealCount() {
		return _sealCount;
	}

	public void setSealCount(int i) {
		_sealCount = i;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	// --------------------end getter/setter--------------------

	public void spawnOne() {
		L1UltimateBattle ub = UBTable.getInstance().getUb(_ubId);
		L1Location loc = ub.getLocation().randomLocation((ub.getLocX2() - ub.getLocX1()) / 2, false);
		L1MonsterInstance mob = new L1MonsterInstance(NpcTable.getInstance().getTemplate(getNpcTemplateId()));

		mob.setId(IdFactory.getInstance().nextId());
		mob.setHeading(5);
		mob.setX(loc.getX());
		mob.setHomeX(loc.getX());
		mob.setY(loc.getY());
		mob.setHomeY(loc.getY());
		mob.setMap((short) loc.getMapId());
		mob.set_storeDroped(!(3 < getGroup()));
		mob.setUbSealCount(getSealCount());
		mob.setUbId(getUbId());

		L1World.getInstance().storeObject(mob);
		L1World.getInstance().addVisibleObject(mob);

		S_NPCPack s_npcPack = new S_NPCPack(mob);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(mob)) {
			pc.addKnownObject(mob);
			mob.addKnownObject(pc);
			pc.sendPackets(s_npcPack);
		}
		// モンスターのＡＩを開始
		mob.onNpcAI();
		mob.turnOnOffLight();
		// mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット開始
	}

	public void spawnAll() {
		for (int i = 0; i < getAmount(); i++) {
			spawnOne();
		}
	}

	@Override
	public int compareTo(L1UbSpawn rhs) {
		// XXX - 本当はもっと厳密な順序付けがあるはずだが、必要なさそうなので後回し
		if (getId() < rhs.getId()) {
			return -1;
		}
		if (getId() > rhs.getId()) {
			return 1;
		}
		return 0;
	}
}
