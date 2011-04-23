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

import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1DragonSlayer;
import l1j.server.server.model.L1NpcTalkData;
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
		short mapId = 0;
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(npcid);

		if (talking != null) {
			if (npcid >= 81273 && npcid <= 81276) { // 龍之門扉
				switch (npcid) {
					case 81273: // 安塔瑞斯 Antharas
						switch (getPortalNumber()) {
							case 0:
								mapId = 1005;
								break;
							case 1:
								mapId = 1006;
								break;
							case 2:
								mapId = 1007;
								break;
							case 3:
								mapId = 1008;
								break;
							case 4:
								mapId = 1009;
								break;
							case 5:
								mapId = 1010;
								break;
							default:
								break;
						}
						if (getRBplayerCount(mapId) >= 32) {
							player.sendPackets(new S_ServerMessage(1536)); // 參與人員已額滿，目前無法再入場。
						} else if (L1DragonSlayer.getInstance().isSpawnDragon()[getPortalNumber()]) {
							player.sendPackets(new S_ServerMessage(1537)); // 攻略已經開始，目前無法入場。
						} else if (player.hasSkillEffect(EFFECT_BLOODSTAIN_OF_ANTHARAS)) {
							player.sendPackets(new S_ServerMessage(1626)); // 龍之血痕已穿透全身，在血痕的氣味消失之前，無法再進入龍之門扉。
						} else if (mapId != 0) {
							player.setPortalNumber(getPortalNumber());
							L1Teleport.teleport(player, 32599, 32742, mapId, 2, true);
						}
						break;
					case 81274: // 法利昂 Fafurion
						switch (getPortalNumber()) {
							case 6:
								mapId = 1011;
								break;
							case 7:
								mapId = 1012;
								break;
							case 8:
								mapId = 1013;
								break;
							case 9:
								mapId = 1014;
								break;
							case 10:
								mapId = 1015;
								break;
							case 11:
								mapId = 1016;
								break;
							default:
								break;
						}
						if (getRBplayerCount((short) 1005) >= 32) {
							player.sendPackets(new S_ServerMessage(1536)); // 參與人員已額滿，目前無法再入場。
						} else if (L1DragonSlayer.getInstance().isSpawnDragon()[getPortalNumber()]) {
							player.sendPackets(new S_ServerMessage(1537)); // 攻略已經開始，目前無法入場。
						} else if (player.hasSkillEffect(EFFECT_BLOODSTAIN_OF_FAFURION)) {
							player.sendPackets(new S_ServerMessage(1626)); // 龍之血痕已穿透全身，在血痕的氣味消失之前，無法再進入龍之門扉。
						} else if (mapId != 0) {
							player.setPortalNumber(getPortalNumber());
							L1Teleport.teleport(player, 32927, 32741, mapId, 2, true);
						}
						break;
					case 81275: // 未開放 Lindvior
						break;
					case 81276: // 未開放 Valakas
						break;
					default:
						break;
				}	
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
