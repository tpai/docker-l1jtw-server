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
package l1j.server.server.clientpackets;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.BoardTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BoardInstance;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 收到由客戶端傳送刪除公告欄的封包
 */
public class C_BoardDelete extends ClientBasePacket {

	private static final String C_BOARD_DELETE = "[C] C_BoardDelete";

	public C_BoardDelete(byte decrypt[], ClientThread client) {
		super(decrypt);
		int objId = readD();
		int topicId = readD();
		L1Object obj = L1World.getInstance().findObject(objId);
		L1BoardInstance board = (L1BoardInstance) obj;
		if (board != null) {
			BoardTable.getInstance().deleteTopic(topicId);
		}
	}

	@Override
	public String getType() {
		return C_BOARD_DELETE;
	}

}
