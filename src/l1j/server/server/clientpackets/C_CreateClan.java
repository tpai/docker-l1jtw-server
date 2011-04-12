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
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來建立血盟的封包
 */
public class C_CreateClan extends ClientBasePacket {

	private static final String C_CREATE_CLAN = "[C] C_CreateClan";

	public C_CreateClan(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);
		String s = readS();

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc.isCrown()) { // 是王族
			if (pc.getClanid() == 0) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) { // 檢查是否有同名的血盟
					if (clan.getClanName().toLowerCase().equals(s.toLowerCase())) {
						pc.sendPackets(new S_ServerMessage(99)); // \f1那個血盟名稱已經存在。
						return;
					}
				}
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 30000)) { // 身上有金幣3萬
					L1Clan clan = ClanTable.getInstance().createClan(pc, s); // 建立血盟
					if (clan != null) {
						pc.sendPackets(new S_ServerMessage(84, s)); // 創立\f1%0  血盟。
						pc.getInventory().consumeItem(L1ItemId.ADENA, 30000);
					}
				} else {
					pc.sendPackets(new S_ServerMessage(189)); // \f1金幣不足。
				}
			} else {
				pc.sendPackets(new S_ServerMessage(86)); // \f1已經創立血盟。
			}
		} else {
			pc.sendPackets(new S_ServerMessage(85)); // \f1王子和公主才可創立血盟。
		}
	}

	@Override
	public String getType() {
		return C_CREATE_CLAN;
	}

}
