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
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

public class NPCTalkDataTable {

	private static Logger _log = Logger.getLogger(NPCTalkDataTable.class.getName());

	private static NPCTalkDataTable _instance;

	private Map<Integer, L1NpcTalkData> _datatable = Maps.newMap();

	public static NPCTalkDataTable getInstance() {
		if (_instance == null) {
			_instance = new NPCTalkDataTable();
		}
		return _instance;
	}

	private NPCTalkDataTable() {
		parseList();
	}

	private void parseList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npcaction");

			rs = pstm.executeQuery();
			while (rs.next()) {
				L1NpcTalkData l1npctalkdata = new L1NpcTalkData();
				l1npctalkdata.setNpcID(rs.getInt(1));
				l1npctalkdata.setNormalAction(rs.getString(2));
				l1npctalkdata.setCaoticAction(rs.getString(3));
				l1npctalkdata.setTeleportURL(rs.getString(4));
				l1npctalkdata.setTeleportURLA(rs.getString(5));
				_datatable.put(new Integer(l1npctalkdata.getNpcID()), l1npctalkdata);
			}
			_log.config("NPCアクションリスト " + _datatable.size() + "件ロード");
		}
		catch (SQLException e) {
			_log.warning("error while creating npc action table " + e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public L1NpcTalkData getTemplate(int i) {
		return _datatable.get(new Integer(i));
	}

}
