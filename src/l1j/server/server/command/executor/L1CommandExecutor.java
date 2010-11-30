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
