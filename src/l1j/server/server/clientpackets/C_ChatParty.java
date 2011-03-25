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
package l1j.server.server.clientpackets;

import l1j.server.server.ClientThread;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Party;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來的聊天組隊封包
 */
public class C_ChatParty extends ClientBasePacket {

	private static final String C_CHAT_PARTY = "[C] C_ChatParty";

	public C_ChatParty(byte abyte0[], ClientThread clientthread) {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc.isGhost()) {
			return;
		}

		int type = readC();
		if (type == 0) { // /chatbanish 的命令
			String name = readS();

			if (!pc.isInChatParty()) {
				// 沒有加入聊天組隊
				pc.sendPackets(new S_ServerMessage(425));
				return;
			}
			if (!pc.getChatParty().isLeader(pc)) {
				// 只有隊長可以踢人
				pc.sendPackets(new S_ServerMessage(427));
				return;
			}
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (targetPc == null) {
				// 沒有叫%0的人。
				pc.sendPackets(new S_ServerMessage(109));
				return;
			}
			if (pc.getId() == targetPc.getId()) {
				return;
			}

			for (L1PcInstance member : pc.getChatParty().getMembers()) {
				if (member.getName().toLowerCase().equals(name.toLowerCase())) {
					pc.getChatParty().kickMember(member);
					return;
				}
			}
			// %0%d 不屬於任何隊伍。
			pc.sendPackets(new S_ServerMessage(426, name));
		}
		else if (type == 1) { // /chatoutparty 的命令
			if (pc.isInChatParty()) {
				pc.getChatParty().leaveMember(pc);
			}
		}
		else if (type == 2) { // /chatparty 的命令
			L1ChatParty chatParty = pc.getChatParty();
			if (pc.isInChatParty()) {
				pc.sendPackets(new S_Party("party", pc.getId(), chatParty.getLeader().getName(), chatParty.getMembersNameList()));
			}
			else {
				pc.sendPackets(new S_ServerMessage(425)); // 您並沒有參加任何隊伍。
				// pc.sendPackets(new S_Party("party", pc.getId()));
			}
		}
	}

	@Override
	public String getType() {
		return C_CHAT_PARTY;
	}

}
