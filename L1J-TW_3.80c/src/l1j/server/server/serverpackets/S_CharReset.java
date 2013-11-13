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
	 * 『來源:伺服器』<位址:64>{長度:8}(時間:1233632532)<br>
     *  0000:  40 01 10 00 01 00 0a 34                     @......4<br>
     *  52等騎士重置
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
	}
	
	/**
	 *  給予角色盟徽編號</br>
	 * 『來源:伺服器』<位址:64>{長度:16}(時間:1607823495)</br>
        0000:  40 3c 15 ea 7a 00 33 b6 00 00 6a 6c cb 92 b5 2d    @<..z.3...jl...-
	 */
	public S_CharReset(int pcObjId, int emblemId) {
		writeC(Opcodes.S_OPCODE_CHARRESET);
		writeC(0x3c);
		writeD(pcObjId);
		writeD(emblemId);
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
