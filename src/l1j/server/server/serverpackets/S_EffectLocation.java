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
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Location;
import l1j.server.server.types.Point;

public class S_EffectLocation extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * 指定された位置へエフェクトを表示するパケットを構築する。
	 * 
	 * @param pt - エフェクトを表示する位置を格納したPointオブジェクト
	 * @param gfxId - 表示するエフェクトのID
	 */
	public S_EffectLocation(Point pt, int gfxId) {
		this(pt.getX(), pt.getY(), gfxId);
	}

	/**
	 * 指定された位置へエフェクトを表示するパケットを構築する。
	 * 
	 * @param loc - エフェクトを表示する位置を格納したL1Locationオブジェクト
	 * @param gfxId - 表示するエフェクトのID
	 */
	public S_EffectLocation(L1Location loc, int gfxId) {
		this(loc.getX(), loc.getY(), gfxId);
	}

	/**
	 * 指定された位置へエフェクトを表示するパケットを構築する。
	 * 
	 * @param x - エフェクトを表示する位置のX座標
	 * @param y - エフェクトを表示する位置のY座標
	 * @param gfxId - 表示するエフェクトのID
	 */
	public S_EffectLocation(int x, int y, int gfxId) {
		writeC(Opcodes.S_OPCODE_EFFECTLOCATION);
		writeH(x);
		writeH(y);
		writeH(gfxId);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}

		return _byte;
	}
}
