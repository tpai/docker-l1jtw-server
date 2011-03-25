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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Lists;

public class IpTable {

	public static IpTable getInstance() {
		if (_instance == null) {
			_instance = new IpTable();
		}
		return _instance;
	}

	private IpTable() {
		if (!isInitialized) {
			_banip = Lists.newList();
			getIpTable();
		}
	}

	public void banIp(String ip) {
		Connection con = null;
		PreparedStatement pstm = null;

		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO ban_ip SET ip=?");
			pstm.setString(1, ip);
			pstm.execute();
			_banip.add(ip);
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public boolean isBannedIp(String s) {
		return _banip.contains(s);
	}

	public void getIpTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ban_ip");

			rs = pstm.executeQuery();

			while (rs.next()) {
				_banip.add(rs.getString(1));
			}

			isInitialized = true;

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

	public boolean liftBanIp(String ip) {
		boolean ret = false;
		Connection con = null;
		PreparedStatement pstm = null;

		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM ban_ip WHERE ip=?");
			pstm.setString(1, ip);
			pstm.execute();
			ret = _banip.remove(ip);
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return ret;
	}

	private static Logger _log = Logger.getLogger(IpTable.class.getName());

	private static List<String> _banip;

	public static boolean isInitialized;

	private static IpTable _instance;

}
