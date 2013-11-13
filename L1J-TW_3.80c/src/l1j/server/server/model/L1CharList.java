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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharPacks;
import l1j.server.server.serverpackets.S_CharSynAck;
import l1j.server.server.utils.SQLUtil;

/**
 * 角色列表 
 * 3.70C中，由於不再有登入公告，正式更名。
 * 處理自動登入後的角色列表封包
 */
public class L1CharList {
	private static Logger _log = Logger.getLogger(L1CharList.class.getName());

	public L1CharList(ClientThread client) {
		deleteCharacter(client); // 到達刪除期限，刪除角色
		int amountOfChars = client.getAccount().countCharacters();
		client.sendPacket(new S_CharAmount(amountOfChars, client));
		client.sendPacket(new S_CharSynAck(S_CharSynAck.SYN));
		if (amountOfChars > 0) {
			sendCharPacks(client);
		}
		client.sendPacket(new S_CharSynAck(S_CharSynAck.ACK));
	}

	private void deleteCharacter(ClientThread client) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid");
			pstm.setString(1, client.getAccountName());
			rs = pstm.executeQuery();

			while (rs.next()) {
				String name = rs.getString("char_name");
				String clanname = rs.getString("Clanname");

				Timestamp deleteTime = rs.getTimestamp("DeleteTime");
				if (deleteTime != null) {
					Calendar cal = Calendar.getInstance();
					long checkDeleteTime = ((cal.getTimeInMillis() - deleteTime.getTime()) / 1000) / 3600;
					if (checkDeleteTime >= 0) {
						L1Clan clan = L1World.getInstance().getClan(clanname);
						if (clan != null) {
							clan.delMemberName(name);
						}
						CharacterTable.getInstance().deleteCharacter(client.getAccountName(), name);
					}
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}
	}

	private void sendCharPacks(ClientThread client) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid");
			pstm.setString(1, client.getAccountName());
			rs = pstm.executeQuery();

			while (rs.next()) {
				String name = rs.getString("char_name");
				String clanname = rs.getString("Clanname");
				int type = rs.getInt("Type");
				byte sex = rs.getByte("Sex");
				int lawful = rs.getInt("Lawful");

				int currenthp = rs.getInt("CurHp");
				if (currenthp < 1) {
					currenthp = 1;
				} else if (currenthp > 32767) {
					currenthp = 32767;
				}

				int currentmp = rs.getInt("CurMp");
				if (currentmp < 1) {
					currentmp = 1;
				} else if (currentmp > 32767) {
					currentmp = 32767;
				}

				int lvl;
				if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
					lvl = rs.getInt("level");
					if (lvl < 1) {
						lvl = 1;
					} else if (lvl > 127) {
						lvl = 127;
					}
				} else {
					lvl = 1;
				}

				int ac = rs.getByte("Ac");
				int str = rs.getByte("Str");
				int dex = rs.getByte("Dex");
				int con = rs.getByte("Con");
				int wis = rs.getByte("Wis");
				int cha = rs.getByte("Cha");
				int intel = rs.getByte("Intel");
				int accessLevel = rs.getShort("AccessLevel");
				Timestamp _birthday = (Timestamp) rs.getTimestamp("birthday");
				SimpleDateFormat SimpleDate = new SimpleDateFormat("yyyyMMdd");
				int birthday = Integer.parseInt(SimpleDate.format(_birthday.getTime()));

				S_CharPacks cpk = new S_CharPacks(name, clanname, type, sex,
						lawful, currenthp, currentmp, ac, lvl, str, dex, con,
						wis, cha, intel, accessLevel, birthday);

				client.sendPacket(cpk);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}
	}
}