package l1j.server.server.model;

import java.util.ArrayList;
import l1j.server.server.utils.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.types.Point;

public class ElementalStoneGenerator implements Runnable {

	private static Logger _log = Logger.getLogger(ElementalStoneGenerator.class
			.getName());

	private static final int ELVEN_FOREST_MAPID = 4;
	private static final int MAX_COUNT = Config.ELEMENTAL_STONE_AMOUNT; // 設置個数
	private static final int INTERVAL = 3; // 設置間隔 秒
	private static final int SLEEP_TIME = 300; // 設置終了後、再設置までのスリープ時間 秒
	private static final int FIRST_X = 32911;
	private static final int FIRST_Y = 32210;
	private static final int LAST_X = 33141;
	private static final int LAST_Y = 32500;
	private static final int ELEMENTAL_STONE_ID = 40515; // 精霊の石

	private ArrayList<L1GroundInventory> _itemList = new ArrayList<L1GroundInventory>(
			MAX_COUNT);

	private static ElementalStoneGenerator _instance = null;

	private ElementalStoneGenerator() {
	}

	public static ElementalStoneGenerator getInstance() {
		if (_instance == null) {
			_instance = new ElementalStoneGenerator();
		}
		return _instance;
	}

	private final L1Object _dummy = new L1Object();

	/**
	 * 指定された位置に石を置けるかを返す。
	 */
	private boolean canPut(L1Location loc) {
		_dummy.setMap(loc.getMap());
		_dummy.setX(loc.getX());
		_dummy.setY(loc.getY());

		// 可視範囲のプレイヤーチェック
		if (L1World.getInstance().getVisiblePlayer(_dummy).size() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 次の設置ポイントを決める。
	 */
	private Point nextPoint() {
		int newX = Random.nextInt(LAST_X - FIRST_X) + FIRST_X;
		int newY = Random.nextInt(LAST_Y - FIRST_Y) + FIRST_Y;

		return new Point(newX, newY);
	}

	/**
	 * 拾われた石をリストから削除する。
	 */
	private void removeItemsPickedUp() {
		for (int i = 0; i < _itemList.size(); i++) {
			L1GroundInventory gInventory = _itemList.get(i);
			if (!gInventory.checkItem(ELEMENTAL_STONE_ID)) {
				_itemList.remove(i);
				i--;
			}
		}
	}

	/**
	 * 指定された位置へ石を置く。
	 */
	private void putElementalStone(L1Location loc) {
		L1GroundInventory gInventory = L1World.getInstance().getInventory(loc);

		L1ItemInstance item = ItemTable.getInstance().createItem(
				ELEMENTAL_STONE_ID);
		item.setEnchantLevel(0);
		item.setCount(1);
		gInventory.storeItem(item);
		_itemList.add(gInventory);
	}

	@Override
	public void run() {
		try {
			L1Map map = L1WorldMap.getInstance().getMap(
					(short) ELVEN_FOREST_MAPID);
			while (true) {
				removeItemsPickedUp();

				while (_itemList.size() < MAX_COUNT) { // 減っている場合セット
					L1Location loc = new L1Location(nextPoint(), map);

					if (!canPut(loc)) {
						// XXX 設置範囲内全てにPCが居た場合無限ループになるが…
						continue;
					}

					putElementalStone(loc);

					Thread.sleep(INTERVAL * 1000); // 一定時間毎に設置
				}
				Thread.sleep(SLEEP_TIME * 1000); // maxまで設置終了後一定時間は再設置しない
			}
		} catch (Throwable e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
