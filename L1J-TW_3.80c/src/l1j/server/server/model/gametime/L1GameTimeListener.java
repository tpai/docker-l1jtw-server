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
