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
import l1j.server.server.model.Instance.L1FollowerInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_NPCPack

public class S_FollowerPack extends ServerBasePacket {

	private static final String S_FOLLOWER_PACK = "[S] S_FollowerPack";

	private static final int STATUS_POISON = 1;

	private byte[] _byte = null;

	public S_FollowerPack(L1FollowerInstance follower, L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_CHARPACK);
		writeH(follower.getX());
		writeH(follower.getY());
		writeD(follower.getId());
		writeH(follower.getGfxId());
		writeC(follower.getStatus());
		writeC(follower.getHeading());
		writeC(follower.getChaLightSize());
		writeC(follower.getMoveSpeed());
		writeD(0);
		writeH(0);
		writeS(follower.getNameId());
		writeS(follower.getTitle());
		int status = 0;
		if (follower.getPoison() != null) { // 毒状態
			if (follower.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
			}
		}
		writeC(status);
		writeD(0);
		writeS(null);
		writeS(null);
		writeC(0);
		writeC(0xFF);
		writeC(0);
		writeC(follower.getLevel());
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
		return S_FOLLOWER_PACK;
	}

}
