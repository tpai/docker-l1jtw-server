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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

public class UBTable {
	private static Logger _log = Logger.getLogger(UBTable.class.getName());

	private static UBTable _instance = new UBTable();

	private Map<Integer, L1UltimateBattle> _ub = Maps.newMap();

	public static UBTable getInstance() {
		return _instance;
	}

	private UBTable() {
		loadTable();
	}

	private void loadTable() {

		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ub_settings");
			rs = pstm.executeQuery();

			while (rs.next()) {

				L1UltimateBattle ub = new L1UltimateBattle();
				ub.setUbId(rs.getInt("ub_id"));
				ub.setMapId(rs.getShort("ub_mapid"));
				ub.setLocX1(rs.getInt("ub_area_x1"));
				ub.setLocY1(rs.getInt("ub_area_y1"));
				ub.setLocX2(rs.getInt("ub_area_x2"));
				ub.setLocY2(rs.getInt("ub_area_y2"));
				ub.setMinLevel(rs.getInt("min_lvl"));
				ub.setMaxLevel(rs.getInt("max_lvl"));
				ub.setMaxPlayer(rs.getInt("max_player"));
				ub.setEnterRoyal(rs.getBoolean("enter_royal"));
				ub.setEnterKnight(rs.getBoolean("enter_knight"));
				ub.setEnterMage(rs.getBoolean("enter_mage"));
				ub.setEnterElf(rs.getBoolean("enter_elf"));
				ub.setEnterDarkelf(rs.getBoolean("enter_darkelf"));
				ub.setEnterDragonKnight(rs.getBoolean("enter_dragonknight"));
				ub.setEnterIllusionist(rs.getBoolean("enter_illusionist"));
				ub.setEnterMale(rs.getBoolean("enter_male"));
				ub.setEnterFemale(rs.getBoolean("enter_female"));
				ub.setUsePot(rs.getBoolean("use_pot"));
				ub.setHpr(rs.getInt("hpr_bonus"));
				ub.setMpr(rs.getInt("mpr_bonus"));
				ub.resetLoc();

				_ub.put(ub.getUbId(), ub);
			}
		}
		catch (SQLException e) {
			_log.warning("ubsettings couldnt be initialized:" + e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
		}

		// ub_managers load
		try {
			pstm = con.prepareStatement("SELECT * FROM ub_managers");
			rs = pstm.executeQuery();

			while (rs.next()) {
				L1UltimateBattle ub = getUb(rs.getInt("ub_id"));
				if (ub != null) {
					ub.addManager(rs.getInt("ub_manager_npc_id"));
				}
			}
		}
		catch (SQLException e) {
			_log.warning("ub_managers couldnt be initialized:" + e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
		}

		// ub_times load
		try {
			pstm = con.prepareStatement("SELECT * FROM ub_times");
			rs = pstm.executeQuery();

			while (rs.next()) {
				L1UltimateBattle ub = getUb(rs.getInt("ub_id"));
				if (ub != null) {
					ub.addUbTime(rs.getInt("ub_time"));
				}
			}
		}
		catch (SQLException e) {
			_log.warning("ub_times couldnt be initialized:" + e);
		}
		finally {
			SQLUtil.close(rs, pstm, con);
		}
		_log.config("UBリスト " + _ub.size() + "件ロード");
	}

	public L1UltimateBattle getUb(int ubId) {
		return _ub.get(ubId);
	}

	public Collection<L1UltimateBattle> getAllUb() {
		return Collections.unmodifiableCollection(_ub.values());
	}

	public L1UltimateBattle getUbForNpcId(int npcId) {
		for (L1UltimateBattle ub : _ub.values()) {
			if (ub.containsManager(npcId)) {
				return ub;
			}
		}
		return null;
	}

	/**
	 * 指定されたUBIDに対するパターンの最大数を返す。
	 * 
	 * @param ubId
	 *            調べるUBID。
	 * @return パターンの最大数。
	 */
	public int getMaxPattern(int ubId) {
		int n = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT MAX(pattern) FROM spawnlist_ub WHERE ub_id=?");
			pstm.setInt(1, ubId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				n = rs.getInt(1);
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
		return n;
	}

}
