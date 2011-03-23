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

package l1j.server.server.command.executor;

import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_AddSkill;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;

/**
 * TODO: 翻譯 GM指令：增加魔法
 */
public class L1AddSkill implements L1CommandExecutor {
	private L1AddSkill() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1AddSkill();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int cnt = 0; // 計數器
			String skill_name = ""; // 技能名稱
			int skill_id = 0; // 技能ID

			int object_id = pc.getId(); // キャラクタのobjectidを取得
			pc.sendPackets(new S_SkillSound(object_id, '\343')); // 魔法習得的效果音效
			pc.broadcastPacket(new S_SkillSound(object_id, '\343'));

			if (pc.isCrown()) {
				pc.sendPackets(new S_AddSkill(255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				for (cnt = 1; cnt <= 16; cnt++) // LV1~2魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
				for (cnt = 113; cnt <= 120; cnt++) // プリ魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
			}
			else if (pc.isKnight()) {
				pc.sendPackets(new S_AddSkill(255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 192, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				for (cnt = 1; cnt <= 8; cnt++) // LV1魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
				for (cnt = 87; cnt <= 91; cnt++) // ナイト魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
			}
			else if (pc.isElf()) {
				pc.sendPackets(new S_AddSkill(255, 255, 127, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, 3, 255, 255, 255, 255, 0, 0, 0, 0, 0,
						0));
				for (cnt = 1; cnt <= 48; cnt++) // LV1~6魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
				for (cnt = 129; cnt <= 176; cnt++) // エルフ魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
			}
			else if (pc.isWizard()) {
				pc.sendPackets(new S_AddSkill(255, 255, 127, 255, 255, 255, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				for (cnt = 1; cnt <= 80; cnt++) // LV1~10魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
			}
			else if (pc.isDarkelf()) {
				pc.sendPackets(new S_AddSkill(255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				for (cnt = 1; cnt <= 16; cnt++) // LV1~2魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
				for (cnt = 97; cnt <= 111; cnt++) // DE魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
			}
			else if (pc.isDragonKnight()) {
				pc.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 240, 255, 7, 0, 0, 0));
				for (cnt = 181; cnt <= 195; cnt++) // ドラゴンナイト秘技
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
			}
			else if (pc.isIllusionist()) {
				pc.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 255, 15));
				for (cnt = 201; cnt <= 220; cnt++) // イリュージョニスト魔法
				{
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(cnt); // 技能情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // 寫入DB
				}
			}
		}
		catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " 指令錯誤。"));
		}
	}
}
