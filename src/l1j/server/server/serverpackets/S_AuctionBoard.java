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

package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Lists;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_AuctionBoard extends ServerBasePacket {

	private static Logger _log = Logger.getLogger(S_AuctionBoard.class.getName());

	private static final String S_AUCTIONBOARD = "[S] S_AuctionBoard";

	private byte[] _byte = null;

	public S_AuctionBoard(L1NpcInstance board) {
		buildPacket(board);
	}

	private void buildPacket(L1NpcInstance board) {
		List<Integer> houseList = Lists.newList();
		int houseId = 0;
		int count = 0;
		int[] id = null;
		String[] name = null;
		int[] area = null;
		int[] month = null;
		int[] day = null;
		int[] price = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_auction");
			rs = pstm.executeQuery();
			while (rs.next()) {
				houseId = rs.getInt(1);
				if ((board.getX() == 33421) && (board.getY() == 32823)) { // 競売掲示板(ギラン)
					if ((houseId >= 262145) && (houseId <= 262189)) {
						houseList.add(houseId);
						count++;
					}
				}
				else if ((board.getX() == 33585) && (board.getY() == 33235)) { // 競売掲示板(ハイネ)
					if ((houseId >= 327681) && (houseId <= 327691)) {
						houseList.add(houseId);
						count++;
					}
				}
				else if ((board.getX() == 33959) && (board.getY() == 33253)) { // 競売掲示板(アデン)
					if ((houseId >= 458753) && (houseId <= 458819)) {
						houseList.add(houseId);
						count++;
					}
				}
				else if ((board.getX() == 32611) && (board.getY() == 32775)) { // 競売掲示板(グルーディン)
					if ((houseId >= 524289) && (houseId <= 524294)) {
						houseList.add(houseId);
						count++;
					}
				}
			}
			id = new int[count];
			name = new String[count];
			area = new int[count];
			month = new int[count];
			day = new int[count];
			price = new int[count];

			for (int i = 0; i < count; ++i) {
				pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
				houseId = houseList.get(i);
				pstm.setInt(1, houseId);
				rs = pstm.executeQuery();
				while (rs.next()) {
					id[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					area[i] = rs.getInt(3);
					Calendar cal = timestampToCalendar((Timestamp) rs.getObject(4));
					month[i] = cal.get(Calendar.MONTH) + 1;
					day[i] = cal.get(Calendar.DATE);
					price[i] = rs.getInt(5);
				}
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

		writeC(Opcodes.S_OPCODE_HOUSELIST);
		writeD(board.getId());
		writeH(count); // レコード数
		for (int i = 0; i < count; ++i) {
			writeD(id[i]); // アジトの番号
			writeS(name[i]); // アジトの名前
			writeH(area[i]); // アジトの広さ
			writeC(month[i]); // 締切月
			writeC(day[i]); // 締切日
			writeD(price[i]); // 現在の入札価格
		}
	}

	private Calendar timestampToCalendar(Timestamp ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		return cal;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_AUCTIONBOARD;
	}
}
