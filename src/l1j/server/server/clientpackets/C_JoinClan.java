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

package l1j.server.server.clientpackets;

import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.FaceToFace;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來加入血盟的封包
 */
public class C_JoinClan extends ClientBasePacket {

	private static final String C_JOIN_CLAN = "[C] C_JoinClan";

	public C_JoinClan(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc.isGhost()) {
			return;
		}

		L1PcInstance target = FaceToFace.faceToFace(pc);
		if (target != null) {
			JoinClan(pc, target);
		}
	}

	private void JoinClan(L1PcInstance player, L1PcInstance target) {
		if (!target.isCrown()) { // 如果面對的對象不是王族
			player.sendPackets(new S_ServerMessage(92, target.getName())); // \f1%0はプリンスやプリンセスではありません。
			return;
		}

		int clan_id = target.getClanid();
		String clan_name = target.getClanname();
		if (clan_id == 0) { // 面對的對象沒有創立血盟
			player.sendPackets(new S_ServerMessage(90, target.getName())); // \f1%0は血盟を創設していない状態です。
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(clan_name);
		if (clan == null) {
			return;
		}

		if (target.getId() != clan.getLeaderId()) { // 面對的對象不是盟主
			player.sendPackets(new S_ServerMessage(92, target.getName())); // \f1%0はプリンスやプリンセスではありません。
			return;
		}

		if (player.getClanid() != 0) { // 已經加入血盟
			if (player.isCrown()) { // 自己是盟主
				String player_clan_name = player.getClanname();
				L1Clan player_clan = L1World.getInstance().getClan(player_clan_name);
				if (player_clan == null) {
					return;
				}

				if (player.getId() != player_clan.getLeaderId()) { // 已經加入其他血盟
					player.sendPackets(new S_ServerMessage(89)); // \f1あなたはすでに血盟に加入しています。
					return;
				}

				if ((player_clan.getCastleId() != 0) || // 有城堡或有血盟小屋
						(player_clan.getHouseId() != 0)) {
					player.sendPackets(new S_ServerMessage(665)); // \f1城やアジトを所有した状態で血盟を解散することはできません。
					return;
				}
			}
			else {
				player.sendPackets(new S_ServerMessage(89)); // \f1あなたはすでに血盟に加入しています。
				return;
			}
		}

		target.setTempID(player.getId()); // 暫時保存面對的人的ID
		target.sendPackets(new S_Message_YN(97, player.getName())); // %0が血盟に加入したがっています。承諾しますか？（Y/N）
	}

	@Override
	public String getType() {
		return C_JOIN_CLAN;
	}
}
