package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.utils.SQLUtil;

public class ClanRecommendTable {
	private static Logger _log = Logger.getLogger(ClanRecommendTable.class.getName());

	private static ClanRecommendTable _instance;
	
	public static ClanRecommendTable getInstance() {
		if (_instance == null) {
			_instance = new ClanRecommendTable();
		}
		return _instance;
	}
	
	/**
	 * 血盟推薦 登陸
	 * @param clan_id 血盟 id
	 * @param clan_type 血盟類型 友好/打怪/戰鬥
	 * @param type_message 類型說明文字
	 */
	public void addRecommendRecord(int clan_id, int clan_type, String type_message){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_recommend_record SET clan_id=?, clan_name=?, crown_name=?, clan_type=?, type_message=?");
			L1Clan clan = ClanTable.getInstance().getTemplate(clan_id);
			pstm.setInt(1, clan_id);
			pstm.setString(2, clan.getClanName());
			pstm.setString(3, clan.getLeaderName());
			pstm.setInt(4, clan_type);
			pstm.setString(5, type_message);
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
	 * 血盟推薦 增加一筆申請
	 * @param clan_id 申請的血盟ID
	 * @param char_name 申請玩家名稱
	 */
	public void addRecommendApply(int clan_id, String char_name){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_recommend_apply SET clan_id=?, clan_name=?, char_name=?");
			L1Clan clan = ClanTable.getInstance().getTemplate(clan_id);
			pstm.setInt(1, clan_id);
			pstm.setString(2, clan.getClanName());
			pstm.setString(3, char_name);
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
	 * 更新登錄資料
	 */
	public void updateRecommendRecord(int clan_id, int clan_type, String type_message){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_recommend_record SET clan_name=?, crown_name=?, clan_type=?, type_message=? WHERE clan_id=?");
			L1Clan clan = ClanTable.getInstance().getTemplate(clan_id);
			pstm.setString(1, clan.getClanName());
			pstm.setString(2, clan.getLeaderName());
			pstm.setInt(3, clan_type);
			pstm.setString(4, type_message);
			pstm.setInt(5, clan_id);
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
	 * 刪除血盟推薦申請
	 * @param id 申請ID
	 */
	public void removeRecommendApply(int id){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_recommend_apply WHERE id=?");
			pstm.setInt(1, id);
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
	
	/**
	 * 刪除血盟推薦 登錄
	 * @param clan_id 血盟 id
	 */
	public void removeRecommendRecord(int clan_id){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_recommend_record WHERE clan_id=?");
			pstm.setInt(1, clan_id);
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
	
	/**
	 * 取得申請的玩家名稱
	 * @param index_id
	 * @return
	 */
	public String getApplyPlayerName(int index_id){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String charName = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_recommend_apply WHERE id=?");
			pstm.setInt(1, index_id);
			rs = pstm.executeQuery();
			
			if(rs.first()){
				charName = rs.getString("char_name");
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return charName;
	}
	
	/**
	 * 該血盟是否登錄
	 * @param clan_id
	 * @return
	 */
	public boolean isRecorded(int clan_id){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_recommend_record WHERE clan_id=?");
			pstm.setInt(1, clan_id);
			rs = pstm.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}
	
	/**
	 * 該玩家是否提出申請
	 * @param char_name
	 * @return
	 */
	public boolean isApplied(String char_name){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_recommend_apply WHERE char_name=?");
			pstm.setString(1, char_name);
			rs = pstm.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}
	
	/**
	 * 該血盟是否有人申請加入
	 */
	public boolean isClanApplyByPlayer(int clan_id){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_recommend_apply WHERE clan_id=?");
			pstm.setInt(1, clan_id);
			rs = pstm.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}
	
	/**
	 * 是否對該血盟提出申請
	 * @param clan_id 血盟Id
	 * @return True:False
	 */
	public boolean isApplyForTheClan(int clan_id, String char_name){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_recommend_apply WHERE clan_id=? AND char_name=?");
			pstm.setInt(1, clan_id);
			pstm.setString(2, char_name);
			rs = pstm.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

}
