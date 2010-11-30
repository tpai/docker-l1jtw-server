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

package l1j.server.server.clientpackets;

import java.util.logging.Logger;

import l1j.server.server.ClientThread;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * TODO: 尚未實裝僱用傭兵的處理
 * 處理收到由客戶端傳來僱用傭兵的封包
 */
public class C_HireSoldier extends ClientBasePacket {

	private static final String C_HIRE_SOLDIER = "[C] C_HireSoldier";

	private static Logger _log = Logger.getLogger(C_HireSoldier.class
			.getName());

	// S_HireSoldierを送ると表示される雇用ウィンドウでOKを押すとこのパケットが送られる
	public C_HireSoldier(byte[] decrypt, ClientThread client) {
		super(decrypt);
		int something1 = readH(); // S_HireSoldierパケットの引数
		int something2 = readH(); // S_HireSoldierパケットの引数
		int something3 = readD(); // 1以外入らない？
		int something4 = readD(); // S_HireSoldierパケットの引数
		int number = readH(); // 雇用する数
		
		// < 傭兵雇用処理
	}

	@Override
	public String getType() {
		return C_HIRE_SOLDIER;
	}
}
