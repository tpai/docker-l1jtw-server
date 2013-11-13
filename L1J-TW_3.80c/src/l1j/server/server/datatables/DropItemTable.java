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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

public final class DropItemTable {
	private class dropItemData {
		public double dropRate = 1;

		public double dropAmount = 1;
	}

	private static Logger _log = Logger.getLogger(DropItemTable.class.getName());

	private static DropItemTable _instance;

	private final Map<Integer, dropItemData> _dropItem = Maps.newMap();

	public static DropItemTable getInstance() {
		if (_instance == null) {
			_instance = new DropItemTable();
		}
		return _instance;
	}

	private DropItemTable() {
		loadMapsFromDatabase();
	}

	private void loadMapsFromDatabase() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM drop_item");

			for (rs = pstm.executeQuery(); rs.next();) {
				dropItemData data = new dropItemData();
				int itemId = rs.getInt("item_id");
				data.dropRate = rs.getDouble("drop_rate");
				data.dropAmount = rs.getDouble("drop_amount");

				_dropItem.put(new Integer(itemId), data);
			}

			_log.config("drop_item " + _dropItem.size());
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

	public double getDropRate(int itemId) {
		dropItemData data = _dropItem.get(itemId);
		if (data == null) {
			return 1;
		}
		return data.dropRate;
	}

	public double getDropAmount(int itemId) {
		dropItemData data = _dropItem.get(itemId);
		if (data == null) {
			return 1;
		}
		return data.dropAmount;
	}

}
