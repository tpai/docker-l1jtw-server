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
import l1j.server.server.templates.L1MobSkill;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

public class MobSkillTable {

	private static Logger _log = Logger.getLogger(MobSkillTable.class.getName());

	private final boolean _initialized;

	private static MobSkillTable _instance;

	private final Map<Integer, L1MobSkill> _mobskills;

	public static MobSkillTable getInstance() {
		if (_instance == null) {
			_instance = new MobSkillTable();
		}
		return _instance;
	}

	public boolean isInitialized() {
		return _initialized;
	}

	private MobSkillTable() {
		_mobskills = Maps.newMap();
		loadMobSkillData();
		_initialized = true;
	}

	private void loadMobSkillData() {
		Connection con = null;
		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;
		ResultSet rs1 = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT mobid,count(*) as cnt FROM mobskill group by mobid");

			int count = 0;
			int mobid = 0;
			int actNo = 0;

			pstm2 = con.prepareStatement("SELECT * FROM mobskill where mobid = ? order by mobid,actNo");

			for (rs1 = pstm1.executeQuery(); rs1.next();) {
				mobid = rs1.getInt("mobid");
				count = rs1.getInt("cnt");

				ResultSet rs2 = null;

				try {
					pstm2.setInt(1, mobid);
					L1MobSkill mobskill = new L1MobSkill(count);
					mobskill.set_mobid(mobid);

					rs2 = pstm2.executeQuery();
					while (rs2.next()) {
						actNo = rs2.getInt("actNo");
						mobskill.setMobName(rs2.getString("mobname"));
						mobskill.setType(actNo, rs2.getInt("type"));
						mobskill.setTriggerRandom(actNo, rs2.getInt("TriRnd"));
						mobskill.setTriggerHp(actNo, rs2.getInt("TriHp"));
						mobskill.setTriggerCompanionHp(actNo, rs2.getInt("TriCompanionHp"));
						mobskill.setTriggerRange(actNo, rs2.getInt("TriRange"));
						mobskill.setTriggerCount(actNo, rs2.getInt("TriCount"));
						mobskill.setChangeTarget(actNo, rs2.getInt("ChangeTarget"));
						mobskill.setRange(actNo, rs2.getInt("Range"));
						mobskill.setAreaWidth(actNo, rs2.getInt("AreaWidth"));
						mobskill.setAreaHeight(actNo, rs2.getInt("AreaHeight"));
						mobskill.setLeverage(actNo, rs2.getInt("Leverage"));
						mobskill.setSkillId(actNo, rs2.getInt("SkillId"));
						mobskill.setGfxid(actNo, rs2.getInt("Gfxid"));
						mobskill.setActid(actNo, rs2.getInt("Actid"));
						mobskill.setSummon(actNo, rs2.getInt("SummonId"));
						mobskill.setSummonMin(actNo, rs2.getInt("SummonMin"));
						mobskill.setSummonMax(actNo, rs2.getInt("SummonMax"));
						mobskill.setPolyId(actNo, rs2.getInt("PolyId"));
					}

					_mobskills.put(new Integer(mobid), mobskill);
				}
				catch (SQLException e1) {
					_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);

				}
				finally {
					SQLUtil.close(rs2);
				}
			}

		}
		catch (SQLException e2) {
			_log.log(Level.SEVERE, "error while creating mobskill table", e2);

		}
		finally {
			SQLUtil.close(rs1);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
	}

	public L1MobSkill getTemplate(int id) {
		return _mobskills.get(id);
	}
}
