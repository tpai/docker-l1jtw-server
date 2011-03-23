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

package l1j.server.server.model;

import l1j.server.server.datatables.TownTable;
import l1j.server.server.templates.L1Town;
import l1j.server.server.types.Point;
import l1j.server.server.utils.Random;

// Referenced classes of package l1j.server.server.model:
// L1CastleLocation

public class L1TownLocation {
	// town_id
	public static final int TOWNID_TALKING_ISLAND = 1;

	public static final int TOWNID_SILVER_KNIGHT_TOWN = 2;

	public static final int TOWNID_GLUDIO = 3;

	public static final int TOWNID_ORCISH_FOREST = 4;

	public static final int TOWNID_WINDAWOOD = 5;

	public static final int TOWNID_KENT = 6;

	public static final int TOWNID_GIRAN = 7;

	public static final int TOWNID_HEINE = 8;

	public static final int TOWNID_WERLDAN = 9;

	public static final int TOWNID_OREN = 10;

	// 下記、町税なし

	public static final int TOWNID_ELVEN_FOREST = 11;

	public static final int TOWNID_ADEN = 12;

	public static final int TOWNID_SILENT_CAVERN = 13;

	public static final int TOWNID_OUM_DUNGEON = 14;

	public static final int TOWNID_RESISTANCE = 15;

	public static final int TOWNID_PIRATE_ISLAND = 16;

	public static final int TOWNID_RECLUSE_VILLAGE = 17;

	// 帰還ロケーション
	private static final short GETBACK_MAP_TALKING_ISLAND = 0;

	private static final Point[] GETBACK_LOC_TALKING_ISLAND =
	{ new Point(32600, 32942), new Point(32574, 32944), new Point(32580, 32923), new Point(32557, 32975), new Point(32597, 32914),
			new Point(32580, 32974), };

	private static final short GETBACK_MAP_SILVER_KNIGHT_TOWN = 4;

	private static final Point[] GETBACK_LOC_SILVER_KNIGHT_TOWN =
	{ new Point(33071, 33402), new Point(33091, 33396), new Point(33085, 33402), new Point(33097, 33366), new Point(33110, 33365),
			new Point(33072, 33392), };

	private static final short GETBACK_MAP_GLUDIO = 4;

	private static final Point[] GETBACK_LOC_GLUDIO =
	{ new Point(32601, 32757), new Point(32625, 32809), new Point(32611, 32726), new Point(32612, 32781), new Point(32605, 32761),
			new Point(32614, 32739), new Point(32612, 32775), };

	private static final short GETBACK_MAP_ORCISH_FOREST = 4;

	private static final Point[] GETBACK_LOC_ORCISH_FOREST =
	{ new Point(32750, 32435), new Point(32745, 32447), new Point(32738, 32452), new Point(32741, 32436), new Point(32749, 32446), };

	private static final short GETBACK_MAP_WINDAWOOD = 4;

	private static final Point[] GETBACK_LOC_WINDAWOOD =
	{ new Point(32608, 33178), new Point(32626, 33185), new Point(32630, 33179), new Point(32625, 33207), new Point(32638, 33203),
			new Point(32621, 33179), };

	private static final short GETBACK_MAP_KENT = 4;

	private static final Point[] GETBACK_LOC_KENT =
	{ new Point(33048, 32750), new Point(33059, 32768), new Point(33047, 32761), new Point(33059, 32759), new Point(33051, 32775),
			new Point(33048, 32778), new Point(33064, 32773), new Point(33057, 32748), };

	private static final short GETBACK_MAP_GIRAN = 4;

	private static final Point[] GETBACK_LOC_GIRAN =
	{ new Point(33435, 32803), new Point(33439, 32817), new Point(33440, 32809), new Point(33419, 32810), new Point(33426, 32823),
			new Point(33418, 32818), new Point(33432, 32824), };

	private static final short GETBACK_MAP_HEINE = 4;

	private static final Point[] GETBACK_LOC_HEINE =
	{ new Point(33593, 33242), new Point(33593, 33248), new Point(33604, 33236), new Point(33599, 33236), new Point(33610, 33247),
			new Point(33610, 33241), new Point(33599, 33252), new Point(33605, 33252), };

