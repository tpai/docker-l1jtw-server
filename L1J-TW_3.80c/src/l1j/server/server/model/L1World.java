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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.types.Point;
import l1j.server.server.utils.collections.Lists;
import l1j.server.server.utils.collections.Maps;

public class L1World {
	private static Logger _log = Logger.getLogger(L1World.class.getName());

	private final Map<String, L1PcInstance> _allPlayers;

	private final Map<Integer, L1PetInstance> _allPets;

	private final Map<Integer, L1SummonInstance> _allSummons;

	private final Map<Integer, L1Object> _allObjects;

	private final Map<Integer, L1Object>[] _visibleObjects;

	private final List<L1War> _allWars;

	private final Map<String, L1Clan> _allClans;

	private int _weather = 4;

	private boolean _worldChatEnabled = true;

	private boolean _processingContributionTotal = false;

	private static final int MAX_MAP_ID = 10000;

	private static L1World _instance;

	@SuppressWarnings("unchecked")
	private L1World() {
		_allPlayers = Maps.newConcurrentMap(); // 全てのプレイヤー
		_allPets = Maps.newConcurrentMap(); // 全てのペット
		_allSummons = Maps.newConcurrentMap(); // 全てのサモンモンスター
		_allObjects = Maps.newConcurrentMap(); // 全てのオブジェクト(L1ItemInstance入り、L1Inventoryはなし)
		_visibleObjects = new Map[MAX_MAP_ID + 1]; // マップ毎のオブジェクト(L1Inventory入り、L1ItemInstanceはなし)
		_allWars = Lists.newConcurrentList(); // 全ての戦争
		_allClans = Maps.newConcurrentMap(); // 所有的血盟物件 (Online/Offline)

		for (int i = 0; i <= MAX_MAP_ID; i++) {
			_visibleObjects[i] = Maps.newConcurrentMap();
		}
	}

	public static L1World getInstance() {
		if (_instance == null) {
			_instance = new L1World();
		}
		return _instance;
	}

	/**
	 * 全ての状態をクリアする。<br>
	 * デバッグ、テストなどの特殊な目的以外で呼び出してはならない。
	 */
	public void clear() {
		_instance = new L1World();
	}

	public void storeObject(L1Object object) {
		if (object == null) {
			throw new NullPointerException();
		}

		_allObjects.put(object.getId(), object);
		if (object instanceof L1PcInstance) {
			_allPlayers.put(((L1PcInstance) object).getName(), (L1PcInstance) object);
		}
		if (object instanceof L1PetInstance) {
			_allPets.put(object.getId(), (L1PetInstance) object);
		}
		if (object instanceof L1SummonInstance) {
			_allSummons.put(object.getId(), (L1SummonInstance) object);
		}
	}

	public void removeObject(L1Object object) {
		if (object == null) {
			throw new NullPointerException();
		}

		_allObjects.remove(object.getId());
		if (object instanceof L1PcInstance) {
			_allPlayers.remove(((L1PcInstance) object).getName());
		}
		if (object instanceof L1PetInstance) {
			_allPets.remove(object.getId());
		}
		if (object instanceof L1SummonInstance) {
			_allSummons.remove(object.getId());
		}
	}

	public L1Object findObject(int oID) {
		return _allObjects.get(oID);
	}

	// _allObjectsのビュー
	private Collection<L1Object> _allValues;

	public Collection<L1Object> getObject() {
		Collection<L1Object> vs = _allValues;
		return (vs != null) ? vs : (_allValues = Collections.unmodifiableCollection(_allObjects.values()));
	}

	public L1GroundInventory getInventory(int x, int y, short map) {
		int inventoryKey = ((x - 30000) * 10000 + (y - 30000)) * -1; // xyのマイナス値をインベントリキーとして使用

		Object object = _visibleObjects[map].get(inventoryKey);
		if (object == null) {
			return new L1GroundInventory(inventoryKey, x, y, map);
		}
		else {
			return (L1GroundInventory) object;
		}
	}

	public L1GroundInventory getInventory(L1Location loc) {
		return getInventory(loc.getX(), loc.getY(), (short) loc.getMap().getId());
	}

	public void addVisibleObject(L1Object object) {
		if (object.getMapId() <= MAX_MAP_ID) {
			_visibleObjects[object.getMapId()].put(object.getId(), object);
		}
	}

