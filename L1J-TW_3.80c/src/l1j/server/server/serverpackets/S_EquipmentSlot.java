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

import static l1j.server.server.Opcodes.S_OPCODE_CHARRESET;

import java.util.Map;

//Referenced classes of package l1j.server.server.serverpackets:
//ServerBasePacket

/**
 * 處理裝備欄裝備封包。  [3.80c] <br>
 * 原本應該與CHARRESET寫在一起，為了方便獨立開來。
 */
public class S_EquipmentSlot extends ServerBasePacket{
	/* 角色登入*/ 
	private static final int TYPE_EQUIPONLOGIN = 0x41;
	/* 穿脫*/
	private static final int TYPE_EQUIPACTION = 0x42; 
	
	
	/**
	 * 裝備欄位顯示 穿脫用
	 * @param pc         玩家物件
	 * @param ItemObjid  物品ObjID
	 * @param index      序號
	 * @param equipOnOff 裝備: 1, 卸除: 0
	 */
	public S_EquipmentSlot(int ItemObjid, int index, boolean equipOnOff) {
		writeC(S_OPCODE_CHARRESET);
		writeC(TYPE_EQUIPACTION);
		writeD(ItemObjid);
		writeC(index);
		writeC(equipOnOff ? 1 : 0);
	}
	
	/**
	 * 裝備欄位顯示 登入時使用
	 * @param pc         玩家物件
	 * @param ItemObjid  物品ObjID
	 * @param index      序號
	 */
	public S_EquipmentSlot(final Map<Integer, Integer> items){
		writeC(S_OPCODE_CHARRESET);
		writeC(TYPE_EQUIPONLOGIN);
		writeC(items.size());
		
		for(int i = 1; i <= 26 ; i++){
			if(items.containsKey(i)){
				writeD(items.get(i).intValue());
				writeD(i);
			}
		}
		writeD(0);
		writeC(0);
	}

	/**
	 * 裝備欄位編號 3.80C
	 */
	public static enum EquipmentIndex {
		/** 頭盔 */
		EQUIPMENT_INDEX_HEMET(1),
		/** 盔甲 */
		EQUIPMENT_INDEX_ARMOR(2),
		/** T恤 */
		EQUIPMENT_INDEX_T(3),
		/** 斗篷 */
		EQUIPMENT_INDEX_CLOAK(4),
		/** 長靴 */
		EQUIPMENT_INDEX_BOOTS(5),
		/** 手套 */
		EQUIPMENT_INDEX_GLOVE(6),
		/** 盾牌 */
		EQUIPMENT_INDEX_SHIELD(7),
		/** 武器 */
		EQUIPMENT_INDEX_WEAPON(8),
		/** 項鏈 */
		EQUIPMENT_INDEX_AMULET(10),
		/** 腰帶 */
		EQUIPMENT_INDEX_BELT(11),
		/** 耳環 */
		EQUIPMENT_INDEX_EARRING(12),
		/** 戒指1 */
		EQUIPMENT_INDEX_RING1(18),
		/** 戒指2 */
		EQUIPMENT_INDEX_RING2(19),
		/** 戒指1 (限等) */
		EQUIPMENT_INDEX_RING1LIMITED(20),
		/** 戒指2 (限等) */
		EQUIPMENT_INDEX_RING2LIMITED(21),
		/** 符紋1 */
		EQUIPMENT_INDEX_RUNE1(22);

		private int index;

		private EquipmentIndex(final int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "[S] S_EquipmentSlot";
	}

}