	private static final short GETBACK_MAP_WERLDAN = 4;

	private static final Point[] GETBACK_LOC_WERLDAN =
	{ new Point(33702, 32492), new Point(33747, 32508), new Point(33696, 32498), new Point(33723, 32512), new Point(33710, 32521),
			new Point(33724, 32488), new Point(33693, 32513), };

	private static final short GETBACK_MAP_OREN = 4;

	private static final Point[] GETBACK_LOC_OREN =
	{ new Point(34086, 32280), new Point(34037, 32230), new Point(34022, 32254), new Point(34021, 32269), new Point(34044, 32290),
			new Point(34049, 32316), new Point(34081, 32249), new Point(34074, 32313), new Point(34064, 32230), };

	private static final short GETBACK_MAP_ELVEN_FOREST = 4;

	private static final Point[] GETBACK_LOC_ELVEN_FOREST =
	{ new Point(33065, 32358), new Point(33052, 32313), new Point(33030, 32342), new Point(33068, 32320), new Point(33071, 32314),
			new Point(33030, 32370), new Point(33076, 32324), new Point(33068, 32336), };

	private static final short GETBACK_MAP_ADEN = 4;

	private static final Point[] GETBACK_LOC_ADEN =
	{ new Point(33915, 33114), new Point(34061, 33115), new Point(34090, 33168), new Point(34011, 33136), new Point(34093, 33117),
			new Point(33959, 33156), new Point(33992, 33120), new Point(34047, 33156), };

	private static final short GETBACK_MAP_SILENT_CAVERN = 304;

	private static final Point[] GETBACK_LOC_SILENT_CAVERN =
	{ new Point(32856, 32898), new Point(32860, 32916), new Point(32868, 32893), new Point(32875, 32903), new Point(32855, 32898), };

	private static final short GETBACK_MAP_OUM_DUNGEON = 310;

	private static final Point[] GETBACK_LOC_OUM_DUNGEON =
	{ new Point(32818, 32805), new Point(32800, 32798), new Point(32815, 32819), new Point(32823, 32811), new Point(32817, 32828), };

	private static final short GETBACK_MAP_RESISTANCE = 400;

	private static final Point[] GETBACK_LOC_RESISTANCE =
	{ new Point(32570, 32667), new Point(32559, 32678), new Point(32564, 32683), new Point(32574, 32661), new Point(32576, 32669),
			new Point(32572, 32662), };

	private static final short GETBACK_MAP_PIRATE_ISLAND = 440;

	private static final Point[] GETBACK_LOC_PIRATE_ISLAND =
	{ new Point(32431, 33058), new Point(32407, 33054), };

	private static final short GETBACK_MAP_RECLUSE_VILLAGE = 400;

	private static final Point[] GETBACK_LOC_RECLUSE_VILLAGE =
	{ new Point(32599, 32916), new Point(32599, 32923), new Point(32603, 32908), new Point(32595, 32908), new Point(32591, 32918), };

	private L1TownLocation() {
	}

