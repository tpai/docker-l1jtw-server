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

import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharReset;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來上傳盟徽的封包
 */
public class C_EmblemUpload extends ClientBasePacket {

	private static final String C_EMBLEMUPLOAD = "[C] C_EmblemUpload";
	private static Logger _log = Logger.getLogger(C_EmblemUpload.class.getName());

	public C_EmblemUpload(byte abyte0[], ClientThread clientthread)throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if (player == null) {
			return;
		} else if(player.getClanRank() != 4 && player.getClanRank() != 10){
			return;
		}
		
		if (player.getClanid() != 0) {
			int newEmblemdId = IdFactory.getInstance().nextId();
			String emblem_file = String.valueOf(newEmblemdId);
			
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("emblem/" + emblem_file);
				for (short cnt = 0; cnt < 384; cnt++) {
					fos.write(readC());
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw e;
			} finally {
				if (null != fos) {
					fos.close();
				}
				fos = null;
			}
			L1Clan clan = ClanTable.getInstance().getTemplate(player.getClanid());
			clan.setEmblemId(newEmblemdId);
			ClanTable.getInstance().updateClan(clan);
			
			/** 廣播封包 */
			for(L1PcInstance pc : clan.getOnlineClanMember()){
				pc.sendPackets(new S_CharReset(pc.getId(), newEmblemdId));
				pc.broadcastPacket(new S_CharReset(pc.getId(), newEmblemdId));
			}
		}
	}

	@Override
	public String getType() {
		return C_EMBLEMUPLOAD;
	}
}
