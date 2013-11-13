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
import l1j.server.server.model.Instance.L1PcInstance;

//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

/**
 * 處理客戶端傳來之血盟注視封包
 */
public class C_ClanAttention extends ClientBasePacket{
	private static final String C_PledgeRecommendation = "[C] C_PledgeRecommendation";

	public C_ClanAttention(byte[] decrypt, ClientThread client){
		super(decrypt);
		
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		int data = readC();
		
		if(data == 0){ // 新增注視血盟
			String clanName = readS();
			
		} else if(data == 2){ // 查詢新增的注視名單
			
		}
		
	}
	
	@Override
	public String getType() {
		return C_PledgeRecommendation;
	}
}
