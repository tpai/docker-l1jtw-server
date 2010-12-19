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
 * TODO: 尚未實裝僱用傭兵的處理
 * 處理收到由客戶端傳來僱用傭兵的封包
 */
public class C_HireSoldier extends ClientBasePacket {

	private static final String C_HIRE_SOLDIER = "[C] C_HireSoldier";

	private static Logger _log = Logger.getLogger(C_HireSoldier.class
			.getName());

	// S_HireSoldierを送ると表示される雇用ウィンドウでOKを押すとこのパケットが送られる
	public C_HireSoldier(byte[] decrypt, ClientThread client) {
		super(decrypt);
		int something1 = readH(); // S_HireSoldierパケットの引数
		int something2 = readH(); // S_HireSoldierパケットの引数
		int something3 = readD(); // 1以外入らない？
		int something4 = readD(); // S_HireSoldierパケットの引数
		int number = readH(); // 雇用する数
		
		// < 傭兵雇用処理
	}

	@Override
	public String getType() {
		return C_HIRE_SOLDIER;
	}
}
