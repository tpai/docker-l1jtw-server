/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.server.model.npc.action;

import java.util.List;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1ObjectAmount;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.serverpackets.S_HowManyMake;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.IterableElementList;
import l1j.server.server.utils.collections.Lists;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class L1NpcMakeItemAction extends L1NpcXmlAction {
	private final List<L1ObjectAmount<Integer>> _materials = Lists.newList();

	private final List<L1ObjectAmount<Integer>> _items = Lists.newList();

	private final boolean _isAmountInputable;

	private final L1NpcAction _actionOnSucceed;

	private final L1NpcAction _actionOnFail;

	public L1NpcMakeItemAction(Element element) {
		super(element);

		_isAmountInputable = L1NpcXmlParser.getBoolAttribute(element, "AmountInputable", true);
		NodeList list = element.getChildNodes();
		for (Element elem : new IterableElementList(list)) {
			if (elem.getNodeName().equalsIgnoreCase("Material")) {
				int id = Integer.valueOf(elem.getAttribute("ItemId"));
				int amount = Integer.valueOf(elem.getAttribute("Amount"));
				_materials.add(new L1ObjectAmount<Integer>(id, amount));
				continue;
			}
			if (elem.getNodeName().equalsIgnoreCase("Item")) {
				int id = Integer.valueOf(elem.getAttribute("ItemId"));
				int amount = Integer.valueOf(elem.getAttribute("Amount"));
				_items.add(new L1ObjectAmount<Integer>(id, amount));
				continue;
			}
		}

		if (_items.isEmpty() || _materials.isEmpty()) {
			throw new IllegalArgumentException();
		}

		Element elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Succeed");
		_actionOnSucceed = elem == null ? null : new L1NpcListedAction(elem);
		elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Fail");
		_actionOnFail = elem == null ? null : new L1NpcListedAction(elem);
	}

	private boolean makeItems(L1PcInstance pc, String npcName, int amount) {
		if (amount <= 0) {
			return false;
		}

		boolean isEnoughMaterials = true;
		for (L1ObjectAmount<Integer> material : _materials) {
			if (!pc.getInventory().checkItemNotEquipped(material.getObject(), material.getAmount() * amount)) {
				L1Item temp = ItemTable.getInstance().getTemplate(material.getObject());
				pc.sendPackets(new S_ServerMessage(337, temp.getName() + "("
						+ ((material.getAmount() * amount) - pc.getInventory().countItems(temp.getItemId())) + ")")); // \f1%0が不足しています。
				isEnoughMaterials = false;
			}
		}
		if (!isEnoughMaterials) {
			return false;
		}

		// 容量と重量の計算
		int countToCreate = 0; // アイテムの個数（纏まる物は1個）
		int weight = 0;

		for (L1ObjectAmount<Integer> makingItem : _items) {
			L1Item temp = ItemTable.getInstance().getTemplate(makingItem.getObject());
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(makingItem.getObject())) {
					countToCreate += 1;
				}
			}
			else {
				countToCreate += makingItem.getAmount() * amount;
			}
			weight += temp.getWeight() * (makingItem.getAmount() * amount) / 1000;
		}
		// 容量確認
		if (pc.getInventory().getSize() + countToCreate > 180) {
			pc.sendPackets(new S_ServerMessage(263)); // \f1一人のキャラクターが持って歩けるアイテムは最大180個までです。
			return false;
		}
		// 重量確認
		if (pc.getMaxWeight() < pc.getInventory().getWeight() + weight) {
			pc.sendPackets(new S_ServerMessage(82)); // アイテムが重すぎて、これ以上持てません。
			return false;
		}

		for (L1ObjectAmount<Integer> material : _materials) {
			// 材料消費
			pc.getInventory().consumeItem(material.getObject(), material.getAmount() * amount);
		}

		for (L1ObjectAmount<Integer> makingItem : _items) {
			L1ItemInstance item = pc.getInventory().storeItem(makingItem.getObject(), makingItem.getAmount() * amount);
			if (item != null) {
				String itemName = ItemTable.getInstance().getTemplate(makingItem.getObject()).getName();
				if (makingItem.getAmount() * amount > 1) {
					itemName = itemName + " (" + makingItem.getAmount() * amount + ")";
				}
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName)); // \f1%0が%1をくれました。
			}
		}
		return true;
	}

	/**
	 * 指定されたインベントリ内に、素材が何セットあるか数える
	 */
	private int countNumOfMaterials(L1PcInventory inv) {
		int count = Integer.MAX_VALUE;
		for (L1ObjectAmount<Integer> material : _materials) {
			int numOfSet = inv.countItems(material.getObject()) / material.getAmount();
			count = Math.min(count, numOfSet);
		}
		return count;
	}

	@Override
	public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
		int numOfMaterials = countNumOfMaterials(pc.getInventory());
		if ((1 < numOfMaterials) && _isAmountInputable) {
			pc.sendPackets(new S_HowManyMake(obj.getId(), numOfMaterials, actionName));
			return null;
		}
		return executeWithAmount(actionName, pc, obj, 1);
	}

	@Override
	public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc, L1Object obj, int amount) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1NpcHtml result = null;
		if (makeItems(pc, npc.getNpcTemplate().get_name(), amount)) {
			if (_actionOnSucceed != null) {
				result = _actionOnSucceed.execute(actionName, pc, obj, new byte[0]);
			}
		}
		else {
			if (_actionOnFail != null) {
				result = _actionOnFail.execute(actionName, pc, obj, new byte[0]);
			}
		}
		return result == null ? L1NpcHtml.HTML_CLOSE : result;
	}

}
