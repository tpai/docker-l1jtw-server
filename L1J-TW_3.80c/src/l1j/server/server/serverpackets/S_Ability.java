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
// ServerBasePacket

public class S_Ability extends ServerBasePacket {

	private static final String S_ABILITY = "[S] S_Ability";

	private byte[] _byte = null;

	/**
	 * @param type
	 * 1, 傳送控制戒指
	 * 2, 變形控制戒指
	 * 3, 全白天
	 * 4, 夜視功能
	 * 5, 召喚控制戒指
	 * @param equipped 是否裝備
	 */
	public S_Ability(int type, boolean equipped) {
		buildPacket(type, equipped);
	}

	private void buildPacket(int type, boolean equipped) {
		writeC(Opcodes.S_OPCODE_ABILITY);
		writeC(type);
		if (equipped) {
			writeC(0x01);
		}
		else {
			writeC(0x00);
		}
		writeC(0x00);
		writeH(0x0000);
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
		return S_ABILITY;
	}
}
