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
import l1j.server.server.serverpackets.S_SkillBuy;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來買魔法的封包
 */
public class C_SkillBuy extends ClientBasePacket {

	private static final String C_SKILL_BUY = "[C] C_SkillBuy";

	public C_SkillBuy(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		int i = readD();

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc.isGhost()) {
			return;
		}
		pc.sendPackets(new S_SkillBuy(i, pc));
		/*
		 * int type = player.get_type(); int lvl = player.get_level();
		 * 
		 * switch(type) { case 0: // 君主 if(lvl >= 10) { player.sendPackets(new
		 * S_SkillBuy(i, player)); } break;
		 * 
		 * case 1: // ナイト if(lvl >= 50) { player.sendPackets(new S_SkillBuy(i,
		 * player)); } break;
		 * 
		 * case 2: // エルフ if(lvl >= 8) { player.sendPackets(new S_SkillBuy(i,
		 * player)); } break;
		 * 
		 * case 3: // WIZ if(lvl >= 4) { player.sendPackets(new S_SkillBuy(i,
		 * player)); } break;
		 * 
		 * case 4: //DE if(lvl >= 12) { player.sendPackets(new S_SkillBuy(i,
		 * player)); } break;
		 * 
		 * default: break; }
		 */
	}

	@Override
	public String getType() {
		return C_SKILL_BUY;
	}

}
