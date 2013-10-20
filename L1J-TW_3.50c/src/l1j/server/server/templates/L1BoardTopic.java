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
package l1j.server.server.templates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.TimeInform;
import l1j.server.server.utils.SQLUtil;

public class L1BoardTopic {
	private static Logger _log = Logger.getLogger(L1BoardTopic.class.getName());

	private final int _id;
	private final String _name;
	private final String _date;
	private final String _title;
	private final String _content;

	public int getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public String getDate() {
		return _date;
	}

	public String getTitle() {
		return _title;
	}

	public String getContent() {
		return _content;
	}

	/**
	 * 取得今日時間
	 * 
	 * @return 今日日期 yy/MM/dd
	 */
	private String today() {
		// 年
		String year = Integer.parseInt(TimeInform.getYear(0, -2000)) < 10 ? "0"
				+ TimeInform.getYear(0, -2000) : TimeInform.getYear(0, -2000);
		// 月
		String month = Integer.parseInt(TimeInform.getMonth()) < 10 ? "0"
				+ TimeInform.getMonth() : TimeInform.getMonth();
		// 日
		String day = Integer.parseInt(TimeInform.getDay()) < 10 ? "0"
				+ TimeInform.getDay() : TimeInform.getDay();
		StringBuilder sb = new StringBuilder();
		// 輸出 yy/MM/dd
		sb.append(year).append("/").append(month).append("/").append(day);
		return sb.toString();
	}

	private L1BoardTopic(int id, String name, String title, String content) {
		_id = id;
		_name = name;
		_date = today();
		_title = title;
		_content = content;
	}

	private L1BoardTopic(ResultSet rs) throws SQLException {
		_id = rs.getInt("id");
		_name = rs.getString("name");
		_date = rs.getString("date");
		_title = rs.getString("title");
		_content = rs.getString("content");
	}

	public synchronized static L1BoardTopic create(String name, String title,
			String content) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT max(id) + 1 as newid FROM board");
			rs = pstm1.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			L1BoardTopic topic = new L1BoardTopic(id, name, title, content);

			pstm2 = con.prepareStatement("INSERT INTO board SET id=?, name=?, date=?, title=?, content=?");
			pstm2.setInt(1, topic.getId());
			pstm2.setString(2, topic.getName());
			pstm2.setString(3, topic.getDate());
			pstm2.setString(4, topic.getTitle());
			pstm2.setString(5, topic.getContent());
			pstm2.execute();

			return topic;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
		return null;
	}

	public void delete() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM board WHERE id=?");
			pstm.setInt(1, getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static L1BoardTopic findById(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board WHERE id=?");
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			if (rs.next()) {
				return new L1BoardTopic(rs);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}

	private static PreparedStatement makeIndexStatement(Connection con, int id,
			int limit) throws SQLException {
		PreparedStatement result = null;
		int offset = 1;
		if (id == 0) {
			result = con.prepareStatement("SELECT * FROM board ORDER BY id DESC LIMIT ?");
		} else {
			result = con.prepareStatement("SELECT * FROM board WHERE id < ? ORDER BY id DESC LIMIT ?");
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}

	public static List<L1BoardTopic> index(int limit) {
		return index(0, limit);
	}

	public static List<L1BoardTopic> index(int id, int limit) {
		List<L1BoardTopic> result = new ArrayList<L1BoardTopic>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatement(con, id, limit);
			rs = pstm.executeQuery();
			while (rs.next()) {
				result.add(new L1BoardTopic(rs));
			}
			return result;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
}
