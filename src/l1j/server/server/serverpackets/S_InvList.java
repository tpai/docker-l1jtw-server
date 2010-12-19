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

import java.util.List;
import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_InvList extends ServerBasePacket {

	private static Logger _log = Logger.getLogger(S_InvList.class.getName());

	private static final String S_INV_LIST = "[S] S_InvList";

	/**
	 * インベントリにアイテムを複数個まとめて追加する。
	 */
	public S_InvList(List<L1ItemInstance> items) {
		writeC(Opcodes.S_OPCODE_INVLIST);
		writeC(items.size()); // アイテム数

		for (L1ItemInstance item : items) {
			writeD(item.getId());
			writeC(item.getItem().getUseType());
			writeC(0);
			writeH(item.get_gfxid());
			writeC(item.getBless());
			writeD(item.getCount());
			writeC((item.isIdentified()) ? 1 : 0);
			writeS(item.getViewName());
			if (!item.isIdentified()) {
				// 未鑑定の場合ステータスを送る必要はない
				writeC(0);
			} else {
				byte[] status = item.getStatusBytes();
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
			}
		}
	}

	@Override
	public byte[] getContent() {
		return _bao.toByteArray();
	}

	@Override
	public String getType() {
		return S_INV_LIST;
	}
}
