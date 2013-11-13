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
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server:
// IdFactory

public class RaceTicketTable {

	private static Logger _log = Logger.getLogger(PetTable.class.getName());

	private static RaceTicketTable _instance;

	private final HashMap<Integer, L1RaceTicket> _tickets = new HashMap<Integer, L1RaceTicket>();

	private int _maxRoundNumber;

	public static RaceTicketTable getInstance() {
		if (_instance == null) {
			_instance = new RaceTicketTable();
		}
		return _instance;
	}

	private RaceTicketTable() {
		load();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM race_ticket");
			int temp = 0;
			rs = pstm.executeQuery();
			while (rs.next()) {
				L1RaceTicket ticket = new L1RaceTicket();
				int itemobjid = rs.getInt(1);
				ticket.set_itemobjid(itemobjid);
				ticket.set_round(rs.getInt(2));
				ticket.set_allotment_percentage(rs.getInt(3));
				ticket.set_victory(rs.getInt(4));
				ticket.set_runner_num(rs.getInt(5));

				if (ticket.get_round() > temp) {
					temp = ticket.get_round();
				}
				_tickets.put(new Integer(itemobjid), ticket);
			}
			_maxRoundNumber = temp;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}

	public void storeNewTiket(L1RaceTicket ticket) {
		// PCのインベントリーが増える場合に実行
		// XXX 呼ばれる前と処理の重複
		if (ticket.get_itemobjid() != 0) {
			_tickets.put(new Integer(ticket.get_itemobjid()), ticket);
		}

		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO race_ticket SET item_obj_id=?,round=?,"
							+ "allotment_percentage=?,victory=?,runner_num=?");
			pstm.setInt(1, ticket.get_itemobjid());
			pstm.setInt(2, ticket.get_round());
			pstm.setDouble(3, ticket.get_allotment_percentage());
			pstm.setInt(4, ticket.get_victory());
			pstm.setInt(5, ticket.get_runner_num());
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}

	public void deleteTicket(int itemobjid) {
		// PCのインベントリーが減少する再に使用
		if (_tickets.containsKey(itemobjid)) {
			_tickets.remove(itemobjid);
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("delete from race_ticket WHERE item_obj_id=?");
			pstm.setInt(1, itemobjid);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void oldTicketDelete(int round) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("delete from race_ticket WHERE item_obj_id=0 and round!=?");
			pstm.setInt(1, round);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void updateTicket(int round, int num, double allotment_percentage) {
		for (L1RaceTicket ticket : getRaceTicketTableList()) {
			if (ticket.get_round() == round && ticket.get_runner_num() == num) {
				ticket.set_victory(1);
				ticket.set_allotment_percentage(allotment_percentage);
			}
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE "
							+ "race_ticket SET victory=? ,allotment_percentage=? WHERE round=? and runner_num=?");

			pstm.setInt(1, 1);
			pstm.setDouble(2, allotment_percentage);
			pstm.setInt(3, round);
			pstm.setInt(4, num);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public L1RaceTicket getTemplate(int itemobjid) {
		if (_tickets.containsKey(itemobjid)) {
			return _tickets.get(itemobjid);
		}
		return null;
	}

	public L1RaceTicket[] getRaceTicketTableList() {
		return _tickets.values().toArray(new L1RaceTicket[_tickets.size()]);
	}

	public int getRoundNumOfMax() {
		return _maxRoundNumber;
	}
}
