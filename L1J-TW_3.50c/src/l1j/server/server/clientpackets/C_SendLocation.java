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

import java.util.Calendar;

import l1j.server.server.ClientThread;
import l1j.server.server.model.L1DragonSlayer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SendLocation;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.L1SpawnUtil;

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
			/*
			 * 視窗內:0d 01 xx // xx 就是值會變動，不知原因。
			 * 視窗外:0d 00 xx // xx 就是值會變動，不知原因。
			*/
		}

		if (type == 0x0b) {
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
		} else if (type == 0x06) {
			@SuppressWarnings("unused")
			int objectId = readD();
			int gate = readD();
			int dragonGate[] = { 81273, 81274, 81275, 81276 };
			L1PcInstance pc = client.getActiveChar();
			if (gate >= 0 && gate <= 3) {
				Calendar nowTime = Calendar.getInstance();
				if (nowTime.get(Calendar.HOUR_OF_DAY) >= 8 && nowTime.get(Calendar.HOUR_OF_DAY) < 12) {
					pc.sendPackets(new S_ServerMessage(1643)); // 每日上午 8 點到 12 點為止，暫時無法使用龍之鑰匙。
				} else {
					boolean limit = true;
					switch (gate) {
						case 0:
							for (int i = 0; i < 6; i++) {
								if (!L1DragonSlayer.getInstance().getPortalNumber()[i]) {
									limit = false;
								}
							}
							break;
						case 1:
							for (int i = 6; i < 12; i++) {
								if (!L1DragonSlayer.getInstance().getPortalNumber()[i]) {
									limit = false;
								}
							}
							break;
					}
					if (!limit) { // 未達上限可開設龍門
						if (!pc.getInventory().consumeItem(47010, 1)) {
							pc.sendPackets(new S_ServerMessage(1567)); // 需要龍之鑰匙。
							return;
						}
						L1SpawnUtil.spawn(pc, dragonGate[gate], 0, 120 * 60 * 1000); // 開啟 2 小時
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_SEND_LOCATION;
	}
}
