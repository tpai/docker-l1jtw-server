/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.telnet.command;

import java.util.Map;

import l1j.server.server.utils.collections.Maps;

public class TelnetCommandList {
	private static Map<String, TelnetCommand> _cmds = Maps.newMap();
	static {
		_cmds.put("echo", new EchoCommand());
		_cmds.put("playerid", new PlayerIdCommand());
		_cmds.put("charstatus", new CharStatusCommand());
		_cmds.put("globalchat", new GlobalChatCommand());
		_cmds.put("shutdown", new ShutDownCommand());
	}

	public static TelnetCommand get(String name) {
		return _cmds.get(name.toLowerCase());
	}
}
