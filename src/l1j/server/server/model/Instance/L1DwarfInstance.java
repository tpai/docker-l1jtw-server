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

import java.util.logging.Logger;

import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Npc;

public class L1DwarfInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1DwarfInstance.class.getName());

	/**
	 * @param template
	 */
	public L1DwarfInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		L1Attack attack = new L1Attack(pc, this);
		attack.calcHit();
		attack.action();
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		int npcId = getNpcTemplate().get_npcId();
		String htmlid = null;

		if (talking != null) {
			if (npcId == 60028) { // エル
				if (!pc.isElf()) {
					htmlid = "elCE1";
				}
			}

			if (htmlid != null) { // htmlidが指定されている場合
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid));
			}
			else {
				if (pc.getLevel() < 5) {
					pc.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				}
				else {
					pc.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
		}
	}

	@Override
	public void onFinalAction(L1PcInstance pc, String Action) {
		if (Action.equalsIgnoreCase("retrieve")) {
			_log.finest("Retrive items in storage");
		}
		else if (Action.equalsIgnoreCase("retrieve-pledge")) {
			_log.finest("Retrive items in pledge storage");

			if (pc.getClanname().equalsIgnoreCase(" ")) {
				_log.finest("pc isnt in a pledge");
				S_ServerMessage talk = new S_ServerMessage((S_ServerMessage.NO_PLEDGE), Action);
				pc.sendPackets(talk);
			}
			else {
				_log.finest("pc is in a pledge");
			}
		}
	}
}
