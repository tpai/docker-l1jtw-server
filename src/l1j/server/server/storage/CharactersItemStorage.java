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

package l1j.server.server.storage;

import java.util.List;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.storage.mysql.MySqlCharactersItemStorage;

public abstract class CharactersItemStorage {
	public abstract List<L1ItemInstance> loadItems(int objId) throws Exception;

	public abstract void storeItem(int objId, L1ItemInstance item) throws Exception;

	public abstract void deleteItem(L1ItemInstance item) throws Exception;

	public abstract void updateItemId(L1ItemInstance item) throws Exception;

	public abstract void updateItemCount(L1ItemInstance item) throws Exception;

	public abstract void updateItemIdentified(L1ItemInstance item) throws Exception;

	public abstract void updateItemEquipped(L1ItemInstance item) throws Exception;

	public abstract void updateItemEnchantLevel(L1ItemInstance item) throws Exception;

	public abstract void updateItemDurability(L1ItemInstance item) throws Exception;

	public abstract void updateItemChargeCount(L1ItemInstance item) throws Exception;

	public abstract void updateItemRemainingTime(L1ItemInstance item) throws Exception;

	public abstract void updateItemDelayEffect(L1ItemInstance item) throws Exception;

	public abstract int getItemCount(int objId) throws Exception;

	public abstract void updateItemBless(L1ItemInstance item) throws Exception;

	public abstract void updateItemAttrEnchantKind(L1ItemInstance item) throws Exception;

	public abstract void updateItemAttrEnchantLevel(L1ItemInstance item) throws Exception;

	public static CharactersItemStorage create() {
		if (_instance == null) {
			_instance = new MySqlCharactersItemStorage();
		}
		return _instance;
	}

	private static CharactersItemStorage _instance;
}
