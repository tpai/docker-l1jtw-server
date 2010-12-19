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

public class L1House {
	public L1House() {
	}

	private int _houseId;

	public int getHouseId() {
		return _houseId;
	}

	public void setHouseId(int i) {
		_houseId = i;
	}

	private String _houseName;

	public String getHouseName() {
		return _houseName;
	}

	public void setHouseName(String s) {
		_houseName = s;
	}

	private int _houseArea;

	public int getHouseArea() {
		return _houseArea;
	}

	public void setHouseArea(int i) {
		_houseArea = i;
	}

	private String _location;

	public String getLocation() {
		return _location;
	}

	public void setLocation(String s) {
		_location = s;
	}

	private int _keeperId;

	public int getKeeperId() {
		return _keeperId;
	}

	public void setKeeperId(int i) {
		_keeperId = i;
	}

	private boolean _isOnSale;

	public boolean isOnSale() {
		return _isOnSale;
	}

	public void setOnSale(boolean flag) {
		_isOnSale = flag;
	}

	private boolean _isPurchaseBasement;

	public boolean isPurchaseBasement() {
		return _isPurchaseBasement;
	}

	public void setPurchaseBasement(boolean flag) {
		_isPurchaseBasement = flag;
	}

	private Calendar _taxDeadline;

	public Calendar getTaxDeadline() {
		return _taxDeadline;
	}

	public void setTaxDeadline(Calendar i) {
		_taxDeadline = i;
	}

}