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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1ArmorSets;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Lists;

public class ArmorSetTable {
	private static Logger _log = Logger.getLogger(ArmorSetTable.class.getName());

	private static ArmorSetTable _instance;

	private final List<L1ArmorSets> _armorSetList = Lists.newList();

	public static ArmorSetTable getInstance() {
		if (_instance == null) {
			_instance = new ArmorSetTable();
		}
		return _instance;
	}

	private ArmorSetTable() {
		load();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM armor_set");
			rs = pstm.executeQuery();
			fillTable(rs);
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating armor_set table", e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void fillTable(ResultSet rs) throws SQLException {
		while (rs.next()) {
			L1ArmorSets as = new L1ArmorSets();
			as.setId(rs.getInt("id"));
			as.setSets(rs.getString("sets"));
			as.setPolyId(rs.getInt("polyid"));
			as.setAc(rs.getInt("ac"));
			as.setHp(rs.getInt("hp"));
			as.setMp(rs.getInt("mp"));
			as.setHpr(rs.getInt("hpr"));
			as.setMpr(rs.getInt("mpr"));
			as.setMr(rs.getInt("mr"));
			as.setStr(rs.getInt("str"));
			as.setDex(rs.getInt("dex"));
			as.setCon(rs.getInt("con"));
			as.setWis(rs.getInt("wis"));
			as.setCha(rs.getInt("cha"));
			as.setIntl(rs.getInt("intl"));
			as.setDefenseWater(rs.getInt("defense_water"));
			as.setDefenseWind(rs.getInt("defense_wind"));
			as.setDefenseFire(rs.getInt("defense_fire"));
			as.setDefenseEarth(rs.getInt("defense_earth"));

			_armorSetList.add(as);
		}
	}

	public L1ArmorSets[] getAllList() {
		return _armorSetList.toArray(new L1ArmorSets[_armorSetList.size()]);
	}

}
