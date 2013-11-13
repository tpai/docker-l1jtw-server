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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanMembersTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharReset;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_ClanAttention;
import l1j.server.server.serverpackets.S_ClanName;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來離開血盟的封包
 */
public class C_LeaveClan extends ClientBasePacket {

	private static final String C_LEAVE_CLAN = "[C] C_LeaveClan";
	private static Logger _log = Logger.getLogger(C_LeaveClan.class.getName());

	public C_LeaveClan(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);
		
		String clan_name = readS();

		L1PcInstance player = clientthread.getActiveChar();
		if (player == null) {
			return;
		}
		
		String player_name = player.getName();
		int clan_id = player.getClanid();
		if (clan_id == 0) {// 還沒加入血盟
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(clan_name);
		if (clan != null) {
			String clan_member_name[] = clan.getAllMembers();
			int i;
			if (player.isCrown() && player.getId() == clan.getLeaderId()) { //是王族而且是連盟王
				int castleId = clan.getCastleId();
				int houseId = clan.getHouseId();
				if (castleId != 0 || houseId != 0) {
					player.sendPackets(new S_ServerMessage(665)); // \f1城やアジトを所有した状態で血盟を解散することはできません。
					return;
				}
				for (L1War war : L1World.getInstance().getWarList()) {
					if (war.CheckClanInWar(clan_name)) {
						player.sendPackets(new S_ServerMessage(302)); // \f1解散させることができません。
						return;
					}
				}

				for (i = 0; i < clan_member_name.length; i++) { // 取得所有血盟成員
					L1PcInstance online_pc = L1World.getInstance().getPlayer(clan_member_name[i]);
					if (online_pc != null) { // 在線上的血盟成員
						online_pc.sendPackets(new S_ClanAttention());
						online_pc.sendPackets(new S_ServerMessage(269, player_name, clan_name)); // 血盟的盟主%0%s解散了血盟
						online_pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, 0x0b, ""));
						online_pc.sendPackets(new S_CharReset(online_pc.getId(), 0));
						online_pc.sendPackets(new S_ClanName(online_pc, false));
						online_pc.sendPackets(new S_ClanAttention());
						online_pc.setClanid(0);
						online_pc.setClanname("");
						online_pc.setClanRank(0);
						online_pc.setClanMemberId(0);
						online_pc.setClanMemberNotes("");
						online_pc.setTitle("");
						online_pc.sendPackets(new S_CharTitle(online_pc.getId(), ""));
						online_pc.broadcastPacket(new S_CharTitle(online_pc.getId(), ""));
						online_pc.broadcastPacket(new S_CharReset(online_pc.getId(), 0));
						online_pc.save(); // 儲存玩家資料到資料庫中
					} else { // 非線上的血盟成員
						try {
							L1PcInstance offline_pc = CharacterTable.getInstance().restoreCharacter(clan_member_name[i]);
							offline_pc.setClanid(0);
							offline_pc.setClanname("");
							offline_pc.setClanRank(0);
							offline_pc.setTitle("");
							offline_pc.save(); // 儲存玩家資料到資料庫中
						} catch (Exception e) {
							_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
						}
					}
				}
				String emblem_file = String.valueOf(clan.getEmblemId());
				File file = new File("emblem/" + emblem_file);
				file.delete();
				ClanTable.getInstance().deleteClan(clan_name);
				ClanMembersTable.getInstance().deleteAllMember(clan.getClanId()); // 刪除所有成員資料
			} else { // 除了聯盟王之外
				L1PcInstance clanMember[] = clan.getOnlineClanMember();
				for (i = 0; i < clanMember.length; i++) {
					clanMember[i].sendPackets(new S_ServerMessage(178, player_name, clan_name)); // \f1%0が%1血盟を脱退しました。
				}
				if (clan.getWarehouseUsingChar() // 血盟成員使用血盟倉庫中
						== player.getId()) {clan.setWarehouseUsingChar(0); // 移除使用血盟倉庫的成員
				}
				player.setClanid(0);
				player.setClanname("");
				player.setClanRank(0);
				player.setClanMemberId(0);
				player.setClanMemberNotes("");
				player.setTitle("");
				player.sendPackets(new S_CharTitle(player.getId(), ""));
				player.broadcastPacket(new S_CharTitle(player.getId(), ""));
				player.save(); // 儲存玩家資料到資料庫中
				player.sendPackets(new S_ClanAttention());
				player.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, 0x0b, ""));
				player.sendPackets(new S_CharReset(player.getId(), 0));
				player.broadcastPacket(new S_CharReset(player.getId(), 0));
				player.sendPackets(new S_ClanName(player, false));
		
				clan.delMemberName(player_name);
				ClanMembersTable.getInstance().deleteMember(player.getId());
			}
		} else {
			player.setClanid(0);
			player.setClanname("");
			player.setClanRank(0);
			player.setClanMemberId(0);
			player.setClanMemberNotes("");
			player.setTitle("");
			player.sendPackets(new S_CharTitle(player.getId(), ""));
			player.broadcastPacket(new S_CharTitle(player.getId(), ""));
			player.sendPackets(new S_CharReset(player.getId(), 0));
			player.broadcastPacket(new S_CharReset(player.getId(), 0));
			player.save(); // 儲存玩家資料到資料庫中
			player.sendPackets(new S_ServerMessage(178, player_name, clan_name)); // \f1%0が%1血盟を脱退しました。
			ClanMembersTable.getInstance().deleteMember(player.getId());
		}
	}

	@Override
	public String getType() {
		return C_LEAVE_CLAN;
	}

}
