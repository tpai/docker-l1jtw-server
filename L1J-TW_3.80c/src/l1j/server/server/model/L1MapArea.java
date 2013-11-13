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

import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.types.Rectangle;

public class L1MapArea extends Rectangle {
	private L1Map _map = L1Map.newNull();

	public L1Map getMap() {
		return _map;
	}

	public void setMap(L1Map map) {
		_map = map;
	}

	public int getMapId() {
		return _map.getId();
	}

	public L1MapArea(int left, int top, int right, int bottom, int mapId) {
		super(left, top, right, bottom);

		_map = L1WorldMap.getInstance().getMap((short) mapId);
	}

	public boolean contains(L1Location loc) {
		return (_map.getId() == loc.getMap().getId()) && super.contains(loc);
	}
}
