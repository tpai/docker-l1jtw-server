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

import java.sql.Timestamp;

/** 旅館 */
public class L1Inn {

	private int _npcId;
	private int _roomNumber;
	private int _keyId;
	private int _lodgerId;
	private boolean _hall;
	private Timestamp _dueTime;

	public int getKeyId() {
		return _keyId;
	}

	public void setKeyId(int i) {
		_keyId = i;
	}

	public int getInnNpcId() {
		return _npcId;
	}

	public void setInnNpcId(int i) {
		_npcId = i;
	}

	public int getRoomNumber() {
		return _roomNumber;
	}

	public void setRoomNumber(int i) {
		_roomNumber = i;
	}

	public int getLodgerId() {
		return _lodgerId;
	}

	public void setLodgerId(int i) {
		_lodgerId = i;
	}

	public boolean isHall() {
		return _hall;
	}

	public void setHall(boolean hall) {
		_hall = hall;
	}

	public Timestamp getDueTime() {
		return _dueTime;
	}

	public void setDueTime(Timestamp i) {
		_dueTime = i;
	}

}
