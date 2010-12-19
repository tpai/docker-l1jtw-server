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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChangeHeading;

/**
 * 收到由客戶端傳來改變面向的封包
 */
public class C_ChangeHeading extends ClientBasePacket {
	private static final String C_CHANGE_HEADING = "[C] C_ChangeHeading";
	private static Logger _log = Logger.getLogger(C_ChangeHeading.class
			.getName());

	public C_ChangeHeading(byte[] decrypt, ClientThread client) {
		super(decrypt);
		int heading = readC();

		L1PcInstance pc = client.getActiveChar();

		pc.setHeading(heading);

		_log.finest("Change Heading : " + pc.getHeading());

		if (pc.isGmInvis() || pc.isGhost()) {
		} else if (pc.isInvisble()) {
			pc.broadcastPacketForFindInvis(new S_ChangeHeading(pc), true);
		} else {
			pc.broadcastPacket(new S_ChangeHeading(pc));
		}
	}

	@Override
	public String getType() {
		return C_CHANGE_HEADING;
	}
}