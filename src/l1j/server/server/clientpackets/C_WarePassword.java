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

import l1j.server.server.Account;
import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DwarfInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_RetrieveElfList;
import l1j.server.server.serverpackets.S_RetrieveList;
import l1j.server.server.serverpackets.S_RetrievePledgeList;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_WarePassword extends ClientBasePacket {
	public C_WarePassword(byte abyte0[], ClientThread client) {
		super(abyte0);
		// 類型(0: 密碼變更, 1: 一般倉庫, 2: 血盟倉庫)
		int type = readC();

		// 取得第一組數值(舊密碼, 或待驗證的密碼)
		int pass1 = readD();

		// 取得第二組數值(新密碼, 或倉庫 NPC 的 objId)
		int pass2 = readD();

		// 不明的2個位元組
		readH();

		// 取得角色物件
		L1PcInstance pc = client.getActiveChar();
		Account account = client.getAccount();
		
		//取得對話的npc
		L1NpcInstance targetNpc = null;
		int targetObj = pc.getTempID();
		if (targetObj != 0) {
			targetNpc = (L1NpcInstance) L1World.getInstance().findObject(targetObj);
		}

		// 變更密碼
		if (type == 0) {
			// 兩次皆直接跳過密碼輸入
			if ((pass1 < 0) && (pass2 < 0)) {
				pc.sendPackets(new S_ServerMessage(79));
			}

			// 進行新密碼的設定
			else if ((pass1 < 0) && (account.getWarePassword() == 0)) {
				// 進行密碼變更
				account.changeWarePassword(pass2);

				pc.sendPackets(new S_SystemMessage("倉庫密碼設定完成，請牢記您的新密碼。"));
			}

			// 進行密碼變更
			else if ((pass1 > 0) && (pass1 == account.getWarePassword())) {
				// 進行密碼變更
				if (pass2 > 0) {
					account.changeWarePassword(pass2);

					pc.sendPackets(new S_SystemMessage("倉庫密碼變更完成，請牢記您的新密碼。"));
				} else if (pass1 == pass2) {
					// [342::你不能使用舊的密碼當作新的密碼。請再次輸入密碼。]
					pc.sendPackets(new S_ServerMessage(342));
				} else {
					account.changeWarePassword(0);
					pc.sendPackets(new S_SystemMessage("倉庫密碼取消完成。"));
				}
			} else {
				// 送出密碼錯誤的提示訊息[835:密碼錯誤。]
				pc.sendPackets(new S_ServerMessage(835));
			}
		}

		// 密碼驗證
		else {
			if (account.getWarePassword() == pass1) {
				int objid = pass2;

				if (type == 1) {
					if (targetNpc != null) {
						if (targetNpc instanceof L1DwarfInstance) {
							L1DwarfInstance npc = (L1DwarfInstance) targetNpc;
							// 判斷npc所屬倉庫類別
							switch (npc.getNpcId()) {

							case 60028:// 倉庫-艾爾(妖森)
								// 密碼吻合 輸出倉庫視窗
								pc.sendPackets(new S_RetrieveElfList(objid, pc));
								break;

							default:
								// 密碼吻合 輸出倉庫視窗
								pc.sendPackets(new S_RetrieveList(objid, pc));
								break;
							}
						}
					} else if (type == 2) {
						if (pc.getLevel() > 4) {
							if (pc.getClanid() == 0) {
								// \f1血盟倉庫を使用するには血盟に加入していなくてはなりません。
								pc.sendPackets(new S_ServerMessage(208));
								return;
							}
							int rank = pc.getClanRank();
							if (rank == L1Clan.CLAN_RANK_PROBATION) {
								// タイトルのない血盟員もしくは、見習い血盟員の場合は、血盟倉庫を利用することはできません。
								pc.sendPackets(new S_ServerMessage(728));
								return;
							}
							if ((rank != L1Clan.CLAN_RANK_PRINCE)
									&& pc.getTitle().equalsIgnoreCase("")) {
								// タイトルのない血盟員もしくは、見習い血盟員の場合は、血盟倉庫を利用することはできません。
								pc.sendPackets(new S_ServerMessage(728));
								return;
							}
							pc.sendPackets(new S_RetrievePledgeList(objid, pc));
						}
					}

				} else {
					// 送出密碼錯誤的提示訊息
					pc.sendPackets(new S_ServerMessage(835));
				}
			}
		}
	}
}
