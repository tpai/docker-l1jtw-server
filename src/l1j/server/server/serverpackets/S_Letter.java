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

import l1j.server.server.model.Instance.L1ItemInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_Letter extends ServerBasePacket {

	private static final String S_LETTER = "[S] S_Letter";

	private byte[] _byte = null;

	public S_Letter(L1ItemInstance item) {
		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		/*
		 * Connection con = null; PreparedStatement pstm = null; ResultSet rs =
		 * null; try { con = L1DatabaseFactory.getInstance().getConnection();
		 * pstm = con
		 * .prepareStatement("SELECT * FROM letter WHERE item_object_id=?");
		 * pstm.setInt(1, item.getId()); rs = pstm.executeQuery(); while
		 * (rs.next()) { writeC(Opcodes.S_OPCODE_LETTER); writeD(item.getId());
		 * if (item.get_gfxid() == 465) { // 開く前 writeH(466); // 開いた後 } else if
		 * (item.get_gfxid() == 606) { writeH(605); } else if (item.get_gfxid()
		 * == 616) { writeH(615); } else { writeH(item.get_gfxid()); }
		 * writeH(rs.getInt(2)); writeS(rs.getString(3));
		 * writeS(rs.getString(4)); writeByte(rs.getBytes(7));
		 * writeByte(rs.getBytes(8)); writeC(rs.getInt(6)); // テンプレ
		 * writeS(rs.getString(5)); // 日付 } } catch (SQLException e) {
		 * _log.log(Level.SEVERE, e.getLocalizedMessage(), e); } finally {
		 * SQLUtil.close(rs); SQLUtil.close(pstm); SQLUtil.close(con); }
		 */
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
		return S_LETTER;
	}
}
