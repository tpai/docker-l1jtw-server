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
package l1j.server.server.serverpackets;

import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_UseAttackSkill extends ServerBasePacket {

	private static final String S_USE_ATTACK_SKILL = "[S] S_UseAttackSkill";

	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

	private byte[] _byte = null;

	// public S_UseAttackSkill(L1Character caster, L1Character target,
	// int spellgfx, boolean motion) {
	// Point pt = target.getLocation();
	// buildPacket(caster, target.getId(), spellgfx, pt.getX(), pt.getY(),
	// ActionCodes.ACTION_SkillAttack, 6, motion);
	// }

	// public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx,
	// int x, int y) {
	// buildPacket(cha, targetobj, spellgfx, x, y,
	// ActionCodes.ACTION_SkillAttack, 6, true);
	// }

	public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId) {
		buildPacket(cha, targetobj, spellgfx, x, y, actionId, 6, true);
	}

	public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, boolean motion) {
		buildPacket(cha, targetobj, spellgfx, x, y, actionId, 0, motion);
	}

	public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int isHit) {
		buildPacket(cha, targetobj, spellgfx, x, y, actionId, isHit, true);
	}

	private void buildPacket(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int isHit, boolean withCastMotion) {
		if (cha instanceof L1PcInstance) {
			// シャドウ系変身中に攻撃魔法を使用するとクライアントが落ちるため暫定対応
			if (cha.hasSkillEffect(SHAPE_CHANGE) && (actionId == ActionCodes.ACTION_SkillAttack)) {
				int tempchargfx = cha.getTempCharGfx();
				if ((tempchargfx == 5727) || (tempchargfx == 5730)) {
					actionId = ActionCodes.ACTION_SkillBuff;
				}
				else if ((tempchargfx == 5733) || (tempchargfx == 5736)) {
					// 補助魔法モーションにすると攻撃魔法のグラフィックと
					// 対象へのダメージモーションが発生しなくなるため
					// 攻撃モーションで代用
					actionId = ActionCodes.ACTION_Attack;
				}
			}
		}
		// 火の精の主がデフォルトだと攻撃魔法のグラフィックが発生しないので強制置き換え
		// どこか別で管理した方が良い？
		if (cha.getTempCharGfx() == 4013) {
			actionId = ActionCodes.ACTION_Attack;
		}

		int newheading = calcheading(cha.getX(), cha.getY(), x, y);
		cha.setHeading(newheading);
		writeC(Opcodes.S_OPCODE_ATTACKPACKET);
		writeC(actionId);
		writeD(withCastMotion ? cha.getId() : 0);
		writeD(targetobj);
		writeH(isHit);
		writeC(newheading);
		writeD(_sequentialNumber.incrementAndGet()); // 番号がダブらないように送る。
		writeH(spellgfx);
		writeC(6); // 0:弓箭 6:遠距離魔法 8:遠距離範圍魔法
		writeH(cha.getX());
		writeH(cha.getY());
		writeH(x);
		writeH(y);
		writeC(0);
		writeC(0);
		writeC(0); // 0:none 2:爪痕 4:雙擊 8:鏡返射
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		else {
			int seq = _sequentialNumber.incrementAndGet();
			_byte[13] = (byte) (seq & 0xff);
			_byte[14] = (byte) (seq >> 8 & 0xff);
			_byte[15] = (byte) (seq >> 16 & 0xff);
			_byte[16] = (byte) (seq >> 24 & 0xff);
		}

		return _byte;
	}

	private static int calcheading(int myx, int myy, int tx, int ty) {
		int newheading = 0;
		if ((tx > myx) && (ty > myy)) {
			newheading = 3;
		}
		if ((tx < myx) && (ty < myy)) {
			newheading = 7;
		}
		if ((tx > myx) && (ty == myy)) {
			newheading = 2;
		}
		if ((tx < myx) && (ty == myy)) {
			newheading = 6;
		}
		if ((tx == myx) && (ty < myy)) {
			newheading = 0;
		}
		if ((tx == myx) && (ty > myy)) {
			newheading = 4;
		}
		if ((tx < myx) && (ty > myy)) {
			newheading = 5;
		}
		if ((tx > myx) && (ty < myy)) {
			newheading = 1;
		}
		return newheading;
	}

	@Override
	public String getType() {
		return S_USE_ATTACK_SKILL;
	}

}