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

import java.util.List;

import l1j.server.server.datatables.HouseTable;
import l1j.server.server.types.Point;

// Referenced classes of package l1j.server.server.model:
// L1HouseLocation

public class L1HouseLocation {
	private static final int[] TELEPORT_LOC_MAPID =
	{ 4, 4, 4, 350, };

	private static final Point[] TELEPORT_LOC_GIRAN =
	{ new Point(33419, 32810), new Point(33343, 32723), // 倉庫、ペット保管所
			new Point(33553, 32712), new Point(32702, 32842), }; // 贖罪の使者、ギラン市場

	private static final Point[] TELEPORT_LOC_HEINE =
	{ new Point(33604, 33236), new Point(33649, 33413), // 倉庫、ペット保管所
			new Point(33553, 32712), new Point(32702, 32842), }; // 贖罪の使者、ギラン市場

	private static final Point[] TELEPORT_LOC_ADEN =
	{ new Point(33966, 33253), new Point(33921, 33177), // 倉庫、ペット保管所
			new Point(33553, 32712), new Point(32702, 32842), }; // 贖罪の使者、ギラン市場

	private static final Point[] TELEPORT_LOC_GLUDIN =
	{ new Point(32628, 32807), new Point(32623, 32729), // 倉庫、ペット保管所
			new Point(33553, 32712), new Point(32702, 32842), }; // 贖罪の使者、ギラン市場

	// アジトID一覧
	private static final List<Integer> _houseIds = HouseTable.getHouseIdList();

	private L1HouseLocation() {
	}