	public void removeVisibleObject(L1Object object) {
		if (object.getMapId() <= MAX_MAP_ID) {
			_visibleObjects[object.getMapId()].remove(object.getId());
		}
	}

	public void moveVisibleObject(L1Object object, int newMap) // set_Mapで新しいMapにするまえに呼ぶこと
	{
		if (object.getMapId() != newMap) {
			if (object.getMapId() <= MAX_MAP_ID) {
				_visibleObjects[object.getMapId()].remove(object.getId());
			}
			if (newMap <= MAX_MAP_ID) {
				_visibleObjects[newMap].put(object.getId(), object);
			}
		}
	}

	private Map<Integer, Integer> createLineMap(Point src, Point target) {
		Map<Integer, Integer> lineMap = Maps.newConcurrentMap();

		/*
		 * http://www2.starcat.ne.jp/~fussy/algo/algo1-1.htmより
		 */
		int E;
		int x;
		int y;
		int key;
		int i;
		int x0 = src.getX();
		int y0 = src.getY();
		int x1 = target.getX();
		int y1 = target.getY();
		int sx = (x1 > x0) ? 1 : -1;
		int dx = (x1 > x0) ? x1 - x0 : x0 - x1;
		int sy = (y1 > y0) ? 1 : -1;
		int dy = (y1 > y0) ? y1 - y0 : y0 - y1;

		x = x0;
		y = y0;
		/* 傾きが1以下の場合 */
		if (dx >= dy) {
			E = -dx;
			for (i = 0; i <= dx; i++) {
				key = (x << 16) + y;
				lineMap.put(key, key);
				x += sx;
				E += 2 * dy;
				if (E >= 0) {
					y += sy;
					E -= 2 * dx;
				}
			}
			/* 傾きが1より大きい場合 */
		}
		else {
			E = -dy;
			for (i = 0; i <= dy; i++) {
				key = (x << 16) + y;
				lineMap.put(key, key);
				y += sy;
				E += 2 * dx;
				if (E >= 0) {
					x += sx;
					E -= 2 * dy;
				}
			}
		}

		return lineMap;
	}

	public List<L1Object> getVisibleLineObjects(L1Object src, L1Object target) {
		Map<Integer, Integer> lineMap = createLineMap(src.getLocation(), target.getLocation());

		int map = target.getMapId();
		List<L1Object> result = Lists.newList();

		if (map <= MAX_MAP_ID) {
			for (L1Object element : _visibleObjects[map].values()) {
				if (element.equals(src)) {
					continue;
				}

				int key = (element.getX() << 16) + element.getY();
				if (lineMap.containsKey(key)) {
					result.add(element);
				}
			}
		}

		return result;
	}

	public List<L1Object> getVisibleBoxObjects(L1Object object, int heading, int width, int height) {
		int x = object.getX();
		int y = object.getY();
		int map = object.getMapId();
		L1Location location = object.getLocation();
		List<L1Object> result = Lists.newList();
		int headingRotate[] =
		{ 6, 7, 0, 1, 2, 3, 4, 5 };
		double cosSita = Math.cos(headingRotate[heading] * Math.PI / 4);
		double sinSita = Math.sin(headingRotate[heading] * Math.PI / 4);

		if (map <= MAX_MAP_ID) {
			for (L1Object element : _visibleObjects[map].values()) {
				if (element.equals(object)) {
					continue;
				}
				if (map != element.getMapId()) {
					continue;
				}

				// 同じ座標に重なっている場合は範囲内とする
				if (location.isSamePoint(element.getLocation())) {
					result.add(element);
					continue;
				}

				int distance = location.getTileLineDistance(element.getLocation());
				// 直線距離が高さ、幅どちらよりも大きい場合、計算するまでもなく範囲外
				if ((distance > height) && (distance > width)) {
					continue;
				}

				// objectの位置を原点とするための座標補正
				int x1 = element.getX() - x;
				int y1 = element.getY() - y;

				// Z軸回転させ角度を0度にする。
				int rotX = (int) Math.round(x1 * cosSita + y1 * sinSita);
				int rotY = (int) Math.round(-x1 * sinSita + y1 * cosSita);

				int xmin = 0;
				int xmax = height;
				int ymin = -width;
				int ymax = width;

				// 奥行きが射程とかみ合わないので直線距離で判定するように変更。
				// if (rotX > xmin && rotX <= xmax && rotY >= ymin && rotY <=
				// ymax) {
				if ((rotX > xmin) && (distance <= xmax) && (rotY >= ymin) && (rotY <= ymax)) {
					result.add(element);
				}
			}
		}

		return result;
	}

