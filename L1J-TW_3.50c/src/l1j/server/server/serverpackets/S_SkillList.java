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

import static l1j.server.server.Opcodes.S_OPCODE_ADDSKILL;
import static l1j.server.server.Opcodes.S_OPCODE_DELSKILL;

import l1j.server.server.templates.L1Skills;

public class S_SkillList extends ServerBasePacket {
	private static final String S_SKILL_LIST = "[S] S_SkillList";

	/*
	 * [Length:40] S -> C 0000 4C 20 FF FF 37 00 00 00 00 00 00 00 00 00 00 00 L
	 * ..7........... 0010 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
	 * ................ 0020 00 00 00 2E EF 67 33 87 .....g3.
	 */
	public S_SkillList(boolean Insert, L1Skills... skills) {
		if (Insert)
			writeC(S_OPCODE_ADDSKILL);
		else
			writeC(S_OPCODE_DELSKILL);

		int[] SkillList = new int[0x20];

		writeC(SkillList.length);

		for (L1Skills skill : skills) {
			int level = skill.getSkillLevel() - 1;

			SkillList[level] |= skill.getId();
		}

		for (int i : SkillList)
			writeC(i);

		writeC(0x00); // 區分用的數值
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SKILL_LIST;
	}
}
