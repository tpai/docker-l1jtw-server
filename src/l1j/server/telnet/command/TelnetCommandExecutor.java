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
package l1j.server.telnet.command;

import java.util.StringTokenizer;
import static l1j.server.telnet.command.TelnetCommandResult.*;

public class TelnetCommandExecutor {
	private static TelnetCommandExecutor _instance = new TelnetCommandExecutor();

	public static TelnetCommandExecutor getInstance() {
		return _instance;
	}

	public TelnetCommandResult execute(String cmd) {
		try {
			StringTokenizer tok = new StringTokenizer(cmd, " ");
			String name = tok.nextToken();

			TelnetCommand command = TelnetCommandList.get(name);
			if (command == null) {
				return new TelnetCommandResult(CMD_NOT_FOUND, cmd
						+ " not found");
			}

			String args = "";
			if (name.length() + 1 < cmd.length()) {
				args = cmd.substring(name.length() + 1);
			}
			return command.execute(args);
		} catch (Exception e) {
			return new TelnetCommandResult(CMD_INTERNAL_ERROR, e
					.getLocalizedMessage());
		}
	}
}
