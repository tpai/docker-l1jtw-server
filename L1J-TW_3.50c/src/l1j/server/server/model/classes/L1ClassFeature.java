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
package l1j.server.server.model.classes;

import l1j.server.server.model.Instance.L1PcInstance;

public abstract class L1ClassFeature {
	public static L1ClassFeature newClassFeature(int classId) {
		if (classId == L1PcInstance.CLASSID_PRINCE
				|| classId == L1PcInstance.CLASSID_PRINCESS) {
			return new L1RoyalClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_ELF_MALE
				|| classId == L1PcInstance.CLASSID_ELF_FEMALE) {
			return new L1ElfClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_KNIGHT_MALE
				|| classId == L1PcInstance.CLASSID_KNIGHT_FEMALE) {
			return new L1KnightClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_WIZARD_MALE
				|| classId == L1PcInstance.CLASSID_WIZARD_FEMALE) {
			return new L1WizardClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_DARK_ELF_MALE
				|| classId == L1PcInstance.CLASSID_DARK_ELF_FEMALE) {
			return new L1DarkElfClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_DRAGON_KNIGHT_MALE
				|| classId == L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE) {
			return new L1DragonKnightClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_ILLUSIONIST_MALE
				|| classId == L1PcInstance.CLASSID_ILLUSIONIST_FEMALE) {
			return new L1IllusionistClassFeature();
		}
		throw new IllegalArgumentException();
	}

	public abstract int getAcDefenseMax(int ac);

	public abstract int getMagicLevel(int playerLevel);
}