	public static int[] getGetBackLoc(int town_id) { // town_idから帰還先の座標をランダムに返す
		int[] loc = new int[3];

		if (town_id == TOWNID_TALKING_ISLAND) { // TI
			int rnd = Random.nextInt(GETBACK_LOC_TALKING_ISLAND.length);
			loc[0] = GETBACK_LOC_TALKING_ISLAND[rnd].getX();
			loc[1] = GETBACK_LOC_TALKING_ISLAND[rnd].getY();
			loc[2] = GETBACK_MAP_TALKING_ISLAND;
		}
		else if (town_id == TOWNID_SILVER_KNIGHT_TOWN) { // SKT
			int rnd = Random.nextInt(GETBACK_LOC_SILVER_KNIGHT_TOWN.length);
			loc[0] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getX();
			loc[1] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getY();
			loc[2] = GETBACK_MAP_SILVER_KNIGHT_TOWN;
		}
		else if (town_id == TOWNID_KENT) { // ケント
			int rnd = Random.nextInt(GETBACK_LOC_KENT.length);
			loc[0] = GETBACK_LOC_KENT[rnd].getX();
			loc[1] = GETBACK_LOC_KENT[rnd].getY();
			loc[2] = GETBACK_MAP_KENT;
		}
		else if (town_id == TOWNID_GLUDIO) { // グル
			int rnd = Random.nextInt(GETBACK_LOC_GLUDIO.length);
			loc[0] = GETBACK_LOC_GLUDIO[rnd].getX();
			loc[1] = GETBACK_LOC_GLUDIO[rnd].getY();
			loc[2] = GETBACK_MAP_GLUDIO;
		}
		else if (town_id == TOWNID_ORCISH_FOREST) { // 火田村
			int rnd = Random.nextInt(GETBACK_LOC_ORCISH_FOREST.length);
			loc[0] = GETBACK_LOC_ORCISH_FOREST[rnd].getX();
			loc[1] = GETBACK_LOC_ORCISH_FOREST[rnd].getY();
			loc[2] = GETBACK_MAP_ORCISH_FOREST;
		}
		else if (town_id == TOWNID_WINDAWOOD) { // ウッドベック
			int rnd = Random.nextInt(GETBACK_LOC_WINDAWOOD.length);
			loc[0] = GETBACK_LOC_WINDAWOOD[rnd].getX();
			loc[1] = GETBACK_LOC_WINDAWOOD[rnd].getY();
			loc[2] = GETBACK_MAP_WINDAWOOD;
		}
		else if (town_id == TOWNID_GIRAN) { // ギラン
			int rnd = Random.nextInt(GETBACK_LOC_GIRAN.length);
			loc[0] = GETBACK_LOC_GIRAN[rnd].getX();
			loc[1] = GETBACK_LOC_GIRAN[rnd].getY();
			loc[2] = GETBACK_MAP_GIRAN;
		}
		else if (town_id == TOWNID_HEINE) { // ハイネ
			int rnd = Random.nextInt(GETBACK_LOC_HEINE.length);
			loc[0] = GETBACK_LOC_HEINE[rnd].getX();
			loc[1] = GETBACK_LOC_HEINE[rnd].getY();
			loc[2] = GETBACK_MAP_HEINE;
		}
		else if (town_id == TOWNID_WERLDAN) { // ウェルダン
			int rnd = Random.nextInt(GETBACK_LOC_WERLDAN.length);
			loc[0] = GETBACK_LOC_WERLDAN[rnd].getX();
			loc[1] = GETBACK_LOC_WERLDAN[rnd].getY();
			loc[2] = GETBACK_MAP_WERLDAN;
		}
		else if (town_id == TOWNID_OREN) { // オーレン
			int rnd = Random.nextInt(GETBACK_LOC_OREN.length);
			loc[0] = GETBACK_LOC_OREN[rnd].getX();
			loc[1] = GETBACK_LOC_OREN[rnd].getY();
			loc[2] = GETBACK_MAP_OREN;
		}
		else if (town_id == TOWNID_ELVEN_FOREST) { // エルフの森
			int rnd = Random.nextInt(GETBACK_LOC_ELVEN_FOREST.length);
			loc[0] = GETBACK_LOC_ELVEN_FOREST[rnd].getX();
			loc[1] = GETBACK_LOC_ELVEN_FOREST[rnd].getY();
			loc[2] = GETBACK_MAP_ELVEN_FOREST;
		}
		else if (town_id == TOWNID_ADEN) { // アデン
			int rnd = Random.nextInt(GETBACK_LOC_ADEN.length);
			loc[0] = GETBACK_LOC_ADEN[rnd].getX();
			loc[1] = GETBACK_LOC_ADEN[rnd].getY();
			loc[2] = GETBACK_MAP_ADEN;
		}
		else if (town_id == TOWNID_SILENT_CAVERN) { // 沈黙の洞窟
			int rnd = Random.nextInt(GETBACK_LOC_SILENT_CAVERN.length);
			loc[0] = GETBACK_LOC_SILENT_CAVERN[rnd].getX();
			loc[1] = GETBACK_LOC_SILENT_CAVERN[rnd].getY();
			loc[2] = GETBACK_MAP_SILENT_CAVERN;
		}
		else if (town_id == TOWNID_OUM_DUNGEON) { // オームダンジョン
			int rnd = Random.nextInt(GETBACK_LOC_OUM_DUNGEON.length);
			loc[0] = GETBACK_LOC_OUM_DUNGEON[rnd].getX();
			loc[1] = GETBACK_LOC_OUM_DUNGEON[rnd].getY();
			loc[2] = GETBACK_MAP_OUM_DUNGEON;
		}
		else if (town_id == TOWNID_RESISTANCE) { // レシスタンス村
			int rnd = Random.nextInt(GETBACK_LOC_RESISTANCE.length);
			loc[0] = GETBACK_LOC_RESISTANCE[rnd].getX();
			loc[1] = GETBACK_LOC_RESISTANCE[rnd].getY();
			loc[2] = GETBACK_MAP_RESISTANCE;
		}
		else if (town_id == TOWNID_PIRATE_ISLAND) { // 海賊島
			int rnd = Random.nextInt(GETBACK_LOC_PIRATE_ISLAND.length);
			loc[0] = GETBACK_LOC_PIRATE_ISLAND[rnd].getX();
			loc[1] = GETBACK_LOC_PIRATE_ISLAND[rnd].getY();
			loc[2] = GETBACK_MAP_PIRATE_ISLAND;
		}
		else if (town_id == TOWNID_RECLUSE_VILLAGE) { // 隠れ里
			int rnd = Random.nextInt(GETBACK_LOC_RECLUSE_VILLAGE.length);
			loc[0] = GETBACK_LOC_RECLUSE_VILLAGE[rnd].getX();
			loc[1] = GETBACK_LOC_RECLUSE_VILLAGE[rnd].getY();
			loc[2] = GETBACK_MAP_RECLUSE_VILLAGE;
		}
		else { // その他はSKT
			int rnd = Random.nextInt(GETBACK_LOC_SILVER_KNIGHT_TOWN.length);
			loc[0] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getX();
			loc[1] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getY();
			loc[2] = GETBACK_MAP_SILVER_KNIGHT_TOWN;
		}
		return loc;
	}

