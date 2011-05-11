package l1j.server.server.templates;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.Random;

public class L1MagicDoll {

	public static int getHitAddByDoll(L1Character _master) { // 近距離的命中率增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getHit();
			}
		}
		return s;
	}

	public static int getDamageAddByDoll(L1Character _master) { // 近距離的攻擊力增加
		int s = 0;
		int chance = Random.nextInt(100) + 1;
		boolean isAdd = false;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (doll.getDmgChance() > 0 && !isAdd) { // 額外傷害發動機率
					if (doll.getDmgChance() >= chance) {
						s += doll.getDmg();
						isAdd = true;
					}
				} else if (doll.getDmg() != 0) { // 額外傷害
					s += doll.getDmg();
				}
			}
		}
		if (isAdd) {
			if (_master instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) _master;
				pc.sendPackets(new S_SkillSound(_master.getId(), 6319));
			}
			_master.broadcastPacket(new S_SkillSound(_master.getId(), 6319));
		}
		return s;
	}

	public static int getDamageReductionByDoll(L1Character _master) { // 傷害減免
		int s = 0;
		int chance = Random.nextInt(100) + 1;
		boolean isReduction = false;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (doll.getDmgReductionChance() > 0 && !isReduction) { // 傷害減免發動機率
					if (doll.getDmgReductionChance() >= chance) {
						s += doll.getDmgReduction();
						isReduction = true;
					}
				} else if (doll.getDmgReduction() != 0) { // 傷害減免
					s += doll.getDmgReduction();
				}
			}
		}
		if (isReduction ) {
			if (_master instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) _master;
				pc.sendPackets(new S_SkillSound(_master.getId(), 6320));
			}
			_master.broadcastPacket(new S_SkillSound(_master.getId(), 6320));
		}
		return s;
	}

	public static int getDamageEvasionByDoll(L1Character _master) { // 傷害迴避
		int chance = Random.nextInt(100) + 1;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (doll.getDmgEvasionChance() >= chance) { // 傷害迴避發動機率
					if (_master instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _master;
						pc.sendPackets(new S_SkillSound(_master.getId(), 6320));
					}
					_master.broadcastPacket(new S_SkillSound(_master.getId(), 6320));
					return 1;
				}
			}
		}
		return 0;
	}

	public static int getBowHitAddByDoll(L1Character _master) { // 弓的命中率增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getBowHit();
			}
		}
		return s;
	}

	public static int getBowDamageByDoll(L1Character _master) { // 弓的攻擊力增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getBowDmg();
			}
		}
		return s;
	}

	public static int getAcByDoll(L1Character _master) { // 防禦力增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getAc();
			}
		}
		return s;
	}

	public static int getRegistStoneByDoll(L1Character _master) { // 石化耐性增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getRegistStone();
			}
		}
		return s;
	}

	public static int getRegistStunByDoll(L1Character _master) { // 昏迷耐性增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getRegistStun();
			}
		}
		return s;
	}

	public static int getRegistSustainByDoll(L1Character _master) { // 支撐耐性增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getRegistSustain();
			}
		}
		return s;
	}

	public static int getRegistBlindByDoll(L1Character _master) { // 闇黑耐性增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getRegistBlind();
			}
		}
		return s;
	}

	public static int getRegistFreezeByDoll(L1Character _master) { // 寒冰耐性增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getRegistFreeze();
			}
		}
		return s;
	}

	public static int getRegistSleepByDoll(L1Character _master) { // 睡眠耐性增加
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getRegistSleep();
			}
		}
		return s;
	}

	public static int getWeightReductionByDoll(L1Character _master) { // 負重減輕
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				s += doll.getWeightReduction();
			}
		}
		return s;
	}

	public static int getHprByDoll(L1Character _master) { // 體力回覆量
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (!doll.getHprTime() && doll.getHpr() != 0) {
					s += doll.getHpr();
				}
			}
		}
		return s;
	}

	public static int getMprByDoll(L1Character _master) { // 魔力回覆量
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (!doll.getMprTime() && doll.getMpr() != 0) {
					s += doll.getMpr();
				}
			}
		}
		return s;
	}

	public static boolean isItemMake(L1Character _master) {
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				L1Item item = ItemTable.getInstance().getTemplate((doll.getMakeItemId()));
				if (item != null) {
					return true;
				}
			}
		}
		return false;
	}

	public static int getMakeItemId(L1Character _master) { // 獲得道具
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				L1Item item = ItemTable.getInstance().getTemplate((doll.getMakeItemId()));
				if (item != null) {
					return item.getItemId();
				}
			}
		}
		return 0;
	}

	public static boolean isHpRegeneration(L1Character _master) { // 回血判斷 (時間固定性)
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (doll.getHprTime() && doll.getHpr() != 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static int getHpByDoll(L1Character _master) { // 體力回覆量 (時間固定性)
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (doll.getHprTime() && doll.getHpr() != 0) {
					s += doll.getHpr();
				}
			}
		}
		return s;
	}

	public static boolean isMpRegeneration(L1Character _master) { // 回魔判斷 (時間固定性)
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (doll.getMprTime() && doll.getMpr() != 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static int getMpByDoll(L1Character _master) { // 魔力回覆量 (時間固定性)
		int s = 0;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (doll.getMprTime() && doll.getMpr() != 0) {
					s += doll.getMpr();
				}
			}
		}
		return s;
	}

	public static int getEffectByDoll(L1Character _master, byte type) { // 效果
		int chance = Random.nextInt(100) + 1;
		for (Object obj : _master.getDollList().values().toArray()) {
			L1MagicDoll doll = MagicDollTable.getInstance().getTemplate(((L1DollInstance) obj).getItemId());
			if (doll != null) {
				if (doll.getEffect() == type) {
					if (chance <= 5) {
						return type;
					}
				}
			}
		}
		return 0;
	}

	private int _itemId;
	private int _dollId;
	private int _ac;
	private int _hpr;
	private int _mpr;
	private boolean _hprTime;
	private boolean _mprTime;
	private int _dmg;
	private int _bowDmg;
	private int _dmgChance;
	private int _hit;
	private int _bowHit;
	private int _dmgReduction;
	private int _dmgReductionChance;
	private int _dmgEvasionChance;
	private int _weightReduction;
	private int _registStun;
	private int _registStone;
	private int _registSleep;
	private int _registFreeze;
	private int _registSustain;
	private int _registBlind;
	private int _makeItemId;
	private byte _effect;
	
	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int i) {
		_itemId = i;
	}

	public int getDollId() {
		return _dollId;
	}

	public void setDollId(int i) {
		_dollId = i;
	}

	public int getAc() {
		return _ac;
	}

	public void setAc(int i) {
		_ac = i;
	}

	public int getHpr() {
		return _hpr;
	}

	public void setHpr(int i) {
		_hpr = i;
	}

	public int getMpr() {
		return _mpr;
	}

	public void setMpr(int i) {
		_mpr = i;
	}

	public boolean getHprTime() {
		return _hprTime;
	}

	public void setHprTime(boolean i) {
		_hprTime = i;
	}

	public boolean getMprTime() {
		return _mprTime;
	}

	public void setMprTime(boolean i) {
		_mprTime = i;
	}

	public int getDmg() {
		return _dmg;
	}

	public void setDmg(int i) {
		_dmg = i;
	}

	public int getBowDmg() {
		return _bowDmg;
	}

	public void setBowDmg(int i) {
		_bowDmg = i;
	}

	public int getDmgChance() {
		return _dmgChance;
	}

	public void setDmgChance(int i) {
		_dmgChance = i;
	}

	public int getHit() {
		return _hit;
	}

	public void setHit(int i) {
		_hit = i;
	}

	public int getBowHit() {
		return _bowHit;
	}

	public void setBowHit(int i) {
		_bowHit = i;
	}

	public int getDmgReduction() {
		return _dmgReduction;
	}

	public void setDmgReduction(int i) {
		_dmgReduction = i;
	}

	public int getDmgReductionChance() {
		return _dmgReductionChance;
	}

	public void setDmgReductionChance(int i) {
		_dmgReductionChance = i;
	}

	public int getDmgEvasionChance() {
		return _dmgEvasionChance;
	}

	public void setDmgEvasionChance(int i) {
		_dmgEvasionChance = i;
	}

	public int getWeightReduction() {
		return _weightReduction;
	}

	public void setWeightReduction(int i) {
		_weightReduction = i;
	}

	public int getRegistStun() {
		return _registStun;
	}

	public void setRegistStun(int i) {
		_registStun = i;
	}

	public int getRegistStone() {
		return _registStone;
	}

	public void setRegistStone(int i) {
		_registStone = i;
	}

	public int getRegistSleep() {
		return _registSleep;
	}

	public void setRegistSleep(int i) {
		_registSleep = i;
	}

	public int getRegistFreeze() {
		return _registFreeze;
	}

	public void setRegistFreeze(int i) {
		_registFreeze = i;
	}

	public int getRegistSustain() {
		return _registSustain;
	}

	public void setRegistSustain(int i) {
		_registSustain = i;
	}

	public int getRegistBlind() {
		return _registBlind;
	}

	public void setRegistBlind(int i) {
		_registBlind = i;
	}

	public int getMakeItemId() {
		return _makeItemId;
	}

	public void setMakeItemId(int i) {
		_makeItemId = i;
	}

	public byte getEffect() {
		return _effect;
	}

	public void setEffect(byte i) {
		_effect = i;
	}
}
