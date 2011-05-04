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
package l1j.server.server.model.Instance;

import static l1j.server.server.model.skill.L1SkillId.EFFECT_BLOODSTAIN_OF_ANTHARAS;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_BLOODSTAIN_OF_FAFURION;

import l1j.server.server.model.L1DragonSlayer;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Npc;

public class L1DragonPortalInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param template
	 */
	public L1DragonPortalInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		int npcid = getNpcTemplate().get_npcId();
		int portalNumber = getPortalNumber(); // 龍門編號
		int X = 32599;
		int Y = 32742;
		short mapId = 0;
		if ((npcid >= 81273 && npcid <= 81276) && (portalNumber != -1)) {
			mapId = (short) (1005 + portalNumber); // 地圖判斷 1005 ~ 1016
			if (getRBplayerCount(mapId) >= 32) {
				player.sendPackets(new S_ServerMessage(1536)); // 參與人員已額滿，目前無法再入場。
			} else if (L1DragonSlayer.getInstance().isSpawnDragon()[portalNumber]) {
				player.sendPackets(new S_ServerMessage(1537)); // 攻略已經開始，目前無法入場。
			} else {
				if (portalNumber >= 0 && portalNumber <= 5) { // 安塔瑞斯副本
					if (player.hasSkillEffect(EFFECT_BLOODSTAIN_OF_ANTHARAS)) {
						player.sendPackets(new S_ServerMessage(1626)); // 龍之血痕已穿透全身，在血痕的氣味消失之前，無法再進入龍之門扉。
						return;
					}
				} else if (portalNumber >= 6 && portalNumber <= 11) { // 法利昂副本
					if (player.hasSkillEffect(EFFECT_BLOODSTAIN_OF_FAFURION)) {
						player.sendPackets(new S_ServerMessage(1626)); // 龍之血痕已穿透全身，在血痕的氣味消失之前，無法再進入龍之門扉。
						return;
					}
					X = 32927;
					Y = 32741;
				}
				player.setPortalNumber(portalNumber);
				L1Teleport.teleport(player, X, Y, mapId, 2, true);
			}
		}
	}

	// 計算某地圖內玩家數量
	private int getRBplayerCount(short mapId) {
		int playerCount = 0;
		for (Object obj : L1World.getInstance().getVisibleObjects(mapId)
				.values()) {
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (pc != null) {
				   playerCount++;
				}
			}
		}
		return playerCount;
	}
}
