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
package l1j.server.server.model.trap;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.storage.TrapStorage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;
import l1j.server.server.utils.collections.Lists;

public class L1MonsterTrap extends L1Trap {
	private static Logger _log = Logger.getLogger(L1MonsterTrap.class.getName());

	private final int _npcId;

	private final int _count;

	private L1Npc _npcTemp = null; // パフォーマンスのためにキャッシュ

	private Constructor<?> _constructor = null; // パフォーマンスのためにキャッシュ

	public L1MonsterTrap(TrapStorage storage) {
		super(storage);

		_npcId = storage.getInt("monsterNpcId");
		_count = storage.getInt("monsterCount");
	}

	private void addListIfPassable(List<Point> list, L1Map map, Point pt) {
		if (map.isPassable(pt)) {
			list.add(pt);
		}
	}

	private List<Point> getSpawnablePoints(L1Location loc, int d) {
		List<Point> result = Lists.newList();
		L1Map m = loc.getMap();
		int x = loc.getX();
		int y = loc.getY();
		// locを中心に、1辺dタイルの正方形を描くPointリストを作る
		for (int i = 0; i < d; i++) {
			addListIfPassable(result, m, new Point(d - i + x, i + y));
			addListIfPassable(result, m, new Point(-(d - i) + x, -i + y));
			addListIfPassable(result, m, new Point(-i + x, d - i + y));
			addListIfPassable(result, m, new Point(i + x, -(d - i) + y));
		}
		return result;
	}

	private Constructor<?> getConstructor(L1Npc npc) throws ClassNotFoundException {
		return Class.forName("l1j.server.server.model.Instance." + npc.getImpl() + "Instance").getConstructors()[0];
	}

	private L1NpcInstance createNpc() throws Exception {
		if (_npcTemp == null) {
			_npcTemp = NpcTable.getInstance().getTemplate(_npcId);
		}
		if (_constructor == null) {
			_constructor = getConstructor(_npcTemp);
		}

		return (L1NpcInstance) _constructor.newInstance(new Object[]
		{ _npcTemp });
	}

	private void spawn(L1Location loc) throws Exception {
		L1NpcInstance npc = createNpc();
		npc.setId(IdFactory.getInstance().nextId());
		npc.getLocation().set(loc);
		npc.setHomeX(loc.getX());
		npc.setHomeY(loc.getY());
		L1World.getInstance().storeObject(npc);
		L1World.getInstance().addVisibleObject(npc);

		npc.onNpcAI();
		npc.turnOnOffLight();
		npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット開始
	}

	@Override
	public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
		sendEffect(trapObj);

		List<Point> points = getSpawnablePoints(trapObj.getLocation(), 5);

		// 沸ける場所が無ければ終了
		if (points.isEmpty()) {
			return;
		}

		try {
			int cnt = 0;
			while (true) {
				for (Point pt : points) {
					spawn(new L1Location(pt, trapObj.getMap()));
					cnt++;
					if (_count <= cnt) {
						return;
					}
				}
			}
		}
		catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
