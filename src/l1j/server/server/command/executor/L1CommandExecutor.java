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
package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;

/**
 * コマンド実行処理インターフェース
 * 
 * コマンド処理クラスは、このインターフェースメソッド以外に<br>
 * public static L1CommandExecutor getInstance()<br>
 * を実装しなければならない。
 * 通常、自クラスをインスタンス化して返すが、必要に応じてキャッシュされたインスタンスを返したり、他のクラスをインスタンス化して返すことができる。
 */
public interface L1CommandExecutor {
	/**
	 * このコマンドを実行する。
	 * 
	 * @param pc
	 *            実行者
	 * @param cmdName
	 *            実行されたコマンド名
	 * @param arg
	 *            引数
	 */
	public void execute(L1PcInstance pc, String cmdName, String arg);
}
