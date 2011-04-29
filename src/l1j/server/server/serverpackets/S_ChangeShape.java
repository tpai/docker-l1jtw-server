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

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SkillIconGFX, S_CharVisualUpdate

public class S_ChangeShape extends ServerBasePacket {

	private byte[] _byte = null;

	public S_ChangeShape(int objId, int polyId) {
		buildPacket(objId, polyId, false);
	}

	public S_ChangeShape(int objId, int polyId, boolean weaponTakeoff) {
		buildPacket(objId, polyId, weaponTakeoff);
	}

	private void buildPacket(int objId, int polyId, boolean weaponTakeoff) {
		writeC(Opcodes.S_OPCODE_POLY);
		writeD(objId);
		writeH(polyId);
		/** 武器類型? */
		writeC(weaponTakeoff ? 0 : 0);//不明為何 false = 0, 無論是 = 11 OR = 29 空手狀態角色都會被恢復為拿單手斧， = 0 則正常
		writeC(0xff);
		writeC(0xff);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}
