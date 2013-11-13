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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1FurnitureItem;
import l1j.server.server.utils.SQLUtil;

public class FurnitureItemTable {

	private static Logger _log = Logger.getLogger(FurnitureItemTable.class.getName());

	private static FurnitureItemTable _instance;

	private final HashMap<Integer, L1FurnitureItem> _furnishings = new HashMap<Integer, L1FurnitureItem>();

	public static FurnitureItemTable getInstance() {
		if (_instance == null) {
			_instance = new FurnitureItemTable();
		}
		return _instance;
	}

	private FurnitureItemTable() {
		load();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM furniture_item");
			rs = pstm.executeQuery();
			while (rs.next()) {
				L1FurnitureItem furniture = new L1FurnitureItem();
				int itemId = rs.getInt("item_id");
				furniture.setFurnitureItemId(itemId);
				furniture.setFurnitureNpcId(rs.getInt("npc_id"));
				_furnishings.put(new Integer(itemId), furniture);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}

	public L1FurnitureItem getTemplate(int itemId) {
		if (_furnishings.containsKey(itemId)) {
			return _furnishings.get(itemId);
		}
		return null;
	}

}
