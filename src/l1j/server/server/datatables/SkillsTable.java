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
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

public class SkillsTable {

	private static Logger _log = Logger.getLogger(SkillsTable.class.getName());

	private static SkillsTable _instance;

	private final Map<Integer, L1Skills> _skills = Maps.newMap();

	private final boolean _initialized;

	public static SkillsTable getInstance() {
		if (_instance == null) {
			_instance = new SkillsTable();
		}
		return _instance;
	}

	private SkillsTable() {
		_initialized = true;
		RestoreSkills();
	}

	private void RestoreSkills() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM skills");
			rs = pstm.executeQuery();
			FillSkillsTable(rs);

		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating skills table", e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void FillSkillsTable(ResultSet rs) throws SQLException {

		while (rs.next()) {
			L1Skills l1skills = new L1Skills();
			int skill_id = rs.getInt("skill_id");
			l1skills.setSkillId(skill_id);
			l1skills.setName(rs.getString("name"));
			l1skills.setSkillLevel(rs.getInt("skill_level"));
			l1skills.setSkillNumber(rs.getInt("skill_number"));
			l1skills.setMpConsume(rs.getInt("mpConsume"));
			l1skills.setHpConsume(rs.getInt("hpConsume"));
			l1skills.setItemConsumeId(rs.getInt("itemConsumeId"));
			l1skills.setItemConsumeCount(rs.getInt("itemConsumeCount"));
			l1skills.setReuseDelay(rs.getInt("reuseDelay"));
			l1skills.setBuffDuration(rs.getInt("buffDuration"));
			l1skills.setTarget(rs.getString("target"));
			l1skills.setTargetTo(rs.getInt("target_to"));
			l1skills.setDamageValue(rs.getInt("damage_value"));
			l1skills.setDamageDice(rs.getInt("damage_dice"));
			l1skills.setDamageDiceCount(rs.getInt("damage_dice_count"));
			l1skills.setProbabilityValue(rs.getInt("probability_value"));
			l1skills.setProbabilityDice(rs.getInt("probability_dice"));
			l1skills.setAttr(rs.getInt("attr"));
			l1skills.setType(rs.getInt("type"));
			l1skills.setLawful(rs.getInt("lawful"));
			l1skills.setRanged(rs.getInt("ranged"));
			l1skills.setArea(rs.getInt("area"));
			l1skills.setThrough(rs.getBoolean("through"));
			l1skills.setId(rs.getInt("id"));
			l1skills.setNameId(rs.getString("nameid"));
			l1skills.setActionId(rs.getInt("action_id"));
			l1skills.setCastGfx(rs.getInt("castgfx"));
			l1skills.setCastGfx2(rs.getInt("castgfx2"));
			l1skills.setSysmsgIdHappen(rs.getInt("sysmsgID_happen"));
			l1skills.setSysmsgIdStop(rs.getInt("sysmsgID_stop"));
			l1skills.setSysmsgIdFail(rs.getInt("sysmsgID_fail"));

			_skills.put(new Integer(skill_id), l1skills);
		}
		_log.config("スキル " + _skills.size() + "件ロード");
	}

	public void spellMastery(int playerobjid, int skillid, String skillname, int active, int time) {
		if (spellCheck(playerobjid, skillid)) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) L1World.getInstance().findObject(playerobjid);
		if (pc != null) {
			pc.setSkillMastery(skillid);
		}

		Connection con = null;
		PreparedStatement pstm = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_skills SET char_obj_id=?, skill_id=?, skill_name=?, is_active=?, activetimeleft=?");
			pstm.setInt(1, playerobjid);
			pstm.setInt(2, skillid);
			pstm.setString(3, skillname);
			pstm.setInt(4, active);
			pstm.setInt(5, time);
			pstm.execute();
		}
		catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		}
		finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void spellLost(int playerobjid, int skillid) {
		L1PcInstance pc = (L1PcInstance) L1World.getInstance().findObject(playerobjid);
		if (pc != null) {
			pc.removeSkillMastery(skillid);
		}

		Connection con = null;
		PreparedStatement pstm = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=? AND skill_id=?");
			pstm.setInt(1, playerobjid);
			pstm.setInt(2, skillid);
			pstm.execute();
		}
		catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		}
		finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public boolean spellCheck(int playerobjid, int skillid) {
		boolean ret = false;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=? AND skill_id=?");
			pstm.setInt(1, playerobjid);
			pstm.setInt(2, skillid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				ret = true;
			}
			else {
				ret = false;
			}
		}
		catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return ret;
	}

	public boolean isInitialized() {
		return _initialized;
	}

	public L1Skills getTemplate(int i) {
		return _skills.get(new Integer(i));
	}

}
