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
package l1j.server.server.templates;

import java.util.Calendar;

public class L1Castle {
	public L1Castle(int id, String name) {
		_id = id;
		_name = name;
	}

	private int _id;

	public int getId() {
		return _id;
	}

	private String _name;

	public String getName() {
		return _name;
	}

	private Calendar _warTime;

	public Calendar getWarTime() {
		return _warTime;
	}

	public void setWarTime(Calendar i) {
		_warTime = i;
	}

	private int _taxRate;

	public int getTaxRate() {
		return _taxRate;
	}

	public void setTaxRate(int i) {
		_taxRate = i;
	}

	private int _publicMoney;

	public int getPublicMoney() {
		if(_publicMoney < 0)
			return 0;
		else
		    return _publicMoney;
	}

	public void setPublicMoney(int i) {
		_publicMoney = i;
	}
	
	private int heldByClan;
	
	public int getHeldClanId(){
		return heldByClan;
	}
	
	public void setHeldClan(int i) {
		heldByClan = i;
	}

}
