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

public class L1NpcChat {
	public L1NpcChat() {
	}

	private int _npcId;

	public int getNpcId() {
		return _npcId;
	}

	public void setNpcId(int i) {
		_npcId = i;
	}

	private int _chatTiming;

	public int getChatTiming() {
		return _chatTiming;
	}

	public void setChatTiming(int i) {
		_chatTiming = i;
	}

	private int _startDelayTime;

	public int getStartDelayTime() {
		return _startDelayTime;
	}

	public void setStartDelayTime(int i) {
		_startDelayTime = i;
	}

	private String _chatId1;

	public String getChatId1() {
		return _chatId1;
	}

	public void setChatId1(String s) {
		_chatId1 = s;
	}

	private String _chatId2;

	public String getChatId2() {
		return _chatId2;
	}

	public void setChatId2(String s) {
		_chatId2 = s;
	}

	private String _chatId3;

	public String getChatId3() {
		return _chatId3;
	}

	public void setChatId3(String s) {
		_chatId3 = s;
	}

	private String _chatId4;

	public String getChatId4() {
		return _chatId4;
	}

	public void setChatId4(String s) {
		_chatId4 = s;
	}

	private String _chatId5;

	public String getChatId5() {
		return _chatId5;
	}

	public void setChatId5(String s) {
		_chatId5 = s;
	}

	private int _chatInterval;

	public int getChatInterval() {
		return _chatInterval;
	}

	public void setChatInterval(int i) {
		_chatInterval = i;
	}

	private boolean _isShout;

	public boolean isShout() {
		return _isShout;
	}

	public void setShout(boolean flag) {
		_isShout = flag;
	}

	private boolean _isWorldChat;

	public boolean isWorldChat() {
		return _isWorldChat;
	}

	public void setWorldChat(boolean flag) {
		_isWorldChat = flag;
	}

	private boolean _isRepeat;

	public boolean isRepeat() {
		return _isRepeat;
	}

	public void setRepeat(boolean flag) {
		_isRepeat = flag;
	}

	private int _repeatInterval;

	public int getRepeatInterval() {
		return _repeatInterval;
	}

	public void setRepeatInterval(int i) {
		_repeatInterval = i;
	}

	private int _gameTime;

	public int getGameTime() {
		return _gameTime;
	}

	public void setGameTime(int i) {
		_gameTime = i;
	}

}