	/**
	 * 指定したlocが指定したアジト番号の座標か判定する。
	 * 
	 * @param houseId
	 * @param cha
	 * @return
	 */
	public static boolean isInHouseLoc(int houseId, int locx, int locy, short mapid) {
		boolean ret = false;
		if (houseId == 262145) { // ギランアジト1
			if (((locx >= 33368) && (locx <= 33375) && (locy >= 32651) && (locy <= 32654) && (mapid == 4))
					|| ((locx >= 33373) && (locx <= 33375) && (locy >= 32655) && (locy <= 32657) && (mapid == 4)) || (mapid == 5068)) {
				ret = true;
			}
		}
		else if (houseId == 262146) { // ギランアジト2
			if (((locx >= 33381) && (locx <= 33387) && (locy >= 32653) && (locy <= 32656) && (mapid == 4)) || (mapid == 5069)) {
				ret = true;
			}
		}
		else if (houseId == 262147) { // ギランアジト3
			if (((locx >= 33392) && (locx <= 33404) && (locy >= 32650) && (locy <= 32656) && (mapid == 4)) || (mapid == 5070)) {
				ret = true;
			}
		}
		else if (houseId == 262148) { // ギランアジト4
			if (((locx >= 33427) && (locx <= 33430) && (locy >= 32656) && (locy <= 32662) && (mapid == 4)) || (mapid == 5071)) {
				ret = true;
			}
		}
		else if (houseId == 262149) { // ギランアジト5
			if (((locx >= 33439) && (locx <= 33445) && (locy >= 32665) && (locy <= 32667) && (mapid == 4))
					|| ((locx >= 33442) && (locx <= 33445) && (locy >= 32668) && (locy <= 32672) && (mapid == 4)) || (mapid == 5072)) {
				ret = true;
			}
		}
		else if (houseId == 262150) { // ギランアジト6
			if (((locx >= 33454) && (locx <= 33466) && (locy >= 32648) && (locy <= 32654) && (mapid == 4)) || (mapid == 5073)) {
				ret = true;
			}
		}
		else if (houseId == 262151) { // ギランアジト7
			if (((locx >= 33476) && (locx <= 33479) && (locy >= 32665) && (locy <= 32671) && (mapid == 4)) || (mapid == 5074)) {
				ret = true;
			}
		}
		else if (houseId == 262152) { // ギランアジト8
			if (((locx >= 33471) && (locx <= 33477) && (locy >= 32678) && (locy <= 32680) && (mapid == 4))
					|| ((locx >= 33474) && (locx <= 33477) && (locy >= 32681) && (locy <= 32685) && (mapid == 4)) || (mapid == 5075)) {
				ret = true;
			}
		}
		else if (houseId == 262153) { // ギランアジト9
			if (((locx >= 33453) && (locx <= 33460) && (locy >= 32694) && (locy <= 32697) && (mapid == 4))
					|| ((locx >= 33458) && (locx <= 33460) && (locy >= 32698) && (locy <= 32700) && (mapid == 4)) || (mapid == 5076)) {
				ret = true;
			}
		}
		else if (houseId == 262154) { // ギランアジト10
			if (((locx >= 33421) && (locx <= 33433) && (locy >= 32685) && (locy <= 32691) && (mapid == 4)) || (mapid == 5077)) {
				ret = true;
			}
		}
		else if (houseId == 262155) { // ギランアジト11
			if (((locx >= 33409) && (locx <= 33415) && (locy >= 32674) && (locy <= 32676) && (mapid == 4))
					|| ((locx >= 33412) && (locx <= 33415) && (locy >= 32677) && (locy <= 32681) && (mapid == 4)) || (mapid == 5078)) {
				ret = true;
			}
		}
		else if (houseId == 262156) { // ギランアジト12
			if (((locx >= 33414) && (locx <= 33421) && (locy >= 32703) && (locy <= 32706) && (mapid == 4))
					|| ((locx >= 33419) && (locx <= 33421) && (locy >= 32707) && (locy <= 32709) && (mapid == 4)) || (mapid == 5079)) {
				ret = true;
			}
		}
		else if (houseId == 262157) { // ギランアジト13
			if (((locx >= 33372) && (locx <= 33384) && (locy >= 32692) && (locy <= 32698) && (mapid == 4)) || (mapid == 5080)) {
				ret = true;
			}
		}
		else if (houseId == 262158) { // ギランアジト14
			if (((locx >= 33362) && (locx <= 33365) && (locy >= 32681) && (locy <= 32687) && (mapid == 4)) || (mapid == 5081)) {
				ret = true;
			}
		}
		else if (houseId == 262159) { // ギランアジト15
			if (((locx >= 33360) && (locx <= 33366) && (locy >= 32669) && (locy <= 32671) && (mapid == 4))
					|| ((locx >= 33363) && (locx <= 33366) && (locy >= 32672) && (locy <= 32676) && (mapid == 4)) || (mapid == 5082)) {
				ret = true;
			}
		}
		else if (houseId == 262160) { // ギランアジト16
			if (((locx >= 33341) && (locx <= 33347) && (locy >= 32660) && (locy <= 32662) && (mapid == 4))
					|| ((locx >= 33344) && (locx <= 33347) && (locy >= 32663) && (locy <= 32667) && (mapid == 4)) || (mapid == 5083)) {
				ret = true;
			}
		}
		else if (houseId == 262161) { // ギランアジト17
			if (((locx >= 33345) && (locx <= 33348) && (locy >= 32672) && (locy <= 32678) && (mapid == 4)) || (mapid == 5084)) {
				ret = true;
			}
		}
		else if (houseId == 262162) { // ギランアジト18
			if (((locx >= 33338) && (locx <= 33350) && (locy >= 32704) && (locy <= 32711) && (mapid == 4)) || (mapid == 5085)) {
				ret = true;
			}
		}
		else if (houseId == 262163) { // ギランアジト19
			if (((locx >= 33349) && (locx <= 33356) && (locy >= 32728) && (locy <= 32731) && (mapid == 4))
					|| ((locx >= 33354) && (locx <= 33356) && (locy >= 32732) && (locy <= 32734) && (mapid == 4)) || (mapid == 5086)) {
				ret = true;
			}
		}
		else if (houseId == 262164) { // ギランアジト20
			if (((locx >= 33366) && (locx <= 33372) && (locy >= 32713) && (locy <= 32715) && (mapid == 4))
					|| ((locx >= 33369) && (locx <= 33372) && (locy >= 32716) && (locy <= 32720) && (mapid == 4)) || (mapid == 5087)) {
				ret = true;
			}
		}
		else if (houseId == 262165) { // ギランアジト21
			if (((locx >= 33380) && (locx <= 33383) && (locy >= 32712) && (locy <= 32718) && (mapid == 4)) || (mapid == 5088)) {
				ret = true;
			}
		}
		else if (houseId == 262166) { // ギランアジト22
			if (((locx >= 33401) && (locx <= 33413) && (locy >= 32733) && (locy <= 32739) && (mapid == 4)) || (mapid == 5089)) {
				ret = true;
			}
		}
		else if (houseId == 262167) { // ギランアジト23
			if (((locx >= 33424) && (locx <= 33430) && (locy >= 32717) && (locy <= 32719) && (mapid == 4))
					|| ((locx >= 33427) && (locx <= 33430) && (locy >= 32720) && (locy <= 32724) && (mapid == 4)) || (mapid == 5090)) {
				ret = true;
			}
		}
		else if (houseId == 262168) { // ギランアジト24
			if (((locx >= 33448) && (locx <= 33451) && (locy >= 32729) && (locy <= 32735) && (mapid == 4)) || (mapid == 5091)) {
				ret = true;
			}
		}
		else if (houseId == 262169) { // ギランアジト25
			if (((locx >= 33404) && (locx <= 33407) && (locy >= 32754) && (locy <= 32760) && (mapid == 4)) || (mapid == 5092)) {
				ret = true;
			}
		}
		else if (houseId == 262170) { // ギランアジト26
			if (((locx >= 33363) && (locx <= 33375) && (locy >= 32755) && (locy <= 32761) && (mapid == 4)) || (mapid == 5093)) {
				ret = true;
			}
		}
		else if (houseId == 262171) { // ギランアジト27
			if (((locx >= 33351) && (locx <= 33357) && (locy >= 32774) && (locy <= 32776) && (mapid == 4))
					|| ((locx >= 33354) && (locx <= 33357) && (locy >= 32777) && (locy <= 32781) && (mapid == 4)) || (mapid == 5094)) {
				ret = true;
			}
		}
		else if (houseId == 262172) { // ギランアジト28
			if (((locx >= 33355) && (locx <= 33361) && (locy >= 32787) && (locy <= 32790) && (mapid == 4)) || (mapid == 5095)) {
				ret = true;
			}
		}
		else if (houseId == 262173) { // ギランアジト29
			if (((locx >= 33366) && (locx <= 33373) && (locy >= 32786) && (locy <= 32789) && (mapid == 4))
					|| ((locx >= 33371) && (locx <= 33373) && (locy >= 32790) && (locy <= 32792) && (mapid == 4)) || (mapid == 5096)) {
				ret = true;
			}
		}
		else if (houseId == 262174) { // ギランアジト30
			if (((locx >= 33383) && (locx <= 33386) && (locy >= 32773) && (locy <= 32779) && (mapid == 4)) || (mapid == 5097)) {
				ret = true;
			}
		}
		else if (houseId == 262175) { // ギランアジト31
			if (((locx >= 33397) && (locx <= 33404) && (locy >= 32788) && (locy <= 32791) && (mapid == 4))
					|| ((locx >= 33402) && (locx <= 33404) && (locy >= 32792) && (locy <= 32794) && (mapid == 4)) || (mapid == 5098)) {
				ret = true;
			}
		}
		else if (houseId == 262176) { // ギランアジト32
			if (((locx >= 33479) && (locx <= 33486) && (locy >= 32788) && (locy <= 32791) && (mapid == 4))
					|| ((locx >= 33484) && (locx <= 33486) && (locy >= 32792) && (locy <= 32794) && (mapid == 4)) || (mapid == 5099)) {
				ret = true;
			}
		}
		else if (houseId == 262177) { // ギランアジト33
			if (((locx >= 33498) && (locx <= 33501) && (locy >= 32801) && (locy <= 32807) && (mapid == 4)) || (mapid == 5100)) {
				ret = true;
			}
		}
		else if (houseId == 262178) { // ギランアジト34
			if (((locx >= 33379) && (locx <= 33385) && (locy >= 32802) && (locy <= 32805) && (mapid == 4)) || (mapid == 5101)) {
				ret = true;
			}
		}
		else if (houseId == 262179) { // ギランアジト35
			if (((locx >= 33373) && (locx <= 33385) && (locy >= 32822) && (locy <= 32829) && (mapid == 4)) || (mapid == 5102)) {
				ret = true;
			}
		}
		else if (houseId == 262180) { // ギランアジト36
			if (((locx >= 33398) && (locx <= 33401) && (locy >= 32810) && (locy <= 32816) && (mapid == 4)) || (mapid == 5103)) {
				ret = true;
			}
		}
		else if (houseId == 262181) { // ギランアジト37
			if (((locx >= 33397) && (locx <= 33403) && (locy >= 32821) && (locy <= 32823) && (mapid == 4))
					|| ((locx >= 33400) && (locx <= 33403) && (locy >= 32824) && (locy <= 32828) && (mapid == 4)) || (mapid == 5104)) {
				ret = true;
			}
		}
		else if (houseId == 262182) { // ギランアジト38
			if (((locx >= 33431) && (locx <= 33438) && (locy >= 32838) && (locy <= 32841) && (mapid == 4))
					|| ((locx >= 33436) && (locx <= 33438) && (locy >= 32842) && (locy <= 32844) && (mapid == 4)) || (mapid == 5105)) {
				ret = true;
			}
		}
		else if (houseId == 262183) { // ギランアジト39
			if (((locx >= 33456) && (locx <= 33462) && (locy >= 32838) && (locy <= 32841) && (mapid == 4)) || (mapid == 5106)) {
				ret = true;
			}
		}
		else if (houseId == 262184) { // ギランアジト40
			if (((locx >= 33385) && (locx <= 33392) && (locy >= 32845) && (locy <= 32848) && (mapid == 4))
					|| ((locx >= 33390) && (locx <= 33392) && (locy >= 32849) && (locy <= 32851) && (mapid == 4)) || (mapid == 5107)) {
				ret = true;
			}
		}
		else if (houseId == 262185) { // ギランアジト41
			if (((locx >= 33399) && (locx <= 33405) && (locy >= 32859) && (locy <= 32861) && (mapid == 4))
					|| ((locx >= 33402) && (locx <= 33405) && (locy >= 32862) && (locy <= 32866) && (mapid == 4)) || (mapid == 5108)) {
				ret = true;
			}
		}
		else if (houseId == 262186) { // ギランアジト42
			if (((locx >= 33414) && (locx <= 33417) && (locy >= 32850) && (locy <= 32856) && (mapid == 4)) || (mapid == 5109)) {
				ret = true;
			}
		}
		else if (houseId == 262187) { // ギランアジト43
			if (((locx >= 33372) && (locx <= 33384) && (locy >= 32867) && (locy <= 32873) && (mapid == 4)) || (mapid == 5110)) {
				ret = true;
			}
		}
		else if (houseId == 262188) { // ギランアジト44
			if (((locx >= 33425) && (locx <= 33437) && (locy >= 32865) && (locy <= 32871) && (mapid == 4)) || (mapid == 5111)) {
				ret = true;
			}
		}
		else if (houseId == 262189) { // ギランアジト45
			if (((locx >= 33443) && (locx <= 33449) && (locy >= 32869) && (locy <= 32871) && (mapid == 4))
					|| ((locx >= 33446) && (locx <= 33449) && (locy >= 32872) && (locy <= 32876) && (mapid == 4)) || (mapid == 5112)) {
				ret = true;
			}
		}
		else if (houseId == 327681) { // ハイネアジト1
			if (((locx >= 33599) && (locx <= 33601) && (locy >= 33213) && (locy <= 33214) && (mapid == 4))
					|| ((locx >= 33602) && (locx <= 33610) && (locy >= 33213) && (locy <= 33218) && (mapid == 4)) || (mapid == 5113)) {
				ret = true;
			}
		}
		else if (houseId == 327682) { // ハイネアジト2
			if (((locx >= 33627) && (locx <= 33632) && (locy >= 33206) && (locy <= 33209) && (mapid == 4)) || (mapid == 5114)) {
				ret = true;
			}
		}
		else if (houseId == 327683) { // ハイネアジト3
			if (((locx >= 33626) && (locx <= 33627) && (locy >= 33225) && (locy <= 33227) && (mapid == 4))
					|| ((locx >= 33628) && (locx <= 33632) && (locy >= 33221) && (locy <= 33230) && (mapid == 4)) || (mapid == 5115)) {
				ret = true;
			}
		}
		else if (houseId == 327684) { // ハイネアジト4
			if (((locx >= 33628) && (locx <= 33636) && (locy >= 33241) && (locy <= 33244) && (mapid == 4))
					|| ((locx >= 33632) && (locx <= 33635) && (locy >= 33245) && (locy <= 33250) && (mapid == 4))
					|| ((locx >= 33634) && (locx <= 33634) && (locy >= 33251) && (locy <= 33252) && (mapid == 4)) || (mapid == 5116)) {
				ret = true;
			}
		}
		else if (houseId == 327685) { // ハイネアジト5
			if (((locx >= 33616) && (locx <= 33621) && (locy >= 33262) && (locy <= 33265) && (mapid == 4)) || (mapid == 5117)) {
				ret = true;
			}
		}
		else if (houseId == 327686) { // ハイネアジト6
			if (((locx >= 33570) && (locx <= 33580) && (locy >= 33228) && (locy <= 33232) && (mapid == 4))
					|| ((locx >= 33574) && (locx <= 33576) && (locy >= 33233) && (locy <= 33234) && (mapid == 4)) || (mapid == 5118)) {
				ret = true;
			}
		}
		else if (houseId == 327687) { // ハイネアジト7
			if (((locx >= 33583) && (locx <= 33588) && (locy >= 33305) && (locy <= 33314) && (mapid == 4))
					|| ((locx >= 33587) && (locx <= 33588) && (locy >= 33315) && (locy <= 33316) && (mapid == 4)) || (mapid == 5119)) {
				ret = true;
			}
		}
		else if (houseId == 327688) { // ハイネアジト8
			if (((locx >= 33577) && (locx <= 33578) && (locy >= 33337) && (locy <= 33337) && (mapid == 4))
					|| ((locx >= 33579) && (locx <= 33588) && (locy >= 33335) && (locy <= 33339) && (mapid == 4))
					|| ((locx >= 33585) && (locx <= 33588) && (locy >= 33340) && (locy <= 33343) && (mapid == 4)) || (mapid == 5120)) {
				ret = true;
			}
		}
		else if (houseId == 327689) { // ハイネアジト9
			if (((locx >= 33615) && (locx <= 33623) && (locy >= 33374) && (locy <= 33377) && (mapid == 4))
					|| ((locx >= 33619) && (locx <= 33622) && (locy >= 33378) && (locy <= 33383) && (mapid == 4))
					|| ((locx >= 33621) && (locx <= 33621) && (locy >= 33384) && (locy <= 33385) && (mapid == 4)) || (mapid == 5121)) {
				ret = true;
			}
		}
		else if (houseId == 327690) { // ハイネアジト10
			if (((locx >= 33624) && (locx <= 33625) && (locy >= 33397) && (locy <= 33399) && (mapid == 4))
					|| ((locx >= 33626) && (locx <= 33630) && (locy >= 33393) && (locy <= 33403) && (mapid == 4)) || (mapid == 5122)) {
				ret = true;
			}
		}
		else if (houseId == 327691) { // ハイネアジト11
			if (((locx >= 33621) && (locx <= 33622) && (locy >= 33444) && (locy <= 33444) && (mapid == 4))
					|| ((locx >= 33622) && (locx <= 33632) && (locy >= 33442) && (locy <= 33446) && (mapid == 4))
					|| ((locx >= 33629) && (locx <= 33632) && (locy >= 33447) && (locy <= 33450) && (mapid == 4)) || (mapid == 5123)) {
				ret = true;
			}
		}
		else if (houseId == 524289) { // グルーディンアジト1
			if ((locx >= 32559) && (locx <= 32566) && (locy >= 32669) && (locy <= 32676) && (mapid == 4)) {
				ret = true;
			}
		}
		else if (houseId == 524290) { // グルーディンアジト2
			if (((locx >= 32548) && (locx <= 32556) && (locy >= 32705) && (locy <= 32716) && (mapid == 4))
					|| ((locx >= 32547) && (locx <= 32547) && (locy >= 32710) && (locy <= 32716) && (mapid == 4))) {
				ret = true;
			}
		}
		else if (houseId == 524291) { // グルーディンアジト3
			if ((locx >= 32537) && (locx <= 32544) && (locy >= 32781) && (locy <= 32791) && (mapid == 4)) {
				ret = true;
			}
		}
		else if (houseId == 524292) { // グルーディンアジト4
			if ((locx >= 32550) && (locx <= 32560) && (locy >= 32780) && (locy <= 32787) && (mapid == 4)) {
				ret = true;
			}
		}
		else if (houseId == 524293) { // グルーディンアジト5
			if (((locx >= 32535) && (locx <= 32543) && (locy >= 32807) && (locy <= 32818) && (mapid == 4))
					|| ((locx >= 32534) && (locx <= 32534) && (locy >= 32812) && (locy <= 32818) && (mapid == 4))) {
				ret = true;
			}
		}
		else if (houseId == 524294) { // グルーディンアジト6
			if ((locx >= 32553) && (locx <= 32560) && (locy >= 32814) && (locy <= 32821) && (mapid == 4)) {
				ret = true;
			}
		}
		return ret;
	}

