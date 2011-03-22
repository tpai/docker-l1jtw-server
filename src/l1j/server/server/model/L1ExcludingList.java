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

package l1j.server.server.model;

import java.util.List;

import l1j.server.server.utils.collections.Lists;

public class L1ExcludingList {

	private List<String> _nameList = Lists.newList();

	public void add(String name) {
		_nameList.add(name);
	}

	/**
	 * 指定した名前のキャラクターを遮断リストから削除する
	 * 
	 * @param name
	 *            対象のキャラクター名
	 * @return 実際に削除された、クライアントの遮断リスト上のキャラクター名。 指定した名前がリストに見つからなかった場合はnullを返す。
	 */
	public String remove(String name) {
		for (String each : _nameList) {
			if (each.equalsIgnoreCase(name)) {
				_nameList.remove(each);
				return each;
			}
		}
		return null;
	}

	/**
	 * 指定した名前のキャラクターを遮断している場合trueを返す
	 */
	public boolean contains(String name) {
		for (String each : _nameList) {
			if (each.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 遮断リストが上限の16名に達しているかを返す
	 */
	public boolean isFull() {
		return (_nameList.size() >= 16) ? true : false;
	}
}
