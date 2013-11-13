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

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_NewCharPacket extends ServerBasePacket {
	private static final String _S__25_NEWCHARPACK = "[S] New Char Packet";
	private byte[] _byte = null;

	public S_NewCharPacket(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_NEWCHARPACK);
		writeS(pc.getName());
		writeS("");
		writeC(pc.getType());
		writeC(pc.get_sex());
		writeH(pc.getLawful());
		writeH(pc.getMaxHp());
		writeH(pc.getMaxMp());
		writeC(pc.getAc());
		writeC(pc.getLevel());
		writeC(pc.getStr());
		writeC(pc.getDex());
		writeC(pc.getCon());
		writeC(pc.getWis());
		writeC(pc.getCha());
		writeC(pc.getInt());
		writeC(0);  // 是否為管理員
		writeD(pc.getSimpleBirthday());
		writeC((pc.getLevel() ^ pc.getStr() ^ pc.getDex() ^ pc.getCon() ^ pc.getWis() ^ pc.getCha() ^ pc.getInt()) & 0xff);  // XOR 驗證
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
		return _S__25_NEWCHARPACK;
	}

}
