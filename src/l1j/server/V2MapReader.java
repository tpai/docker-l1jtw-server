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

package l1j.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1V2Map;
import l1j.server.server.utils.BinaryInputStream;
import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.collections.Lists;
import l1j.server.server.utils.collections.Maps;

/**
 * 地圖 (v2maps/\d*.txt)讀取 (測試用)
 */
public class V2MapReader extends MapReader {

	/** 地圖的路徑 */
	private static final String MAP_DIR = "./v2maps/";

	/**
	 * 傳回所有地圖的編號
	 * 
	 * @return ArraryList
	 */
	private List<Integer> listMapIds() {
		List<Integer> ids = Lists.newList();

		File mapDir = new File(MAP_DIR);
		for (String name : mapDir.list()) {
			File mapFile = new File(mapDir, name);
			if (!mapFile.exists()) {
				continue;
			}
			if (!FileUtil.getExtension(mapFile).toLowerCase().equals("md")) {
				continue;
			}
			int id = 0;
			try {
				String idStr = FileUtil.getNameWithoutExtension(mapFile);
				id = Integer.parseInt(idStr);
			}
			catch (NumberFormatException e) {
				continue;
			}
			ids.add(id);
		}
		return ids;
	}

	/**
	 * 取得所有地圖與編號的 Mapping
	 * 
	 * @return Map
	 * @throws IOException
	 */
	@Override
	public Map<Integer, L1Map> read() throws IOException {
		Map<Integer, L1Map> maps = Maps.newMap();
		for (int id : listMapIds()) {
			maps.put(id, read(id));
		}
		return maps;
	}

	/**
	 * 從地圖中讀取特定編號的地圖
	 * 
	 * @param mapId
	 *            地圖編號
	 * @return L1Map
	 * @throws IOException
	 */
	@Override
	public L1Map read(final int mapId) throws IOException {
		File file = new File(MAP_DIR + mapId + ".md");
		if (!file.exists()) {
			throw new FileNotFoundException("MapId: " + mapId);
		}

		BinaryInputStream in = new BinaryInputStream(new BufferedInputStream(new InflaterInputStream(new FileInputStream(file))));

		int id = in.readInt();
		if (mapId != id) {
			throw new FileNotFoundException("MapId: " + mapId);
		}

		int xLoc = in.readInt();
		int yLoc = in.readInt();
		int width = in.readInt();
		int height = in.readInt();

		byte[] tiles = new byte[width * height * 2];
		for (int i = 0; i < width * height * 2; i++) {
			tiles[i] = (byte) in.readByte();
		}
		in.close();

		L1V2Map map = new L1V2Map(id, tiles, xLoc, yLoc, width, height, MapsTable.getInstance().isUnderwater(mapId), MapsTable.getInstance()
				.isMarkable(mapId), MapsTable.getInstance().isTeleportable(mapId), MapsTable.getInstance().isEscapable(mapId), MapsTable
				.getInstance().isUseResurrection(mapId), MapsTable.getInstance().isUsePainwand(mapId), MapsTable.getInstance().isEnabledDeathPenalty(
				mapId), MapsTable.getInstance().isTakePets(mapId), MapsTable.getInstance().isRecallPets(mapId), MapsTable.getInstance().isUsableItem(
				mapId), MapsTable.getInstance().isUsableSkill(mapId));
		return map;
	}
}
