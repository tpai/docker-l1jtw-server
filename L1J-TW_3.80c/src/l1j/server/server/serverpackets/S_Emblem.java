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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_Emblem extends ServerBasePacket {
	private static final String S_EMBLEM = "[S] S_Emblem";

	public S_Emblem(int emblemId) {
		BufferedInputStream bis = null;
		try {
			String emblem_file = String.valueOf(emblemId);
			File file = new File("emblem/" + emblem_file);
			if (file.exists()) {
				int data = 0;
				bis = new BufferedInputStream(new FileInputStream(file));
				writeC(Opcodes.S_OPCODE_EMBLEM);
				writeD(emblemId);
				while ((data = bis.read()) != -1) {
					writeP(data);
				}
			}
		}
		catch (Exception e) {}
		finally {
			if (bis != null) {
				try {
					bis.close();
				}
				catch (IOException ignore) {
					// ignore
				}
			}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_EMBLEM;
	}
}
