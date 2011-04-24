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
package l1j.server;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import l1j.server.server.utils.Internationalization.*;

/**
 * 國際化的英文是Internationalization 因為單字中總共有18個字母，簡稱I18N， 目的是讓應用程式可以應地區不同而顯示不同的訊息。
 */
public class L1Message {

	private static L1Message _instance;
	ResourceBundle resource;

	private L1Message() {
		try {
			resource = ResourceBundle.getBundle(messages.class.getName());
			initLocaleMessage();
		} catch (MissingResourceException mre) {
			mre.printStackTrace();
		}
	}

	public static L1Message getInstance() {
		if (_instance == null) {
			_instance = new L1Message();
		}
		return _instance;
	}

	/** 簡短化變數名詞 */
	public void initLocaleMessage() {
		memoryUse = resource.getString("l1j.server.memoryUse");
		onGroundItem = resource.getString("l1j.server.server.model.onGroundItem");
		secondsDelete = resource.getString("l1j.server.server.model.seconds");
		deleted = resource.getString("l1j.server.server.model.deleted");
		ver = resource.getString("l1j.server.server.GameServer.ver");
		settingslist = resource.getString("l1j.server.server.GameServer.settingslist");
		exp = resource.getString("l1j.server.server.GameServer.exp");
		x = resource.getString("l1j.server.server.GameServer.x");
		level = resource.getString("l1j.server.server.GameServer.level");
		justice = resource.getString("l1j.server.server.GameServer.justice");
		karma = resource.getString("l1j.server.server.GameServer.karma");
		dropitems = resource.getString("l1j.server.server.GameServer.dropitems");
		dropadena = resource.getString("l1j.server.server.GameServer.dropadena");
		enchantweapon = resource.getString("l1j.server.server.GameServer.enchantweapon");
		enchantarmor = resource.getString("l1j.server.server.GameServer.enchantarmor");
		chatlevel = resource.getString("l1j.server.server.GameServer.chatlevel");
		nonpvpNo = resource.getString("l1j.server.server.GameServer.nonpvp1");
		nonpvpYes = resource.getString("l1j.server.server.GameServer.nonpvp2");
		memory = resource.getString("l1j.server.server.GameServer.memory");
		maxplayer = resource.getString("l1j.server.server.GameServer.maxplayer");
		player = resource.getString("l1j.server.server.GameServer.player");
		waitingforuser = resource.getString("l1j.server.server.GameServer.waitingforuser");
		from = resource.getString("l1j.server.server.GameServer.from");
		attempt = resource.getString("l1j.server.server.GameServer.attempt");
		setporton = resource.getString("l1j.server.server.GameServer.setporton");
		initialfinished = resource.getString("l1j.server.server.GameServer.initialfinished");
	}

	/** static 變數 */
	public static String memoryUse;
	public static String onGroundItem;
	public static String secondsDelete;
	public static String deleted;
	public static String ver;
	public static String settingslist;
	public static String exp;
	public static String x;
	public static String level;
	public static String justice;
	public static String karma;
	public static String dropitems;
	public static String dropadena;
	public static String enchantweapon;
	public static String enchantarmor;
	public static String chatlevel;
	public static String nonpvpNo;
	public static String nonpvpYes;
	public static String memory;
	public static String maxplayer;
	public static String player;
	public static String waitingforuser;
	public static String from;
	public static String attempt;
	public static String setporton;
	public static String initialfinished;
}
