/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.server.utils;

import java.util.List;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.utils:
// FaceToFace

public class FaceToFace {

	private FaceToFace() {
	}

	public static L1PcInstance faceToFace(L1PcInstance pc) {
		int pcX = pc.getX();
		int pcY = pc.getY();
		int pcHeading = pc.getHeading();
		List<L1PcInstance> players = L1World.getInstance().getVisiblePlayer(pc, 1);

		if (players.size() == 0) { // 1セル以内にPCが居ない場合
			pc.sendPackets(new S_ServerMessage(93)); // \f1そこには誰もいません。
			return null;
		}
		for (L1PcInstance target : players) {
			int targetX = target.getX();
			int targetY = target.getY();
			int targetHeading = target.getHeading();
			if ((pcHeading == 0) && (pcX == targetX) && (pcY == (targetY + 1))) {
				if (targetHeading == 4) {
					return target;
				}
				else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0があなたを見ていません。
					return null;
				}
			}
			else if ((pcHeading == 1) && (pcX == (targetX - 1)) && (pcY == (targetY + 1))) {
				if (targetHeading == 5) {
					return target;
				}
				else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0があなたを見ていません。
					return null;
				}
			}
			else if ((pcHeading == 2) && (pcX == (targetX - 1)) && (pcY == targetY)) {
				if (targetHeading == 6) {
					return target;
				}
				else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0があなたを見ていません。
					return null;
				}
			}
			else if ((pcHeading == 3) && (pcX == (targetX - 1)) && (pcY == (targetY - 1))) {
				if (targetHeading == 7) {
					return target;
				}
				else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0があなたを見ていません。
					return null;
				}
			}
			else if ((pcHeading == 4) && (pcX == targetX) && (pcY == (targetY - 1))) {
				if (targetHeading == 0) {
					return target;
				}
				else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0があなたを見ていません。
					return null;
				}
			}
			else if ((pcHeading == 5) && (pcX == (targetX + 1)) && (pcY == (targetY - 1))) {
				if (targetHeading == 1) {
					return target;
				}
				else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0があなたを見ていません。
					return null;
				}
			}
			else if ((pcHeading == 6) && (pcX == (targetX + 1)) && (pcY == targetY)) {
				if (targetHeading == 2) {
					return target;
				}
				else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0があなたを見ていません。
					return null;
				}
			}
			else if ((pcHeading == 7) && (pcX == (targetX + 1)) && (pcY == (targetY + 1))) {
				if (targetHeading == 3) {
					return target;
				}
				else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0があなたを見ていません。
					return null;
				}
			}
		}
		pc.sendPackets(new S_ServerMessage(93)); // \f1そこには誰もいません。
		return null;
	}
}
