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
package l1j.server.server.model;

import java.util.List;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1Message;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.model:
// L1DeleteItemOnGround

public class L1DeleteItemOnGround {
	private DeleteTimer _deleteTimer;

	private static final Logger _log = Logger
			.getLogger(L1DeleteItemOnGround.class.getName());

	public L1DeleteItemOnGround() {
	}

	private class DeleteTimer implements Runnable {
		public DeleteTimer() {
		}

		@Override
		public void run() {
			L1Message.getInstance();// Locale 多國語系
			int time = Config.ALT_ITEM_DELETION_TIME * 60 * 1000 - 10 * 1000;
			for (;;) {
				try {
					Thread.sleep(time);
				} catch (Exception exception) {
					_log.warning("L1DeleteItemOnGround error: " + exception);
					break;
				}
				L1World.getInstance().broadcastPacketToAll(
						new S_ServerMessage(166, L1Message.onGroundItem,
								L1Message.secondsDelete + "。"));
				try {
					Thread.sleep(10000);
				} catch (Exception exception) {
					_log.warning("L1DeleteItemOnGround error: " + exception);
					break;
				}
				deleteItem();
				L1World.getInstance().broadcastPacketToAll(
						new S_ServerMessage(166, L1Message.onGroundItem,
								L1Message.deleted + "。"));
			}
		}
	}

	public void initialize() {
		if (!Config.ALT_ITEM_DELETION_TYPE.equalsIgnoreCase("auto")) {
			return;
		}

		_deleteTimer = new DeleteTimer();
		GeneralThreadPool.getInstance().execute(_deleteTimer); // タイマー開始
	}

	private void deleteItem() {
		int numOfDeleted = 0;
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (!(obj instanceof L1ItemInstance)) {
				continue;
			}

			L1ItemInstance item = (L1ItemInstance) obj;
			if (item.getX() == 0 && item.getY() == 0) { // 地面上のアイテムではなく、誰かの所有物
				continue;
			}
			if (item.getItem().getItemId() == 40515) { // 精霊の石
				continue;
			}
			if (L1HouseLocation.isInHouse(item.getX(), item.getY(),
					item.getMapId())) { // アジト内
				continue;
			}

			List<L1PcInstance> players = L1World.getInstance()
					.getVisiblePlayer(item, Config.ALT_ITEM_DELETION_RANGE);
			if (players.isEmpty()) { // 指定範囲内にプレイヤーが居なければ削除
				L1Inventory groundInventory = L1World
						.getInstance()
						.getInventory(item.getX(), item.getY(), item.getMapId());
				groundInventory.removeItem(item);
				numOfDeleted++;
			}
		}
		_log.fine("ワールドマップ上のアイテムを自動削除。削除数: " + numOfDeleted);
	}
}
