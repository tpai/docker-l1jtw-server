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
import l1j.server.server.model.Instance.L1PcInstance;

public class S_UseMap extends ServerBasePacket {
	private static final String S_USE_MAP = "[S] S_UseMap";

	public S_UseMap(L1PcInstance pc, int objid, int itemid) {

		writeC(Opcodes.S_OPCODE_USEMAP);
		writeD(objid);

		switch (itemid) {
			case 40373:
				writeD(16);
				break;
			case 40374:
				writeD(1);
				break;
			case 40375:
				writeD(2);
				break;
			case 40376:
				writeD(3);
				break;
			case 40377:
				writeD(4);
				break;
			case 40378:
				writeD(5);
				break;
			case 40379:
				writeD(6);
				break;
			case 40380:
				writeD(7);
				break;
			case 40381:
				writeD(8);
				break;
			case 40382:
				writeD(9);
				break;
			case 40383:
				writeD(10);
				break;
			case 40384:
				writeD(11);
				break;
			case 40385:
				writeD(12);
				break;
			case 40386:
				writeD(13);
				break;
			case 40387:
				writeD(14);
				break;
			case 40388:
				writeD(15);
				break;
			case 40389:
				writeD(17);
				break;
			case 40390:
				writeD(18);
				break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_USE_MAP;
	}
}