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

package l1j.server.server.model.Instance;

import static l1j.server.server.model.skill.L1SkillId.CANCELLATION;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1HauntedHouse;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Npc;

public class L1FieldObjectInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	public L1FieldObjectInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		if (getNpcTemplate().get_npcId() == 81171) { // おばけ屋敷のゴールの炎
			if (L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_PLAYING) {
				int winnersCount = L1HauntedHouse.getInstance().getWinnersCount();
				int goalCount = L1HauntedHouse.getInstance().getGoalCount();
				if (winnersCount == goalCount + 1) {
					L1ItemInstance item = ItemTable.getInstance().createItem(49280); // 勇者のパンプキン袋(銅)
					int count = 1;
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
							item.setCount(count);
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0を手に入れました。
						}
					}
					L1HauntedHouse.getInstance().endHauntedHouse();
				}
				else if (winnersCount > goalCount + 1) {
					L1HauntedHouse.getInstance().setGoalCount(goalCount + 1);
					L1HauntedHouse.getInstance().removeMember(pc);
					L1ItemInstance item = null;
					if (winnersCount == 3) {
						if (goalCount == 1) {
							item = ItemTable.getInstance().createItem(49278); // 勇者のパンプキン袋(金)
						}
						else if (goalCount == 2) {
							item = ItemTable.getInstance().createItem(49279); // 勇者のパンプキン袋(銀)
						}
					}
					else if (winnersCount == 2) {
						item = ItemTable.getInstance().createItem(49279); // 勇者のパンプキン袋(銀)
					}
					int count = 1;
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
							item.setCount(count);
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0を手に入れました。
						}
					}
					L1SkillUse l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);
					L1Teleport.teleport(pc, 32624, 32813, (short) 4, 5, true);
				}
			}
		}
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this));
		}
		removeAllKnownObjects();
	}
}
