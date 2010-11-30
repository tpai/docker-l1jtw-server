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

package l1j.server.server.model.map;

import l1j.server.server.types.Point;

/**
 * L1Map マップ情報を保持し、それに対する様々なインターフェースを提供する。
 */
public abstract class L1Map {
	private static L1NullMap _nullMap = new L1NullMap();

	protected L1Map() {
	}

	/**
	 * このマップのマップIDを返す。
	 * 
	 * @return マップID
	 */
	public abstract int getId();

	// TODO JavaDoc
	public abstract int getX();

	public abstract int getY();

	public abstract int getWidth();

	public abstract int getHeight();

	/**
	 * 指定された座標の値を返す。
	 * 
	 * 推奨されていません。このメソッドは、既存コードとの互換性の為に提供されています。
	 * L1Mapの利用者は通常、マップにどのような値が格納されているかを知る必要はありません。
	 * また、格納されている値に依存するようなコードを書くべきではありません。 デバッグ等の特殊な場合に限り、このメソッドを利用できます。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return 指定された座標の値
	 */
	public abstract int getTile(int x, int y);

	/**
	 * 指定された座標の値を返す。
	 * 
	 * 推奨されていません。このメソッドは、既存コードとの互換性の為に提供されています。
	 * L1Mapの利用者は通常、マップにどのような値が格納されているかを知る必要はありません。
	 * また、格納されている値に依存するようなコードを書くべきではありません。 デバッグ等の特殊な場合に限り、このメソッドを利用できます。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return 指定された座標の値
	 */
	public abstract int getOriginalTile(int x, int y);

	/**
	 * 指定された座標がマップの範囲内であるかを返す。
	 * 
	 * @param pt
	 *            座標を保持するPointオブジェクト
	 * @return 範囲内であればtrue
	 */
	public abstract boolean isInMap(Point pt);

	/**
	 * 指定された座標がマップの範囲内であるかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return 範囲内であればtrue
	 */
	public abstract boolean isInMap(int x, int y);

	/**
	 * 指定された座標が通行可能であるかを返す。
	 * 
	 * @param pt
	 *            座標を保持するPointオブジェクト
	 * @return 通行可能であればtrue
	 */
	public abstract boolean isPassable(Point pt);

	/**
	 * 指定された座標が通行可能であるかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return 通行可能であればtrue
	 */
	public abstract boolean isPassable(int x, int y);

	/**
	 * 指定された座標のheading方向が通行可能であるかを返す。
	 * 
	 * @param pt
	 *            座標を保持するPointオブジェクト
	 * @return 通行可能であればtrue
	 */
	public abstract boolean isPassable(Point pt, int heading);

	/**
	 * 指定された座標のheading方向が通行可能であるかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return 通行可能であればtrue
	 */
	public abstract boolean isPassable(int x, int y, int heading);

	/**
	 * 指定された座標の通行可能、不能を設定する。
	 * 
	 * @param pt
	 *            座標を保持するPointオブジェクト
	 * @param isPassable
	 *            通行可能であればtrue
	 */
	public abstract void setPassable(Point pt, boolean isPassable);

	/**
	 * 指定された座標の通行可能、不能を設定する。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @param isPassable
	 *            通行可能であればtrue
	 */
	public abstract void setPassable(int x, int y, boolean isPassable);

	/**
	 * 指定された座標がセーフティーゾーンであるかを返す。
	 * 
	 * @param pt
	 *            座標を保持するPointオブジェクト
	 * @return セーフティーゾーンであればtrue
	 */
	public abstract boolean isSafetyZone(Point pt);

	/**
	 * 指定された座標がセーフティーゾーンであるかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return セーフティーゾーンであればtrue
	 */
	public abstract boolean isSafetyZone(int x, int y);

	/**
	 * 指定された座標がコンバットゾーンであるかを返す。
	 * 
	 * @param pt
	 *            座標を保持するPointオブジェクト
	 * @return コンバットゾーンであればtrue
	 */
	public abstract boolean isCombatZone(Point pt);

	/**
	 * 指定された座標がコンバットゾーンであるかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return コンバットゾーンであればtrue
	 */
	public abstract boolean isCombatZone(int x, int y);

	/**
	 * 指定された座標がノーマルゾーンであるかを返す。
	 * 
	 * @param pt
	 *            座標を保持するPointオブジェクト
	 * @return ノーマルゾーンであればtrue
	 */
	public abstract boolean isNormalZone(Point pt);

	/**
	 * 指定された座標がノーマルゾーンであるかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return ノーマルゾーンであればtrue
	 */
	public abstract boolean isNormalZone(int x, int y);

	/**
	 * 指定された座標が矢や魔法を通すかを返す。
	 * 
	 * @param pt
	 *            座標を保持するPointオブジェクト
	 * @return 矢や魔法を通す場合、true
	 */
	public abstract boolean isArrowPassable(Point pt);

	/**
	 * 指定された座標が矢や魔法を通すかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return 矢や魔法を通す場合、true
	 */
	public abstract boolean isArrowPassable(int x, int y);

	/**
	 * 指定された座標のheading方向が矢や魔法を通すかを返す。
	 * 
	 * @param pt
	 *            座標を保持するPointオブジェクト
	 * @param heading
	 *            方向
	 * @return 矢や魔法を通す場合、true
	 */
	public abstract boolean isArrowPassable(Point pt, int heading);

	/**
	 * 指定された座標のheading方向が矢や魔法を通すかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @param heading
	 *            方向
	 * @return 矢や魔法を通す場合、true
	 */
	public abstract boolean isArrowPassable(int x, int y, int heading);

