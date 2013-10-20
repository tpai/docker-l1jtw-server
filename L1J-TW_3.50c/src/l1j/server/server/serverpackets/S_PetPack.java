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
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_NPCPack

public class S_PetPack extends ServerBasePacket {

	private static final String S_PET_PACK = "[S] S_PetPack";

	private static final int STATUS_POISON = 1;

	private byte[] _byte = null;

	public S_PetPack(L1PetInstance pet, L1PcInstance pc) {
		buildPacket(pet, pc);
	}

	private void buildPacket(L1PetInstance pet, L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_CHARPACK);
		writeH(pet.getX());
		writeH(pet.getY());
		writeD(pet.getId());
		writeH(pet.getGfxId()); // SpriteID in List.spr
		writeC(pet.getStatus()); // Modes in List.spr
		writeC(pet.getHeading());
		writeC(pet.getChaLightSize()); // (Bright) - 0~15
		writeC(pet.getMoveSpeed()); // スピード - 0:normal, 1:fast,
		// 2:slow
		writeD(pet.getExp());
		writeH(pet.getTempLawful());
		writeS(pet.getName());
		writeS(pet.getTitle());
		int status = 0;
		if (pet.getPoison() != null) { // 毒状態
			if (pet.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
			}
		}
		writeC(status);
		writeD(0); // ??
		writeS(null); // ??
		writeS(pet.getMaster() != null ? pet.getMaster().getName() : "");
		writeC(0); // ??
		// HPのパーセント
		if ((pet.getMaster() != null) && (pet.getMaster().getId() == pc.getId())) {
			writeC(100 * pet.getCurrentHp() / pet.getMaxHp());
		}
		else {
			writeC(0xFF);
		}
		writeC(0);
		writeC(pet.getLevel()); // PC = 0, Mon = Lv
		writeC(0);
		writeC(0xFF);
		writeC(0xFF);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}

	@Override
	public String getType() {
		return S_PET_PACK;
	}

}
