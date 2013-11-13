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

public class S_SkillSound extends ServerBasePacket {
	private static final String S_SKILL_SOUND = "[S] S_SkillSound";

	private byte[] _byte = null;

	public S_SkillSound(int objid, int gfxid, int aid) {

		buildPacket(objid, gfxid, aid);
	}

	public S_SkillSound(int objid, int gfxid) {
		buildPacket(objid, gfxid, 0);
	}

	private void buildPacket(int objid, int gfxid, int aid) {
		// aidは使われていない
		writeC(Opcodes.S_OPCODE_SKILLSOUNDGFX);
		writeD(objid);
		writeH(gfxid);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}

		return _byte;
	}

	@Override
	public String getType() {
		return S_SKILL_SOUND;
	}
}
