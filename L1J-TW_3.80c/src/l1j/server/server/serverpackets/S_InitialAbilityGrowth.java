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

/**
 * @category 初始能力加成, 用於查看創腳色時初始能力的增幅
 */
public class S_InitialAbilityGrowth extends ServerBasePacket {
	public S_InitialAbilityGrowth(L1PcInstance pc) {

		int Str = pc.getOriginalStr();// 力量
		int Dex = pc.getOriginalDex();// 敏捷
		int Con = pc.getOriginalCon();// 體質
		int Wis = pc.getOriginalWis();// 精神
		int Cha = pc.getOriginalCha();// 魅力
		int Int = pc.getOriginalInt();// 智力
		int[] growth = new int[6];

		// 王族
		if (pc.isCrown()) {
			int Initial[] = { 13, 10, 10, 11, 13, 10 };
			growth[0] = Str - Initial[0];
			growth[1] = Dex - Initial[1];
			growth[2] = Con - Initial[2];
			growth[3] = Wis - Initial[3];
			growth[4] = Cha - Initial[4];
			growth[5] = Int - Initial[5];
		}
		// 法師
		if (pc.isWizard()) {
			int[] Initial = { 8, 7, 12, 12, 8, 12 };
			growth[0] = Str - Initial[0];
			growth[1] = Dex - Initial[1];
			growth[2] = Con - Initial[2];
			growth[3] = Wis - Initial[3];
			growth[4] = Cha - Initial[4];
			growth[5] = Int - Initial[5];
		}
		// 騎士
		if (pc.isKnight()) {
			int[] Initial = { 16, 12, 14, 9, 12, 8 };
			growth[0] = Str - Initial[0];
			growth[1] = Dex - Initial[1];
			growth[2] = Con - Initial[2];
			growth[3] = Wis - Initial[3];
			growth[4] = Cha - Initial[4];
			growth[5] = Int - Initial[5];
		}
		// 妖精
		if (pc.isElf()) {
			int[] Initial = { 11, 12, 12, 12, 9, 12 };
			growth[0] = Str - Initial[0];
			growth[1] = Dex - Initial[1];
			growth[2] = Con - Initial[2];
			growth[3] = Wis - Initial[3];
			growth[4] = Cha - Initial[4];
			growth[5] = Int - Initial[5];
		}
		// 黑妖
		if (pc.isDarkelf()) {
			int[] Initial = { 12, 15, 8, 10, 9, 11 };
			growth[0] = Str - Initial[0];
			growth[1] = Dex - Initial[1];
			growth[2] = Con - Initial[2];
			growth[3] = Wis - Initial[3];
			growth[4] = Cha - Initial[4];
			growth[5] = Int - Initial[5];
		}
		// 龍騎士
		if (pc.isDragonKnight()) {
			int[] Initial = { 13, 11, 14, 12, 8, 11 };
			growth[0] = Str - Initial[0];
			growth[1] = Dex - Initial[1];
			growth[2] = Con - Initial[2];
			growth[3] = Wis - Initial[3];
			growth[4] = Cha - Initial[4];
			growth[5] = Int - Initial[5];
		}
		// 幻術師
		if (pc.isIllusionist()) {
			int[] Initial = { 11, 10, 12, 12, 8, 12 };
			growth[0] = Str - Initial[0];
			growth[1] = Dex - Initial[1];
			growth[2] = Con - Initial[2];
			growth[3] = Wis - Initial[3];
			growth[4] = Cha - Initial[4];
			growth[5] = Int - Initial[5];
		}

		buildPacket(pc, growth[0], growth[1], growth[2], growth[3], growth[4], growth[5]);
	}

	/**
	 * 
	 * @param pc
	 *            腳色
	 * @param Str
	 *            力量
	 * @param Dex
	 *            敏捷
	 * @param Con
	 *            體質
	 * @param Wis
	 *            精神
	 * @param Cha
	 *            魅力
	 * @param Int
	 *            智力
	 */
	private void buildPacket(L1PcInstance pc, int Str, int Dex, int Con,int Wis, int Cha, int Int) {
		int write1 = (Int * 16) + Str;
		int write2 = (Dex * 16) + Wis;
		int write3 = (Cha * 16) + Con;
		writeC(Opcodes.S_OPCODE_CHARRESET);
		writeC(0x04);
		writeC(write1);// 智力&力量
		writeC(write2);// 敏捷&精神
		writeC(write3);// 魅力&體質
		writeC(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
