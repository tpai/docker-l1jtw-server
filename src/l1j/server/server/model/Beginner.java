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
package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.model:
// L1PcInstance

public class Beginner {

	private static Logger _log = Logger.getLogger(Beginner.class.getName());

	private static Beginner _instance;

	public static Beginner getInstance() {
		if (_instance == null) {
			_instance = new Beginner();
		}
		return _instance;
	}

	private Beginner() {
	}

	public int GiveItem(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con
					.prepareStatement("SELECT * FROM beginner WHERE activate IN(?,?)");

			pstm1.setString(1, "A");
			if (pc.isCrown()) {
				pstm1.setString(2, "P");
			} else if (pc.isKnight()) {
				pstm1.setString(2, "K");
			} else if (pc.isElf()) {
				pstm1.setString(2, "E");
			} else if (pc.isWizard()) {
				pstm1.setString(2, "W");
			} else if (pc.isDarkelf()) {
				pstm1.setString(2, "D");
			} else if (pc.isDragonKnight()) { // ドラゴンナイト
				pstm1.setString(2, "R");
			} else if (pc.isIllusionist()) { // イリュージョニスト
				pstm1.setString(2, "I");
			} else {
				pstm1.setString(2, "A");// 万が一どれでもなかった場合のエラー回避用
			}
			rs = pstm1.executeQuery();

			while (rs.next()) {
				PreparedStatement pstm2 = null;
				try {
					pstm2 = con
							.prepareStatement("INSERT INTO character_items SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?");
					pstm2.setInt(1, IdFactory.getInstance().nextId());
					pstm2.setInt(2, rs.getInt("item_id"));
					pstm2.setInt(3, pc.getId());
					pstm2.setString(4, rs.getString("item_name"));
					pstm2.setInt(5, rs.getInt("count"));
					pstm2.setInt(6, 0);
					pstm2.setInt(7, rs.getInt("enchantlvl"));
					pstm2.setInt(8, 0);
					pstm2.setInt(9, 0);
					pstm2.setInt(10, rs.getInt("charge_count"));
					pstm2.setInt(11, 0);
					pstm2.setTimestamp(12, null);
					pstm2.setInt(13, 1);
					pstm2.execute();
				} catch (SQLException e2) {
					_log.log(Level.SEVERE, e2.getLocalizedMessage(), e2);
				} finally {
					SQLUtil.close(pstm2);
				}
			}
		} catch (SQLException e1) {
			_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}
		return 0;
	}
}