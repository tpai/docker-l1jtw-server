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

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;
import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.MEDITATION;
import l1j.server.Config;
import l1j.server.server.ClientThread;
import l1j.server.server.model.AcceleratorChecker;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理客戶端傳來攻擊的封包
 */
public class C_Attack extends ClientBasePacket {

	public C_Attack(byte[] decrypt, ClientThread client) {
		super(decrypt);
		int targetId = readD();
		int x = readH();
		int y = readH();

		L1PcInstance pc = client.getActiveChar();

		if (pc.isGhost() || pc.isDead() || pc.isTeleport()|| pc.isParalyzed() || pc.isSleeped()) {
			return;
		}

		L1Object target = L1World.getInstance().findObject(targetId);

		// 確認是否可以攻擊
		if (pc.getInventory().getWeight242() >= 197) { // 是否超重
			pc.sendPackets(new S_ServerMessage(110)); // \f1アイテムが重すぎて戦闘することができません。
			return;
		}

		if (pc.isInvisble()) { // 是否隱形
			return;
		}

		if (pc.isInvisDelay()) { // 是否在隱形解除的延遲中
			return;
		}

		if (target instanceof L1Character) {
			if ((target.getMapId() != pc.getMapId())
					|| (pc.getLocation().getLineDistance(target.getLocation()) > 20D)) { // 如果目標距離玩家太遠(外掛)
				return;
			}
		}

		if (target instanceof L1NpcInstance) {
			int hiddenStatus = ((L1NpcInstance) target).getHiddenStatus();
			if ((hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK)
					|| (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_FLY)) { // 如果目標躲到土裡面，或是飛起來了
				return;
			}
		}

		// 是否要檢查攻擊的間隔
		if (Config.CHECK_ATTACK_INTERVAL) {
			int result;
			result = pc.getAcceleratorChecker().checkInterval(
					AcceleratorChecker.ACT_TYPE.ATTACK);
			if (result == AcceleratorChecker.R_DISPOSED) {
				return;
			}
		}

		if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) { // 取消絕對屏障
			pc.removeSkillEffect(ABSOLUTE_BARRIER);
		}
		if (pc.hasSkillEffect(MEDITATION)) { // 取消冥想效果
			pc.removeSkillEffect(MEDITATION);
		}

		pc.delInvis(); // 解除隱形狀態

		pc.setRegenState(REGENSTATE_ATTACK);

		if ((target != null)
				&& !((L1Character) target).isDead()) {
			target.onAction(pc);
		} else { // 目標為空或死亡
			L1Character cha = new L1Character();
			cha.setId(targetId);
			cha.setX(x);
			cha.setY(y);
			L1Attack atk = new L1Attack(pc, cha);
			atk.actionPc();
		}
	}
}
