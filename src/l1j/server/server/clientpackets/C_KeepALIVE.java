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

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來維持連線的封包
 */
public class C_KeepALIVE extends ClientBasePacket {
	private static Logger _log = Logger.getLogger(C_KeepALIVE.class.getName());
	private static final String C_KEEP_ALIVE = "[C] C_KeepALIVE";

	public C_KeepALIVE(byte decrypt[], ClientThread client) {
		super(decrypt);
		// XXX:GameTimeを送信（3バイトのデータを送って来ているのでそれを何かに利用しないといけないかもしれない）
// L1PcInstance pc = client.getActiveChar();
// pc.sendPackets(new S_GameTime());
	}

	@Override
	public String getType() {
		return C_KEEP_ALIVE;
	}
}