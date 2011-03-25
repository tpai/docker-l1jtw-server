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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 收到由客戶端傳來離開組隊的封包
 */
public class C_BanParty extends ClientBasePacket {

	private static final String C_BAN_PARTY = "[C] C_BanParty";

	public C_BanParty(byte decrypt[], ClientThread client) throws Exception {
		super(decrypt);
		String s = readS();

		L1PcInstance player = client.getActiveChar();
		if (!player.getParty().isLeader(player)) {
			// 是組對對長
			player.sendPackets(new S_ServerMessage(427)); // 只有領導者才有驅逐隊伍成員的權力。
			return;
		}

		for (L1PcInstance member : player.getParty().getMembers()) {
			if (member.getName().toLowerCase().equals(s.toLowerCase())) {
				player.getParty().kickMember(member);
				return;
			}
		}

		player.sendPackets(new S_ServerMessage(426, s)); // %0%d 不屬於任何隊伍。
	}

	@Override
	public String getType() {
		return C_BAN_PARTY;
	}

}
