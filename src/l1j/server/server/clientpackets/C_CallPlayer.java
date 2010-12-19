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

import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket


/**
 * 收到由客戶端傳來呼叫玩家的封包
 */
public class C_CallPlayer extends ClientBasePacket {

	private static final String C_CALL = "[C] C_Call";

	private static Logger _log = Logger.getLogger(C_CallPlayer.class.getName());

	public C_CallPlayer(byte[] decrypt, ClientThread client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();

		if (!pc.isGm()) {
			return;
		}

		String name = readS();
		if (name.isEmpty()) {
			return;
		}

		L1PcInstance target = L1World.getInstance().getPlayer(name);

		if (target == null) {
			return;
		}

		L1Location loc =
				L1Location.randomLocation(target.getLocation(), 1, 2, false);
		L1Teleport.teleport(pc, loc.getX(), loc.getY(), target.getMapId(), pc
				.getHeading(), false);
	}

	@Override
	public String getType() {
		return C_CALL;
	}
}
