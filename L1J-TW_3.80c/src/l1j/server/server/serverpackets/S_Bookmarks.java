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
package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.utils.SQLUtil;

public class S_Bookmarks extends ServerBasePacket {
	private static final String _S__1F_S_Bookmarks = "[S] S_Bookmarks";

	private byte[] _byte = null;

	public S_Bookmarks(String name, int map, int id, int x, int y) {
		buildPacket(name, map, id, x, y);
	}
	
	/**
	 * 角色重登載入
	 * @param pc
	 */
	public S_Bookmarks(L1PcInstance pc){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_teleport WHERE char_id=? ORDER BY name ASC");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			rs.last();                  // 為了取得總列數，先將指標拉到最後
			int rowcount = rs.getRow(); // 取得總列數
            rs.beforeFirst();           // 將指標移回最前頭
            writeC(Opcodes.S_OPCODE_CHARRESET);
			writeC(0x2a);
			writeC(0x80);
			writeC(0x00); 
			writeC(0x02);
            for(int i = 0; i<= 126 ; i++){
            	if(i < rowcount){
            		writeC(i);
            	}else{
            		writeC(0x00);
            	}
            }
            writeC(0x3c);
			writeC(0);
			writeC(rowcount);
			writeC(0);
			
			while (rs.next()) {
				L1BookMark bookmark = new L1BookMark();
				bookmark.setId(rs.getInt("id"));
				bookmark.setCharId(rs.getInt("char_id"));
				bookmark.setName(rs.getString("name"));
				bookmark.setLocX(rs.getInt("locx"));
				bookmark.setLocY(rs.getInt("locy"));
				bookmark.setMapId(rs.getShort("mapid"));
				writeH(bookmark.getLocX());
				writeH(bookmark.getLocY());
				writeS(bookmark.getName());
				writeH(bookmark.getMapId());
				writeD(bookmark.getId());
				pc.addBookMark(bookmark);
			}

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void buildPacket(String name, int map, int id , int x , int y) {
		writeC(Opcodes.S_OPCODE_BOOKMARKS);
		writeS(name);
		writeH(map);
		writeD(id);
		writeH(x);
		writeH(y);
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
		return _S__1F_S_Bookmarks;
	}
}