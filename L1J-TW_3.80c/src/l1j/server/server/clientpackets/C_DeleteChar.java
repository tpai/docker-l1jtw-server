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

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DeleteCharOK;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket, C_DeleteChar

/**
 * 處理收到由客戶端傳來刪除角色的封包
 */
public class C_DeleteChar extends ClientBasePacket {

	private static final String C_DELETE_CHAR = "[C] RequestDeleteChar";

	private static Logger _log = Logger.getLogger(C_DeleteChar.class.getName());

	public C_DeleteChar(byte decrypt[], ClientThread client) throws Exception {
		super(decrypt);
		String name = readS();

		try {
			L1PcInstance pc = CharacterTable.getInstance().restoreCharacter(name);
			if (pc != null && pc.getLevel() >= 5 && Config.DELETE_CHARACTER_AFTER_7DAYS) {
				if (pc.getType() < 32) {
					if (pc.isCrown()) {
						pc.setType(32);
					} else if (pc.isKnight()) {
						pc.setType(33);
					} else if (pc.isElf()) {
						pc.setType(34);
					} else if (pc.isWizard()) {
						pc.setType(35);
					} else if (pc.isDarkelf()) {
						pc.setType(36);
					} else if (pc.isDragonKnight()) {
						pc.setType(37);
					} else if (pc.isIllusionist()) {
						pc.setType(38);
					}
					Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + 604800000); // 7日後
					pc.setDeleteTime(deleteTime);
					pc.save(); // 儲存到資料庫中
				} else {
					if (pc.isCrown()) {
						pc.setType(0);
					} else if (pc.isKnight()) {
						pc.setType(1);
					} else if (pc.isElf()) {
						pc.setType(2);
					} else if (pc.isWizard()) {
						pc.setType(3);
					} else if (pc.isDarkelf()) {
						pc.setType(4);
					} else if (pc.isDragonKnight()) {
						pc.setType(5);
					} else if (pc.isIllusionist()) {
						pc.setType(6);
					}
					pc.setDeleteTime(null);
					pc.save(); // 儲存到資料庫中
				}
				client.sendPacket(new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_AFTER_7DAYS));
				return;
			}

			if (pc != null) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					clan.delMemberName(name);
				}
			}
			CharacterTable.getInstance().deleteCharacter(client.getAccountName(), name);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			client.close();
			return;
		}
		client.sendPacket(new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_NOW));
	}

	@Override
	public String getType() {
		return C_DELETE_CHAR;
	}

}
