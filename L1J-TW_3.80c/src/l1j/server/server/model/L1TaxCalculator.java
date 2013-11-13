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

public class L1TaxCalculator {
	/**
	 * 戦争税は15%固定
	 */
	private static final int WAR_TAX_RATES = 15;

	/**
	 * 国税は10%固定（地域税に対する割合）
	 */
	private static final int NATIONAL_TAX_RATES = 10;

	/**
	 * ディアド税は10%固定（戦争税に対する割合）
	 */
	private static final int DIAD_TAX_RATES = 10;

	private final int _taxRatesCastle;
	private final int _taxRatesTown;
	private final int _taxRatesWar = WAR_TAX_RATES;

	/**
	 * @param merchantNpcId
	 *            計算対象商店のNPCID
	 */
	public L1TaxCalculator(int merchantNpcId) {
		_taxRatesCastle = L1CastleLocation.getCastleTaxRateByNpcId(merchantNpcId);
		_taxRatesTown = L1TownLocation.getTownTaxRateByNpcid(merchantNpcId);
	}

	public int calcTotalTaxPrice(int price) {
		int taxCastle = price * _taxRatesCastle;
		int taxTown = price * _taxRatesTown;
		int taxWar = price * WAR_TAX_RATES;
		return (taxCastle + taxTown + taxWar) / 100;
	}

	// XXX 個別に計算する為、丸め誤差が出る。
	public int calcCastleTaxPrice(int price) {
		return (price * _taxRatesCastle) / 100 - calcNationalTaxPrice(price);
	}

	public int calcNationalTaxPrice(int price) {
		return (price * _taxRatesCastle) / 100 / (100 / NATIONAL_TAX_RATES);
	}

	public int calcTownTaxPrice(int price) {
		return (price * _taxRatesTown) / 100;
	}

	public int calcWarTaxPrice(int price) {
		return (price * _taxRatesWar) / 100;
	}

	public int calcDiadTaxPrice(int price) {
		return (price * _taxRatesWar) / 100 / (100 / DIAD_TAX_RATES);
	}

	/**
	 * 課税後の価格を求める。
	 * 
	 * @param price
	 *            課税前の価格
	 * @return 課税後の価格
	 */
	public int layTax(int price) {
		return price + calcTotalTaxPrice(price);
	}
}
