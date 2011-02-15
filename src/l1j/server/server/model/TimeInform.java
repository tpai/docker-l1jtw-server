package l1j.server.server.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;

/**
 * <strong>Time Information</strong>
 * 
 * @author ibm
 * 
 */
public class TimeInform {
	/**
	 * Calendar本身是abstract,使用getInstance()來取得物件
	 */
	static TimeZone timezone = TimeZone.getTimeZone(Config.TIME_ZONE);
	static Calendar rightNow = Calendar.getInstance(timezone);

	/**
	 * @return getYear 年 param type 1:西元 2:民國
	 */
	public static String getYear(byte type) {
		String year;
		if (type == 0)// 西元
			year = " 西元 " + String.valueOf(rightNow.get(Calendar.YEAR));
		else          // 民國
			year = " 民國 " + String.valueOf(rightNow.get(Calendar.YEAR) - 1911);
		return year;
	}

	/**
	 * @return getMonth 月
	 */
	public static String getMonth() {
		// Calendar.MONTH - index從0開始
		return String.valueOf(rightNow.get(Calendar.MONTH) + 1);
	}

	/**
	 * @return getDay 日
	 */
	public static String getDay() {

		return String.valueOf(rightNow.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * @return getHour 時
	 */
	public static String getHour() {

		return String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
	}

	/**
	 * @return getMinute 分
	 */
	public static String getMinute() {

		return String.valueOf(rightNow.get(Calendar.MINUTE));
	}

	/**
	 * @return getSecond 秒
	 */
	public static String getSecond() {

		return String.valueOf(rightNow.get(Calendar.MINUTE));
	}

	/**
	 * 
	 * @param type
	 *            時間格式<BR>
	 *            type = 1 : X年X月X日<BR>
	 *            type = 2 : X時X分X秒<BR>
	 *            type = 3 : X年X月X日-X時X分X秒<BR>
	 * @param type_year
	 *            0:西元 1:民國
	 * @return
	 */
	public static String getNowTime(int type, byte type_year) {
		String NowTime = null;
		switch (type) {
		case 1:
			NowTime = TimeInform.getYear(type_year) + " 年 "
					+ TimeInform.getMonth() + " 月 " + TimeInform.getDay()
					+ " 日 ";
			break;
		case 2:
			NowTime = TimeInform.getHour() + "時" + TimeInform.getMinute() + "分"
					+ TimeInform.getSecond() + "秒";
			break;
		case 3:
			NowTime = TimeInform.getYear(type_year) + " 年 "
					+ TimeInform.getMonth() + " 月 " + TimeInform.getDay()
					+ " 日    " + TimeInform.getHour() + "時"
					+ TimeInform.getMinute() + "分" + TimeInform.getSecond()
					+ "秒";
		default:

		}
		return NowTime;
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String getNowTime_Standard() {

		String NowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(rightNow);

		return NowTime;
	}
}
