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

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_AddSkill extends ServerBasePacket {
	private static final String S_ADD_SKILL = "[S] S_AddSkill";

	private byte[] _byte = null;

	public S_AddSkill(int level, int id) {
		int ids[] = new int[28];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = 0;
		}
		ids[level] = id;

		boolean hasLevel5to8 = 0 < (ids[4] + ids[5] + ids[6] + ids[7]);
		boolean hasLevel9to10 = 0 < (ids[8] + ids[9]);

		writeC(Opcodes.S_OPCODE_ADDSKILL);
		if (hasLevel5to8 && !hasLevel9to10) {
			writeC(50);
		}
		else if (hasLevel9to10) {
			writeC(100);
		}
		else {
			writeC(32);
		}
		for (int i : ids) {
			writeC(i);
		}
		writeD(0);
		writeD(0);
	}

	public S_AddSkill(int level1, int level2, int level3, int level4, int level5, int level6, int level7, int level8, int level9, int level10,
			int knight, int l2, int de1, int de2, int royal, int l3, int elf1, int elf2, int elf3, int elf4, int elf5, int elf6, int k5, int l5,
			int m5, int n5, int o5, int p5) {
		int i6 = level5 + level6 + level7 + level8;
		int j6 = level9 + level10;
		writeC(Opcodes.S_OPCODE_ADDSKILL);
		if ((i6 > 0) && (j6 == 0)) {
			writeC(50);
		}
		else if (j6 > 0) {
			writeC(100);
		}
		else {
			writeC(32);
		}
		writeC(level1);
		writeC(level2);
		writeC(level3);
		writeC(level4);
		writeC(level5);
		writeC(level6);
		writeC(level7);
		writeC(level8);
		writeC(level9);
		writeC(level10);
		writeC(knight);
		writeC(l2);
		writeC(de1);
		writeC(de2);
		writeC(royal);
		writeC(l3);
		writeC(elf1);
		writeC(elf2);
		writeC(elf3);
		writeC(elf4);
		writeC(elf5);
		writeC(elf6);
		writeC(k5);
		writeC(l5);
		writeC(m5);
		writeC(n5);
		writeC(o5);
		writeC(p5);
		writeD(0);
		writeD(0);
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
		return S_ADD_SKILL;
	}

}
