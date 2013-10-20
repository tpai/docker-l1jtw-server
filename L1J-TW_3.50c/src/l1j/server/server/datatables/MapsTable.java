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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

public final class MapsTable {
	private class MapData {
		public int startX = 0;

		public int endX = 0;

		public int startY = 0;

		public int endY = 0;

		public double monster_amount = 1;

		public double dropRate = 1;

		public boolean isUnderwater = false;

		public boolean markable = false;

		public boolean teleportable = false;

		public boolean escapable = false;

		public boolean isUseResurrection = false;

		public boolean isUsePainwand = false;

		public boolean isEnabledDeathPenalty = false;

		public boolean isTakePets = false;

		public boolean isRecallPets = false;

		public boolean isUsableItem = false;

		public boolean isUsableSkill = false;
	}

	private static Logger _log = Logger.getLogger(MapsTable.class.getName());

	private static MapsTable _instance;

	/**
	 * KeyにマップID、Valueにテレポート可否フラグが格納されるHashMap
	 */
	private final Map<Integer, MapData> _maps = Maps.newMap();

	/**
	 * 新しくMapsTableオブジェクトを生成し、マップのテレポート可否フラグを読み込む。
	 */
	private MapsTable() {
		loadMapsFromDatabase();
	}

	/**
	 * マップのテレポート可否フラグをデータベースから読み込み、HashMap _mapsに格納する。
	 */
	private void loadMapsFromDatabase() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM mapids");

			for (rs = pstm.executeQuery(); rs.next();) {
				MapData data = new MapData();
				int mapId = rs.getInt("mapid");
				// rs.getString("locationname");
				data.startX = rs.getInt("startX");
				data.endX = rs.getInt("endX");
				data.startY = rs.getInt("startY");
				data.endY = rs.getInt("endY");
				data.monster_amount = rs.getDouble("monster_amount");
				data.dropRate = rs.getDouble("drop_rate");
				data.isUnderwater = rs.getBoolean("underwater");
				data.markable = rs.getBoolean("markable");
				data.teleportable = rs.getBoolean("teleportable");
				data.escapable = rs.getBoolean("escapable");
				data.isUseResurrection = rs.getBoolean("resurrection");
				data.isUsePainwand = rs.getBoolean("painwand");
				data.isEnabledDeathPenalty = rs.getBoolean("penalty");
				data.isTakePets = rs.getBoolean("take_pets");
				data.isRecallPets = rs.getBoolean("recall_pets");
				data.isUsableItem = rs.getBoolean("usable_item");
				data.isUsableSkill = rs.getBoolean("usable_skill");

				_maps.put(new Integer(mapId), data);
			}

			_log.config("Maps " + _maps.size());
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * MapsTableのインスタンスを返す。
	 * 
	 * @return MapsTableのインスタンス
	 */
	public static MapsTable getInstance() {
		if (_instance == null) {
			_instance = new MapsTable();
		}
		return _instance;
	}

	/**
	 * マップがのX開始座標を返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * @return X開始座標
	 */
	public int getStartX(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).startX;
	}

	/**
	 * マップがのX終了座標を返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * @return X終了座標
	 */
	public int getEndX(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).endX;
	}

	/**
	 * マップがのY開始座標を返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * @return Y開始座標
	 */
	public int getStartY(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).startY;
	}

	/**
	 * マップがのY終了座標を返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * @return Y終了座標
	 */
	public int getEndY(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).endY;
	}

	/**
	 * マップのモンスター量倍率を返す
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * @return モンスター量の倍率
	 */
	public double getMonsterAmount(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return map.monster_amount;
	}

	/**
	 * マップのドロップ倍率を返す
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * @return ドロップ倍率
	 */
	public double getDropRate(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return map.dropRate;
	}

	/**
	 * マップが、水中であるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * 
	 * @return 水中であればtrue
	 */
	public boolean isUnderwater(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUnderwater;
	}

	/**
	 * マップが、ブックマーク可能であるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * @return ブックマーク可能であればtrue
	 */
	public boolean isMarkable(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).markable;
	}

	/**
	 * マップが、ランダムテレポート可能であるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * @return 可能であればtrue
	 */
	public boolean isTeleportable(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).teleportable;
	}

	/**
	 * マップが、MAPを超えたテレポート可能であるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * @return 可能であればtrue
	 */
	public boolean isEscapable(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).escapable;
	}

	/**
	 * マップが、復活可能であるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * 
	 * @return 復活可能であればtrue
	 */
	public boolean isUseResurrection(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUseResurrection;
	}

	/**
	 * マップが、パインワンド使用可能であるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * 
	 * @return パインワンド使用可能であればtrue
	 */
	public boolean isUsePainwand(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUsePainwand;
	}

	/**
	 * マップが、デスペナルティがあるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * 
	 * @return デスペナルティであればtrue
	 */
	public boolean isEnabledDeathPenalty(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isEnabledDeathPenalty;
	}

	/**
	 * マップが、ペット・サモンを連れて行けるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * 
	 * @return ペット・サモンを連れて行けるならばtrue
	 */
	public boolean isTakePets(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isTakePets;
	}

	/**
	 * マップが、ペット・サモンを呼び出せるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * 
	 * @return ペット・サモンを呼び出せるならばtrue
	 */
	public boolean isRecallPets(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isRecallPets;
	}

	/**
	 * マップが、アイテムを使用できるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * 
	 * @return アイテムを使用できるならばtrue
	 */
	public boolean isUsableItem(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUsableItem;
	}

	/**
	 * マップが、スキルを使用できるかを返す。
	 * 
	 * @param mapId
	 *            調べるマップのマップID
	 * 
	 * @return スキルを使用できるならばtrue
	 */
	public boolean isUsableSkill(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUsableSkill;
	}

}
