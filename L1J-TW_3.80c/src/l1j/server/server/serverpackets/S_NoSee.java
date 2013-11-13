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

import static l1j.server.server.Opcodes.S_OPCODE_SHOWHTML;

public class S_NoSee extends ServerBasePacket{
	
	private byte[] _byte = null;
	
	private static final String S_NoSee = "[S] S_NoSee";
	
	/**
	 *『來源:伺服器』<位址:39>{長度:32}(時間:-1841663603)
	 * 0000:  27 [00 00 00 00] [6e 6f 73 65 65 62 00] 00 01 00 a7    '....noseeb.....
	 * 0010:  c6 bf d5 a9 5f a8 c8 b4 b5 00 8f 00 00 07 af 69    ...._..........i
	 */
	public S_NoSee(String targetName){
		writeC(S_OPCODE_SHOWHTML);
		writeD(0x00000000);
		writeS("noseeb");
		writeC(0);
		writeH(1);
		writeS(targetName);
		writeC(0x8f);
		writeH(0);
	}
	
	
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_NoSee;
	}
}
