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
package l1j.server.server.utils;

import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_Weather;

/**
 * @category 隨機天氣控制
 * @author L1J-TW
 * @since 2010.12.19
 */
public class Weather {
	/**
	 * @宣告
	 */
	private static int[] WeatherId =
	{ 0, 1, 2, 3, 16, 17, 18, 19 };

	/**
	 * @天氣 0<無雪雨> 1<小雪>、2<中雪> 3<大雪> 16<停止下雨> 17<小雨> 18<中雨> 19<大雨>
	 */
	public Weather() {
		int ran = Random.nextInt(8); // 讀取亂數
		L1World.getInstance().setWeather(WeatherId[ran]);
		L1World.getInstance().broadcastPacketToAll(new S_Weather(WeatherId[ran]));
	}
}
