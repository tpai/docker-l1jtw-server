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
