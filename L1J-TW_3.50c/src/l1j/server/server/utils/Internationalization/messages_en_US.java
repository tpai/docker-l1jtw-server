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
package l1j.server.server.utils.Internationalization;

import java.util.ListResourceBundle;

/**
 * @category 英美-英語<br>
 *           國際化的英文是Internationalization 因為單字中總共有18個字母，簡稱I18N，
 *           目的是讓應用程式可以應地區不同而顯示不同的訊息。
 */

public class messages_en_US extends ListResourceBundle {
	static final Object[][] contents = {
		{ "l1j.server.memoryUse", "Used: " },
		{ "l1j.server.memory", "MB of memory" },
		{ "l1j.server.server.model.onGroundItem", "items on the ground" },
		{ "l1j.server.server.model.seconds","will be delete after 10 seconds" },
		{ "l1j.server.server.model.deleted", "was deleted" }, 
		{ "l1j.server.server.GameServer.ver","Version: Lineage 3.5C  Dev. By L1J-TW For All User" },
		{ "l1j.server.server.GameServer.settingslist","●●●●〈Server Config List〉●●●●"},
		{ "l1j.server.server.GameServer.exp","「exp」"},
		{ "l1j.server.server.GameServer.x","【times】"},
		{ "l1j.server.server.GameServer.level","【LV】"},
		{ "l1j.server.server.GameServer.justice","「justice」"},
		{ "l1j.server.server.GameServer.karma","「karma」"},
		{ "l1j.server.server.GameServer.dropitems","「dropitems」"},
		{ "l1j.server.server.GameServer.dropadena","「dropadena」"},
		{ "l1j.server.server.GameServer.enchantweapon","「enchantweapon」"},
		{ "l1j.server.server.GameServer.enchantarmor","「enchantarmor」"},
		{ "l1j.server.server.GameServer.chatlevel","「chatLevel」"},
		{ "l1j.server.server.GameServer.nonpvp1","「Non-PvP」: Not Work (PvP)"},
		{ "l1j.server.server.GameServer.nonpvp2","「Non-PvP」: Work (Non-PvP)"},
		{ "l1j.server.server.GameServer.maxplayer","Max connection limit "},
		{ "l1j.server.server.GameServer.player"," players"},
		{ "l1j.server.server.GameServer.waitingforuser","Waiting for user's connection..."},
		{ "l1j.server.server.GameServer.from","from "},
		{ "l1j.server.server.GameServer.attempt"," attempt to connect."},
		{ "l1j.server.server.GameServer.setporton","Server is successfully set on port "},
		{ "l1j.server.server.GameServer.initialfinished","Initialize finished.."}};

	@Override
	protected Object[][] getContents() {
		return contents;
	}

}
