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

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來血盟階級的封包
 */
public class C_Rank extends ClientBasePacket {

	private static final String C_RANK = "[C] C_Rank";
	private static Logger _log = Logger.getLogger(C_Rank.class.getName());

	public C_Rank(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);

		int data = readC(); // ?

		L1PcInstance pc = clientthread.getActiveChar();

		if (data == 1) {
			int rank = readC();
			String name = readS();
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (pc == null) {
				return;
			}

			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan == null) {
				return;
			}

			if ((rank < 1) && (3 < rank)) {
				// 請輸入想要變更階級的人的名稱與階級。[階級 = 守護騎士、一般、見習]
				pc.sendPackets(new S_ServerMessage(781));
				return;
			}

			if (pc.isCrown()) { // 君主
				if (pc.getId() != clan.getLeaderId()) { // 血盟主
					pc.sendPackets(new S_ServerMessage(785)); // 你不再是君主了
					return;
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(518)); // 血盟君主才可使用此命令。
				return;
			}

			if (targetPc != null) { // 玩家在線上
				if (pc.getClanid() == targetPc.getClanid()) { // 同血盟
					try {
						targetPc.setClanRank(rank);
						targetPc.save(); // 儲存玩家的資料到資料庫中
						targetPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank)); // 你的階級變更為%s
					}
					catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
				else {
					pc.sendPackets(new S_ServerMessage(414)); // 您只能邀請您血盟中的成員。
					return;
				}
			}
			else { // オフライン中
				L1PcInstance restorePc = CharacterTable.getInstance().restoreCharacter(name);
				if ((restorePc != null) && (restorePc.getClanid() == pc.getClanid())) { // 同じ血盟
					try {
						restorePc.setClanRank(rank);
						restorePc.save(); // 儲存玩家的資料到資料庫中
					}
					catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
				else {
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
		} else {
			/*
			*/
		}
	}

	@Override
	public String getType() {
		return C_RANK;
	}
}
