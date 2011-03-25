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
import l1j.server.server.ActionCodes;
import l1j.server.server.ClientThread;
import l1j.server.server.model.AcceleratorChecker;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_UseArrowSkill;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理客戶端傳來攻擊的封包
 */
public class C_Attack extends ClientBasePacket {

	private int _targetX = 0;

	private int _targetY = 0;

	public C_Attack(byte[] decrypt, ClientThread client) {
		super(decrypt);
		int targetId = readD();
		int x = readH();
		int y = readH();
		_targetX = x;
		_targetY = y;

		L1PcInstance pc = client.getActiveChar();

		if (pc.isGhost() || pc.isDead() || pc.isTeleport()) {
			return;
		}

		L1Object target = L1World.getInstance().findObject(targetId);

		// 確認是否可以攻擊
		if (pc.getInventory().getWeight240() >= 197) { // 是否超重
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
			if (result == AcceleratorChecker.R_DISCONNECTED) {
				return;
			}
		}

		// 如果在絕對屏障攻擊別人
		if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) { // 取消絕對屏障
			pc.killSkillEffectTimer(ABSOLUTE_BARRIER);
			pc.startHpRegeneration();
			pc.startMpRegeneration();
			pc.startMpRegenerationByDoll();
		}
		pc.killSkillEffectTimer(MEDITATION);

		pc.delInvis(); // 解除隱形狀態

		pc.setRegenState(REGENSTATE_ATTACK);

		if ((target != null) && !((L1Character) target).isDead()) {
			target.onAction(pc);
		} else { // 空攻撃
			L1ItemInstance weapon = pc.getWeapon();
			int weaponId = 0;
			int weaponType = 0;
			L1ItemInstance arrow = null;
			L1ItemInstance sting = null;
			if (weapon != null) {
				weaponId = weapon.getItem().getItemId();
				weaponType = weapon.getItem().getType1();
				if (weaponType == 20) {
					arrow = pc.getInventory().getArrow();
				}
				if (weaponType == 62) {
					sting = pc.getInventory().getSting();
				}
			}
			pc.setHeading(pc.targetDirection(x, y));
			if ((weaponType == 20) && ((weaponId == 190) || (arrow != null))) {
				calcOrbit(pc.getX(), pc.getY(), pc.getHeading()); // 軌道計算
				if (arrow != null) { // 使用弓箭
					pc.sendPackets(new S_UseArrowSkill(pc, 0, 66, _targetX,
							_targetY, true));
					pc.broadcastPacket(new S_UseArrowSkill(pc, 0, 66, _targetX,
							_targetY, true));
					pc.getInventory().removeItem(arrow, 1);
				} else if (weaponId == 190) { // 撒哈弓
					pc.sendPackets(new S_UseArrowSkill(pc, 0, 2349, _targetX,
							_targetY, true));
					pc.broadcastPacket(new S_UseArrowSkill(pc, 0, 2349,
							_targetX, _targetY, true));
				}
			} else if ((weaponType == 62) && (sting != null)) {
				calcOrbit(pc.getX(), pc.getY(), pc.getHeading()); // 軌道計算
				pc.sendPackets(new S_UseArrowSkill(pc, 0, 2989, _targetX,
						_targetY, true));
				pc.broadcastPacket(new S_UseArrowSkill(pc, 0, 2989, _targetX,
						_targetY, true));
				pc.getInventory().removeItem(sting, 1);
			} else {
				pc.sendPackets(new S_AttackPacket(pc, 0,
						ActionCodes.ACTION_Attack));
				pc.broadcastPacket(new S_AttackPacket(pc, 0,
						ActionCodes.ACTION_Attack));
			}
		}
	}

	private void calcOrbit(int cX, int cY, int head) {
		float disX = Math.abs(cX - _targetX);
		float disY = Math.abs(cY - _targetY);
		float dis = Math.max(disX, disY);
		float avgX = 0;
		float avgY = 0;
		if (dis == 0) {
			if (head == 1) {
				avgX = 1;
				avgY = -1;
			} else if (head == 2) {
				avgX = 1;
				avgY = 0;
			} else if (head == 3) {
				avgX = 1;
				avgY = 1;
			} else if (head == 4) {
				avgX = 0;
				avgY = 1;
			} else if (head == 5) {
				avgX = -1;
				avgY = 1;
			} else if (head == 6) {
				avgX = -1;
				avgY = 0;
			} else if (head == 7) {
				avgX = -1;
				avgY = -1;
			} else if (head == 0) {
				avgX = 0;
				avgY = -1;
			}
		} else {
			avgX = disX / dis;
			avgY = disY / dis;
		}

		int addX = (int) Math.floor((avgX * 15) + 0.59f);
		int addY = (int) Math.floor((avgY * 15) + 0.59f);

		if (cX > _targetX) {
			addX *= -1;
		}
		if (cY > _targetY) {
			addY *= -1;
		}

		_targetX = _targetX + addX;
		_targetY = _targetY + addY;
	}
}
