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
package l1j.server.server.serverpackets;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;

//Referenced classes of package l1j.server.server.serverpackets:
//ServerBasePacket


public class S_Pledge extends ServerBasePacket {
	
	private static final String _S_Pledge = "[S] _S_Pledge";
	
	private byte[] _byte = null;
	
	/**
	 * 盟友查詢 公告視窗
	 * @param ClanId 血盟Id
	 */
	public S_Pledge(int ClanId) {
		L1Clan clan = ClanTable.getInstance().getTemplate(ClanId);
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(S_PacketBox.HTML_PLEDGE_ANNOUNCE);
		writeS(clan.getClanName());
		writeS(clan.getLeaderName());
		writeD(clan.getEmblemId()); // 盟徽id
		writeD((int) (clan.getFoundDate().getTime() / 1000)); // 血盟創立日
		try {
			byte[] text = new byte[478];
			Arrays.fill(text, (byte) 0);
			int i = 0;
			for (byte b : clan.getAnnouncement().getBytes("Big5")) {
				text[i++] = b;
			}
			writeByte(text);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 盟友查詢 盟友清單
	 * @param clanName 
	 * @throws Exception 
	 */
	public S_Pledge(L1PcInstance pc) throws Exception {
		L1Clan clan = ClanTable.getInstance().getTemplate(pc.getClanid());
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(S_PacketBox.HTML_PLEDGE_MEMBERS);
		writeH(1);
		writeC(clan.getAllMembers().length); // 血盟總人數

		// 血盟成員資料
		/* Name/Rank/Level/Notes/MemberId/ClassType */
		for (String member : clan.getAllMembers()) {
			L1PcInstance clanMember = CharacterTable.getInstance().restoreCharacter(member);
			writeS(clanMember.getName());
			writeC(clanMember.getClanRank());
			writeC(clanMember.getLevel());
			
			/** 產生全由0填充的byte陣列 */
			byte[] text = new byte[62];
			Arrays.fill(text, (byte) 0);
			
			/** 將備註字串填入byte陣列*/
			if (clanMember.getClanMemberNotes().length() != 0) {
				int i = 0;
				for (byte b : clanMember.getClanMemberNotes().getBytes("Big5")) {
					text[i++] = b;
				}
			}
			writeByte(text);
			writeD(clanMember.getClanMemberId());
			writeC(clanMember.getType());
		}
	}
	
	/**
	 * 盟友查詢 寫入備註
	 * @param name 玩家名稱
	 * @param notes 備註文字
	 */
	public S_Pledge(String name, String notes){
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(S_PacketBox.HTML_PLEDGE_WRITE_NOTES);
		writeS(name);
		
		/** 產生全由0填充的byte陣列 */
		byte[] text = new byte[62];
		Arrays.fill(text, (byte) 0);
		
		/** 將備註字串填入byte陣列*/
		if (notes.length() != 0) {
			int i = 0;
			try {
				for (byte b : notes.getBytes("Big5")) {
					text[i++] = b;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		writeByte(text);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return _S_Pledge;
	}
}
