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
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來離開血盟的封包
 */
public class C_LeaveClan extends ClientBasePacket {

	private static final String C_LEAVE_CLAN = "[C] C_LeaveClan";
	private static Logger _log = Logger.getLogger(C_LeaveClan.class.getName());

	public C_LeaveClan(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if (player == null) {
			return;
		}
		
		String player_name = player.getName();
		String clan_name = player.getClanname();
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
					L1PcInstance online_pc = L1World.getInstance().getPlayer(
							clan_member_name[i]);
					if (online_pc != null) { // 在線上的血盟成員
						online_pc.setClanid(0);
						online_pc.setClanname("");
						online_pc.setClanRank(0);
						online_pc.setTitle("");
						online_pc.sendPackets(new S_CharTitle(online_pc
								.getId(), ""));
						online_pc.broadcastPacket(new S_CharTitle(online_pc
								.getId(), ""));
						online_pc.save(); // 儲存玩家資料到資料庫中
						online_pc.sendPackets(new S_ServerMessage(269,
								player_name, clan_name)); // %1血盟の血盟主%0が血盟を解散させました。
					} else { // 非線上的血盟成員
						try {
							L1PcInstance offline_pc = CharacterTable
									.getInstance().restoreCharacter(
											clan_member_name[i]);
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
				String emblem_file = String.valueOf(clan_id);
				File file = new File("emblem/" + emblem_file);
				file.delete();
				ClanTable.getInstance().deleteClan(clan_name);
			} else { // 除了聯盟主之外
				L1PcInstance clanMember[] = clan.getOnlineClanMember();
				for (i = 0; i < clanMember.length; i++) {
					clanMember[i].sendPackets(new S_ServerMessage(178,
							player_name, clan_name)); // \f1%0が%1血盟を脱退しました。
				}
				if (clan.getWarehouseUsingChar() // 血盟成員使用血盟倉庫中
						== player.getId()) {
					clan.setWarehouseUsingChar(0); // 移除使用血盟倉庫的成員
				}
				player.setClanid(0);
				player.setClanname("");
				player.setClanRank(0);
				player.setTitle("");
				player.sendPackets(new S_CharTitle(player.getId(), ""));
				player.broadcastPacket(new S_CharTitle(player.getId(), ""));
				player.save(); // 儲存玩家資料到資料庫中
				clan.delMemberName(player_name);
			}
		} else {
			player.setClanid(0);
			player.setClanname("");
			player.setClanRank(0);
			player.setTitle("");
			player.sendPackets(new S_CharTitle(player.getId(), ""));
			player.broadcastPacket(new S_CharTitle(player.getId(), ""));
			player.save(); // 儲存玩家資料到資料庫中
			player
					.sendPackets(new S_ServerMessage(178, player_name,
							clan_name)); // \f1%0が%1血盟を脱退しました。
		}
	}

	@Override
	public String getType() {
		return C_LEAVE_CLAN;
	}

}
