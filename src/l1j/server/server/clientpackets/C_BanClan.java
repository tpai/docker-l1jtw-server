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
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理從客戶端傳來脫離血盟的封包
 */
public class C_BanClan extends ClientBasePacket {

	private static final String C_BAN_CLAN = "[C] C_BanClan";
	private static Logger _log = Logger.getLogger(C_BanClan.class.getName());

	public C_BanClan(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);
		String s = readS();

		L1PcInstance pc = clientthread.getActiveChar();
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			String clanMemberName[] = clan.getAllMembers();
			int i;
			if (pc.isCrown() && pc.getId() == clan.getLeaderId()) { // 王族，或者已經創立血盟
				for (i = 0; i < clanMemberName.length; i++) {
					if (pc.getName().toLowerCase().equals(s.toLowerCase())) { // 是血盟創立者
						return;
					}
				}
				L1PcInstance tempPc = L1World.getInstance().getPlayer(s);
				if (tempPc != null) { // 玩家在線上
					if (tempPc.getClanid() == pc.getClanid()) { // 確定同血盟
						tempPc.setClanid(0);
						tempPc.setClanname("");
						tempPc.setClanRank(0);
						tempPc.save(); // 儲存玩家的資料到資料庫中
						clan.delMemberName(tempPc.getName());
						tempPc.sendPackets(new S_ServerMessage(238, pc
								.getClanname())); // 你被 %0 血盟驅逐了。
						pc.sendPackets(new S_ServerMessage(240, tempPc
								.getName())); // %0%o 被你從你的血盟驅逐了。
					} else {
						pc.sendPackets(new S_ServerMessage(109, s)); // 沒有叫%0的人。
					}
				} else { // 玩家離線中
					try {
						L1PcInstance restorePc = CharacterTable.getInstance()
								.restoreCharacter(s);
						if (restorePc != null
								&& restorePc.getClanid() == pc.getClanid()) { // 確定同血盟
							restorePc.setClanid(0);
							restorePc.setClanname("");
							restorePc.setClanRank(0);
							restorePc.save(); // 儲存玩家的資料到資料庫中
							clan.delMemberName(restorePc.getName());
							pc.sendPackets(new S_ServerMessage(240, restorePc
									.getName())); // %0%o 被你從你的血盟驅逐了。
						} else {
							pc.sendPackets(new S_ServerMessage(109, s)); // %0%o 被你從你的血盟驅逐了。
						}
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			} else {
				pc.sendPackets(new S_ServerMessage(518)); // 血盟君主才可使用此命令。
			}
		}
	}

	@Override
	public String getType() {
		return C_BAN_CLAN;
	}
}
