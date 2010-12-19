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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1HateList {
	private final Map<L1Character, Integer> _hateMap;

	private L1HateList(Map<L1Character, Integer> hateMap) {
		_hateMap = hateMap;
	}

	public L1HateList() {
		/*
		 * ConcurrentHashMapを利用するより、 全てのメソッドを同期する方がメモリ使用量、速度共に優れていた。
		 * 但し、今後このクラスの利用方法が変わった場合、 例えば多くのスレッドから同時に読み出しがかかるようになった場合は、
		 * ConcurrentHashMapを利用した方が良いかもしれない。
		 */
		_hateMap = new HashMap<L1Character, Integer>();
	}

	public synchronized void add(L1Character cha, int hate) {
		if (cha == null) {
			return;
		}
		if (_hateMap.containsKey(cha)) {
			_hateMap.put(cha, _hateMap.get(cha) + hate);
		} else {
			_hateMap.put(cha, hate);
		}
	}

	public synchronized int get(L1Character cha) {
		return _hateMap.get(cha);
	}

	public synchronized boolean containsKey(L1Character cha) {
		return _hateMap.containsKey(cha);
	}

	public synchronized void remove(L1Character cha) {
		_hateMap.remove(cha);
	}

	public synchronized void clear() {
		_hateMap.clear();
	}

	public synchronized boolean isEmpty() {
		return _hateMap.isEmpty();
	}

	public synchronized L1Character getMaxHateCharacter() {
		L1Character cha = null;
		int hate = Integer.MIN_VALUE;

		for (Map.Entry<L1Character, Integer> e : _hateMap.entrySet()) {
			if (hate < e.getValue()) {
				cha = e.getKey();
				hate = e.getValue();
			}
		}
		return cha;
	}

	public synchronized void removeInvalidCharacter(L1NpcInstance npc) {
		ArrayList<L1Character> invalidChars = new ArrayList<L1Character>();
		for (L1Character cha : _hateMap.keySet()) {
			if (cha == null || cha.isDead() || !npc.knownsObject(cha)) {
				invalidChars.add(cha);
			}
		}

		for (L1Character cha : invalidChars) {
			_hateMap.remove(cha);
		}
	}

	public synchronized int getTotalHate() {
		int totalHate = 0;
		for (int hate : _hateMap.values()) {
			totalHate += hate;
		}
		return totalHate;
	}

	public synchronized int getTotalLawfulHate() {
		int totalHate = 0;
		for (Map.Entry<L1Character, Integer> e : _hateMap.entrySet()) {
			if (e.getKey() instanceof L1PcInstance) {
				totalHate += e.getValue();
			}
		}
		return totalHate;
	}

	public synchronized int getPartyHate(L1Party party) {
		int partyHate = 0;

		for (Map.Entry<L1Character, Integer> e : _hateMap.entrySet()) {
			L1PcInstance pc = null;
			if (e.getKey() instanceof L1PcInstance) {
				pc = (L1PcInstance) e.getKey();
			}
			if (e.getKey() instanceof L1NpcInstance) {
				L1Character cha = ((L1NpcInstance) e.getKey()).getMaster();
				if (cha instanceof L1PcInstance) {
					pc = (L1PcInstance) cha;
				}
			}

			if (pc != null && party.isMember(pc)) {
				partyHate += e.getValue();
			}
		}
		return partyHate;
	}

	public synchronized int getPartyLawfulHate(L1Party party) {
		int partyHate = 0;

		for (Map.Entry<L1Character, Integer> e : _hateMap.entrySet()) {
			L1PcInstance pc = null;
			if (e.getKey() instanceof L1PcInstance) {
				pc = (L1PcInstance) e.getKey();
			}

			if (pc != null && party.isMember(pc)) {
				partyHate += e.getValue();
			}
		}
		return partyHate;
	}

	public synchronized L1HateList copy() {
		return new L1HateList(new HashMap<L1Character, Integer>(_hateMap));
	}

	public synchronized Set<Entry<L1Character, Integer>> entrySet() {
		return _hateMap.entrySet();
	}

	public synchronized ArrayList<L1Character> toTargetArrayList() {
		return new ArrayList<L1Character>(_hateMap.keySet());
	}

	public synchronized ArrayList<Integer> toHateArrayList() {
		return new ArrayList<Integer>(_hateMap.values());
	}
}