	public static int[] getHouseLoc(int houseId) { // houseIdからアジトの座標を返す
		int[] loc = new int[3];
		if (houseId == 262145) { // ギランアジト1
			loc[0] = 33374;
			loc[1] = 32657;
			loc[2] = 4;
		}
		else if (houseId == 262146) { // ギランアジト2
			loc[0] = 33384;
			loc[1] = 32655;
			loc[2] = 4;
		}
		else if (houseId == 262147) { // ギランアジト3
			loc[0] = 33395;
			loc[1] = 32656;
			loc[2] = 4;
		}
		else if (houseId == 262148) { // ギランアジト4
			loc[0] = 33428;
			loc[1] = 32659;
			loc[2] = 4;
		}
		else if (houseId == 262149) { // ギランアジト5
			loc[0] = 33439;
			loc[1] = 32666;
			loc[2] = 4;
		}
		else if (houseId == 262150) { // ギランアジト6
			loc[0] = 33457;
			loc[1] = 32654;
			loc[2] = 4;
		}
		else if (houseId == 262151) { // ギランアジト7
			loc[0] = 33477;
			loc[1] = 32668;
			loc[2] = 4;
		}
		else if (houseId == 262152) { // ギランアジト8
			loc[0] = 33471;
			loc[1] = 32679;
			loc[2] = 4;
		}
		else if (houseId == 262153) { // ギランアジト9
			loc[0] = 33459;
			loc[1] = 32700;
			loc[2] = 4;
		}
		else if (houseId == 262154) { // ギランアジト10
			loc[0] = 33424;
			loc[1] = 32691;
			loc[2] = 4;
		}
		else if (houseId == 262155) { // ギランアジト11
			loc[0] = 33409;
			loc[1] = 32675;
			loc[2] = 4;
		}
		else if (houseId == 262156) { // ギランアジト12
			loc[0] = 33420;
			loc[1] = 32709;
			loc[2] = 4;
		}
		else if (houseId == 262157) { // ギランアジト13
			loc[0] = 33375;
			loc[1] = 32698;
			loc[2] = 4;
		}
		else if (houseId == 262158) { // ギランアジト14
			loc[0] = 33363;
			loc[1] = 32684;
			loc[2] = 4;
		}
		else if (houseId == 262159) { // ギランアジト15
			loc[0] = 33360;
			loc[1] = 32670;
			loc[2] = 4;
		}
		else if (houseId == 262160) { // ギランアジト16
			loc[0] = 33341;
			loc[1] = 32661;
			loc[2] = 4;
		}
		else if (houseId == 262161) { // ギランアジト17
			loc[0] = 33346;
			loc[1] = 32675;
			loc[2] = 4;
		}
		else if (houseId == 262162) { // ギランアジト18
			loc[0] = 33341;
			loc[1] = 32710;
			loc[2] = 4;
		}
		else if (houseId == 262163) { // ギランアジト19
			loc[0] = 33355;
			loc[1] = 32734;
			loc[2] = 4;
		}
		else if (houseId == 262164) { // ギランアジト20
			loc[0] = 33366;
			loc[1] = 32714;
			loc[2] = 4;
		}
		else if (houseId == 262165) { // ギランアジト21
			loc[0] = 33381;
			loc[1] = 32715;
			loc[2] = 4;
		}
		else if (houseId == 262166) { // ギランアジト22
			loc[0] = 33404;
			loc[1] = 32739;
			loc[2] = 4;
		}
		else if (houseId == 262167) { // ギランアジト23
			loc[0] = 33424;
			loc[1] = 32718;
			loc[2] = 4;
		}
		else if (houseId == 262168) { // ギランアジト24
			loc[0] = 33449;
			loc[1] = 32732;
			loc[2] = 4;
		}
		else if (houseId == 262169) { // ギランアジト25
			loc[0] = 33405;
			loc[1] = 32757;
			loc[2] = 4;
		}
		else if (houseId == 262170) { // ギランアジト26
			loc[0] = 33366;
			loc[1] = 32761;
			loc[2] = 4;
		}
		else if (houseId == 262171) { // ギランアジト27
			loc[0] = 33351;
			loc[1] = 32775;
			loc[2] = 4;
		}
		else if (houseId == 262172) { // ギランアジト28
			loc[0] = 33358;
			loc[1] = 32789;
			loc[2] = 4;
		}
		else if (houseId == 262173) { // ギランアジト29
			loc[0] = 33372;
			loc[1] = 32792;
			loc[2] = 4;
		}
		else if (houseId == 262174) { // ギランアジト30
			loc[0] = 33384;
			loc[1] = 32776;
			loc[2] = 4;
		}
		else if (houseId == 262175) { // ギランアジト31
			loc[0] = 33403;
			loc[1] = 32794;
			loc[2] = 4;
		}
		else if (houseId == 262176) { // ギランアジト32
			loc[0] = 33485;
			loc[1] = 32794;
			loc[2] = 4;
		}
		else if (houseId == 262177) { // ギランアジト33
			loc[0] = 33499;
			loc[1] = 32804;
			loc[2] = 4;
		}
		else if (houseId == 262178) { // ギランアジト34
			loc[0] = 33382;
			loc[1] = 32804;
			loc[2] = 4;
		}
		else if (houseId == 262179) { // ギランアジト35
			loc[0] = 33376;
			loc[1] = 32828;
			loc[2] = 4;
		}
		else if (houseId == 262180) { // ギランアジト36
			loc[0] = 33399;
			loc[1] = 32813;
			loc[2] = 4;
		}
		else if (houseId == 262181) { // ギランアジト37
			loc[0] = 33397;
			loc[1] = 32822;
			loc[2] = 4;
		}
		else if (houseId == 262182) { // ギランアジト38
			loc[0] = 33437;
			loc[1] = 32844;
			loc[2] = 4;
		}
		else if (houseId == 262183) { // ギランアジト39
			loc[0] = 33459;
			loc[1] = 32840;
			loc[2] = 4;
		}
		else if (houseId == 262184) { // ギランアジト40
			loc[0] = 33391;
			loc[1] = 32851;
			loc[2] = 4;
		}
		else if (houseId == 262185) { // ギランアジト41
			loc[0] = 33399;
			loc[1] = 32860;
			loc[2] = 4;
		}
		else if (houseId == 262186) { // ギランアジト42
			loc[0] = 33415;
			loc[1] = 32853;
			loc[2] = 4;
		}
		else if (houseId == 262187) { // ギランアジト43
			loc[0] = 33375;
			loc[1] = 32873;
			loc[2] = 4;
		}
		else if (houseId == 262188) { // ギランアジト44
			loc[0] = 33428;
			loc[1] = 32871;
			loc[2] = 4;
		}
		else if (houseId == 262189) { // ギランアジト45
			loc[0] = 33443;
			loc[1] = 32870;
			loc[2] = 4;
		}
		else if (houseId == 327681) { // ハイネアジト1
			loc[0] = 33609;
			loc[1] = 33217;
			loc[2] = 4;
		}
		else if (houseId == 327682) { // ハイネアジト2
			loc[0] = 33630;
			loc[1] = 33209;
			loc[2] = 4;
		}
		else if (houseId == 327683) { // ハイネアジト3
			loc[0] = 33628;
			loc[1] = 33226;
			loc[2] = 4;
		}
		else if (houseId == 327684) { // ハイネアジト4
			loc[0] = 33633;
			loc[1] = 33248;
			loc[2] = 4;
		}
		else if (houseId == 327685) { // ハイネアジト5
			loc[0] = 33619;
			loc[1] = 33265;
			loc[2] = 4;
		}
		else if (houseId == 327686) { // ハイネアジト6
			loc[0] = 33575;
			loc[1] = 33233;
			loc[2] = 4;
		}
		else if (houseId == 327687) { // ハイネアジト7
			loc[0] = 33584;
			loc[1] = 33306;
			loc[2] = 4;
		}
		else if (houseId == 327688) { // ハイネアジト8
			loc[0] = 33581;
			loc[1] = 33338;
			loc[2] = 4;
		}
		else if (houseId == 327689) { // ハイネアジト9
			loc[0] = 33620;
			loc[1] = 33381;
			loc[2] = 4;
		}
		else if (houseId == 327690) { // ハイネアジト10
			loc[0] = 33625;
			loc[1] = 33398;
			loc[2] = 4;
		}
		else if (houseId == 327691) { // ハイネアジト11
			loc[0] = 33625;
			loc[1] = 33445;
			loc[2] = 4;
		}
		else if (houseId == 524289) { // グルーディンアジト1
			loc[0] = 32564;
			loc[1] = 32675;
			loc[2] = 4;
		}
		else if (houseId == 524290) { // グルーディンアジト2
			loc[0] = 32549;
			loc[1] = 32707;
			loc[2] = 4;
		}
		else if (houseId == 524291) { // グルーディンアジト3
			loc[0] = 32538;
			loc[1] = 32782;
			loc[2] = 4;
		}
		else if (houseId == 524292) { // グルーディンアジト4
			loc[0] = 32558;
			loc[1] = 32786;
			loc[2] = 4;
		}
		else if (houseId == 524293) { // グルーディンアジト5
			loc[0] = 32536;
			loc[1] = 32809;
			loc[2] = 4;
		}
		else if (houseId == 524294) { // グルーディンアジト6
			loc[0] = 32554;
			loc[1] = 32819;
			loc[2] = 4;
		}
		return loc;
	}

