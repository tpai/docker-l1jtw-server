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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1MobGroup;
import l1j.server.server.templates.L1NpcCount;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Lists;

public class MobGroupTable {
	private static Logger _log = Logger
			.getLogger(MobGroupTable.class.getName());

	private static MobGroupTable _instance;

	private final HashMap<Integer, L1MobGroup> _mobGroupIndex = new HashMap<Integer, L1MobGroup>();

	public static MobGroupTable getInstance() {
		if (_instance == null) {
			_instance = new MobGroupTable();
		}
		return _instance;
	}

	private MobGroupTable() {
		loadMobGroup();
	}

	private void loadMobGroup() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM mobgroup");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int mobGroupId = rs.getInt("id");
				boolean isRemoveGroup = (rs
						.getBoolean("remove_group_if_leader_die"));
				int leaderId = rs.getInt("leader_id");
				List<L1NpcCount> minions = Lists.newArrayList();
				for (int i = 1; i <= 7; i++) {
					int id = rs.getInt("minion" + i + "_id");
					int count = rs.getInt("minion" + i + "_count");
					minions.add(new L1NpcCount(id, count));
				}
				L1MobGroup mobGroup = new L1MobGroup(mobGroupId, leaderId,
						minions, isRemoveGroup);
				_mobGroupIndex.put(mobGroupId, mobGroup);
			}
			_log.config("MOBグループリスト " + _mobGroupIndex.size() + "件ロード");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating mobgroup table", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public L1MobGroup getTemplate(int mobGroupId) {
		return _mobGroupIndex.get(mobGroupId);
	}

}
