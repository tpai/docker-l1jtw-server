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
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1DragonSlayer;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
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
				target.sendPackets(new S_SendLocation(type, sender, mapId, x, y, msgId));
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
		} else if(type == 0x2e){ // 識別盟徽 狀態
			L1PcInstance pc = client.getActiveChar();
			// 如果不是君主或聯盟王
			if(pc.getClanRank() != 4 && pc.getClanRank() != 10){
				return;
			}
			
			int emblemStatus = readC(); // 0: 關閉 1:開啟
			
			L1Clan clan = pc.getClan();
			clan.setEmblemStatus(emblemStatus);
			ClanTable.getInstance().updateClan(clan);
			
			for(L1PcInstance member: clan.getOnlineClanMember()){
				member.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, emblemStatus));
			}
		} else if(type == 0x30){ // 村莊便利傳送
			int mapIndex = readH(); // 1: 亞丁 2:古魯丁 3: 奇岩
			int point = readH();
			int locx = 0;
			int locy = 0;
			L1PcInstance pc = client.getActiveChar();
			if(mapIndex == 1){ 
				if(point == 0){ // 亞丁-村莊北邊地區
					//X34079 Y33136 右下角 X 34090 Y 33150
					locx = 34079 + (int)(Math.random() * 12);
					locy = 33136 + (int)(Math.random() * 15);
				} else if(point == 1){ // 亞丁-村莊中心地區
					//左上角 X 33970 Y 33243 右下角 X33979 Y33256 
					locx = 33970 + (int)(Math.random() * 10);
					locy = 33243 + (int)(Math.random() * 14);
				} else if(point == 2){ // 亞丁-村莊教堂地區
					// 左上 X33925 Y33351 右下 X33938 Y33359
					locx = 33925 + (int)(Math.random() * 14);
					locy = 33351 + (int)(Math.random() * 9);
				}
			} else if(mapIndex == 2){
				if(point == 0){ // 古魯丁-北邊地區
					//左上 X32615 Y32719 右下 X32625 Y32725
					locx = 32615 + (int)(Math.random() * 11);
					locy = 32719 + (int)(Math.random() * 7);
				} else if(point == 1){ // 古魯丁-南邊地區 
					//左上 X32621 Y32788 右下 X32629 Y32800  
					locx = 32621 + (int)(Math.random() * 9);
					locy = 32788 + (int)(Math.random() * 13);
				}
			} else if(mapIndex == 3){
				if(point == 0){ // 奇岩-北邊地區
					//左上 X33501 Y32765 右下 X33511 Y32773
					locx = 33501 + (int)(Math.random() * 11);
					locy = 32765 + (int)(Math.random() * 9);
				} else if(point == 1){ // 奇岩-南邊地區 
					//左上 X33440 Y32784 右下 X33450 Y32794 
					locx = 33440 + (int)(Math.random() * 11);
					locy = 32784 + (int)(Math.random() * 11);
				}
			}
			L1Teleport.teleport(pc, locx, locy, pc.getMapId() , pc.getHeading(), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.TOWN_TELEPORT, pc));
		} else if(type == 0x32){
			
		}
	}

	@Override
	public String getType() {
		return C_SEND_LOCATION;
	}
}
