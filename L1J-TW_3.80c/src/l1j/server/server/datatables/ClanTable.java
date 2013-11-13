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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

// Referenced classes of package l1j.server.server:
// IdFactory

public class ClanTable {

	private static Logger _log = Logger.getLogger(ClanTable.class.getName());

	private final Map<Integer, L1Clan> _clans = Maps.newMap();

	private static ClanTable _instance;

	public static ClanTable getInstance() {
		if (_instance == null) {
			_instance = new ClanTable();
		}
		return _instance;
	}

	private ClanTable() {
		{
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM clan_data ORDER BY clan_id");

				rs = pstm.executeQuery();
				while (rs.next()) {
					L1Clan clan = new L1Clan();
					// clan.SetClanId(clanData.getInt(1));
					int clan_id = rs.getInt(1);
					clan.setClanId(clan_id);
					clan.setClanName(rs.getString(2));
					clan.setLeaderId(rs.getInt(3));
					clan.setLeaderName(rs.getString(4));
					clan.setCastleId(rs.getInt(5));
					clan.setHouseId(rs.getInt(6));
					clan.setFoundDate(rs.getTimestamp(7));
					clan.setAnnouncement(rs.getString(8));
					clan.setEmblemId(rs.getInt(9));
					clan.setEmblemStatus(rs.getInt(10));
	
					L1World.getInstance().storeClan(clan);
					_clans.put(clan_id, clan);
				}

			}
			catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}

		Collection<L1Clan> AllClan = L1World.getInstance().getAllClans();
		for (L1Clan clan : AllClan) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT char_name FROM characters WHERE ClanID = ?");
				pstm.setInt(1, clan.getClanId());
				rs = pstm.executeQuery();

				while (rs.next()) {
					clan.addMemberName(rs.getString(1));
				}
			}
			catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}
		// クラン倉庫のロード
		for (L1Clan clan : AllClan) {
			clan.getDwarfForClanInventory().loadItems();
		}
	}

	public L1Clan createClan(L1PcInstance player, String clan_name) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
				return null;
			}
		}
		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setFoundDate(new Timestamp(System.currentTimeMillis()));
		clan.setAnnouncement("");
		clan.setEmblemId(0);
		clan.setEmblemStatus(0);

		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, found_date=?, announcement=?, emblem_id=?, emblem_status=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, clan.getClanName());
			pstm.setInt(3, clan.getLeaderId());
			pstm.setString(4, clan.getLeaderName());
			pstm.setInt(5, clan.getCastleId());
			pstm.setInt(6, clan.getHouseId());
			pstm.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			pstm.setString(8, "");
			pstm.setInt(9, 0);
			pstm.setInt(10, 0);
			pstm.execute();
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);

		player.setClanid(clan.getClanId());
		player.setClanname(clan.getClanName());
		
		/** 授予一般君主權限 或者 聯盟王權限*/
		if(player.getQuest().isEnd(L1Quest.QUEST_LEVEL45)){ // 通過45任務
			player.setClanRank(L1Clan.CLAN_RANK_LEAGUE_PRINCE);
			player.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, L1Clan.CLAN_RANK_LEAGUE_PRINCE, player.getName())); // 你的階級變更為%s
		} else {
			player.setClanRank(L1Clan.CLAN_RANK_PRINCE);
			player.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, L1Clan.CLAN_RANK_PRINCE, player.getName())); // 你的階級變更為%s
		}
		
		clan.addMemberName(player.getName());
		try {
			// DBにキャラクター情報を書き込む
			player.save();
		}
		catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return clan;
	}

	public void updateClan(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET clan_id=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, found_date=?, announcement=?, emblem_id=?, emblem_status=? WHERE clan_name=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setInt(2, clan.getLeaderId());
			pstm.setString(3, clan.getLeaderName());
			pstm.setInt(4, clan.getCastleId());
			pstm.setInt(5, clan.getHouseId());
			pstm.setTimestamp(6, clan.getFoundDate());
			pstm.setString(7, clan.getAnnouncement());
			pstm.setInt(8, clan.getEmblemId());
			pstm.setInt(9, clan.getEmblemStatus());
			pstm.setString(10, clan.getClanName());
			pstm.execute();
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void deleteClan(String clan_name) {
		L1Clan clan = L1World.getInstance().getClan(clan_name);
		if (clan == null) {
			return;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_data WHERE clan_name=?");
			pstm.setString(1, clan_name);
			pstm.execute();
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		clan.getDwarfForClanInventory().clearItems();
		clan.getDwarfForClanInventory().deleteAllItems();

		L1World.getInstance().removeClan(clan);
		_clans.remove(clan.getClanId());
	}

	public L1Clan getTemplate(int clan_id) {
		return _clans.get(clan_id);
	}

}
