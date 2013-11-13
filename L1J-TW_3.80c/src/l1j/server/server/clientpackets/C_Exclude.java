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
import java.util.logging.Level;

import l1j.server.server.ClientThread;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來封鎖密語的封包
 */
public class C_Exclude extends ClientBasePacket {

	private static final String C_EXCLUDE = "[C] C_Exclude";
	private static Logger _log = Logger.getLogger(C_Exclude.class.getName());

	/**
	 * C_1 輸入 /exclude 指令的時候 
	 */
	public C_Exclude(byte[] decrypt, ClientThread client) {
		super(decrypt);
		
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		String name = readS();
		if (name.isEmpty()) {
			return;
		}
		
		try {
			L1ExcludingList exList = pc.getExcludingList();
			if (exList.isFull()) {
				pc.sendPackets(new S_ServerMessage(472)); // 被拒絕的玩家太多。
				return;
			}
			if (exList.contains(name)) {
				String temp = exList.remove(name);
				pc.sendPackets(new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp));
			} else {
				exList.add(name);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, name));
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	@Override
	public String getType() {
		return C_EXCLUDE;
	}
}
