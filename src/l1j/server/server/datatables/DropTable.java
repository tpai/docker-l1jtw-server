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

package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Drop;
import l1j.server.server.utils.Random;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Lists;
import l1j.server.server.utils.collections.Maps;

// Referenced classes of package l1j.server.server.templates:
// L1Npc, L1Item, ItemTable

public class DropTable {

	private static final Logger _log = Logger.getLogger(DropTable.class.getName());

	private static DropTable _instance;

	private final Map<Integer, List<L1Drop>> _droplists; // モンスター毎のドロップリスト

	public static DropTable getInstance() {
		if (_instance == null) {
			_instance = new DropTable();
		}
		return _instance;
	}

	private DropTable() {
		_droplists = allDropList();
	}

	private Map<Integer, List<L1Drop>> allDropList() {
		Map<Integer, List<L1Drop>> droplistMap = Maps.newMap();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from droplist");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int mobId = rs.getInt("mobId");
				int itemId = rs.getInt("itemId");
				int min = rs.getInt("min");
				int max = rs.getInt("max");
				int chance = rs.getInt("chance");

				L1Drop drop = new L1Drop(mobId, itemId, min, max, chance);

				List<L1Drop> dropList = droplistMap.get(drop.getMobid());
				if (dropList == null) {
					dropList = Lists.newList();
					droplistMap.put(new Integer(drop.getMobid()), dropList);
				}
				dropList.add(drop);
			}
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return droplistMap;
	}

	// インベントリにドロップを設定
	public void setDrop(L1NpcInstance npc, L1Inventory inventory) {
		// ドロップリストの取得
		int mobId = npc.getNpcTemplate().get_npcId();
		List<L1Drop> dropList = _droplists.get(mobId);
		if (dropList == null) {
			return;
		}

		// レート取得
		double droprate = Config.RATE_DROP_ITEMS;
		if (droprate <= 0) {
			droprate = 0;
		}
		double adenarate = Config.RATE_DROP_ADENA;
		if (adenarate <= 0) {
			adenarate = 0;
		}
		if ((droprate <= 0) && (adenarate <= 0)) {
			return;
		}

		int itemId;
		int itemCount;
		int addCount;
		int randomChance;
		L1ItemInstance item;

		for (L1Drop drop : dropList) {
			// ドロップアイテムの取得
			itemId = drop.getItemid();
			if ((adenarate == 0) && (itemId == L1ItemId.ADENA)) {
				continue; // アデナレート０でドロップがアデナの場合はスルー
			}

			// ドロップチャンス判定
			randomChance = Random.nextInt(0xf4240) + 1;
			double rateOfMapId = MapsTable.getInstance().getDropRate(npc.getMapId());
			double rateOfItem = DropItemTable.getInstance().getDropRate(itemId);
			if ((droprate == 0) || (drop.getChance() * droprate * rateOfMapId * rateOfItem < randomChance)) {
				continue;
			}

			// ドロップ個数を設定
			double amount = DropItemTable.getInstance().getDropAmount(itemId);
			int min = (int) (drop.getMin() * amount);
			int max = (int) (drop.getMax() * amount);

			itemCount = min;
			addCount = max - min + 1;
			if (addCount > 1) {
				itemCount += Random.nextInt(addCount);
			}
			if (itemId == L1ItemId.ADENA) { // ドロップがアデナの場合はアデナレートを掛ける
				itemCount *= adenarate;
			}
			if (itemCount < 0) {
				itemCount = 0;
			}
			if (itemCount > 2000000000) {
				itemCount = 2000000000;
			}

			// アイテムの生成
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(itemCount);

			// アイテム格納
			inventory.storeItem(item);
		}
	}

	// ドロップを分配
	public void dropShare(L1NpcInstance npc, ArrayList acquisitorList, ArrayList hateList) {
		L1Inventory inventory = npc.getInventory();
		if (inventory.getSize() == 0) {
			return;
		}
		if (acquisitorList.size() != hateList.size()) {
			return;
		}
		// ヘイトの合計を取得
		int totalHate = 0;
		L1Character acquisitor;
		for (int i = hateList.size() - 1; i >= 0; i--) {
			acquisitor = (L1Character) acquisitorList.get(i);
			if ((Config.AUTO_LOOT == 2) // オートルーティング２の場合はサモン及びペットは省く
					&& ((acquisitor instanceof L1SummonInstance) || (acquisitor instanceof L1PetInstance))) {
				acquisitorList.remove(i);
				hateList.remove(i);
			}
			else if ((acquisitor != null) && (acquisitor.getMapId() == npc.getMapId())
					&& (acquisitor.getLocation().getTileLineDistance(npc.getLocation()) <= Config.LOOTING_RANGE)) {
				totalHate += (Integer) hateList.get(i);
			}
			else { // nullだったり死んでたり遠かったら排除
				acquisitorList.remove(i);
				hateList.remove(i);
			}
		}

		// ドロップの分配
		L1ItemInstance item;
		L1Inventory targetInventory = null;
		L1PcInstance player;
		L1PcInstance[] partyMember;
		int randomInt;
		int chanceHate;
		int itemId;
		for (int i = inventory.getSize(); i > 0; i--) {
			item = inventory.getItems().get(0);
			itemId = item.getItemId();
			boolean isGround = false;
			if ((item.getItem().getType2() == 0) && (item.getItem().getType() == 2)) { // light系アイテム
				item.setNowLighting(false);
			}

			if (((Config.AUTO_LOOT != 0) || (itemId == L1ItemId.ADENA)) && (totalHate > 0)) { // オートルーティングかアデナで取得者がいる場合
				randomInt = Random.nextInt(totalHate);
				chanceHate = 0;
				for (int j = hateList.size() - 1; j >= 0; j--) {
					chanceHate += (Integer) hateList.get(j);
					if (chanceHate > randomInt) {
						acquisitor = (L1Character) acquisitorList.get(j);
						if ((itemId >= 40131) && (itemId <= 40135)) {
							if (!(acquisitor instanceof L1PcInstance) || (hateList.size() > 1)) {
								targetInventory = null;
								break;
							}
							player = (L1PcInstance) acquisitor;
							if (player.getQuest().get_step(L1Quest.QUEST_LYRA) != 1) {
								targetInventory = null;
								break;
							}
						}
						if (acquisitor.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
							targetInventory = acquisitor.getInventory();
							if (acquisitor instanceof L1PcInstance) {
								player = (L1PcInstance) acquisitor;
								L1ItemInstance l1iteminstance = player.getInventory().findItemId(L1ItemId.ADENA); // 所持アデナをチェック
								if ((l1iteminstance != null) && (l1iteminstance.getCount() > 2000000000)) {
									targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId()); // 持てないので足元に落とす
									isGround = true;
									player.sendPackets(new S_ServerMessage(166, "所持しているアデナ", "2,000,000,000を超過しています。")); // \f1%0が%4%1%3%2
								}
								else {
									if (player.isInParty()) { // パーティの場合
										partyMember = player.getParty().getMembers();
										for (L1PcInstance element : partyMember) {
											element.sendPackets(new S_ServerMessage(813, npc.getName(), item.getLogName(), player.getName()));
										}
									}
									else {
										// ソロの場合
										player.sendPackets(new S_ServerMessage(143, npc.getName(), item.getLogName())); // \f1%0が%1をくれました。
									}
								}
							}
						}
						else {
							targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId()); // 持てないので足元に落とす
							isGround = true;
						}
						break;
					}
				}
			}
			else { // ノンオートルーティング
				List<Integer> dirList = Lists.newList();
				for (int j = 0; j < 8; j++) {
					dirList.add(j);
				}
				int x = 0;
				int y = 0;
				int dir = 0;
				do {
					if (dirList.isEmpty()) {
						x = 0;
						y = 0;
						break;
					}
					randomInt = Random.nextInt(dirList.size());
					dir = dirList.get(randomInt);
					dirList.remove(randomInt);
					switch (dir) {
						case 0:
							x = 0;
							y = -1;
							break;
						case 1:
							x = 1;
							y = -1;
							break;
						case 2:
							x = 1;
							y = 0;
							break;
						case 3:
							x = 1;
							y = 1;
							break;
						case 4:
							x = 0;
							y = 1;
							break;
						case 5:
							x = -1;
							y = 1;
							break;
						case 6:
							x = -1;
							y = 0;
							break;
						case 7:
							x = -1;
							y = -1;
							break;
					}
				}
				while (!npc.getMap().isPassable(npc.getX(), npc.getY(), dir));
				targetInventory = L1World.getInstance().getInventory(npc.getX() + x, npc.getY() + y, npc.getMapId());
				isGround = true;
			}
			if ((itemId >= 40131) && (itemId <= 40135)) {
				if (isGround || (targetInventory == null)) {
					inventory.removeItem(item, item.getCount());
					continue;
				}
			}
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}
		npc.turnOnOffLight();
	}

}