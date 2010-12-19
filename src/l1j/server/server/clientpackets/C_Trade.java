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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.utils.FaceToFace;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來交易的封包
 */
public class C_Trade extends ClientBasePacket {

	private static final String C_TRADE = "[C] C_Trade";
	private static Logger _log = Logger.getLogger(C_Trade.class.getName());

	public C_Trade(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if (player.isGhost()) {
			return;
		}
		L1PcInstance target = FaceToFace.faceToFace(player);
		if (target != null) {
			if (!target.isParalyzed()) {
				player.setTradeID(target.getId()); // 相手のオブジェクトIDを保存しておく
				target.setTradeID(player.getId());
				target.sendPackets(new S_Message_YN(252, player.getName())); // %0%sがあなたとアイテムの取引を望んでいます。取引しますか？（Y/N）
			}
		}
	}

	@Override
	public String getType() {
		return C_TRADE;
	}
}