	public static int getTownTaxRateByNpcid(int npcid) { // npcidから町税率を返す
		int tax_rate = 0;

		int town_id = getTownIdByNpcid(npcid);
		if ((town_id >= 1) && (town_id <= 10)) {
			L1Town town = TownTable.getInstance().getTownTable(town_id);
			tax_rate = town.get_tax_rate() + 2; // 2%は固定税
		}
		return tax_rate;
	}

	public static int getTownIdByNpcid(int npcid) { // npcidからtown_idを返す
		// アデン城：アデン王国全域
		// ケント城：ケント、グルーディン
		// ウィンダウッド城：ウッドベック、オアシス、シルバーナイトタウン
		// ギラン城：ギラン、話せる島
		// ハイネ城：ハイネ
		// ドワーフ城：ウェルダン、象牙の塔、象牙の塔の村
		// オーク砦：火田村
		// ディアド要塞：戦争税の一部

		// XXX:まだNPCはL1CastleLocationから持ってきたままの状態（未整理）
		int town_id = 0;

		switch (npcid) {
			case 70528: // タウンマスター（TI）
			case 50015: // ルーカス（テレポーター）
			case 70010: // バルシム（犬小屋裏道具屋）
			case 70011: // 船着場管理人
			case 70012: // セレナ（宿屋）
			case 70014: // パンドラ（港道具屋）
			case 70532: // ジョンソン（ペット屋）
			case 70536: // トーマ（鍛冶屋）
				town_id = TOWNID_TALKING_ISLAND;
				break;

			case 70799: // タウンマスター（SKT）
			case 50056: // メット（テレポーター）
			case 70073: // グレン（武器屋）
			case 70074: // メリン（道具屋）
			case 70075: // ミランダ（宿屋）
				town_id = TOWNID_SILVER_KNIGHT_TOWN;
				break;

			case 70546: // タウンマスター（KENT）
			case 50020: // スタンリー（テレポーター）
			case 70018: // イソーリア（道具屋）
			case 70016: // アンディン（武器屋）
			case 70544: // リック（ペット屋）
				town_id = TOWNID_KENT;
				break;

			case 70567: // タウンマスター（グル）
			case 50024: // スティーブ（グルテレポーター）
			case 70019: // ロリア（グル宿屋）
			case 70020: // ロルコ（グル古代物品商人）
			case 70021: // ロッテ（グル道具屋）
			case 70022: // 船着場管理人
			case 70024: // ケティ（グル武器屋）
				town_id = TOWNID_GLUDIO;
				break;

			case 70815: // 火田村タウンマスター
			case 70079: // ジャクソン（道具屋）
			case 70836: // ハンス（ペット屋）
				town_id = TOWNID_ORCISH_FOREST;
				break;

			case 70774: // タウンマスター（WB）
			case 50054: // トレイ（テレポーター）
			case 70070: // ベリッサ（宿屋）
			case 70071: // アシュール（オアシス）
			case 70072: // エルミナ（道具屋）
			case 70773: // マービン（ペット屋）
				town_id = TOWNID_WINDAWOOD;
				break;

			case 70594: // タウンマスター（ギラン）
			case 50036: // ウィルマ（テレポーター）
			case 70026: // デレック（ハンター）
			case 70028: // ランダル（薬品商人）
			case 70029: // マーガレット（食料品商人）
			case 70030: // メイアー（道具屋）
			case 70031: // モーリ（宿屋）
			case 70032: // バージル（防具屋）
			case 70033: // ベリタ（道具屋）
			case 70038: // エバート（布商人）
			case 70039: // ワーナー（武器屋）
			case 70043: // フィリップ（皮商人）
			case 70617: // アルモン（ペット屋）
			case 70632: // ケビン（ペット屋）
				town_id = TOWNID_GIRAN;
				break;

			case 70860: // タウンマスター（ハイネ）
			case 50066: // リオル（テレポーター）
			case 70082: // ブリット（道具屋）
			case 70083: // シバン（武器屋）
			case 70084: // エリー（宿屋）
			case 70873: // エラン（ペット屋）
				town_id = TOWNID_HEINE;
				break;

			case 70654: // タウンマスター（ウェルダン）
			case 50039: // レスリー（テレポーター）
			case 70045: // ベリー（道具屋）
			case 70044: // ラルフ（武器屋）
			case 70664: // コブ（ペット屋）
				town_id = TOWNID_WERLDAN;
				break;

			case 70748: // タウンマスター（オーレン）
			case 50051: // キリウス（テレポーター）
			case 70059: // ディコ（国境要塞道具屋）
			case 70060: // リンダ（象牙の塔精霊魔法屋）
			case 70061: // マンドラ（武器屋）
			case 70062: // バリエス（象牙の塔魔法屋）
			case 70063: // ビウス（道具屋）
			case 70065: // エンケ（宿屋）
			case 70066: // クリスト（象牙の塔魔法屋）
			case 70067: // パゴル（象牙の塔道具屋）
			case 70068: // フランコ（古代物品商人）
			case 70749: // マイルド（ペット屋）
				town_id = TOWNID_OREN;
				break;

			case 50044: // シリウス（テレポーター）
			case 70057: // キャサリン（道具屋）
			case 70048: // ラオン（道具屋）
			case 70052: // メリサ（道具屋）
			case 70053: // シャル（食料品屋）
			case 70049: // ローゼン（ポーション屋）
			case 70051: // マグス（道具屋）
			case 70047: // デフマン（武器屋）
			case 70058: // フェガ（防具屋）
			case 70054: // スビン（宿屋）
			case 70055: // エイシヌ（ペットショップ）
			case 70056: // ゾード（ジプシータウン古代物品商人）
				town_id = TOWNID_ADEN;
				break;

			case 70092: // 商人 エマルト
			case 70093: // 商人 カルプ
				town_id = TOWNID_OUM_DUNGEON;
				break;

			default:
				break;
		}
		return town_id;
	}
}
