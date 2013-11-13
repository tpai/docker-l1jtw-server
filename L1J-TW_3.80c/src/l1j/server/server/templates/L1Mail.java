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

public class L1Mail {
	public L1Mail() {
	}

	private int _id;

	public int getId() {
		return _id;
	}

	public void setId(int i) {
		_id = i;
	}

	private int _type;

	public int getType() {
		return _type;
	}

	public void setType(int i) {
		_type = i;
	}

	private String _senderName;

	public String getSenderName() {
		return _senderName;
	}

	public void setSenderName(String s) {
		_senderName = s;
	}

	private String _receiverName;

	public String getReceiverName() {
		return _receiverName;
	}

	public void setReceiverName(String s) {
		_receiverName = s;
	}

	private Timestamp _date = null;

	public Timestamp getDate() {
		return _date;
	}

	public void setDate(Timestamp s) {
		_date = s;
	}

	private int _readStatus = 0;

	public int getReadStatus() {
		return _readStatus;
	}

	public void setReadStatus(int i) {
		_readStatus = i;
	}

	private byte[] _subject = null;

	public byte[] getSubject() {
		return _subject;
	}

	public void setSubject(byte[] arg) {
		_subject = arg;
	}

	private byte[] _content = null;

	public byte[] getContent() {
		return _content;
	}

	public void setContent(byte[] arg) {
		_content = arg;
	}
	
	private int _inBoxId = 0;

	public int getInBoxId() {
		return _inBoxId;
	}

	public void setInBoxId(int i) {
		_inBoxId = i;
	}

}
