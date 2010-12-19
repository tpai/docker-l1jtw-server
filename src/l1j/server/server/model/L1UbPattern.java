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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class L1UbPattern {
	private boolean _isFrozen = false;
	private Map<Integer, ArrayList<L1UbSpawn>> _groups = new HashMap<Integer, ArrayList<L1UbSpawn>>();

	public void addSpawn(int groupNumber, L1UbSpawn spawn) {
		if (_isFrozen) {
			return;
		}

		ArrayList<L1UbSpawn> spawnList = _groups.get(groupNumber);
		if (spawnList == null) {
			spawnList = new ArrayList<L1UbSpawn>();
			_groups.put(groupNumber, spawnList);
		}

		spawnList.add(spawn);
	}

	public void freeze() {
		if (_isFrozen) {
			return;
		}

		// 格納されているグループのスポーンリストをID順にソート
		for (ArrayList<L1UbSpawn> spawnList : _groups.values()) {
			Collections.sort(spawnList);
		}

		_isFrozen = true;
	}

	public boolean isFrozen() {
		return _isFrozen;
	}

	public ArrayList<L1UbSpawn> getSpawnList(int groupNumber) {
		if (!_isFrozen) {
			return null;
		}

		return _groups.get(groupNumber);
	}
}
