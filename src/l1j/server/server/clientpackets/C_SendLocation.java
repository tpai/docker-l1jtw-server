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
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SendLocation;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_SendLocation extends ClientBasePacket {

	private static final String C_SEND_LOCATION = "[C] C_SendLocation";

	public C_SendLocation(byte abyte0[], ClientThread client) {
		super(abyte0);
		int type = readC();

		// クライアントがアクティブ,ノンアクティブ転換時に
		// オペコード 0x57 0x0dパケットを送ってくるが詳細不明の為無視
		// マップ座標転送時は0x0bパケット
		if (type == 0x0d) {
			return;
		}

		String name = readS();
		int mapId = readH();
		int x = readH();
		int y = readH();
		int msgId = readC();

		if (name.isEmpty()) {
			return;
		}
		L1PcInstance target = L1World.getInstance().getPlayer(name);
		if (target != null) {
			L1PcInstance pc = client.getActiveChar();
			String sender = pc.getName();
			target.sendPackets(new S_SendLocation(type, sender, mapId, x, y,
					msgId));
			// 将来的にtypeを使う可能性があるので送る
			pc.sendPackets(new S_ServerMessage(1783, name));
		}
	}

	@Override
	public String getType() {
		return C_SEND_LOCATION;
	}
}
