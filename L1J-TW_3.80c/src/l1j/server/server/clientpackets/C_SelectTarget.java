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
package l1j.server.server.clientpackets;

import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來選擇目標的封包
 */
public class C_SelectTarget extends ClientBasePacket {

	private static final String C_SELECT_TARGET = "[C] C_SelectTarget";

	public C_SelectTarget(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		int petId = readD();
		readC();
		int targetId = readD();

		L1PetInstance pet = (L1PetInstance) L1World.getInstance().findObject(petId);
		L1Character target = (L1Character) L1World.getInstance().findObject(targetId);

		if ((pet != null) && (target != null)) {
			// 目標為玩家
			if (target instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) target;
				// 目標在安區、攻擊者在安區、NOPVP
				if ((pc.getZoneType() == 1) || (pet.getZoneType() == 1) || (pc.checkNonPvP(pc, pet))) {
					// 寵物主人
					if (pet.getMaster() instanceof L1PcInstance) {
						L1PcInstance petMaster = (L1PcInstance) pet.getMaster();
						petMaster.sendPackets(new S_ServerMessage(328)); // 請選擇正確的對象。
					}
					return;
				}
			}
			// 目標為寵物
			else if (target instanceof L1PetInstance) {
				L1PetInstance targetPet = (L1PetInstance) target;
				// 目標在安區、攻擊者在安區
				if ((targetPet.getZoneType() == 1) || (pet.getZoneType() == 1)) {
					// 寵物主人
					if (pet.getMaster() instanceof L1PcInstance) {
						L1PcInstance petMaster = (L1PcInstance) pet.getMaster();
						petMaster.sendPackets(new S_ServerMessage(328)); // 請選擇正確的對象。
					}
					return;
				}
			}
			// 目標為召喚怪
			else if (target instanceof L1SummonInstance) {
				L1SummonInstance targetSummon = (L1SummonInstance) target;
				// 目標在安區、攻擊者在安區
				if ((targetSummon.getZoneType() == 1) || (pet.getZoneType() == 1)) {
					// 寵物主人
					if (pet.getMaster() instanceof L1PcInstance) {
						L1PcInstance petMaster = (L1PcInstance) pet.getMaster();
						petMaster.sendPackets(new S_ServerMessage(328)); // 請選擇正確的對象。
					}
					return;
				}
			}
			// 目標為怪物
			else if (target instanceof L1MonsterInstance) {
				L1MonsterInstance mob = (L1MonsterInstance) target;
				// 特定狀態下才可攻擊
				if (pet.getMaster().isAttackMiss(pet.getMaster(), mob.getNpcId())) {
					if (pet.getMaster() instanceof L1PcInstance) {
						L1PcInstance petMaster = (L1PcInstance) pet.getMaster();
						petMaster.sendPackets(new S_ServerMessage(328)); // 請選擇正確的對象。
					}
					return;
				}
			}
			pet.setMasterTarget(target);
		}
	}

	@Override
	public String getType() {
		return C_SELECT_TARGET;
	}
}
