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
package l1j.server.server.command.executor;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Tile implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Tile.class.getName());

	private L1Tile() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Tile();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int locX = pc.getX();
			int locY = pc.getY();
			short mapId = pc.getMapId();
			int tile0 = L1WorldMap.getInstance().getMap(mapId)
					.getOriginalTile(locX, locY - 1);
			int tile1 = L1WorldMap.getInstance().getMap(mapId)
					.getOriginalTile(locX + 1, locY - 1);
			int tile2 = L1WorldMap.getInstance().getMap(mapId)
					.getOriginalTile(locX + 1, locY);
			int tile3 = L1WorldMap.getInstance().getMap(mapId)
					.getOriginalTile(locX + 1, locY + 1);
			int tile4 = L1WorldMap.getInstance().getMap(mapId)
					.getOriginalTile(locX, locY + 1);
			int tile5 = L1WorldMap.getInstance().getMap(mapId)
					.getOriginalTile(locX - 1, locY + 1);
			int tile6 = L1WorldMap.getInstance().getMap(mapId)
					.getOriginalTile(locX - 1, locY);
			int tile7 = L1WorldMap.getInstance().getMap(mapId)
					.getOriginalTile(locX - 1, locY - 1);
			String msg = String
					.format("0:%d 1:%d 2:%d 3:%d 4:%d 5:%d 6:%d 7:%d",
					tile0, tile1, tile2, tile3, tile4, tile5, tile6, tile7);
			pc.sendPackets(new S_SystemMessage(msg));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