	public List<L1Object> getVisibleObjects(L1Object object) {
		return getVisibleObjects(object, -1);
	}

	public List<L1Object> getVisibleObjects(L1Object object, int radius) {
		L1Map map = object.getMap();
		Point pt = object.getLocation();
		List<L1Object> result = Lists.newList();
		if (map.getId() <= MAX_MAP_ID) {
			for (L1Object element : _visibleObjects[map.getId()].values()) {
				if (element.equals(object)) {
					continue;
				}
				if (map != element.getMap()) {
					continue;
				}

				if (radius == -1) {
					if (pt.isInScreen(element.getLocation())) {
						result.add(element);
					}
				}
				else if (radius == 0) {
					if (pt.isSamePoint(element.getLocation())) {
						result.add(element);
					}
				}
				else {
					if (pt.getTileLineDistance(element.getLocation()) <= radius) {
						result.add(element);
					}
				}
			}
		}

		return result;
	}

	public List<L1Object> getVisiblePoint(L1Location loc, int radius) {
		List<L1Object> result = Lists.newList();
		int mapId = loc.getMapId(); // ループ内で呼ぶと重いため

		if (mapId <= MAX_MAP_ID) {
			for (L1Object element : _visibleObjects[mapId].values()) {
				if (mapId != element.getMapId()) {
					continue;
				}

				if (loc.getTileLineDistance(element.getLocation()) <= radius) {
					result.add(element);
				}
			}
		}

		return result;
	}

	public List<L1PcInstance> getVisiblePlayer(L1Object object) {
		return getVisiblePlayer(object, -1);
	}

	public List<L1PcInstance> getVisiblePlayer(L1Object object, int radius) {
		int map = object.getMapId();
		Point pt = object.getLocation();
		List<L1PcInstance> result = Lists.newList();

		for (L1PcInstance element : _allPlayers.values()) {
			if (element.equals(object)) {
				continue;
			}

			if (map != element.getMapId()) {
				continue;
			}

			if (radius == -1) {
				if (pt.isInScreen(element.getLocation())) {
					result.add(element);
				}
			}
			else if (radius == 0) {
				if (pt.isSamePoint(element.getLocation())) {
					result.add(element);
				}
			}
			else {
				if (pt.getTileLineDistance(element.getLocation()) <= radius) {
					result.add(element);
				}
			}
		}
		return result;
	}

