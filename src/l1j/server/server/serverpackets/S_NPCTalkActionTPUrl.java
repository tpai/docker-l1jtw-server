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
import l1j.server.server.model.L1NpcTalkData;

public class S_NPCTalkActionTPUrl extends ServerBasePacket {
	private static final String _S__25_TalkReturnAction = "[S] S_NPCTalkActionTPUrl";
	private byte[] _byte = null;

	public S_NPCTalkActionTPUrl(L1NpcTalkData cha, Object[] prices, int objid) {
		buildPacket(cha, prices, objid);
	}

	private void buildPacket(L1NpcTalkData npc, Object[] prices, int objid) {
		String htmlid = "";
		htmlid = npc.getTeleportURL();
		writeC(Opcodes.S_OPCODE_SHOWHTML);
		writeD(objid);
		writeS(htmlid);
		writeH(0x01); // 不明
		writeH(prices.length); // 引数の数

		for (Object price : prices) {
			writeS(String.valueOf(((Integer) price).intValue()));
		}
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
		return _S__25_TalkReturnAction;
	}
}
