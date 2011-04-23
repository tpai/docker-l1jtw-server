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

import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 送出顯示龍門選單動作
 */
public class S_DragonGate extends ServerBasePacket {
	private static final String S_DRAGON_GATE = "[S] S_DragonGate";

	private static Logger _log = Logger.getLogger(S_DragonGate.class.getName());

	private byte[] _byte = null;

	public S_DragonGate(L1PcInstance pc ,boolean[] i) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(0x66); // = 102
		writeD(pc.getId());
		// true 可點選，false 不能點選
		writeC(i[0] ? 1 : 0); // 安塔瑞斯
		writeC(i[1] ? 1 : 0); // 法利昂
		writeC(i[2] ? 1 : 0); // 林德拜爾
		writeC(i[3] ? 1 : 0); // 巴拉卡斯
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
		return S_DRAGON_GATE;
	}
}
