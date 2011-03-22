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
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.utils.collections.Lists;

// Referenced classes of package l1j.server.server.model:
// L1MobGroupInfo

public class L1MobGroupInfo {
	private static final Logger _log = Logger.getLogger(L1MobGroupInfo.class.getName());

	private final List<L1NpcInstance> _membersList = Lists.newList();

	private L1NpcInstance _leader;

	public L1MobGroupInfo() {
	}

	public void setLeader(L1NpcInstance npc) {
		_leader = npc;
	}

	public L1NpcInstance getLeader() {
		return _leader;
	}

	public boolean isLeader(L1NpcInstance npc) {
		return npc.getId() == _leader.getId();
	}

	private L1Spawn _spawn;

	public void setSpawn(L1Spawn spawn) {
		_spawn = spawn;
	}

	public L1Spawn getSpawn() {
		return _spawn;
	}

	public void addMember(L1NpcInstance npc) {
		if (npc == null) {
			throw new NullPointerException();
		}

		// 最初のメンバーであればリーダーにする
		if (_membersList.isEmpty()) {
			setLeader(npc);
			// リーダーの再ポップ情報を保存する
			if (npc.isReSpawn()) {
				setSpawn(npc.getSpawn());
			}
		}

		if (!_membersList.contains(npc)) {
			_membersList.add(npc);
		}
		npc.setMobGroupInfo(this);
		npc.setMobGroupId(_leader.getId());
	}

	public synchronized int removeMember(L1NpcInstance npc) {
		if (npc == null) {
			throw new NullPointerException();
		}

		if (_membersList.contains(npc)) {
			_membersList.remove(npc);
		}
		npc.setMobGroupInfo(null);

		// リーダーで他のメンバーがいる場合は、新リーダーにする
		if (isLeader(npc)) {
			if (isRemoveGroup() && (_membersList.size() != 0)) { // リーダーが死亡したらグループ解除する場合
				for (L1NpcInstance minion : _membersList) {
					minion.setMobGroupInfo(null);
					minion.setSpawn(null);
					minion.setreSpawn(false);
				}
				return 0;
			}
			if (_membersList.size() != 0) {
				setLeader(_membersList.get(0));
			}
		}

		// 残りのメンバー数を返す
		return _membersList.size();
	}

	public int getNumOfMembers() {
		return _membersList.size();
	}

	private boolean _isRemoveGroup;

	public boolean isRemoveGroup() {
		return _isRemoveGroup;
	}

	public void setRemoveGroup(boolean flag) {
		_isRemoveGroup = flag;
	}

}
