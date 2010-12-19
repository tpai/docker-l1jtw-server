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

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_CommonNews extends ServerBasePacket {

	public S_CommonNews() {
		_announcements = new ArrayList();
		loadAnnouncements();
		writeC(Opcodes.S_OPCODE_COMMONNEWS);
		String message = "";
		for (int i = 0; i < _announcements.size(); i++) {
			message = (new StringBuilder()).append(message).append(
					_announcements.get(i).toString()).append("\n").toString();
		}
		writeS(message);
	}

	public S_CommonNews(String s) {
		writeC(Opcodes.S_OPCODE_COMMONNEWS);
		writeS(s);
	}

	private void loadAnnouncements() {
		_announcements.clear();
		File file = new File("data/announcements.txt");
		if (file.exists()) {
			readFromDisk(file);
		}
	}

	private void readFromDisk(File file) {
		LineNumberReader lnr = null;
		try {
			String line = null;
			lnr = new LineNumberReader(new FileReader(file));
			do {
				if ((line = lnr.readLine()) == null) {
					break;
				}
				StringTokenizer st = new StringTokenizer(line, "\n\r");
				if (st.hasMoreTokens()) {
					String announcement = st.nextToken();
					_announcements.add(announcement);
				} else {
					_announcements.add(" ");
				}
			} while (true);
		} catch (Exception e) {
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "[S] S_CommonNews";
	}

	private static final String _S__0B_COMMONNEWS = "[S] S_CommonNews";

	private static Logger _log = Logger.getLogger(S_CommonNews.class.getName());

	private ArrayList _announcements;

}
