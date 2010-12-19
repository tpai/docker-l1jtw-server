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
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來交易OK的封包
 */
public class C_TradeOK extends ClientBasePacket {

	private static final String C_TRADE_CANCEL = "[C] C_TradeOK";
	private static Logger _log = Logger.getLogger(C_TradeOK.class.getName());

	public C_TradeOK(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance()
				.findObject(player.getTradeID());
		if (trading_partner != null) {
			player.setTradeOk(true);

			if (player.getTradeOk() && trading_partner.getTradeOk()) // 同時都壓OK
			{
				// 檢查身上的空間是否還有 (180 - 16)
				if (player.getInventory().getSize() < (180 - 16)
						&& trading_partner.getInventory().getSize() < (180 - 16)) // お互いのアイテムを相手に渡す
				{
					L1Trade trade = new L1Trade();
					trade.TradeOK(player);
				} else // お互いのアイテムを手元に戻す
				{
					player.sendPackets(new S_ServerMessage(263)); // \f1一人のキャラクターが持って歩けるアイテムは最大180個までです。
					trading_partner.sendPackets(new S_ServerMessage(263)); // \f1一人のキャラクターが持って歩けるアイテムは最大180個までです。
					L1Trade trade = new L1Trade();
					trade.TradeCancel(player);
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_TRADE_CANCEL;
	}

}