	/**
	 * このマップが、水中マップであるかを返す。
	 * 
	 * @return 水中であれば、true
	 */
	public abstract boolean isUnderwater();

	/**
	 * このマップが、ブックマーク可能であるかを返す。
	 * 
	 * @return ブックマーク可能であれば、true
	 */
	public abstract boolean isMarkable();

	/**
	 * このマップが、ランダムテレポート可能であるかを返す。
	 * 
	 * @return ランダムテレポート可能であれば、true
	 */
	public abstract boolean isTeleportable();

	/**
	 * このマップが、MAPを超えたテレポート可能であるかを返す。
	 * 
	 * @return テレポート可能であれば、true
	 */
	public abstract boolean isEscapable();

	/**
	 * このマップが、復活可能であるかを返す。
	 * 
	 * @return 復活可能であれば、true
	 */
	public abstract boolean isUseResurrection();

	/**
	 * このマップが、パインワンド使用可能であるかを返す。
	 * 
	 * @return パインワンド使用可能であれば、true
	 */
	public abstract boolean isUsePainwand();

	/**
	 * このマップが、デスペナルティがあるかを返す。
	 * 
	 * @return デスペナルティがあれば、true
	 */
	public abstract boolean isEnabledDeathPenalty();

	/**
	 * このマップが、ペット・サモンを連れて行けるかを返す。
	 * 
	 * @return ペット・サモンを連れて行けるならばtrue
	 */
	public abstract boolean isTakePets();

	/**
	 * このマップが、ペット・サモンを呼び出せるかを返す。
	 * 
	 * @return ペット・サモンを呼び出せるならばtrue
	 */
	public abstract boolean isRecallPets();

	/**
	 * このマップが、アイテムを使用できるかを返す。
	 * 
	 * @return アイテムを使用できるならばtrue
	 */
	public abstract boolean isUsableItem();

	/**
	 * このマップが、スキルを使用できるかを返す。
	 * 
	 * @return スキルを使用できるならばtrue
	 */
	public abstract boolean isUsableSkill();

	/**
	 * 指定された座標が釣りゾーンであるかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return 釣りゾーンであればtrue
	 */
    public abstract boolean isFishingZone(int x, int y);

	/**
	 * 指定された座標にドアが存在するかを返す。
	 * 
	 * @param x
	 *            座標のX値
	 * @param y
	 *            座標のY値
	 * @return ドアがあればtrue
	 */
    public abstract boolean isExistDoor(int x, int y);

	public static L1Map newNull() {
		return _nullMap;
	}

	/**
	 * 指定されたptのタイルの文字列表現を返す。
	 */
	public abstract String toString(Point pt);

	/**
	 * このマップがnullであるかを返す。
	 * 
	 * @return nullであれば、true
	 */
	public boolean isNull() {
		return false;
	}
}

/**
 * 何もしないMap。
 */
class L1NullMap extends L1Map {
	public L1NullMap() {
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getTile(int x, int y) {
		return 0;
	}

	@Override
	public int getOriginalTile(int x, int y) {
		return 0;
	}

	@Override
	public boolean isInMap(int x, int y) {
		return false;
	}

	@Override
	public boolean isInMap(Point pt) {
		return false;
	}

	@Override
	public boolean isPassable(int x, int y) {
		return false;
	}

	@Override
	public boolean isPassable(Point pt) {
		return false;
	}

	@Override
	public boolean isPassable(int x, int y, int heading) {
		return false;
	}

	@Override
	public boolean isPassable(Point pt, int heading) {
		return false;
	}

	@Override
	public void setPassable(int x, int y, boolean isPassable) {
	}

	@Override
	public void setPassable(Point pt, boolean isPassable) {
	}

	@Override
	public boolean isSafetyZone(int x, int y) {
		return false;
	}

	@Override
	public boolean isSafetyZone(Point pt) {
		return false;
	}

	@Override
	public boolean isCombatZone(int x, int y) {
		return false;
	}

	@Override
	public boolean isCombatZone(Point pt) {
		return false;
	}

	@Override
	public boolean isNormalZone(int x, int y) {
		return false;
	}

	@Override
	public boolean isNormalZone(Point pt) {
		return false;
	}

	@Override
	public boolean isArrowPassable(int x, int y) {
		return false;
	}

	@Override
	public boolean isArrowPassable(Point pt) {
		return false;
	}

	@Override
	public boolean isArrowPassable(int x, int y, int heading) {
		return false;
	}

	@Override
	public boolean isArrowPassable(Point pt, int heading) {
		return false;
	}

	@Override
	public boolean isUnderwater() {
		return false;
	}

	@Override
	public boolean isMarkable() {
		return false;
	}

	@Override
	public boolean isTeleportable() {
		return false;
	}

	@Override
	public boolean isEscapable() {
		return false;
	}

	@Override
	public boolean isUseResurrection() {
		return false;
	}

	@Override
	public boolean isUsePainwand() {
		return false;
	}

	@Override
	public boolean isEnabledDeathPenalty() {
		return false;
	}

	@Override
	public boolean isTakePets() {
		return false;
	}

	@Override
	public boolean isRecallPets() {
		return false;
	}

	@Override
	public boolean isUsableItem() {
		return false;
	}

	@Override
	public boolean isUsableSkill() {
		return false;
	}

	@Override
	public boolean isFishingZone(int x, int y) {
		return false;
	}

	@Override
	public boolean isExistDoor(int x, int y) {
		return false;
	}

	@Override
	public String toString(Point pt) {
		return "null";
	}

	@Override
	public boolean isNull() {
		return true;
	}
}
