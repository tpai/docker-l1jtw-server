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
import l1j.server.server.serverpackets.S_Emblem;

//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

/**
 * 處理收到由客戶端傳來下載盟徽請求的封包
 */
public class C_EmblemDownload extends ClientBasePacket {

	private static final String C_EMBLEMDOWNLOAD = "[C] C_EmblemDownload";

	public C_EmblemDownload(byte abyte0[], ClientThread clientthread) {
		super(abyte0);

		int emblemId = readD();
		
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		pc.sendPackets(new S_Emblem(emblemId));
	}

	@Override
	public String getType() {
		return C_EMBLEMDOWNLOAD;
	}
}
