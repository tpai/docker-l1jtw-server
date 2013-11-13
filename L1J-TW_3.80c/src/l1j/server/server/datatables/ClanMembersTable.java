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
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class ClanMembersTable {
	private static Logger _log = Logger.getLogger(ClanMembersTable.class.getName());

	private static ClanMembersTable _instance;
	
	public static ClanMembersTable getInstance() {
		if (_instance == null) {
			_instance = new ClanMembersTable();
		}
		return _instance;
	}
	
	/**
	 * 寫入新的血盟成員紀錄
	 */
	public void newMember(L1PcInstance pc){
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;
		int nextId = IdFactory.getInstance().nextId();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT * FROM clan_members ORDER BY clan_id");
			rs = pstm1.executeQuery();
			pstm2 = con.prepareStatement("INSERT INTO clan_members SET clan_id=?, index_id=?, char_id=?, char_name=?, notes=?");
			pstm2.setInt(1, pc.getClanid());
			pstm2.setInt(2, nextId);
			pstm2.setInt(3, pc.getId());
			pstm2.setString(4, pc.getName());
			pstm2.setString(5, "");
			pstm2.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
		pc.setClanMemberId(nextId);
	}
	
	/**
	 * 更新血盟成員資料
	 */
	public void updateMember(L1PcInstance pc){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_members SET clan_id=?, index_id=?, char_id=?, char_name=?");
			pstm.setInt(1, pc.getClanid());
			pstm.setInt(2, IdFactory.getInstance().nextId());
			pstm.setInt(3, pc.getId());
			pstm.setString(4, pc.getName());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	/**
	 * 更新血盟成員備註欄位
	 */
	public void updateMemberNotes(L1PcInstance pc, String notes){
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("UPDATE clan_members SET notes=? WHERE char_id=?");
			pstm1.setString(1, notes);
			pstm1.setInt(2, pc.getId());
			pstm1.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}
	}
	
	/**
	 * 刪除血盟成員
	 */
	public void deleteMember(int charId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_members WHERE char_id=?");
			pstm.setInt(1, charId);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	/**
	 * 刪除整個血盟
	 */
	public void deleteAllMember(int clanId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_members WHERE clan_id=?");
			pstm.setInt(1, clanId);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
