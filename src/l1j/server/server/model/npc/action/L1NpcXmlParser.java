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
package l1j.server.server.model.npc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l1j.server.server.model.L1Quest;
import l1j.server.server.utils.IterableElementList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class L1NpcXmlParser {
	public static List<L1NpcAction> listActions(Element element) {
		List<L1NpcAction> result = new ArrayList<L1NpcAction>();
		NodeList list = element.getChildNodes();
		for (Element elem : new IterableElementList(list)) {
			L1NpcAction action = L1NpcActionFactory.newAction(elem);
			if (action != null) {
				result.add(action);
			}
		}
		return result;
	}

	public static Element getFirstChildElementByTagName(Element element,
			String tagName) {
		IterableElementList list = new IterableElementList(element
				.getElementsByTagName(tagName));
		for (Element elem : list) {
			return elem;
		}
		return null;
	}

	public static int getIntAttribute(Element element, String name,
			int defaultValue) {
		int result = defaultValue;
		try {
			result = Integer.valueOf(element.getAttribute(name));
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public static boolean getBoolAttribute(Element element, String name,
			boolean defaultValue) {
		boolean result = defaultValue;
		String value = element.getAttribute(name);
		if (!value.equals("")) {
			result = Boolean.valueOf(value);
		}
		return result;
	}

	private final static Map<String, Integer> _questIds = new HashMap<String, Integer>();
	static {
		_questIds.put("level15", L1Quest.QUEST_LEVEL15);
		_questIds.put("level30", L1Quest.QUEST_LEVEL30);
		_questIds.put("level45", L1Quest.QUEST_LEVEL45);
		_questIds.put("level50", L1Quest.QUEST_LEVEL50);
		_questIds.put("lyra", L1Quest.QUEST_LYRA);
		_questIds.put("oilskinmant", L1Quest.QUEST_OILSKINMANT);
		_questIds.put("doromond", L1Quest.QUEST_DOROMOND);
		_questIds.put("ruba", L1Quest.QUEST_RUBA);
		_questIds.put("lukein", L1Quest.QUEST_LUKEIN1);
		_questIds.put("tbox1", L1Quest.QUEST_TBOX1);
		_questIds.put("tbox2", L1Quest.QUEST_TBOX2);
		_questIds.put("tbox3", L1Quest.QUEST_TBOX3);
		_questIds.put("cadmus", L1Quest.QUEST_CADMUS);
		_questIds.put("resta", L1Quest.QUEST_RESTA);
		_questIds.put("kamyla", L1Quest.QUEST_KAMYLA);
		_questIds.put("lizard", L1Quest.QUEST_LIZARD);
		_questIds.put("desire", L1Quest.QUEST_DESIRE);
		_questIds.put("shadows", L1Quest.QUEST_SHADOWS);
		_questIds.put("toscroll", L1Quest.QUEST_TOSCROLL);
		_questIds.put("moonoflongbow", L1Quest.QUEST_MOONOFLONGBOW);
		_questIds.put("Generalhamelofresentment", L1Quest
				.QUEST_GENERALHAMELOFRESENTMENT);
	}
	public static int parseQuestId(String questId) {
		if (questId.equals("")) {
			return -1;
		}
		Integer result = _questIds.get(questId.toLowerCase());
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	public static int parseQuestStep(String questStep) {
		if (questStep.equals("")) {
			return -1;
		}
		if (questStep.equalsIgnoreCase("End")) {
			return L1Quest.QUEST_END;
		}
		return Integer.parseInt(questStep);
	}
}
