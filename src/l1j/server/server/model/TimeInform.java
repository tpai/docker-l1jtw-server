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
package l1j.server.server.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
	 * @return getYear 年 param type 0:原始(可加減) 1:西元 2:民國
	 * @param i = +|- years
	 */
	public static String getYear(int type, int i) {
		String year = null;
		if (type == 0)
			year = String.valueOf(rightNow.get(Calendar.YEAR + i));
		else if (type == 1)// 西元
			year = "西元 " + String.valueOf(rightNow.get(Calendar.YEAR));
		else if (type == 2)
			// 民國
			year = "民國 " + String.valueOf(rightNow.get(Calendar.YEAR) - 1911);
		else
			year = null;
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
	 *            type = 1 : X年X月X日星期X<BR>
	 *            type = 2 : X時X分X秒<BR>
	 *            type = 3 : X年X月X日-X時X分X秒<BR>
	 * @param type_year
	 *            0:西元 1:民國
	 * @return
	 */
	public static String getNowTime(int type, int type_year) {
		String NowTime = null;
		switch (type) {
		case 1:
			NowTime = TimeInform.getYear(type_year, 0) + "年 "
					+ TimeInform.getMonth() + "月" + TimeInform.getDay() + "日 "
					+ TimeInform.getDayOfWeek();
			break;
		case 2:
			NowTime = TimeInform.getHour() + "時" + TimeInform.getMinute() + "分"
					+ TimeInform.getSecond() + "秒";
			break;
		case 3:
			NowTime = TimeInform.getYear(type_year, 0) + "年"
					+ TimeInform.getMonth() + "月" + TimeInform.getDay() + "日"
					+ TimeInform.getHour() + "時" + TimeInform.getMinute() + "分"
					+ TimeInform.getSecond() + "秒";
		default:

		}
		return NowTime;
	}

	/**
	 * @return getDayOfWeek 星期
	 */
	public static String getDayOfWeek() {
		String DayOfWeek = null;
		switch (rightNow.get(Calendar.DAY_OF_WEEK)) {
		case 1:// index 1~7 星期日~星期六
			DayOfWeek = "星期日";
			break;
		case 2:
			DayOfWeek = "星期一";
			break;
		case 3:
			DayOfWeek = "星期二";
			break;
		case 4:
			DayOfWeek = "星期三";
			break;
		case 5:
			DayOfWeek = "星期四";
			break;
		case 6:
			DayOfWeek = "星期五";
			break;
		case 7:
			DayOfWeek = "星期六";
			break;

		}
		return DayOfWeek;
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String getNowTime_Standard() {

		String NowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());

		return NowTime;
	}
}
