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
package l1j.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Ress implements L1CommandExecutor {
	private L1Ress() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Ress();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int objid = pc.getId();
			pc.sendPackets(new S_SkillSound(objid, 759));
			pc.broadcastPacket(new S_SkillSound(objid, 759));
			pc.setCurrentHp(pc.getMaxHp());
			pc.setCurrentMp(pc.getMaxMp());
			for (L1PcInstance tg : L1World.getInstance().getVisiblePlayer(pc)) {
				if ((tg.getCurrentHp() == 0) && tg.isDead()) {
					tg.sendPackets(new S_SystemMessage("GM給予了重生。"));
					tg.broadcastPacket(new S_SkillSound(tg.getId(), 3944));
					tg.sendPackets(new S_SkillSound(tg.getId(), 3944));
					// 祝福された 復活スクロールと同じ効果
					tg.setTempID(objid);
					tg.sendPackets(new S_Message_YN(322, "")); // また復活したいですか？（Y/N）
				}
				else {
					tg.sendPackets(new S_SystemMessage("GM給予了治療。"));
					tg.broadcastPacket(new S_SkillSound(tg.getId(), 832));
					tg.sendPackets(new S_SkillSound(tg.getId(), 832));
					tg.setCurrentHp(tg.getMaxHp());
					tg.setCurrentMp(tg.getMaxMp());
				}
			}
		}
		catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " 指令錯誤"));
		}
	}
}
