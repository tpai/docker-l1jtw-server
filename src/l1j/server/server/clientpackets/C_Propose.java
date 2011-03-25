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
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.FaceToFace;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來結婚的封包
 */
public class C_Propose extends ClientBasePacket {

	private static final String C_PROPOSE = "[C] C_Propose";

	public C_Propose(byte abyte0[], ClientThread clientthread) {
		super(abyte0);
		int c = readC();

		L1PcInstance pc = clientthread.getActiveChar();
		if (c == 0) { // /propose（/結婚）
			if (pc.isGhost()) {
				return;
			}
			L1PcInstance target = FaceToFace.faceToFace(pc);
			if (target != null) {
				if (pc.getPartnerId() != 0) {
					pc.sendPackets(new S_ServerMessage(657)); // \f1あなたはすでに結婚しています。
					return;
				}
				if (target.getPartnerId() != 0) {
					pc.sendPackets(new S_ServerMessage(658)); // \f1その相手はすでに結婚しています。
					return;
				}
				if (pc.get_sex() == target.get_sex()) {
					pc.sendPackets(new S_ServerMessage(661)); // \f1結婚相手は異性でなければなりません。
					return;
				}
				if ((pc.getX() >= 33974) && (pc.getX() <= 33976) && (pc.getY() >= 33362) && (pc.getY() <= 33365) && (pc.getMapId() == 4)
						&& (target.getX() >= 33974) && (target.getX() <= 33976) && (target.getY() >= 33362) && (target.getY() <= 33365)
						&& (target.getMapId() == 4)) {
					target.setTempID(pc.getId()); // 暫時儲存對象的角色ID
					target.sendPackets(new S_Message_YN(654, pc.getName())); // %0%sあなたと結婚したがっています。%0と結婚しますか？（Y/N）
				}
			}
		}
		else if (c == 1) { // /divorce（/離婚）
			if (pc.getPartnerId() == 0) {
				pc.sendPackets(new S_ServerMessage(662)); // \f1あなたは結婚していません。
				return;
			}
			pc.sendPackets(new S_Message_YN(653, "")); // 離婚をするとリングは消えてしまいます。離婚を望みますか？（Y/N）
		}
	}

	@Override
	public String getType() {
		return C_PROPOSE;
	}
}
