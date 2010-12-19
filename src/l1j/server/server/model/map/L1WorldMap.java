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
package l1j.server.server.model.map;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.MapReader;
import l1j.server.server.utils.PerformanceTimer;

public class L1WorldMap {
	private static Logger _log = Logger.getLogger(L1WorldMap.class.getName());

	private static L1WorldMap _instance;
	private Map<Integer, L1Map> _maps;

	public static L1WorldMap getInstance() {
		if (_instance == null) {
			_instance = new L1WorldMap();
		}
		return _instance;
	}

	private L1WorldMap() {
		PerformanceTimer timer = new PerformanceTimer();
		System.out.print("loading map...");

		MapReader in = MapReader.getDefaultReader();

		try {
			_maps = in.read();
			if (_maps == null) {
				throw new RuntimeException("マップの読み込みに失敗");
			}
		} catch (Exception e) {
			// 復帰不能
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

			System.exit(0);
		}

		System.out.println("OK! " + timer.get() + "ms");
	}

	/**
	 * 指定されたマップの情報を保持するL1Mapを返す。
	 * 
	 * @param mapId
	 *            マップID
	 * @return マップ情報を保持する、L1Mapオブジェクト。
	 */
	public L1Map getMap(short mapId) {
		L1Map map = _maps.get((int) mapId);
		if (map == null) { // マップ情報が無い
			map = L1Map.newNull(); // 何もしないMapを返す。
		}
		return map;
	}
}
