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
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharPack;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來搭船的封包
 */
public class C_Ship extends ClientBasePacket {

	private static final String C_SHIP = "[C] C_Ship";

	public C_Ship(byte abyte0[], ClientThread client) {
		super(abyte0);

		int shipMapId = readH();
		int locX = readH();
		int locY = readH();

		L1PcInstance pc = client.getActiveChar();
		int mapId = pc.getMapId();

		if (mapId == 5) { // Talking Island Ship to Aden Mainland
			pc.getInventory().consumeItem(40299, 1);
		}
		else if (mapId == 6) { // Aden Mainland Ship to Talking Island
			pc.getInventory().consumeItem(40298, 1);
		}
		else if (mapId == 83) { // Forgotten Island Ship to Aden Mainland
			pc.getInventory().consumeItem(40300, 1);
		}
		else if (mapId == 84) { // Aden Mainland Ship to Forgotten Island
			pc.getInventory().consumeItem(40301, 1);
		}
		else if (mapId == 446) { // Ship Hidden dock to Pirate island
			pc.getInventory().consumeItem(40303, 1);
		}
		else if (mapId == 447) { // Ship Pirate island to Hidden dock
			pc.getInventory().consumeItem(40302, 1);
		}
		pc.sendPackets(new S_OwnCharPack(pc));
		L1Teleport.teleport(pc, locX, locY, (short) shipMapId, 0, false);
	}

	@Override
	public String getType() {
		return C_SHIP;
	}
}
