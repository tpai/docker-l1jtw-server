/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.templates;

public class L1MobSkill implements Cloneable {
	public static final int TYPE_NONE = 0;

	public static final int TYPE_PHYSICAL_ATTACK = 1;

	public static final int TYPE_MAGIC_ATTACK = 2;

	public static final int TYPE_SUMMON = 3;

	public static final int TYPE_POLY = 4;

	public static final int CHANGE_TARGET_NO = 0;

	public static final int CHANGE_TARGET_COMPANION = 1;

	public static final int CHANGE_TARGET_ME = 2;

	public static final int CHANGE_TARGET_RANDOM = 3;

	private final int skillSize;

	@Override
	public L1MobSkill clone() {
		try {
			return (L1MobSkill) (super.clone());
		} catch (CloneNotSupportedException e) {
			throw (new InternalError(e.getMessage()));
		}
	}

	public int getSkillSize() {
		return skillSize;
	}

	public L1MobSkill(int sSize) {
		skillSize = sSize;

		type = new int[skillSize];
		triRnd = new int[skillSize];
		triHp = new int[skillSize];
		triCompanionHp = new int[skillSize];
		triRange = new int[skillSize];
		triCount = new int[skillSize];
		changeTarget = new int[skillSize];
		range = new int[skillSize];
		areaWidth = new int[skillSize];
		areaHeight = new int[skillSize];
		leverage = new int[skillSize];
		skillId = new int[skillSize];
		gfxid = new int[skillSize];
		actid = new int[skillSize];
		summon = new int[skillSize];
		summonMin = new int[skillSize];
		summonMax = new int[skillSize];
		polyId = new int[skillSize];
	}

	private int mobid;

	public int get_mobid() {
		return mobid;
	}

	public void set_mobid(int i) {
		mobid = i;
	}

	private String mobName;

	public String getMobName() {
		return mobName;
	}

	public void setMobName(String s) {
		mobName = s;
	}

	/*
	 * スキルのタイプ 0→何もしない、1→物理攻撃、2→魔法攻撃、3→サモン
	 */
	private int type[];

	public int getType(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return type[idx];
	}

	public void setType(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		type[idx] = i;
	}

	/*
	 * スキル発動条件：ランダムな確率（0%～100%）でスキル発動
	 */
	private int triRnd[];

	public int getTriggerRandom(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return triRnd[idx];
	}

	public void setTriggerRandom(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		triRnd[idx] = i;
	}

	/*
	 * スキル発動条件：HPが%以下で発動
	 */
	int triHp[];

	public int getTriggerHp(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return triHp[idx];
	}

	public void setTriggerHp(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		triHp[idx] = i;
	}

	/*
	 * スキル発動条件：同族のHPが%以下で発動
	 */
	int triCompanionHp[];

	public int getTriggerCompanionHp(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return triCompanionHp[idx];
	}

	public void setTriggerCompanionHp(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		triCompanionHp[idx] = i;
	}

	/*
	 * スキル発動条件：triRange<0の場合、対象との距離がabs(triRange)以下のとき発動
	 * triRange>0の場合、対象との距離がtriRange以上のとき発動
	 */
	int triRange[];

	public int getTriggerRange(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return triRange[idx];
	}

	public void setTriggerRange(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		triRange[idx] = i;
	}

	// distanceが指定idxスキルの発動条件を満たしているか
	public boolean isTriggerDistance(int idx, int distance) {
		int triggerRange = getTriggerRange(idx);

		if ((triggerRange < 0 && distance <= Math.abs(triggerRange))
				|| (triggerRange > 0 && distance >= triggerRange)) {
			return true;
		}
		return false;
	}

	int triCount[];

	/*
	 * スキル発動条件：スキルの発動回数がtriCount以下のとき発動
	 */
	public int getTriggerCount(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return triCount[idx];
	}

	public void setTriggerCount(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		triCount[idx] = i;
	}

	/*
	 * スキル発動時、ターゲットを変更するか
	 */
	int changeTarget[];

	public int getChangeTarget(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return changeTarget[idx];
	}

	public void setChangeTarget(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		changeTarget[idx] = i;
	}

	/*
	 * rangeまでの距離ならば攻撃可能、物理攻撃をするならば近接攻撃の場合でも1以上を設定
	 */
	int range[];

	public int getRange(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return range[idx];
	}

	public void setRange(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		range[idx] = i;
	}

	/*
	 * 範囲攻撃の横幅、単体攻撃ならば0を設定、範囲攻撃するならば0以上を設定
	 * WidthとHeightの設定は攻撃者からみて横幅をWidth、奥行きをHeightとする。
	 * Widthは+-あるので、1を指定すれば、ターゲットを中心として左右1までが対象となる。
	 */
	int areaWidth[];

	public int getAreaWidth(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return areaWidth[idx];
	}

	public void setAreaWidth(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		areaWidth[idx] = i;
	}

	/*
	 * 範囲攻撃の高さ、単体攻撃ならば0を設定、範囲攻撃するならば1以上を設定
	 */
	int areaHeight[];

	public int getAreaHeight(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return areaHeight[idx];
	}

	public void setAreaHeight(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		areaHeight[idx] = i;
	}

	/*
	 * ダメージの倍率、1/10で表す。物理攻撃、魔法攻撃共に有効
	 */
	int leverage[];

	public int getLeverage(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return leverage[idx];
	}

	public void setLeverage(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		leverage[idx] = i;
	}

	/*
	 * 魔法を使う場合、SkillIdを指定
	 */
	int skillId[];

	public int getSkillId(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return skillId[idx];
	}

	public void setSkillId(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		skillId[idx] = i;
	}

	/*
	 * 物理攻撃のモーショングラフィック
	 */
	int gfxid[];

	public int getGfxid(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return gfxid[idx];
	}

	public void setGfxid(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		gfxid[idx] = i;
	}

	/*
	 * 物理攻撃のグラフィックのアクションID
	 */
	int actid[];

	public int getActid(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return actid[idx];
	}

	public void setActid(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		actid[idx] = i;
	}

	/*
	 * サモンするモンスターのNPCID
	 */
	int summon[];

	public int getSummon(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return summon[idx];
	}

	public void setSummon(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		summon[idx] = i;
	}

	/*
	 * サモンするモンスターの最少数
	 */
	int summonMin[];

	public int getSummonMin(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return summonMin[idx];
	}

	public void setSummonMin(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		summonMin[idx] = i;
	}

	/*
	 * サモンするモンスターの最大数
	 */
	int summonMax[];

	public int getSummonMax(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return summonMax[idx];
	}

	public void setSummonMax(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		summonMax[idx] = i;
	}

	/*
	 * 何に強制変身させるか
	 */
	int polyId[];

	public int getPolyId(int idx) {
		if (idx < 0 || idx >= getSkillSize()) {
			return 0;
		}
		return polyId[idx];
	}

	public void setPolyId(int idx, int i) {
		if (idx < 0 || idx >= getSkillSize()) {
			return;
		}
		polyId[idx] = i;
	}
}
