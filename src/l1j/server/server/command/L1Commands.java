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
package l1j.server.server.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Command;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Lists;

/**
 * 處理 GM 指令
 */
public class L1Commands {
	private static Logger _log = Logger.getLogger(L1Commands.class.getName());

	private static L1Command fromResultSet(ResultSet rs) throws SQLException {
		return new L1Command(rs.getString("name"), rs.getInt("access_level"), rs.getString("class_name"));
	}

	public static L1Command get(String name) {
		/*
		 * 每次為便於調試和實驗，以便讀取數據庫。緩存性能低於理論是微不足道的。
		 */
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM commands WHERE name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return null;
			}
			return fromResultSet(rs);
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, "錯誤的指令", e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return null;
	}

	public static List<L1Command> availableCommandList(int accessLevel) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<L1Command> result = Lists.newList();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM commands WHERE access_level <= ?");
			pstm.setInt(1, accessLevel);
			rs = pstm.executeQuery();
			while (rs.next()) {
				result.add(fromResultSet(rs));
			}
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, "錯誤的指令", e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}
}
