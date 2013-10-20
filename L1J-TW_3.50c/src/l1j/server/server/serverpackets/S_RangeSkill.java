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

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_RangeSkill extends ServerBasePacket {

	private static final String S_RANGE_SKILL = "[S] S_RangeSkill";

	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

	private byte[] _byte = null;

	public static final int TYPE_NODIR = 0;

	public static final int TYPE_DIR = 8;

	public S_RangeSkill(L1Character cha, L1Character[] target, int spellgfx, int actionId, int type) {
		buildPacket(cha, target, spellgfx, actionId, type);
	}

	private void buildPacket(L1Character cha, L1Character[] target, int spellgfx, int actionId, int type) {
		writeC(Opcodes.S_OPCODE_RANGESKILLS);
		writeC(actionId);
		writeD(cha.getId());
		writeH(cha.getX());
		writeH(cha.getY());
		if (type == TYPE_NODIR) {
			writeC(cha.getHeading());
		}
		else if (type == TYPE_DIR) {
			int newHeading = calcheading(cha.getX(), cha.getY(), target[0].getX(), target[0].getY());
			cha.setHeading(newHeading);
			writeC(cha.getHeading());
		}
		writeD(_sequentialNumber.incrementAndGet()); // 番号がダブらないように送る。
		writeH(spellgfx);
		writeC(type); // 0:範囲 6:遠距離 8:範囲&遠距離
		writeH(0);
		writeH(target.length);
		for (L1Character element : target) {
			writeD(element.getId());
			writeH(0x20); // 0:ダメージモーションあり 0以外:なし
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	private static int calcheading(int myx, int myy, int tx, int ty) {
		int newheading = 0;
		if ((tx > myx) && (ty > myy)) {
			newheading = 3;
		}
		if ((tx < myx) && (ty < myy)) {
			newheading = 7;
		}
		if ((tx > myx) && (ty == myy)) {
			newheading = 2;
		}
		if ((tx < myx) && (ty == myy)) {
			newheading = 6;
		}
		if ((tx == myx) && (ty < myy)) {
			newheading = 0;
		}
		if ((tx == myx) && (ty > myy)) {
			newheading = 4;
		}
		if ((tx < myx) && (ty > myy)) {
			newheading = 5;
		}
		if ((tx > myx) && (ty < myy)) {
			newheading = 1;
		}
		return newheading;
	}

	@Override
	public String getType() {
		return S_RANGE_SKILL;
	}

}