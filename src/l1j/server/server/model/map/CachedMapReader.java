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
package l1j.server.server.model.map;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.collections.Lists;
import l1j.server.server.utils.collections.Maps;

/**
 * 將地圖做快取的動作以減少讀取的時間。
 */
public class CachedMapReader extends MapReader {

	/** 地圖檔的路徑 */
	private static final String MAP_DIR = "./maps/";

	/** cache 後地圖檔的路徑 */
	private static final String CACHE_DIR = "./data/mapcache/";

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
			if (!FileUtil.getExtension(mapFile).toLowerCase().equals("txt")) {
				continue;
			}
			int id = 0;
			try {
				String idStr = FileUtil.getNameWithoutExtension(mapFile);
				id = Integer.parseInt(idStr);
			} catch (NumberFormatException e) {
				continue;
			}
			ids.add(id);
		}
		return ids;
	}

	/**
	 * 將指定編號的地圖轉成快取的地圖格式
	 * 
	 * @param mapId
	 *            地圖編號
	 * @return L1V1Map
	 * @throws IOException
	 */
	private L1V1Map cacheMap(final int mapId) throws IOException {
		File file = new File(CACHE_DIR);
		if (!file.exists()) {
			file.mkdir();
		}

		L1V1Map map = (L1V1Map) new TextMapReader().read(mapId);

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(CACHE_DIR + mapId + ".map")));

		out.writeInt(map.getId());
		out.writeInt(map.getX());
		out.writeInt(map.getY());
		out.writeInt(map.getWidth());
		out.writeInt(map.getHeight());

		for (byte[] line : map.getRawTiles()) {
			for (byte tile : line) {
				out.writeByte(tile);
			}
		}
		out.flush();
		out.close();

		return map;
	}

	/**
	 * 從快取地圖中讀取特定編號的地圖
	 * 
	 * @param mapId
	 *            地圖編號
	 * @return L1Map
	 * @throws IOException
	 */
	@Override
	public L1Map read(final int mapId) throws IOException {
		File file = new File(CACHE_DIR + mapId + ".map");
		if (!file.exists()) {
			return cacheMap(mapId);
		}

		DataInputStream in = new DataInputStream(new BufferedInputStream(
				new FileInputStream(CACHE_DIR + mapId + ".map")));

		int id = in.readInt();
		if (mapId != id) {
			throw new FileNotFoundException();
		}

		int xLoc = in.readInt();
		int yLoc = in.readInt();
		int width = in.readInt();
		int height = in.readInt();

		byte[][] tiles = new byte[width][height];
		for (byte[] line : tiles) {
			in.read(line);
		}

		in.close();
		L1V1Map map = new L1V1Map(id, tiles, xLoc, yLoc, MapsTable
				.getInstance().isUnderwater(mapId), MapsTable.getInstance()
				.isMarkable(mapId), MapsTable.getInstance().isTeleportable(
				mapId), MapsTable.getInstance().isEscapable(mapId), MapsTable
				.getInstance().isUseResurrection(mapId), MapsTable
				.getInstance().isUsePainwand(mapId), MapsTable.getInstance()
				.isEnabledDeathPenalty(mapId), MapsTable.getInstance()
				.isTakePets(mapId),
				MapsTable.getInstance().isRecallPets(mapId), MapsTable
						.getInstance().isUsableItem(mapId), MapsTable
						.getInstance().isUsableSkill(mapId));
		return map;
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
}
