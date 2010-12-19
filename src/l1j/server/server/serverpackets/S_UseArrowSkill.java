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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_UseArrowSkill extends ServerBasePacket {

	private static final String S_USE_ARROW_SKILL = "[S] S_UseArrowSkill";
	private static Logger _log = Logger.getLogger(S_UseArrowSkill.class
			.getName());

	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

	private byte[] _byte = null;

	public S_UseArrowSkill(L1Character cha, int targetobj, int spellgfx,
			int x, int y, boolean isHit) {

		int aid = 1;
		// オークアーチャーのみ変更
		if (cha.getTempCharGfx() == 3860) {
			aid = 21;
		}
		writeC(Opcodes.S_OPCODE_ATTACKPACKET);
		writeC(aid);
		writeD(cha.getId());
		writeD(targetobj);
		writeC(isHit ? 6 : 0);
		writeC(cha.getHeading());
		// writeD(0x12000000);
		// writeD(246);
		writeD(_sequentialNumber.incrementAndGet());
		writeH(spellgfx);
		writeC(127); // スキル使用時の光源の広さ？
		writeH(cha.getX());
		writeH(cha.getY());
		writeH(x);
		writeH(y);
		// writeC(228);
		// writeC(231);
		// writeC(95);
		// writeC(82);
		// writeC(170);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		} else {
			int seq = _sequentialNumber.incrementAndGet();
			_byte[12] = (byte) (seq & 0xff);
			_byte[13] = (byte) (seq >> 8 & 0xff);
			_byte[14] = (byte) (seq >> 16 & 0xff);
			_byte[15] = (byte) (seq >> 24 & 0xff);
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_USE_ARROW_SKILL;
	}

}
