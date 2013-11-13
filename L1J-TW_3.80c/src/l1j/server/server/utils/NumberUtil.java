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

public class NumberUtil {

	/**
	 * 少数を小数点第二位までの確率で上か下に丸めた整数を返す。
	 * 例えば1.3は30%の確率で切り捨て、70%の確率で切り上げられる。
	 * 
	 * @param number - もとの少数
	 * @return 丸められた整数
	 */
	public static int randomRound(double number) {
		double percentage = (number - Math.floor(number)) * 100;
		
		if (percentage == 0) {
			return ((int) number);
		} else {
			int r = Random.nextInt(100);
			if (r < percentage) {
				return ((int) number + 1);
			} else {
				return ((int) number);
			}
		}
	}
}
