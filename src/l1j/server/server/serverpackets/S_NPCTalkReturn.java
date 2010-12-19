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
import l1j.server.server.model.npc.L1NpcHtml;

public class S_NPCTalkReturn extends ServerBasePacket {
	private static final String _S__25_TalkReturn = "[S] _S__25_TalkReturn";
	private byte[] _byte = null;

	public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action,
			String[] data) {

		String htmlid = "";

		if (action == 1) {
			htmlid = npc.getNormalAction();
		} else if (action == 2) {
			htmlid = npc.getCaoticAction();
		} else {
			throw new IllegalArgumentException();
		}

		buildPacket(objid, htmlid, data);
	}

	public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action) {
		this(npc, objid, action, null);
	}

	public S_NPCTalkReturn(int objid, String htmlid, String[] data) {
		buildPacket(objid, htmlid, data);
	}

	public S_NPCTalkReturn(int objid, String htmlid) {
		buildPacket(objid, htmlid, null);
	}

	public S_NPCTalkReturn(int objid, L1NpcHtml html) {
		buildPacket(objid, html.getName(), html.getArgs());
	}

	private void buildPacket(int objid, String htmlid, String[] data) {

		writeC(Opcodes.S_OPCODE_SHOWHTML);
		writeD(objid);
		writeS(htmlid);
		if (data != null && 1 <= data.length) {
			writeH(0x01); // 不明バイト 分かる人居たら修正願います
			writeH(data.length); // 引数の数
			for (String datum : data) {
				writeS(datum);
			}
		} else {
			writeH(0x00);
			writeH(0x00);
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
		return _S__25_TalkReturn;
	}
}
