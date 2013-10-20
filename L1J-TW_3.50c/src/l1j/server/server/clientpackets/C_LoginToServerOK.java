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
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來登入到伺服器OK的封包
 */
public class C_LoginToServerOK extends ClientBasePacket {

	private static final String C_LOGIN_TO_SERVER_OK = "[C] C_LoginToServerOK";

	public C_LoginToServerOK(byte[] decrypt, ClientThread client) {
		super(decrypt);

		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		int type = readC();
		int button = readC();

		if (type == 255) { // 全體聊天 && 密語
			if ((button == 95) || (button == 127)) {
				pc.setShowWorldChat(true); // open
				pc.setCanWhisper(true); // open
			} else if ((button == 91) || (button == 123)) {
				pc.setShowWorldChat(true); // open
				pc.setCanWhisper(false); // close
			} else if ((button == 94) || (button == 126)) {
				pc.setShowWorldChat(false); // close
				pc.setCanWhisper(true); // open
			} else if ((button == 90) || (button == 122)) {
				pc.setShowWorldChat(false); // close
				pc.setCanWhisper(false); // close
			}
		} else if (type == 0) { // 全體聊天
			if (button == 0) { // close
				pc.setShowWorldChat(false);
			} else if (button == 1) { // open
				pc.setShowWorldChat(true);
			}
		} else if (type == 2) { // 密語
			if (button == 0) { // close
				pc.setCanWhisper(false);
			} else if (button == 1) { // open
				pc.setCanWhisper(true);
			}
		} else if (type == 6) { // 交易頻道
			if (button == 0) { // close
				pc.setShowTradeChat(false);
			} else if (button == 1) { // open
				pc.setShowTradeChat(true);
			}
		} else if (type == 9) { // 血盟
			if (button == 0) { // open
				pc.setShowClanChat(true);
			} else if (button == 1) { // close
				pc.setShowClanChat(false);
			}
		} else if (type == 10) { // 組隊
			if (button == 0) { // close
				pc.setShowPartyChat(false);
			} else if (button == 1) { // open
				pc.setShowPartyChat(true);
			}
		}
	}

	@Override
	public String getType() {
		return C_LOGIN_TO_SERVER_OK;
	}
}
