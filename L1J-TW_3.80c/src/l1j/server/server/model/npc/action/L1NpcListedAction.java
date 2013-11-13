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

import java.util.List;

import org.w3c.dom.Element;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtml;

public class L1NpcListedAction extends L1NpcXmlAction {
	private List<L1NpcAction> _actions;

	public L1NpcListedAction(Element element) {
		super(element);
		_actions = L1NpcXmlParser.listActions(element);
	}

	@Override
	public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj,
			byte[] args) {
		L1NpcHtml result = null;
		for (L1NpcAction action : _actions) {
			if (!action.acceptsRequest(actionName, pc, obj)) {
				continue;
			}
			L1NpcHtml r = action.execute(actionName, pc, obj, args);
			if (r != null) {
				result = r;
			}
		}
		return result;
	}

	@Override
	public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc,
			L1Object obj, int amount) {
		L1NpcHtml result = null;
		for (L1NpcAction action : _actions) {
			if (!action.acceptsRequest(actionName, pc, obj)) {
				continue;
			}
			L1NpcHtml r = action.executeWithAmount(actionName, pc, obj, amount);
			if (r != null) {
				result = r;
			}
		}
		return result;
	}
}
