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

public class S_Pledge extends ServerBasePacket {
	private static final String _S_Pledge = "[S] _S_Pledge";

	private byte[] _byte = null;

	public S_Pledge(String htmlid, int objid) {
		buildPacket(htmlid, objid, 0, "", "", "");
	}

	public S_Pledge(String htmlid, int objid, String clanname, String olmembers) {
		buildPacket(htmlid, objid, 1, clanname, olmembers, "");
	}

	public S_Pledge(String htmlid, int objid, String clanname,
			String olmembers, String allmembers) {

		buildPacket(htmlid, objid, 2, clanname, olmembers, allmembers);
	}

	private void buildPacket(String htmlid, int objid, int type,
			String clanname, String olmembers, String allmembers) {

		writeC(Opcodes.S_OPCODE_SHOWHTML);
		writeD(objid);
		writeS(htmlid);
		writeH(type);
		writeH(0x03);
		writeS(clanname); // clanname
		writeS(olmembers); // clanmember with a space in the end
		writeS(allmembers); // all clan members names with a space in the
		// end
		// example: "player1 player2 player3 "
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
		return _S_Pledge;
	}
}
