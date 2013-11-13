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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.identity.L1SystemMessageId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.Random;

//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

/**
 * 處理接收由客戶端傳來的開啟重新開始選單請求
 */
public class C_RestartMenu extends ClientBasePacket {
	private static Logger _log = Logger.getLogger(C_RestartMenu.class.getName());

	private static final String C_RESTARTMENU = "[C] C_RestartMenu";

	public C_RestartMenu(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}

		int data = readC();
		
		if(data == 1){ // 請求授予血盟RANK
			int rank = readC();
			String name = readS();
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);

			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan == null) {
				return;
			}
			
			int userRank = pc.getClanRank(); // 授予者的階級
			
			if(userRank == 0 || userRank > 10){
				pc.sendPackets(new S_ServerMessage(518)); // 血盟君主才可使用此命令。
				return;
			}
			
			if(name.equalsIgnoreCase(pc.getName())){
				pc.sendPackets(new S_ServerMessage(2068));
				return;
			}

			if (!(rank > 1 && rank < 11)) {
				// 請輸入想要變更階級的人的名稱與階級。[階級 = 實習→ㄧ般→守護→副主]
				pc.sendPackets(new S_ServerMessage(781));
				return;
			}
			
			/** 可以使用的權限清單*/
			List<Integer> rankList = new ArrayList<Integer>();
			switch(userRank){ // 各階級限制
			case 3:
				rankList.add(2);
				rankList.add(5);
				rankList.add(6);
				break;
			case 4:
				rankList.add(2);
				rankList.add(3);
				rankList.add(5);
				rankList.add(6);
				break;
			case 6:
				rankList.add(2);
				rankList.add(5);
				break;
			case 9:
				rankList.add(7);
				rankList.add(8);
				break;
			case 10:
				rankList.add(7);
				rankList.add(8);
				rankList.add(9);
				break;
			
			}

			if (rank == 3) { // 如果授予副王階級給其他職業
				if (!rankList.contains(3)) {
					return;
				} else if (!targetPc.isCrown()) {
					pc.sendPackets(new S_ServerMessage(2064));
					return;
				}
			} else if (rank == 2 || rank == 5) { // 授予 聯盟一般/ 聯盟見習
				if (!rankList.contains(2) && !rankList.contains(5)) {
					return;
				} else if(userRank == 4 || userRank == 10 || userRank == 3){
				} else if (targetPc.getClanRank() == 6
						|| targetPc.getClanRank() == 9
						|| targetPc.getClanRank() == 3
						|| targetPc.getClanRank() == 4
						|| targetPc.getClanRank() == 10) {
					pc.sendPackets(new S_ServerMessage(2065));
					return;
				} else if (pc.getLevel() < 25) {
					pc.sendPackets(new S_ServerMessage(2471));
					return;
				}
			} else if (rank == 6 || rank == 9) { // 授予守護騎士
				if (!rankList.contains(6) && !rankList.contains(9)) {
					return;
				} else if (targetPc.getClanRank() == 6
						|| targetPc.getClanRank() == 9
						|| targetPc.getClanRank() == 3
						|| targetPc.getClanRank() == 4
						|| targetPc.getClanRank() == 10) {
					pc.sendPackets(new S_ServerMessage(2065));
					return;
				} else if (pc.getLevel() < 40 && userRank != 10 && userRank != 4 && userRank != 3) {
					pc.sendPackets(new S_ServerMessage(2472));
					return;
				}
				if (targetPc.getLevel() < 40) {
					pc.sendPackets(new S_ServerMessage(2473));
					return;
				}
			} else if (rank == 7 || rank == 8) { // 授予 一般/ 見習
				if ((targetPc.getClanRank() == 9 || targetPc.getClanRank() == 10) && userRank != 10) {
					pc.sendPackets(new S_ServerMessage(2065));
					return;
				}
			}
			

			if (targetPc != null) { // 玩家在線上
				if (pc.getClanid() == targetPc.getClanid()) { // 同血盟
					try {
						targetPc.setClanRank(rank);
						targetPc.save(); // 儲存玩家的資料到資料庫中
						targetPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank, name)); // 你的階級變更為%s
						pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank, name)); // 你的階級變更為%s
					}
					catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				} else {
					pc.sendPackets(new S_ServerMessage(414)); // 您只能邀請您血盟中的成員。
					return;
				}
			}
			else { // 離線盟友
				L1PcInstance restorePc = CharacterTable.getInstance().restoreCharacter(name);
				if ((restorePc != null) && (restorePc.getClanid() == pc.getClanid())) { // 同じ血盟
					pc.sendPackets(new S_ServerMessage(2069));
					return;
				} else {
					pc.sendPackets(new S_ServerMessage(109, name)); // %0という名前の人はいません。
					return;
				}
			}
		} else if (data == 2) {
			pc.sendPackets(new S_ServerMessage(74, "同盟目錄"));
		} else if (data == 3) {
			pc.sendPackets(new S_ServerMessage(74, "加入同盟"));
		} else if (data == 4) {
			pc.sendPackets(new S_ServerMessage(74, "退出同盟"));
		} else if(data == 5){ // 請求施放 生存吶喊 (CTRL+E)
			if (pc.getWeapon() == null) {
				pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$1973));
				return;
			}
			if (pc.getCurrentHp() >= pc.getMaxHp()) {
				pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$1974));
				return;
			}
			if (pc.get_food() >= 225) { 
				int addHp = 0;
				int gfxId1 = 8683;
				int gfxId2 = 829;
				long curTime = System.currentTimeMillis() / 1000;  // 現在時間
				int fullTime = (int) ((curTime - pc.getCryOfSurvivalTime()) / 60); // 飽食經過時間(分)
				if (fullTime <= 0) {
					pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$1974));
					return;
				} else if (fullTime >= 1 && fullTime <= 29) {
					addHp = (int) (pc.getMaxHp() * (fullTime / 100.0D));
				} else if (fullTime >= 30) {
					int weaponEnchantLv = pc.getWeapon().getEnchantLevel();
					if (weaponEnchantLv <= 6) { 
						gfxId1 = 8684;
						gfxId2 = 8907;
						addHp = (int) (pc.getMaxHp() * (20 + Random.nextInt(20) / 100.0D));
					} else if (weaponEnchantLv == 7 || weaponEnchantLv == 8){
						gfxId1 = 8685;
						gfxId2 = 8909;
						addHp = (int) (pc.getMaxHp() * ((30 + Random.nextInt(20)) / 100.0D));
					} else if (weaponEnchantLv == 9 || weaponEnchantLv == 10) {
						gfxId1 = 8773;
						gfxId2 = 8910;
						addHp = (int) (pc.getMaxHp() * ((50 + Random.nextInt(10)) / 100.0D));
					} else if (weaponEnchantLv  >= 11) {
						gfxId1 = 8686;
						gfxId2 = 8908;
						addHp = (int) (pc.getMaxHp() * (0.7));
					}
				}
				
				S_SkillSound sound = new S_SkillSound(pc.getId(), gfxId1);
				pc.sendPackets(sound);
				pc.broadcastPacket(sound);
				
				sound = new S_SkillSound(pc.getId(), gfxId2);
				pc.sendPackets(sound);
				pc.broadcastPacket(sound);
				
				if (addHp != 0) {
					pc.set_food(0);
					pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, 0));
					pc.setCurrentHp(pc.getCurrentHp() + addHp);
				}
			}
		} else if(data == 6){ // 請求顯示 生存吶喊 頭頂動畫(ALT+0)
			int gfxId = 8683;
			long curTime = System.currentTimeMillis() / 1000; // 現在時間
			int fullTime = (int) ((curTime - pc.getCryOfSurvivalTime()) / 60); // 飽食經過時間(分)
			if (pc.getWeapon() == null) {
				pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$1973));  
				return;
			}
			if (fullTime >= 30) {
				int weaponEnchantLv = pc.getWeapon().getEnchantLevel();
				if (weaponEnchantLv <= 6) {
					gfxId = 8684;
				} else if (weaponEnchantLv >= 7 && weaponEnchantLv <= 8){
					gfxId = 8685;
				} else if (weaponEnchantLv >= 9 && weaponEnchantLv <= 10) {
					gfxId = 8773;
				} else if (weaponEnchantLv  >= 11) {
					gfxId = 8686;
				}
			}
			S_SkillSound sound = new S_SkillSound(pc.getId(), gfxId);
			pc.sendPackets(sound);
			pc.broadcastPacket(sound);
		} else if(data == 9){
			// TODO 未來完成地圖得計時器後，改成動態即時讀取。
			String[] map = { "$12125", "$6081", "$14250", "$12126" };
			int[] time = { 180, 60, 120, 120 };
			pc.sendPackets(new S_PacketBox(S_PacketBox.MAP_TIME, map, time));
		}
	}

	@Override
	public String getType() {
		return C_RESTARTMENU;
	}
}
