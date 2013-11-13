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
import l1j.server.server.model.Instance.L1ItemInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SendInvOnLogin

public class S_ItemName extends ServerBasePacket {

	private static final String S_ITEM_NAME = "[S] S_ItemName";

	/**
	 * アイテムの名前を変更する。装備や強化状態が変わったときに送る。
	 */
	public S_ItemName(L1ItemInstance item) {
		if (item == null) {
			return;
		}
		// jumpを見る限り、このOpcodeはアイテム名を更新させる目的だけに使用される模様（装備後やOE後専用？）
		// 後に何かデータを続けて送っても全て無視されてしまう
		writeC(Opcodes.S_OPCODE_ITEMNAME);
		writeD(item.getId());
		writeS(item.getViewName());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_ITEM_NAME;
	}
}
