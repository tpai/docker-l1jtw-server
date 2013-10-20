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

import l1j.server.server.Account;
import l1j.server.server.ClientThread;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 處理收到由客戶端傳來斷線的封包
 */
public class C_Disconnect extends ClientBasePacket {
	private static final String C_DISCONNECT = "[C] C_Disconnect";
	private static Logger _log = Logger.getLogger(C_Disconnect.class.getName());

	public C_Disconnect(byte[] decrypt, ClientThread client) {
		super(decrypt);
		client.CharReStart(true);
		L1PcInstance pc = client.getActiveChar();
		if (pc != null) {

			_log.fine("Disconnect from: " + pc.getName());

			if (client.getAccount() != null)
				Account.online(client.getAccount(), false);

			ClientThread.quitGame(pc);

			synchronized (pc) {
				pc.logout();
				client.setActiveChar(null);
			}
		} else {
			_log.fine("Disconnect Request from Account : "
					+ client.getAccountName());
		}
	}

	@Override
	public String getType() {
		return C_DISCONNECT;
	}
}
