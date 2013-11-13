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
import l1j.server.server.serverpackets.S_ServerVersion;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來要求伺服器版本的封包
 */
public class C_ServerVersion extends ClientBasePacket {

	private static final String C_SERVER_VERSION = "[C] C_ServerVersion";
	private static final int currentClientVersion = 0x00000000;

	@SuppressWarnings("unused")
	public C_ServerVersion(byte decrypt[], ClientThread client) throws Exception {
		super(decrypt);
		
		/* From client: client version
		 * [Client] opcode = 14
		 * 0000: 0e 34 00/ b6 /03 00 00 00 /09 f0 6e f0 65 51 c7 00 .4........n.eQ..
		 * 0010: 01 00 06 00 ....
		 */
		readH();
		readC(); 
		int clientLanguage = readD();   // 主程式語系
		int unknownVer1 = readH();      // 未知的版本號
		int unknownVer2 = readH();      // 未知的版本號
		int clientVersion = readD();    // 主程式版本號

		client.sendPacket(new S_ServerVersion());
	}

	@Override
	public String getType() {
		return C_SERVER_VERSION;
	}

}
