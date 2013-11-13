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
import l1j.server.server.datatables.ClanMembersTable;
import l1j.server.server.datatables.ClanRecommendTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharReset;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_ClanAttention;
import l1j.server.server.serverpackets.S_ClanName;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PledgeRecommendation;
import l1j.server.server.serverpackets.S_ServerMessage;

//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

public class C_PledgeRecommendation extends ClientBasePacket {
	private static final String C_PledgeRecommendation = "[C] C_PledgeRecommendation";

	public C_PledgeRecommendation(byte[] decrypt, ClientThread client) throws Exception {
		super(decrypt);

		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		int data = readC();
		
		if(data == 0){ // 登陸推薦血盟
			/** 血盟類型 戰鬥/打怪/友好*/
			int clanType = readC(); 
			String TypeMessage = readS();
			if(ClanRecommendTable.getInstance().isRecorded(pc.getClanid())){ // Update
				ClanRecommendTable.getInstance().updateRecommendRecord(pc.getClanid(), clanType, TypeMessage);
			} else {
				ClanRecommendTable.getInstance().addRecommendRecord(pc.getClanid(), clanType, TypeMessage);
			}
			pc.sendPackets(new S_PledgeRecommendation(true, pc.getClanid()));
		} else if(data == 1){ // 取消登錄
			ClanRecommendTable.getInstance().removeRecommendRecord(pc.getClanid());
			pc.sendPackets(new S_PledgeRecommendation(false, pc.getClanid()));
		} else if(data == 2){ // 打開推薦血盟
			pc.sendPackets(new S_PledgeRecommendation(data, pc.getName()));
		} else if(data == 3){ // 打開申請目錄
			pc.sendPackets(new S_PledgeRecommendation(data, pc.getName()));
		} else if(data == 4){ // 打開邀請目錄
			if(pc.getClanRank() > 0){
				pc.sendPackets(new S_PledgeRecommendation(data, pc.getClanid()));
			}
		} else if(data == 5){ // 申請加入
			int clan_id = readD();
			ClanRecommendTable.getInstance().addRecommendApply(clan_id, pc.getName());
			pc.sendPackets(new S_PledgeRecommendation(data, clan_id, 0));
		} else if(data == 6){ // 審核已登記資料
			int index = readD();
			int type = readC();
			
            if(type == 1){ // 接受玩家加入
            	L1Clan clan = pc.getClan();
            	L1PcInstance joinPc = L1World.getInstance().getPlayer(ClanRecommendTable.getInstance().getApplyPlayerName(index));
            	for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
					clanMembers.sendPackets(new S_ServerMessage(94,joinPc.getName())); // \f1你接受%0當你的血盟成員。
				}
            	joinPc.setClanid(clan.getClanId());
				joinPc.setClanname(clan.getClanName());
				joinPc.setClanMemberNotes("");
				joinPc.setTitle("");
				joinPc.sendPackets(new S_CharTitle(joinPc.getId(),""));
				joinPc.broadcastPacket(new S_CharTitle(joinPc.getId(), ""));
				clan.addMemberName(joinPc.getName());
				ClanMembersTable.getInstance().newMember(joinPc);
				// 聯盟
            	if(pc.getClanRank() < 7){ 
            		joinPc.setClanRank(L1Clan.CLAN_RANK_LEAGUE_PUBLIC);
            		joinPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, L1Clan.CLAN_RANK_LEAGUE_PUBLIC, joinPc.getName())); // 你的階級變更為
            	} else { // 一般血盟
            		joinPc.setClanRank(L1Clan.CLAN_RANK_PUBLIC);
            		joinPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, L1Clan.CLAN_RANK_PUBLIC, joinPc.getName())); // 你的階級變更為
            	}
            	joinPc.save(); // 儲存加入的玩家資料
				joinPc.sendPackets(new S_ServerMessage(95, clan.getClanName())); // \f1加入%0血盟。
				joinPc.sendPackets(new S_ClanName(joinPc, true));
				joinPc.sendPackets(new S_CharReset(joinPc.getId(), clan.getClanId()));
				joinPc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getEmblemStatus()));
				joinPc.sendPackets(new S_ClanAttention());
				for(L1PcInstance player : clan.getOnlineClanMember()){
					player.sendPackets(new S_CharReset(joinPc.getId(), joinPc.getClan().getEmblemId()));
					player.broadcastPacket(new S_CharReset(player.getId(), joinPc.getClan().getEmblemId()));
				}
			} else if(type == 2){ // 拒絕玩家加入
				ClanRecommendTable.getInstance().removeRecommendApply(index);
			} else if(type == 3){ // 刪除申請
				ClanRecommendTable.getInstance().removeRecommendApply(index);
			} 
            pc.sendPackets(new S_PledgeRecommendation(data, index, type));
		} 
		

	}

	@Override
	public String getType() {
		return C_PledgeRecommendation;
	}
}
