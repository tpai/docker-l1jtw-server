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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1BossSpawn;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class BossSpawnTable {
	private static Logger _log = Logger.getLogger(BossSpawnTable.class
			.getName());

	private BossSpawnTable() {
	}

	public static void fillSpawnTable() {

		int spawnCount = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_boss");
			rs = pstm.executeQuery();

			L1BossSpawn spawnDat;
			L1Npc template1;
			while (rs.next()) {
				int npcTemplateId = rs.getInt("npc_id");
				template1 = NpcTable.getInstance().getTemplate(npcTemplateId);

				if (template1 == null) {
					_log.warning("mob data for id:" + npcTemplateId
							+ " missing in npc table");
					spawnDat = null;
				} else {
					spawnDat = new L1BossSpawn(template1);
					spawnDat.setId(rs.getInt("id"));
					spawnDat.setNpcid(npcTemplateId);
					spawnDat.setLocation(rs.getString("location"));
					spawnDat.setCycleType(rs.getString("cycle_type"));
					spawnDat.setAmount(rs.getInt("count"));
					spawnDat.setGroupId(rs.getInt("group_id"));
					spawnDat.setLocX(rs.getInt("locx"));
					spawnDat.setLocY(rs.getInt("locy"));
					spawnDat.setRandomx(rs.getInt("randomx"));
					spawnDat.setRandomy(rs.getInt("randomy"));
					spawnDat.setLocX1(rs.getInt("locx1"));
					spawnDat.setLocY1(rs.getInt("locy1"));
					spawnDat.setLocX2(rs.getInt("locx2"));
					spawnDat.setLocY2(rs.getInt("locy2"));
					spawnDat.setHeading(rs.getInt("heading"));
					spawnDat.setMapId(rs.getShort("mapid"));
					spawnDat.setRespawnScreen(rs.getBoolean("respawn_screen"));
					spawnDat
							.setMovementDistance(rs.getInt("movement_distance"));
					spawnDat.setRest(rs.getBoolean("rest"));
					spawnDat.setSpawnType(rs.getInt("spawn_type"));
					spawnDat.setPercentage(rs.getInt("percentage"));

					spawnDat.setName(template1.get_name());

					// start the spawning
					spawnDat.init();
					spawnCount += spawnDat.getAmount();

				}

			}

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.log(Level.FINE, "総ボスモンスター数 " + spawnCount + "匹");
	}
}