	public List<L1PcInstance> getVisiblePlayerExceptTargetSight(L1Object object, L1Object target) {
		int map = object.getMapId();
		Point objectPt = object.getLocation();
		Point targetPt = target.getLocation();
		List<L1PcInstance> result = Lists.newList();

		for (L1PcInstance element : _allPlayers.values()) {
			if (element.equals(object)) {
				continue;
			}

			if (map != element.getMapId()) {
				continue;
			}

			if (Config.PC_RECOGNIZE_RANGE == -1) {
				if (objectPt.isInScreen(element.getLocation())) {
					if (!targetPt.isInScreen(element.getLocation())) {
						result.add(element);
					}
				}
			}
			else {
				if (objectPt.getTileLineDistance(element.getLocation()) <= Config.PC_RECOGNIZE_RANGE) {
					if (targetPt.getTileLineDistance(element.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
						result.add(element);
					}
				}
			}
		}
		return result;
	}

	/**
	 * objectを認識できる範囲にいるプレイヤーを取得する
	 * 
	 * @param object
	 * @return
	 */
	public List<L1PcInstance> getRecognizePlayer(L1Object object) {
		return getVisiblePlayer(object, Config.PC_RECOGNIZE_RANGE);
	}

	// _allPlayersのビュー
	private Collection<L1PcInstance> _allPlayerValues;

	public Collection<L1PcInstance> getAllPlayers() {
		Collection<L1PcInstance> vs = _allPlayerValues;
		return (vs != null) ? vs : (_allPlayerValues = Collections.unmodifiableCollection(_allPlayers.values()));
	}

	/**
	 * ワールド内にいる指定された名前のプレイヤーを取得する。
	 * 
	 * @param name
	 *            - プレイヤー名(小文字・大文字は無視される)
	 * @return 指定された名前のL1PcInstance。該当プレイヤーが存在しない場合はnullを返す。
	 */
	public L1PcInstance getPlayer(String name) {
		if (_allPlayers.containsKey(name)) {
			return _allPlayers.get(name);
		}
		for (L1PcInstance each : getAllPlayers()) {
			if (each.getName().equalsIgnoreCase(name)) {
				return each;
			}
		}
		return null;
	}

	// _allPetsのビュー
	private Collection<L1PetInstance> _allPetValues;

	public Collection<L1PetInstance> getAllPets() {
		Collection<L1PetInstance> vs = _allPetValues;
		return (vs != null) ? vs : (_allPetValues = Collections.unmodifiableCollection(_allPets.values()));
	}

	// _allSummonsのビュー
	private Collection<L1SummonInstance> _allSummonValues;

	public Collection<L1SummonInstance> getAllSummons() {
		Collection<L1SummonInstance> vs = _allSummonValues;
		return (vs != null) ? vs : (_allSummonValues = Collections.unmodifiableCollection(_allSummons.values()));
	}

	public final Map<Integer, L1Object> getAllVisibleObjects() {
		return _allObjects;
	}

	public final Map<Integer, L1Object>[] getVisibleObjects() {
		return _visibleObjects;
	}

	public final Map<Integer, L1Object> getVisibleObjects(int mapId) {
		return _visibleObjects[mapId];
	}

	public Object getRegion(Object object) {
		return null;
	}

	public void addWar(L1War war) {
		if (!_allWars.contains(war)) {
			_allWars.add(war);
		}
	}

	public void removeWar(L1War war) {
		if (_allWars.contains(war)) {
			_allWars.remove(war);
		}
	}

	// _allWarsのビュー
	private List<L1War> _allWarList;

	public List<L1War> getWarList() {
		List<L1War> vs = _allWarList;
		return (vs != null) ? vs : (_allWarList = Collections.unmodifiableList(_allWars));
	}

	public void storeClan(L1Clan clan) {
		L1Clan temp = getClan(clan.getClanName());
		if (temp == null) {
			_allClans.put(clan.getClanName(), clan);
		}
	}

	public void removeClan(L1Clan clan) {
		L1Clan temp = getClan(clan.getClanName());
		if (temp != null) {
			_allClans.remove(clan.getClanName());
		}
	}

	public L1Clan getClan(String clan_name) {
		return _allClans.get(clan_name);
	}

	// _allClansのビュー
	private Collection<L1Clan> _allClanValues;

	public Collection<L1Clan> getAllClans() {
		Collection<L1Clan> vs = _allClanValues;
		return (vs != null) ? vs : (_allClanValues = Collections.unmodifiableCollection(_allClans.values()));
	}

	public void setWeather(int weather) {
		_weather = weather;
	}

	public int getWeather() {
		return _weather;
	}

	public void set_worldChatElabled(boolean flag) {
		_worldChatEnabled = flag;
	}

	public boolean isWorldChatElabled() {
		return _worldChatEnabled;
	}

	public void setProcessingContributionTotal(boolean flag) {
		_processingContributionTotal = flag;
	}

	public boolean isProcessingContributionTotal() {
		return _processingContributionTotal;
	}

	/**
	 * ワールド上に存在する全てのプレイヤーへパケットを送信する。
	 * 
	 * @param packet
	 *            送信するパケットを表すServerBasePacketオブジェクト。
	 */
	public void broadcastPacketToAll(ServerBasePacket packet) {
		_log.finest("players to notify : " + getAllPlayers().size());
		for (L1PcInstance pc : getAllPlayers()) {
			pc.sendPackets(packet);
		}
	}

	/**
	 * ワールド上に存在する全てのプレイヤーへサーバーメッセージを送信する。
	 * 
	 * @param message
	 *            送信するメッセージ
	 */
	public void broadcastServerMessage(String message) {
		broadcastPacketToAll(new S_SystemMessage(message));
	}
}