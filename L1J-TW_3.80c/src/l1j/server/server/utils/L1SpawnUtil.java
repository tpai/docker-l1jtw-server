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
package l1j.server.server.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1DragonSlayer;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.serverpackets.S_ServerMessage;

public class L1SpawnUtil {
	private static Logger _log = Logger.getLogger(L1SpawnUtil.class.getName());

	public static void spawn(L1PcInstance pc, int npcId, int randomRange,
			int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			if (randomRange == 0) {
				npc.getLocation().set(pc.getLocation());
				npc.getLocation().forward(pc.getHeading());
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(pc.getX() + Random.nextInt(randomRange) - Random.nextInt(randomRange));
					npc.setY(pc.getY() + Random.nextInt(randomRange) - Random.nextInt(randomRange));
					if (npc.getMap().isInMap(npc.getLocation())
							&& npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(pc.getLocation());
					npc.getLocation().forward(pc.getHeading());
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(pc.getHeading());
			// 紀錄龍之門扉編號
			if (npc.getNpcId() == 81273) {
				for (int i = 0; i < 6; i++) {
					if (!L1DragonSlayer.getInstance().getPortalNumber()[i]) {
						L1DragonSlayer.getInstance().setPortalNumber(i, true);
						// 重置副本
						L1DragonSlayer.getInstance().resetDragonSlayer(i);
						npc.setPortalNumber(i);
						L1DragonSlayer.getInstance().portalPack()[i] = npc;
						break;
					}
				}
			} else if (npc.getNpcId() == 81274) {
				for (int i = 6; i < 12; i++) {
					if (!L1DragonSlayer.getInstance().getPortalNumber()[i]) {
						L1DragonSlayer.getInstance().setPortalNumber(i, true);
						// 重置副本
						L1DragonSlayer.getInstance().resetDragonSlayer(i);
						npc.setPortalNumber(i);
						L1DragonSlayer.getInstance().portalPack()[i] = npc;
						break;
					}
				}
			}
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			if (npc.getTempCharGfx() == 7548 || npc.getTempCharGfx() == 7550 || npc.getTempCharGfx() == 7552
					|| npc.getTempCharGfx() == 7554 || npc.getTempCharGfx() == 7585 || npc.getTempCharGfx() == 7591) {
				npc.broadcastPacket(new S_NPCPack(npc));
				npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_AxeWalk));
			} else if (npc.getTempCharGfx() == 7539 || npc.getTempCharGfx() == 7557 || npc.getTempCharGfx() == 7558
					|| npc.getTempCharGfx() == 7864 || npc.getTempCharGfx() == 7869 || npc.getTempCharGfx() == 7870) {
				for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(npc, 50)) {
					if (npc.getTempCharGfx() == 7539) {
						_pc.sendPackets(new S_ServerMessage(1570));
					} else if (npc.getTempCharGfx() == 7864) {
						_pc.sendPackets(new S_ServerMessage(1657));
					}
					npc.onPerceive(_pc);
					S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_AxeWalk);
					_pc.sendPackets(gfx);
				}
				npc.npcSleepTime(ActionCodes.ACTION_AxeWalk, L1NpcInstance.ATTACK_SPEED);
			} else if (npc.getTempCharGfx() == 145) { // 史巴托
				npc.setStatus(11);
				npc.broadcastPacket(new S_NPCPack(npc));
				npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Appear));
				npc.setStatus(0);
				npc.broadcastPacket(new S_CharVisualUpdate(npc, npc.getStatus()));
			}

			npc.turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット開始
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc,
						timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
