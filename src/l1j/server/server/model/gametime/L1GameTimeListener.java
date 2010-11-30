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
package l1j.server.server.model.gametime;

/**
 * <p>
 * アデン時間の変化を受け取るためのリスナーインターフェース。
 * </p>
 * <p>
 * アデン時間の変化を監視すべきクラスは、このインターフェースに含まれているすべてのメソッドを定義してこのインターフェースを実装するか、
 * 関連するメソッドだけをオーバーライドしてabstractクラスL1GameTimeAdapterを拡張する。
 * </p>
 * <p>
 * そのようなクラスから作成されたリスナーオブジェクトは、L1GameTimeClockのaddListenerメソッドを使用してL1GameTimeClockに登録される。
 * アデン時間変化の通知は、月日時分がそれぞれ変わったときに行われる。
 * </p>
 * <p>
 * これらのメソッドは、L1GameTimeClockのスレッド上で動作する。
 * これらのメソッドの処理に時間がかかった場合、他のリスナーへの通知が遅れる可能性がある。
 * 完了までに時間を要する処理や、スレッドをブロックするメソッドの呼び出しが含まれる処理を行う場合は、内部で新たにスレッドを作成して処理を行うべきである。
 * </p>
 * 
 */
public interface L1GameTimeListener {
	/**
	 * アデン時間で月が変わったときに呼び出される。
	 * 
	 * @param time
	 *            最新のアデン時間
	 */
	public void onMonthChanged(L1GameTime time);

	/**
	 * アデン時間で日が変わったときに呼び出される。
	 * 
	 * @param time
	 *            最新のアデン時間
	 */
	public void onDayChanged(L1GameTime time);

	/**
	 * アデン時間で時間が変わったときに呼び出される。
	 * 
	 * @param time
	 *            最新のアデン時間
	 */
	public void onHourChanged(L1GameTime time);

	/**
	 * アデン時間で分が変わったときに呼び出される。
	 * 
	 * @param time
	 *            最新のアデン時間
	 */
	public void onMinuteChanged(L1GameTime time);
}
