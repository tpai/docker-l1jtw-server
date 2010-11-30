/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.utils;

import java.util.Random;

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
			int r = new Random().nextInt(100);
			if (r < percentage) {
				return ((int) number + 1);
			} else {
				return ((int) number);
			}
		}
	}
}
