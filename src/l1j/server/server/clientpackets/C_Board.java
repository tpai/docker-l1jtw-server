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

import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AuctionBoardInstance;
import l1j.server.server.model.Instance.L1BoardInstance;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket, C_Board

/**
 * 收到由客戶端傳送打開公告欄的封包
 */
public class C_Board extends ClientBasePacket {

	private static final String C_BOARD = "[C] C_Board";
	private static Logger _log = Logger.getLogger(C_Board.class.getName());

	private boolean isBoardInstance(L1Object obj) {
		return (obj instanceof L1BoardInstance
				|| obj instanceof L1AuctionBoardInstance);
	}

	public C_Board(byte abyte0[], ClientThread client) {
		super(abyte0);
		int objectId = readD();
		L1Object obj = L1World.getInstance().findObject(objectId);
		if (!isBoardInstance(obj)) {
			return; // 不可能一個無賴客戶端
		}
		obj.onAction(client.getActiveChar());
	}

	@Override
	public String getType() {
		return C_BOARD;
	}

}
