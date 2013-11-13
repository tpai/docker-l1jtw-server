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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class ChatLogTable {
	private static Logger _log = Logger.getLogger(ChatLogTable.class.getName());

	/*
	 * コード的にはHashMapを利用すべきだが、パフォーマンス上の問題があるかもしれない為、配列で妥協。
	 * HashMapへの変更を検討する場合は、パフォーマンス上問題が無いか十分注意すること。
	 */
	private final boolean[] loggingConfig = new boolean[15];

	private ChatLogTable() {
		loadConfig();
	}

	private void loadConfig() {
		loggingConfig[0] = Config.LOGGING_CHAT_NORMAL;
		loggingConfig[1] = Config.LOGGING_CHAT_WHISPER;
		loggingConfig[2] = Config.LOGGING_CHAT_SHOUT;
		loggingConfig[3] = Config.LOGGING_CHAT_WORLD;
		loggingConfig[4] = Config.LOGGING_CHAT_CLAN;
		loggingConfig[11] = Config.LOGGING_CHAT_PARTY;
		loggingConfig[13] = Config.LOGGING_CHAT_COMBINED;
		loggingConfig[14] = Config.LOGGING_CHAT_CHAT_PARTY;
	}

	private static ChatLogTable _instance;

	public static ChatLogTable getInstance() {
		if (_instance == null) {
			_instance = new ChatLogTable();
		}
		return _instance;
	}

	private boolean isLoggingTarget(int type) {
		return loggingConfig[type];
	}

	public void storeChat(L1PcInstance pc, L1PcInstance target, String text,
			int type) {
		if (!isLoggingTarget(type)) {
			return;
		}

		// type
		// 0:通常チャット
		// 1:Whisper
		// 2:叫び
		// 3:全体チャット
		// 4:血盟チャット
		// 11:パーティチャット
		// 13:連合チャット
		// 14:チャットパーティ
		// 17:血盟王族公告頻道
		Connection con = null;
		PreparedStatement pstm = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			if (target != null) {
				pstm = con.prepareStatement("INSERT INTO log_chat (account_name, char_id, name, clan_id, clan_name, locx, locy, mapid, type, target_account_name, target_id, target_name, target_clan_id, target_clan_name, target_locx, target_locy, target_mapid, content, datetime) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE())");
				pstm.setString(1, pc.getAccountName());
				pstm.setInt(2, pc.getId());
				pstm.setString(3, pc.isGm() ? "******" : pc.getName());
				pstm.setInt(4, pc.getClanid());
				pstm.setString(5, pc.getClanname());
				pstm.setInt(6, pc.getX());
				pstm.setInt(7, pc.getY());
				pstm.setInt(8, pc.getMapId());
				pstm.setInt(9, type);
				pstm.setString(10, target.getAccountName());
				pstm.setInt(11, target.getId());
				pstm.setString(12, target.getName());
				pstm.setInt(13, target.getClanid());
				pstm.setString(14, target.getClanname());
				pstm.setInt(15, target.getX());
				pstm.setInt(16, target.getY());
				pstm.setInt(17, target.getMapId());
				pstm.setString(18, text);
			} else {
				pstm = con.prepareStatement("INSERT INTO log_chat (account_name, char_id, name, clan_id, clan_name, locx, locy, mapid, type, content, datetime) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE())");
				pstm.setString(1, pc.getAccountName());
				pstm.setInt(2, pc.getId());
				pstm.setString(3, pc.isGm() ? "******" : pc.getName());
				pstm.setInt(4, pc.getClanid());
				pstm.setString(5, pc.getClanname());
				pstm.setInt(6, pc.getX());
				pstm.setInt(7, pc.getY());
				pstm.setInt(8, pc.getMapId());
				pstm.setInt(9, type);
				pstm.setString(10, text);
			}
			pstm.execute();

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

}