	public static int[] getBasementLoc(int houseId) { // houseIdからアジトの地下室の座標を返す
		int[] loc = new int[3];
		if ((houseId >= 262145) && (houseId <= 262189)) { // ギランアジト1~45
			loc[0] = 32780;
			loc[1] = 32815;
			loc[2] = houseId - 257077;
		}
		else if ((houseId >= 327681) && (houseId <= 327691)) { // ハイネアジト1~11
			loc[0] = 32772;
			loc[1] = 32814;
			loc[2] = houseId - 322568;
		}
		else if ((houseId >= 524289) && (houseId <= 524294)) { // グルーディンアジト1~6
			// 地下室がないため、アジトの入り口の座標を返す
			loc = getHouseLoc(houseId);
		}
		return loc;
	}

	/**
	 * 指定した座標がアジト内かどうかを返す
	 * 
	 * @param cha
	 * @return
	 */
	public static boolean isInHouse(int locx, int locy, short mapid) {
		boolean result = false;
		for (Integer houseId : _houseIds) {
			if (isInHouseLoc(houseId, locx, locy, mapid)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static int[] getHouseTeleportLoc(int houseId, int number) { // houseIdからテレポート先の座標を返す
		int[] loc = new int[3];
		if ((houseId >= 262145) && (houseId <= 262189)) { // ギランアジト
			loc[0] = TELEPORT_LOC_GIRAN[number].getX();
			loc[1] = TELEPORT_LOC_GIRAN[number].getY();
			loc[2] = TELEPORT_LOC_MAPID[number];
		}
		else if ((houseId >= 327681) && (houseId <= 327691)) { // ハイネアジト
			loc[0] = TELEPORT_LOC_HEINE[number].getX();
			loc[1] = TELEPORT_LOC_HEINE[number].getY();
			loc[2] = TELEPORT_LOC_MAPID[number];
		}
		else if ((houseId >= 458753) && (houseId <= 458819)) { // アデンアジト
			loc[0] = TELEPORT_LOC_ADEN[number].getX();
			loc[1] = TELEPORT_LOC_ADEN[number].getY();
			loc[2] = TELEPORT_LOC_MAPID[number];
		}
		else if ((houseId >= 524289) && (houseId <= 524294)) { // グルーディンアジト1~6
			loc[0] = TELEPORT_LOC_GLUDIN[number].getX();
			loc[1] = TELEPORT_LOC_GLUDIN[number].getY();
			loc[2] = TELEPORT_LOC_MAPID[number];
		}
		return loc;
	}

}
