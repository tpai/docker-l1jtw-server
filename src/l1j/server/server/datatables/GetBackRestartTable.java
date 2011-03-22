/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1GetBackRestart;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

// Referenced classes of package l1j.server.server:
// IdFactory

public class GetBackRestartTable {

	private static Logger _log = Logger.getLogger(GetBackRestartTable.class.getName());

	private static GetBackRestartTable _instance;

	private final Map<Integer, L1GetBackRestart> _getbackrestart = Maps.newMap();

	public static GetBackRestartTable getInstance() {
		if (_instance == null) {
			_instance = new GetBackRestartTable();
		}
		return _instance;
	}

	public GetBackRestartTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM getback_restart");
			rs = pstm.executeQuery();
			while (rs.next()) {
				L1GetBackRestart gbr = new L1GetBackRestart();
				int area = rs.getInt("area");
				gbr.setArea(area);
				gbr.setLocX(rs.getInt("locx"));
				gbr.setLocY(rs.getInt("locy"));
				gbr.setMapId(rs.getShort("mapid"));

				_getbackrestart.put(new Integer(area), gbr);
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

	public L1GetBackRestart[] getGetBackRestartTableList() {
		return _getbackrestart.values().toArray(new L1GetBackRestart[_getbackrestart.size()]);
	}

}
