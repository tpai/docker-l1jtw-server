/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_CharReset extends ServerBasePacket {

	private static final String S_CHAR_RESET = "[S] S_CharReset";

	private byte[] _byte = null;

	/**
	 * 重置升級能力更新 [Server] opcode = 43 0000: 2b /02/ 01 2d/ 0f 00/ 04 00/ 0a 00
	 * /0c 0c 0c 0c 12 09 +..-............
	 */
	public S_CharReset(L1PcInstance pc, int lv, int hp, int mp, int ac, int str, int intel, int wis, int dex, int con, int cha) {
		writeC(Opcodes.S_OPCODE_CHARRESET);
		writeC(0x02);
		writeC(lv);
		writeC(pc.getTempMaxLevel()); // max lv
		writeH(hp);
		writeH(mp);
		writeH(ac);
		writeC(str);
		writeC(intel);
		writeC(wis);
		writeC(dex);
		writeC(con);
		writeC(cha);
	}

	public S_CharReset(int point) {
		writeC(Opcodes.S_OPCODE_CHARRESET);
		writeC(0x03);
		writeC(point);
	}

	/**
	 * 45及腰精進入崇志 [Server] opcode = 43 0000: 2b 01 0f 00 04 00 0a 2d 56法進入崇志
	 * [Server] opcode = 43 0000: 2b 01 0c 00 06 00 0a 38
	 */
	public S_CharReset(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_CHARRESET);
		writeC(0x01);
		if (pc.isCrown()) {
			writeH(14);
			writeH(2);
		}
		else if (pc.isKnight()) {
			writeH(16);
			writeH(1);
		}
		else if (pc.isElf()) {
			writeH(15);
			writeH(4);
		}
		else if (pc.isWizard()) {
			writeH(12);
			writeH(6);
		}
		else if (pc.isDarkelf()) {
			writeH(12);
			writeH(3);
		}
		else if (pc.isDragonKnight()) {
			writeH(15);
			writeH(4);
		}
		else if (pc.isIllusionist()) {
			writeH(15);
			writeH(4);
		}
		writeC(0x0a); // AC
		writeC(pc.getTempMaxLevel()); // Lv
		/**
		 * 0000: 2b 04 60 04 06 01 07 1e 不知道幹麻用的
		 */
		// }else if(type == 4){
		// writeC(Opcodes.S_OPCODE_CHARRESET);
		// writeC(4);
		// writeC(0x60);
		// writeC(0x04);
		// writeC(0x09);
		// writeC(0x01);
		// writeC(0x07);
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
		return S_CHAR_RESET;
	}
}
