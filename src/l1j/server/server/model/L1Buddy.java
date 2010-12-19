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

import java.util.LinkedHashMap;
import java.util.Map;

import l1j.server.server.model.Instance.L1PcInstance;

public class L1Buddy {
	private final int _charId;

	private final LinkedHashMap<Integer, String> _buddys = new LinkedHashMap<Integer, String>();

	public L1Buddy(int charId) {
		_charId = charId;
	}

	public int getCharId() {
		return _charId;
	}

	public boolean add(int objId, String name) {
		if (_buddys.containsKey(objId)) {
			return false;
		}
		_buddys.put(objId, name);
		return true;
	}

	public boolean remove(int objId) {
		String result = _buddys.remove(objId);
		return (result != null ? true : false);
	}

	public boolean remove(String name) {
		int id = 0;
		for (Map.Entry<Integer, String> buddy : _buddys.entrySet()) {
			if (name.equalsIgnoreCase(buddy.getValue())) {
				id = buddy.getKey();
				break;
			}
		}
		if (id == 0) {
			return false;
		}
		_buddys.remove(id);
		return true;
	}

	public String getOnlineBuddyListString() {
		String result = new String("");
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (_buddys.containsKey(pc.getId())) {
				result += pc.getName() + " ";
			}
		}
		return result;
	}

	public String getBuddyListString() {
		String result = new String("");
		for (String name : _buddys.values()) {
			result += name + " ";
		}
		return result;
	}

	public boolean containsId(int objId) {
		return _buddys.containsKey(objId);
	}

	public boolean containsName(String name) {
		for (String buddyName : _buddys.values()) {
			if (name.equalsIgnoreCase(buddyName)) {
				return true;
			}
		}
		return false;
	}

	public int size() {
		return _buddys.size();
	}
}
