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
 * @category 日本-日本語<br>
 *           國際化的英文是Internationalization 因為單字中總共有18個字母，簡稱I18N，
 *           目的是讓應用程式可以應地區不同而顯示不同的訊息。
 */
public class messages_ja_JP extends ListResourceBundle {
	static final Object[][] contents = { 
		{ "l1j.server.memoryUse", "利用メモリ: " },
		{ "l1j.server.memory", "MB" },
		{ "l1j.server.server.model.onGroundItem", "ワールドマップ上のアイテム" },
		{ "l1j.server.server.model.seconds", "10秒後に削除されます" },
		{ "l1j.server.server.model.deleted", "削除されました" },
		{ "l1j.server.server.GameServer.ver","バージョン: Lineage 3.5C 開発  By L1J For All User" },
		{ "l1j.server.server.GameServer.settingslist","●●●●〈サーバー設定〉●●●●"},
		{ "l1j.server.server.GameServer.exp","「経験値」"},
		{ "l1j.server.server.GameServer.x","【倍】"},
		{ "l1j.server.server.GameServer.level",""},
		{ "l1j.server.server.GameServer.justice","「アライメント」"},
		{ "l1j.server.server.GameServer.karma","「カルマ」"},
		{ "l1j.server.server.GameServer.dropitems","「ドロップ率」"},
		{ "l1j.server.server.GameServer.dropadena","「取得アデナ」"},
		{ "l1j.server.server.GameServer.enchantweapon","「武器エンチャント成功率」"},
		{ "l1j.server.server.GameServer.enchantarmor","「防具エンチャント成功率」"},
		{ "l1j.server.server.GameServer.chatlevel","「全体チャット可能Lv」"},
		{ "l1j.server.server.GameServer.nonpvp1","「Non-PvP設定」: 無効（PvP可能）"},
		{ "l1j.server.server.GameServer.nonpvp2","「Non-PvP設定」: 有効（PvP不可）"},
		{ "l1j.server.server.GameServer.maxplayer","接続人数制限： 最大 "},
		{ "l1j.server.server.GameServer.player"," 人 "},
		{ "l1j.server.server.GameServer.waitingforuser","クライアント接続待機中..."},
		{ "l1j.server.server.GameServer.from","接続試行中IP "},
		{ "l1j.server.server.GameServer.attempt",""},
		{ "l1j.server.server.GameServer.setporton","サーバーセッティング: サーバーソケット生成 "},
		{ "l1j.server.server.GameServer.initialfinished","ローディング完了"}};

	@Override
	protected Object[][] getContents() {
		return contents;
	}

}
