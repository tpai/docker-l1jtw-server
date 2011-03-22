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

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.collections.Lists;

// Referenced classes of package l1j.server.server.model:
// L1ChatParty

public class L1ChatParty {
	private static final Logger _log = Logger.getLogger(L1ChatParty.class.getName());

	private final List<L1PcInstance> _membersList = Lists.newList();

	private L1PcInstance _leader = null;

	public void addMember(L1PcInstance pc) {
		if (pc == null) {
			throw new NullPointerException();
		}
		if (((_membersList.size() == Config.MAX_CHAT_PT) && !_leader.isGm()) || _membersList.contains(pc)) {
			return;
		}

		if (_membersList.isEmpty()) {
			// 最初のPTメンバーであればリーダーにする
			setLeader(pc);
		}

		_membersList.add(pc);
		pc.setChatParty(this);
	}

	private void removeMember(L1PcInstance pc) {
		if (!_membersList.contains(pc)) {
			return;
		}

		_membersList.remove(pc);
		pc.setChatParty(null);
	}

	public boolean isVacancy() {
		return _membersList.size() < Config.MAX_CHAT_PT;
	}

	public int getVacancy() {
		return Config.MAX_CHAT_PT - _membersList.size();
	}

	public boolean isMember(L1PcInstance pc) {
		return _membersList.contains(pc);
	}

	private void setLeader(L1PcInstance pc) {
		_leader = pc;
	}

	public L1PcInstance getLeader() {
		return _leader;
	}

	public boolean isLeader(L1PcInstance pc) {
		return pc.getId() == _leader.getId();
	}

	public String getMembersNameList() {
		String _result = new String("");
		for (L1PcInstance pc : _membersList) {
			_result = _result + pc.getName() + " ";
		}
		return _result;
	}

	private void breakup() {
		L1PcInstance[] members = getMembers();

		for (L1PcInstance member : members) {
			removeMember(member);
			member.sendPackets(new S_ServerMessage(418)); // パーティーを解散しました。
		}
	}

	public void leaveMember(L1PcInstance pc) {
		L1PcInstance[] members = getMembers();
		if (isLeader(pc)) {
			// パーティーリーダーの場合
			breakup();
		}
		else {
			// パーティーリーダーでない場合
			if (getNumOfMembers() == 2) {
				// パーティーメンバーが自分とリーダーのみ
				removeMember(pc);
				L1PcInstance leader = getLeader();
				removeMember(leader);

				sendLeftMessage(pc, pc);
				sendLeftMessage(leader, pc);
			}
			else {
				// 残りのパーティーメンバーが２人以上いる
				removeMember(pc);
				for (L1PcInstance member : members) {
					sendLeftMessage(member, pc);
				}
				sendLeftMessage(pc, pc);
			}
		}
	}

	public void kickMember(L1PcInstance pc) {
		if (getNumOfMembers() == 2) {
			// パーティーメンバーが自分とリーダーのみ
			removeMember(pc);
			L1PcInstance leader = getLeader();
			removeMember(leader);
		}
		else {
			// 残りのパーティーメンバーが２人以上いる
			removeMember(pc);
		}
		pc.sendPackets(new S_ServerMessage(419)); // パーティーから追放されました。
	}

	public L1PcInstance[] getMembers() {
		return _membersList.toArray(new L1PcInstance[_membersList.size()]);
	}

	public int getNumOfMembers() {
		return _membersList.size();
	}

	private void sendLeftMessage(L1PcInstance sendTo, L1PcInstance left) {
		// %0がパーティーから去りました。
		sendTo.sendPackets(new S_ServerMessage(420, left.getName()));
	}

}
