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

package l1j.server.server.clientpackets;

import l1j.server.server.ClientThread;
import l1j.server.server.FishingTimeController;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.Random;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來釣魚的封包
 */
public class C_FishClick extends ClientBasePacket {

	private static final String C_FISHCLICK = "[C] C_FishClick";

	private static final int HEADING_TABLE_X[] =
	{ 0, 1, 1, 1, 0, -1, -1, -1 };

	private static final int HEADING_TABLE_Y[] =
	{ -1, -1, 0, 1, 1, 1, 0, -1 };

	public C_FishClick(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		long currentTime = System.currentTimeMillis();
		long time = pc.getFishingTime();

		if ((currentTime < (time + 500)) && (currentTime > (time - 500)) && pc.isFishingReady()) {
			finishFishing(pc);

			int chance = Random.nextInt(200) + 1;
			if (chance < 50) {
				successFishing(pc, 41298, "$5256"); // 25%
			}
			else if (chance < 65) {
				successFishing(pc, 41300, "$5258"); // 7.5%
			}
			else if (chance < 80) {
				successFishing(pc, 41299, "$5257"); // 7.5%
			}
			else if (chance < 90) {
				successFishing(pc, 41296, "$5249"); // 5%
			}
			else if (chance < 100) {
				successFishing(pc, 41297, "$5250"); // 5%
			}
			else if (chance < 105) {
				successFishing(pc, 41301, "$5259"); // 2.5%
			}
			else if (chance < 110) {
				successFishing(pc, 41302, "$5260"); // 2.5%
			}
			else if (chance < 115) {
				successFishing(pc, 41303, "$5261"); // 2.5%
			}
			else if (chance < 120) {
				successFishing(pc, 41304, "$5262"); // 2.5%
			}
			else if (chance < 123) {
				successFishing(pc, 41306, "$5263"); // 1.5%
			}
			else if (chance < 126) {
				successFishing(pc, 41307, "$5265"); // 1.5%
			}
			else if (chance < 129) {
				successFishing(pc, 41305, "$5264"); // 1.5%
			}
			else if (chance < 134) {
				successFishing(pc, 21051, "$5269"); // 2.5%
			}
			else if (chance < 139) {
				successFishing(pc, 21052, "$5270"); // 2.5%
			}
			else if (chance < 144) {
				successFishing(pc, 21053, "$5271"); // 2.5%
			}
			else if (chance < 159) {
				successFishing(pc, 21054, "$5272"); // 2.5%
			}
			else if (chance < 164) {
				successFishing(pc, 21055, "$5273"); // 2.5%
			}
			else if (chance < 169) {
				successFishing(pc, 21056, "$5274"); // 2.5%
			}
			else if (chance < 171) {
				successFishing(pc, 41252, "$5248"); // 1.0%
			}
			else {
				pc.sendPackets(new S_ServerMessage(1136, "")); // 釣魚失敗。
			}
		}
		else {
			finishFishing(pc);
			pc.sendPackets(new S_ServerMessage(1136, "")); // 釣魚失敗。
		}
	}

	private void finishFishing(L1PcInstance pc) {
		pc.setFishingTime(0);
		pc.setFishingReady(false);
		pc.setFishing(false);
		pc.sendPackets(new S_CharVisualUpdate(pc));
		pc.broadcastPacket(new S_CharVisualUpdate(pc));
		FishingTimeController.getInstance().removeMember(pc);
	}

	private void successFishing(L1PcInstance pc, int itemId, String message) {
		L1ItemInstance item = ItemTable.getInstance().createItem(itemId);
		item.startItemOwnerTimer(pc);
		int heading = pc.getHeading();
		int[] loc =
		{ pc.getX(), pc.getY() };
		int[] dropLoc = new int[2];

		dropLoc[0] = loc[0] - HEADING_TABLE_X[heading];
		dropLoc[1] = loc[1] - HEADING_TABLE_Y[heading];

		if (pc.getMap().isPassable(dropLoc[0], dropLoc[1])) {
			L1World.getInstance().getInventory(dropLoc[0], dropLoc[1], pc.getMapId()).storeItem(item);
		}
		else {
			L1World.getInstance().getInventory(loc[0], loc[1], pc.getMapId()).storeItem(item);
		}
		pc.sendPackets(new S_ServerMessage(1185, message)); // 釣魚成功並釣到 %0%o 了。
	}

	@Override
	public String getType() {
		return C_FISHCLICK;
	}
}
