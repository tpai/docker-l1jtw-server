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
package l1j.server.server;

public class Opcodes {

	public Opcodes() {
	}
	
	/**
	 * @3.80C Taiwan Server 2013.08.21
	 */
	
	/** 3.80C Client Packet */
	public static final int C_OPCODE_TRADE = 2; // 請求交易
	public static final int C_OPCODE_BOOKMARKDELETE = 3; // 請求刪除記憶座標
	public static final int C_OPCODE_BUDDYLIST = 4; // 請求查詢好友名單
	public static final int C_OPCODE_FIGHT = 5; // 請求決鬥
	public static final int C_OPCODE_USESKILL = 6; // 請求使用技能
	public static final int C_OPCODE_CHANGECHAR = 7; // 請求切換角色
	public static final int C_OPCODE_BOARD = 10; // 請求瀏覽公佈欄
	public static final int C_OPCODE_AMOUNT = 11; // 請求傳回選取的數量
	public static final int C_OPCODE_WAREHOUSELOCK = 13; // 請求變更倉庫密碼 && 送出倉庫密碼
	public static final int C_OPCODE_CLIENTVERSION = 14; // 請求驗證客戶端版本
	// 16  公告 確認
	
	public static final int C_OPCODE_EMBLEMUPLOAD = 18; // 請求上傳盟徽
	public static final int C_OPCODE_TAXRATE = 19; // 請求配置稅收
	public static final int C_OPCODE_SELECTLIST = 20; // 請求修理道具
	public static final int C_OPCODE_DROPITEM = 25; // 請求丟棄物品
	public static final int C_OPCODE_LOGINTOSERVEROK = 26; // 請求配置角色設定
	public static final int C_OPCODE_MOVECHAR = 29; // 請求移動角色
	
	public static final int C_OPCODE_LEAVEPARTY = 33; // 請求退出隊伍
	public static final int C_OPCODE_NPCTALK = 34; // 請求對話視窗
	public static final int C_OPCODE_TRADEADDITEM = 37; // 請求交易(添加物品)
	public static final int C_OPCODE_SHOP = 38; // 請求開設個人商店
	public static final int C_OPCODE_SKILLBUY = 39; // 請求查詢可以學習的魔法清單
	public static final int C_OPCODE_CHATGLOBAL = 40; // 請求使用廣播聊天頻道
	public static final int C_OPCODE_DOOR = 41; // 請求開門或關門
	public static final int C_OPCODE_PARTYLIST = 43; // 請求查詢隊伍成員
	public static final int C_OPCODE_DRAWAL = 44; // 請求領取城堡寶庫資金
	public static final int C_OPCODE_GIVEITEM = 45; // 請求給予物品
	public static final int C_OPCODE_PRIVATESHOPLIST = 47; // 請求購買指定的個人商店商品
	
	public static final int C_OPCODE_PROPOSE = 50; // 請求結婚
	public static final int C_OPCODE_CHECKPK = 51; // 請求查詢PK次數
	public static final int C_OPCODE_TELEPORT = 52; // 請求解除傳送鎖定
	public static final int C_OPCODE_DEPOSIT = 56; // 請求將資金存入城堡寶庫
	public static final int C_OPCODE_LEAVECLANE = 61; // 請求離開血盟
	public static final int C_OPCODE_FISHCLICK = 62; // 請求釣魚收竿
	public static final int C_OPCODE_RESTARTMENU = 63; // 請求開起重新開始選單
	public static final int C_OPCODE_RANK = 1033; // 請求給予角色血盟階級
	
	public static final int C_OPCODE_PLEDGE = 68; // 請求查詢血盟成員
	public static final int C_OPCODE_BANCLAN = 69; // 請求驅逐血盟成員
	public static final int C_OPCODE_TRADEADDOK = 71; // 請求完成交易
	public static final int C_OPCODE_EMBLEMDOWNLOAD = 72; // 請求下載盟徽
	public static final int C_OPCODE_PLEDGE_RECOMMENDATION = 76; // 請求打開推薦血盟
	
	public static final int C_OPCODE_PLEDGECONTENT = 78; // 請求寫入血盟查詢名單內容
	
	public static final int C_OPCODE_NEWCHAR = 84; // 請求創造角色
	public static final int C_OPCODE_TRADEADDCANCEL = 86; // 請求取消交易
	public static final int C_OPCODE_MAIL = 87; // 請求閱讀信件
	public static final int C_OPCODE_TITLE = 90; // 請求賦予封號
	public static final int C_OPCODE_KEEPALIVE = 95; // 請求更新連線狀態
	public static final int C_OPCODE_CHARRESET = 98; // 要求重置人物點數
	public static final int C_OPCODE_PETMENU = 103; // 請求寵物回報選單
	public static final int C_OPCODE_PICKUPITEM = 112; // 請求拾取物品
	public static final int C_OPCODE_BOARDREAD = 114; // 請求閱讀佈告單個欄訊息
	
	public static final int C_OPCODE_FIX_WEAPON_LIST = 118; // 請求查詢損壞的道具
	public static final int C_OPCODE_LOGINPACKET = 119; // 請求登錄伺服器 (已不使用)
	public static final int C_OPCODE_EXTCOMMAND = 120; // 請求角色表情動作
	public static final int C_OPCODE_ATTR = 121; // 請求點選項目的結果
	public static final int C_OPCODE_QUITGAME = 122; // 請求離開遊戲
	public static final int C_OPCODE_ARROWATTACK = 123; // 請求使用遠距攻擊
	public static final int C_OPCODE_NPCACTION = 125; // 請求執行對話視窗的動作
	public static final int C_OPCODE_CASTLESECURITY = 128; // 請求管理城內治安
	public static final int C_OPCODE_CLANATTENTION = 129; // 請求使用血盟注視
	
	public static final int C_OPCODE_CHAT = 136; // 請求使用一般聊天頻道
	public static final int C_OPCODE_LOGINTOSERVER = 137; // 請求登錄角色
	public static final int C_OPCODE_DELETEINVENTORYITEM = 138; // 請求刪除物品
	public static final int C_OPCODE_BOARDWRITE = 141; // 請求撰寫新的佈告欄訊息
	public static final int C_OPCODE_BOARDDELETE = 153; // 請求刪除公佈欄內容
	
	public static final int C_OPCODE_RESULT = 161; // 請求取得列表中的項目
	public static final int C_OPCODE_DELETECHAR = 162; // 請求刪除角色
	public static final int C_OPCODE_USEITEM = 164; // 請求使用物品
	public static final int C_OPCODE_BOOKMARK = 165; // 請求增加記憶座標
	public static final int C_OPCODE_EXCLUDE = 171; // 請求使用拒絕名單(開啟指定人物訊息)
	public static final int C_OPCODE_EXIT_GHOST = 173; // 請求退出觀看模式
	public static final int C_OPCODE_RESTART = 177; // 請求死亡後重新開始
	public static final int C_OPCODE_CHATWHISPER = 184; // 請求使用密語聊天頻道
	public static final int C_OPCODE_CALL = 185; // 請求傳送至指定的外掛使用者身旁
	public static final int C_OPCODE_JOINCLAN = 194; // 請求加入血盟
	public static final int C_OPCODE_CAHTPARTY = 199; // 請求聊天隊伍
	public static final int C_OPCODE_DELBUDDY = 202; // 請求刪除好友
	public static final int C_OPCODE_WHO = 206; // 請求查詢遊戲人數
	public static final int C_OPCODE_ADDBUDDY = 207; // 請求新增好友
	public static final int C_OPCODE_BEANFUNLOGINPACKET = 210; // 請求登錄伺服器【beanfun】
	public static final int C_OPCODE_ENTERPORTAL = 219; // 請求傳送 (進入地監)
	
	
	public static final int C_OPCODE_CREATECLAN = 222; // 請求創立血盟
	public static final int C_OPCODE_SELECTTARGET = 223; // 請求攻擊指定物件(寵物&召喚)
	public static final int C_OPCODE_CHANGEHEADING = 225; // 請求改變角色面向
	public static final int C_OPCODE_WAR = 227; // 請求宣戰
	public static final int C_OPCODE_ATTACK = 229; // 請求攻擊對象
	public static final int C_OPCODE_CREATEPARTY = 230; // 請求邀請加入隊伍或建立隊伍
	public static final int C_OPCODE_SHIP = 231; // 請求下船
	public static final int C_OPCODE_CHARACTERCONFIG = 244; // 請求紀錄快速鍵
	public static final int C_OPCODE_SMS = 253; // 請求傳送簡訊
	public static final int C_OPCODE_SENDLOCATION = 254; // 請求傳送位置
	public static final int C_OPCODE_BANPARTY = 255; // 請求驅逐隊伍
	
	/** 3.80C Server Packet */
	public static final int S_OPCODE_PLEDGE_RECOMMENDATION = 0; // 推薦血盟資訊更新
	
	public static final int S_OPCODE_DEPOSIT = 4; // 存入資金城堡寶庫
	public static final int S_OPCODE_INVLIST = 5; // 載入角色背包資料
	public static final int S_OPCODE_DETELECHAROK = 6; // 角色移除 (立即或非立即)
	public static final int S_OPCODE_OWNCHARSTATUS = 8; // 角色屬性與能力值
	public static final int S_OPCODE_MOVEOBJECT = 10; // 移動物件
	public static final int S_OPCODE_TRUETARGET = 11; // 法術效果-精準目標
	public static final int S_OPCODE_ADDITEM = 15; // 物品增加封包
	
	public static final int S_OPCODE_SOUND = 22; // 撥放音效
	public static final int S_OPCODE_SKILLBUY = 23; // 魔法購買 (金幣)
	public static final int S_OPCODE_ITEMSTATUS = 24; // 物品狀態更新
	public static final int S_OPCODE_ITEMAMOUNT = 24; // 物品可用次數
	public static final int S_OPCODE_ATTACKPACKET = 30; // 物件攻擊
	public static final int S_OPCODE_MPUPDATE = 33; // 魔力與最大魔力更新
	public static final int S_OPCODE_LAWFUL = 34; // 正義值更新
	public static final int S_OPCODE_TRADEADDITEM = 35; // 增加交易物品封包
	public static final int S_OPCODE_ABILITY = 36; // 配置封包 (傳送戒)
	public static final int S_OPCODE_SPMR = 37; // 魔法攻擊力與魔法防禦力
	
	public static final int S_OPCODE_SHOWHTML = 39; // 產生對話視窗
	public static final int S_OPCODE_LIGHT = 40; // 物件亮度 
	public static final int S_OPCODE_SKILLBUY_2 = 41; // 學習魔法 (何侖)
	public static final int S_OPCODE_RANGESKILLS = 42; // 範圍魔法
	
	public static final int S_OPCODE_CHANGENAME = 46; // 改變物件名稱
	public static final int S_OPCODE_CURSEBLIND = 47; // 法術效果-暗盲咒術
	public static final int S_OPCODE_COMMONNEWS = 48; // 公告視窗
	public static final int S_OPCODE_TRADE = 52; // 交易封包
	public static final int S_OPCODE_SELECTTARGET = 54; // 選擇一個目標
	public static final int S_OPCODE_SKILLSOUNDGFX = 55; // 產生動畫 [自身]
	public static final int S_OPCODE_TELEPORT = 566; // 傳送術或瞬間移動卷軸-傳送鎖定 TODO
	public static final int S_OPCODE_DELETEINVENTORYITEM = 57; // 刪除物品
	// 58  帳號??已經登入 來自(紅色訊息)
	public static final int S_OPCODE_PINKNAME = 60; // 角色名稱變紫色
	public static final int S_OPCODE_CHARSYNACK = 64; // 角色清單確認
	public static final int S_OPCODE_CHARRESET = 64; // 角色重置 
	public static final int S_OPCODE_SHOWSHOPSELLLIST = 65; // 商店收購清單
	public static final int S_OPCODE_SKILLBRAVE = 67; // 魔法或物品效果圖示-勇氣藥水類
	public static final int S_OPCODE_BOARD = 68; // 佈告欄 (對話視窗)
	public static final int S_OPCODE_CASTLEMASTER = 69; // 角色皇冠
	public static final int S_OPCODE_SHOWSHOPBUYLIST = 70; // 商店販售清單
	public static final int S_OPCODE_SERVERMSG = 71; // 系統訊息
	public static final int S_OPCODE_CLANNAME = 72; // 血盟名稱
	public static final int S_OPCODE_WHISPERCHAT = 73; // 密語聊天頻道
	public static final int S_OPCODE_POLY = 76; // 改變外型
	public static final int S_OPCODE_NORMALCHAT = 81; // 一般聊天或大喊聊天頻道
	
	public static final int S_OPCODE_SELECTLIST = 83; // 損壞武器名單
	public static final int S_OPCODE_WAR = 84; // 血盟戰爭
	public static final int S_OPCODE_RESURRECTION = 85; // 將死亡的對象復活
	public static final int S_OPCODE_CHARPACK = 87; // 物件封包
	public static final int S_OPCODE_DROPITEM = 87; // 物件封包 (道具)
	public static final int S_OPCODE_BOOKMARKS = 92; // 插入記憶座標
	public static final int S_OPCODE_CHARLIST = 93; // 角色資訊
	
	public static final int S_OPCODE_NEWCHARWRONG = 98; // 角色創造例外
	public static final int S_OPCODE_ITEMNAME = 100; // 物品名稱
	public static final int S_OPCODE_LIQUOR = 103; // 海浪波浪
	public static final int S_OPCODE_REDMESSAGE = 105; // 畫面正中央紅色訊息
	public static final int S_OPCODE_EFFECTLOCATION = 106; // 產生動畫 [座標]
	public static final int S_OPCODE_TRADESTATUS = 112; // 交易是否成功
	
	public static final int S_OPCODE_WEATHER = 115; // 遊戲天氣
	
	public static final int S_OPCODE_PUTSOLDIER = 117; // 配置已僱用傭兵
	public static final int S_OPCODE_EMBLEM = 118; // 下載血盟徽章
	public static final int S_OPCODE_CHARVISUALUPDATE = 119; // 切換物件外觀動作
	public static final int S_OPCODE_REMOVE_OBJECT = 120; // 物件刪除
	public static final int S_OPCODE_CHANGEHEADING = 122; // 物件面向
	public static final int S_OPCODE_GAMETIME = 123; // 更新目前遊戲時間
	public static final int S_OPCODE_BLESSOFEVA = 126; // 效果圖示 (水底呼吸)
	public static final int S_OPCODE_NEWCHARPACK = 127; // 角色創造成功
	
	public static final int S_OPCODE_INPUTAMOUNT = 136; // 輸入數量要產生的數量
	public static final int S_OPCODE_SERVERVERSION = 139; // 伺服器版本
	public static final int S_OPCODE_PRIVATESHOPLIST = 140; // 個人商店販賣或購買
	public static final int S_OPCODE_DRAWAL = 141; // 領取城堡寶庫資金
	public static final int S_OPCODE_BOARDREAD = 148; // 佈告欄(訊息閱讀)
	
	public static final int S_OPCODE_INITPACKET = 150; // 初始化演算法
	public static final int S_OPCODE_OWNCHARSTATUS2 = 155; // 角色能力值
	public static final int S_OPCODE_HOUSELIST = 156; // 血盟小屋名單
	public static final int S_OPCODE_DOACTIONGFX = 158; // 執行物件外觀動作
	public static final int S_OPCODE_DELSKILL = 160; // 移除指定的魔法
	public static final int S_OPCODE_NPCSHOUT = 161; // 一般聊天或大喊聊天頻道
	public static final int S_OPCODE_ADDSKILL = 164; // 增加魔法進魔法名單
	public static final int S_OPCODE_POISON = 165; // 魔法效果:中毒
	public static final int S_OPCODE_STRUP = 166; // 力量提升封包
	public static final int S_OPCODE_INVIS = 171; // 物件隱形或現形
	
	public static final int S_OPCODE_OWNCHARATTRDEF = 174; // 角色屬性值
	public static final int S_OPCODE_SHOWRETRIEVELIST = 176; // 倉庫物品名單
	public static final int S_OPCODE_CHARAMOUNT = 178; // 角色數量
	public static final int S_OPCODE_CHARTITLE = 183; // 角色封號
	public static final int S_OPCODE_TAXRATE = 185; // 設定稅收封包
	
	public static final int S_OPCODE_MAIL = 186; // 郵件封包
	public static final int S_OPCODE_HOUSEMAP = 187; // 血盟小屋地圖 [地點]
	public static final int S_OPCODE_DEXUP = 188; // 敏捷提升封包
	
	public static final int S_OPCODE_CLANATTENTION = 200; // 血盟注視
	public static final int S_OPCODE_PARALYSIS = 202; // 魔法效果 : 麻痺類
	public static final int S_OPCODE_MAPID = 206; // 更新現在的地圖
	public static final int S_OPCODE_UNDERWATER = 206; // 更新現在的地圖 （水中）
	public static final int S_OPCODE_ATTRIBUTE = 209; // 物件屬性
	public static final int S_OPCODE_SKILLICONSHIELD = 216; // 魔法效果 : 防禦纇
	public static final int S_OPCODE_YES_NO = 219; // 確認視窗
	public static final int S_OPCODE_LOGINTOGAME = 223; // 進入遊戲
	public static final int S_OPCODE_HPUPDATE = 225; // 體力與最大體力更新
	public static final int S_OPCODE_DISCONNECT = 227; // 立即中斷連線
	public static final int S_OPCODE_LOGINRESULT = 233; // 登入結果
	public static final int S_OPCODE_HPMETER = 237; // 物件血條
	public static final int S_OPCODE_ITEMCOLOR = 240; // 物品屬性狀態
	public static final int S_OPCODE_GLOBALCHAT = 243; // 廣播聊天頻道
	public static final int S_OPCODE_IDENTIFYDESC = 245; // 物品資訊訊息
	public static final int S_OPCODE_PACKETBOX = 250; // 多功能封包
	public static final int S_OPCODE_ACTIVESPELLS = 250; // 多功能封包
	public static final int S_OPCODE_SKILLICONGFX = 250; // 多功能封包
	public static final int S_OPCODE_UNKNOWN2 = 250; // 多功能封包
	
	public static final int S_OPCODE_SKILLHASTE = 255; // 魔法或物品產生的加速效果

	
	/**
	 * @3.5C Taiwan Server <b>2011.08.09 Lin.bin
	 */

	/** 3.5C Client Packet */
	//public static final int C_OPCODE_BANPARTY = 0; // 請求驅逐隊伍
	//public static final int C_OPCODE_SHIP = 1; // 請求下船
	public static final int C_OPCODE_TELEPORTLOCK = 2; // 玩家傳送鎖定(回溯檢測用)
	public static final int C_OPCODE_SKILLBUYOK = 44444; // 請求學習魔法
	//public static final int C_OPCODE_ADDBUDDY = 5; // 請求新增好友
	//public static final int C_OPCODE_WAREHOUSELOCK = 8; // 請求變更倉庫密碼 && 送出倉庫密碼
	//public static final int C_OPCODE_DROPITEM = 9; // 請求丟棄物品
	//public static final int C_OPCODE_BOARDNEXT = 11; // 請求查看下一頁佈告欄的訊息
	//public static final int C_OPCODE_PETMENU = 12; // 請求寵物回報選單
	//public static final int C_OPCODE_JOINCLAN = 13; // 請求加入血盟
	//public static final int C_OPCODE_GIVEITEM = 14; // 請求給予物品
	//public static final int C_OPCODE_USESKILL = 16; // 請求使用技能
	//public static final int C_OPCODE_RESULT = 17; // 請求取得列表中的項目
	//public static final int C_OPCODE_DELETECHAR = 19; // 請求刪除角色
	//public static final int C_OPCODE_BOARD = 1111; // 請求瀏覽公佈欄
	//public static final int C_OPCODE_TRADEADDCANCEL = 23; // 請求取消交易
	//public static final int C_OPCODE_USEITEM = 24; // 請求使用物品
	//public static final int C_OPCODE_PROPOSE = 25; // 請求結婚
	//public static final int C_OPCODE_BOARDDELETE = 26; // 請求刪除公佈欄內容
	//public static final int C_OPCODE_CHANGEHEADING = 27; // 請求改變角色面向
	//public static final int C_OPCODE_BOOKMARKDELETE = 28; // 請求刪除記憶座標
	//public static final int C_OPCODE_SELECTLIST = 29; // 請求修理道具
	//public static final int C_OPCODE_SELECTTARGET = 32; // 請求攻擊指定物件(寵物&召喚)
	public static final int C_OPCODE_DELEXCLUDE = 33; // 請求使用開啟名單(拒絕指定人物訊息)
	//public static final int C_OPCODE_BUDDYLIST = 34; // 請求查詢好友名單
	//public static final int C_OPCODE_SENDLOCATION = 35; // 請求傳送位置
	//public static final int C_OPCODE_TITLE = 37; // 請求賦予封號
	//public static final int C_OPCODE_TRADEADDOK = 38; // 請求完成交易
	//public static final int C_OPCODE_EMBLEM = 39; // 請求上傳盟徽
	//public static final int C_OPCODE_MOVECHAR = 40; // 請求移動角色
	//public static final int C_OPCODE_CHECKPK = 41; // 請求查詢PK次數
	// public static final int C_OPCODE_COMMONCLICK = 42; // 請求下一步 (伺服器公告)
	//public static final int C_OPCODE_QUITGAME = 43; // 請求離開遊戲
	//public static final int C_OPCODE_DEPOSIT = 56; // 請求將資金存入城堡寶庫
	//public static final int C_OPCODE_BEANFUN_LOGIN = 45; // 請求使用樂豆自動登錄伺服器 (未實裝)
	//public static final int C_OPCODE_BOOKMARK = 46; // 請求增加記憶座標
	//public static final int C_OPCODE_SHOP = 47; // 請求開設個人商店
	//public static final int C_OPCODE_CHATWHISPER = 48; // 請求使用密語聊天頻道
	//public static final int C_OPCODE_PRIVATESHOPLIST = 49; // 請求購買指定的個人商店商品
	//public static final int C_OPCODE_EXTCOMMAND = 52; // 請求角色表情動作
	//public static final int C_OPCODE_CLIENTVERSION = 54; // 請求驗證客戶端版本
	//public static final int C_OPCODE_LOGINTOSERVER = 55; // 請求登錄角色
	//public static final int C_OPCODE_ATTR = 56; // 請求點選項目的結果
	//public static final int C_OPCODE_NPCTALK = 57; // 請求對話視窗
	//public static final int C_OPCODE_NEWCHAR = 58; // 請求創造角色
	//public static final int C_OPCODE_TRADE = 59; // 請求交易
	//public static final int C_OPCODE_DELBUDDY = 61; // 請求刪除好友
	//public static final int C_OPCODE_BANCLAN = 62; // 請求驅逐血盟成員
	//public static final int C_OPCODE_FISHCLICK = 63; // 請求釣魚收竿
	//public static final int C_OPCODE_LEAVECLANE = 65; // 請求離開血盟
	//public static final int C_OPCODE_TAXRATE = 125; // 請求配置稅收
	// public static final int C_OPCODE_RESTART = 70; // 請求重新開始
	//public static final int C_OPCODE_ENTERPORTAL = 71; // 請求傳送 (進入地監)
	//public static final int C_OPCODE_SKILLBUY = 72; // 請求查詢可以學習的魔法清單
	//public static final int C_OPCODE_TELEPORT = 73; // 請求解除傳送鎖定
	//public static final int C_OPCODE_DELETEINVENTORYITEM = 74; // 請求刪除物品
	//public static final int C_OPCODE_CHAT = 75; // 請求使用一般聊天頻道
	//public static final int C_OPCODE_ARROWATTACK = 77; // 請求使用遠距攻擊
	public static final int C_OPCODE_USEPETITEM = 78555; // 請求使用寵物裝備
	//public static final int C_OPCODE_EXCLUDE = 79; // 請求使用拒絕名單(開啟指定人物訊息)
	//public static final int C_OPCODE_FIX_WEAPON_LIST = 80; // 請求查詢損壞的道具
	//public static final int C_OPCODE_PLEDGE = 84; // 請求查詢血盟成員
	//public static final int C_OPCODE_NPCACTION = 87; // 請求執行對話視窗的動作
	//public static final int C_OPCODE_EXIT_GHOST = 9066; // 請求退出觀看模式
	//public static final int C_OPCODE_CALL = 91; // 請求傳送至指定的外掛使用者身旁
	//public static final int C_OPCODE_MAIL = 92; // 請求打開郵箱
	//public static final int C_OPCODE_WHO = 93; // 請求查詢遊戲人數
	//public static final int C_OPCODE_PICKUPITEM = 94; // 請求拾取物品
	//public static final int C_OPCODE_CHARRESET = 955; // 要求重置人物點數
	//public static final int C_OPCODE_AMOUNT = 96; // 請求傳回選取的數量
	//public static final int C_OPCODE_RANK = 1033; // 請求給予角色血盟階級
	//public static final int C_OPCODE_FIGHT = 104; // 請求決鬥
	//public static final int C_OPCODE_DRAWAL = 105; // 請求領取城堡寶庫資金
	//public static final int C_OPCODE_KEEPALIVE = 106; // 請求更新連線狀態
	//public static final int C_OPCODE_CHARACTERCONFIG = 108; // 請求紀錄快速鍵
	//public static final int C_OPCODE_CHATGLOBAL = 109; // 請求使用廣播聊天頻道
	//public static final int C_OPCODE_WAR = 110; // 請求宣戰
	//public static final int C_OPCODE_CREATECLAN = 1122; // 請求創立血盟
	//public static final int C_OPCODE_LOGINTOSERVEROK = 114; // 請求配置角色設定
	//public static final int C_OPCODE_LOGINPACKET = 115; // 請求登錄伺服器
	//public static final int C_OPCODE_DOOR = 116; // 請求開門或關門
	//public static final int C_OPCODE_ATTACK = 117; // 請求攻擊對象
	//public static final int C_OPCODE_TRADEADDITEM = 119; // 請求交易(添加物品)
	//public static final int C_OPCODE_SMS = 121; // 請求傳送簡訊
	//public static final int C_OPCODE_LEAVEPARTY = 123; // 請求退出隊伍
	//public static final int C_OPCODE_CASTLESECURITY = 128; // 請求管理城內治安
	//public static final int C_OPCODE_BOARDREAD = 125; // 請求閱讀佈告單個欄訊息
	//public static final int C_OPCODE_CHANGECHAR = 126; // 請求切換角色
	//public static final int C_OPCODE_PARTYLIST = 127; // 請求查詢隊伍成員
	//public static final int C_OPCODE_BOARDWRITE = 129; // 請求撰寫新的佈告欄訊息
	//public static final int C_OPCODE_CREATEPARTY = 130; // 請求邀請加入隊伍或建立隊伍
	//public static final int C_OPCODE_CAHTPARTY = 131; // 請求聊天隊伍

	/** 3.5C Server Packet */
	//public static final int S_OPCODE_PUTSOLDIER = 3333; // 配置已的僱用傭兵
	//public static final int S_OPCODE_SKILLBUY_2 = 14444; // 學習魔法 (何侖)
	//public static final int S_OPCODE_SHOWSHOPSELLLIST = 2; // 商店收購清單
	// public static final int S_OPCODE_PINGTIME = 3; // Ping Time
	//public static final int S_OPCODE_DETELECHAROK = 4; // 角色移除 (立即或非立即)
	//public static final int S_OPCODE_CHANGEHEADING = 5; // 物件面向
	//public static final int S_OPCODE_SKILLICONSHIELD = 6; // 魔法效果 : 防禦纇
	//public static final int S_OPCODE_RANGESKILLS = 7; // 範圍魔法
	//public static final int S_OPCODE_INPUTAMOUNT = 8; // 輸入數量要產生的數量
	//public static final int S_OPCODE_DELSKILL = 9; // 移除指定的魔法
	public static final int S_OPCODE_PUTHIRESOLDIER = 2210; // 配置傭兵
	//public static final int S_OPCODE_SKILLHASTE = 11; // 魔法或物品產生的加速效果
	//public static final int S_OPCODE_CHARAMOUNT = 12; // 角色列表
	//public static final int S_OPCODE_BOOKMARKS = 13; // 插入記憶座標
	public static final int S_OPCODE_EXCEPTION_3 = 14; // 例外事件_3
	//public static final int S_OPCODE_MPUPDATE = 15; // 魔力與最大魔力更新
	public static final int S_OPCODE_EXCEPTION_2 = 16; // 例外事件_2
	//public static final int S_OPCODE_SERVERVERSION = 43; // 伺服器版本
	//public static final int S_OPCODE_CHARVISUALUPDATE = 18; // 切換物件外觀動作
	//public static final int S_OPCODE_PARALYSIS = 19; // 魔法效果 : 麻痺類
	public static final int S_OPCODE_MOVELOCK = 20; // 移動鎖定封包(疑似開加速器則會用這個封包將玩家鎖定)
	//public static final int S_OPCODE_DELETEINVENTORYITEM = 21; // 刪除物品
	public static final int S_OPCODE_NEW1 = 22; // 不明封包 (會變更頭銜)
	// 23 彷彿是伺服器選單
	public static final int S_OPCODE_HIRESOLDIER = 22224; // 僱用傭兵
	//public static final int S_OPCODE_PINKNAME = 25; // 角色名稱變紫色
	//public static final int S_OPCODE_TELEPORT = 26; // 傳送術或瞬間移動卷軸-傳送鎖定
	//public static final int S_OPCODE_INITPACKET = 27; // 初始化演算法
	//public static final int S_OPCODE_CHANGENAME = 28; // 改變物件名稱
	//public static final int S_OPCODE_NEWCHARWRONG = 29; // 角色創造例外
	//public static final int S_OPCODE_DRAWAL = 4; // 領取城堡寶庫資金
	//public static final int S_OPCODE_MAPID = 32; // 更新現在的地圖
	//public static final int S_OPCODE_UNDERWATER = 32; // 更新現在的地圖 （水中）
	//public static final int S_OPCODE_OWNCHARSTATUS = 34; // 角色屬性與能力值
	public static final int S_OPCODE_EXCEPTION_1 = 35; // 例外事件_1
	//public static final int S_OPCODE_COMMONNEWS = 36; // 公告視窗
	//public static final int S_OPCODE_TRUETARGET = 37; // 法術效果-精準目標
	//public static final int S_OPCODE_HPUPDATE = 38; // 體力與最大體力更新
	//public static final int S_OPCODE_TRADESTATUS = 39; // 交易是否成功
	//public static final int S_OPCODE_SHOWSHOPBUYLIST = 40; // 商店販售清單
	//public static final int S_OPCODE_LOGINTOGAME = 41; // 進入遊戲
	//public static final int S_OPCODE_INVIS = 42; // 物件隱形或現形
	//public static final int S_OPCODE_CHARRESET = 43; // 角色重置
	//public static final int S_OPCODE_PETCTRL = 43; // 寵物控制介面
	//public static final int S_OPCODE_IDENTIFYDESC = 45; // 物品資訊訊息
	//public static final int S_OPCODE_POISON = 47; // 魔法效果:中毒
	//public static final int S_OPCODE_GAMETIME = 48; // 更新目前遊戲時間
	//public static final int S_OPCODE_SKILLBUY = 50; // 魔法購買 (金幣)
	//public static final int S_OPCODE_TRADE = 51; // 交易封包
	//public static final int S_OPCODE_WAR = 444452; // 血盟戰爭
	//public static final int S_OPCODE_NPCSHOUT = 53; // 一般聊天或大喊聊天頻道
	public static final int S_OPCODE_COMMONNEWS2 = 444454; // 系統訊息視窗
	//public static final int S_OPCODE_CHARPACK = 55; // 物件封包
	//public static final int S_OPCODE_DROPITEM = 55; // 物件封包 (道具)
	//public static final int S_OPCODE_NORMALCHAT = 56; // 一般聊天或大喊聊天頻道
	//public static final int S_OPCODE_MAIL = 57; // 郵件封包
	//public static final int S_OPCODE_STRUP = 58; // 力量提升封包
	//public static final int S_OPCODE_CURSEBLIND = 59; // 法術效果-暗盲咒術
	//public static final int S_OPCODE_ITEMCOLOR = 44460; // 物品屬性狀態
	public static final int S_OPCODE_USECOUNT = 44461; // 魔杖的使用次數
	//public static final int S_OPCODE_MOVEOBJECT = 62; // 移動物件
	//public static final int S_OPCODE_BOARD = 63; // 佈告欄 (對話視窗)
	//public static final int S_OPCODE_ADDITEM = 64; // 物品增加封包
	//public static final int S_OPCODE_SHOWRETRIEVELIST = 65; // 倉庫物品名單
	//public static final int S_OPCODE_YES_NO = 68; // 確認視窗
	//public static final int S_OPCODE_INVLIST = 69; // 插入批次道具
	//public static final int S_OPCODE_OWNCHARSTATUS2 = 2270; // 角色能力值
	public static final int S_OPCODE_NEW3 = 222271; // 不明封包 (商店)
	//public static final int S_OPCODE_HPMETER = 222272; // 物件血條
	//public static final int S_OPCODE_PRIVATESHOPLIST = 75; // 個人商店販賣或購買
	//public static final int S_OPCODE_GLOBALCHAT = 76; // 廣播聊天頻道
	//public static final int S_OPCODE_ADDSKILL = 77; // 增加魔法進魔法名單
	//public static final int S_OPCODE_SKILLBRAVE = 78; // 魔法或物品效果圖示-勇氣藥水類
	//public static final int S_OPCODE_WEATHER = 79; // 遊戲天氣
	//public static final int S_OPCODE_CHARLIST = 80; // 角色資訊
	//public static final int S_OPCODE_OWNCHARATTRDEF = 81; // 角色屬性值
	//public static final int S_OPCODE_EFFECTLOCATION = 82; // 產生動畫 [座標]
	//public static final int S_OPCODE_SPMR = 83; // 魔法攻擊力與魔法防禦力
	//public static final int S_OPCODE_SELECTTARGET = 84; // 選擇一個目標
	//public static final int S_OPCODE_BOARDREAD = 85; // 佈告欄(訊息閱讀)
	//public static final int S_OPCODE_SKILLSOUNDGFX = 86; // 產生動畫 [自身]
	//public static final int S_OPCODE_DISCONNECT = 88; // 立即中斷連線
	public static final int S_OPCODE_SPECIALATTACK = 89; // 特殊攻擊
	public static final int S_OPCODE_SPOLY = 908; // 特別變身封包
	//public static final int S_OPCODE_SHOWHTML = 91; // 產生對話視窗
	//public static final int S_OPCODE_ABILITY = 92; // 配置封包 TODO 傳送戒
	//public static final int S_OPCODE_DEPOSIT = 938; // 存入資金城堡寶庫
	//public static final int S_OPCODE_ATTACKPACKET = 94; // 物件攻擊
	//public static final int S_OPCODE_ITEMSTATUS = 95; // 物品狀態更新
	//public static final int S_OPCODE_ITEMAMOUNT = 95; // 物品可用次數
	public static final int S_OPCODE_NEW2 = 97; // 不明封包 (會將頭銜變更為空白)
	//public static final int S_OPCODE_NEWCHARPACK = 98; // 角色創造成功
	//public static final int S_OPCODE_PACKETBOX = 100; // 多功能封包
	//public static final int S_OPCODE_ACTIVESPELLS = 100; // 多功能封包
	//public static final int S_OPCODE_SKILLICONGFX = 100; // 多功能封包
	//public static final int S_OPCODE_UNKNOWN2 = 100; // 多功能封包
	//public static final int S_OPCODE_DEXUP = 101; // 敏捷提升封包
	//public static final int S_OPCODE_LIGHT = 102; // 物件亮度
	//public static final int S_OPCODE_POLY = 103; // 改變外型
	//public static final int S_OPCODE_SOUND = 104; // 撥放音效
	//public static final int S_OPCODE_BLESSOFEVA = 106; // 效果圖示 (水底呼吸)
	//public static final int S_OPCODE_CHARTITLE = 108; // 角色封號
	//public static final int S_OPCODE_TAXRATE = 1095; // 設定稅收封包
	//public static final int S_OPCODE_ITEMNAME = 110; // 物品名稱
	public static final int S_OPCODE_MATERIAL = 1115; // 魔法學習-材料不足
	//public static final int S_OPCODE_WHISPERCHAT = 113; // 密語聊天頻道
	//public static final int S_OPCODE_REDMESSAGE = 1145; // 畫面正中出現紅色
	//public static final int S_OPCODE_ATTRIBUTE = 115; // 物件屬性
	//public static final int S_OPCODE_LAWFUL = 117; // 正義值更新
	// public static final int S_OPCODE_LOGINRESULT = 118; // 登入狀態
	//public static final int S_OPCODE_CASTLEMASTER = 119; // 角色皇冠
	//public static final int S_OPCODE_SERVERMSG = 120; // 系統訊息
	//public static final int S_OPCODE_HOUSEMAP = 121; // 血盟小屋地圖 [地點]
	//public static final int S_OPCODE_RESURRECTION = 122; // 將死亡的對象復活
	//public static final int S_OPCODE_DOACTIONGFX = 123; // 執行物件外觀動作
	//public static final int S_OPCODE_REMOVE_OBJECT = 124; // 物件刪除
	//public static final int S_OPCODE_EMBLEM = 125; // 下載血盟徽章
	//public static final int S_OPCODE_LIQUOR = 1264; // 海浪波浪 
	//public static final int S_OPCODE_HOUSELIST = 127; // 血盟小屋名單

	/** 3.3C Client Packet (3.5C 未抓取) id非正確 */
	public static final int C_OPCODE_HIRESOLDIER = 1411;//要求僱傭傭兵列表(購買)
	public static final int C_OPCODE_CHANGEWARTIME = 1443;//修正城堡總管全部功能
	public static final int C_OPCODE_PUTSOLDIER = 1453;//要求配置已僱用士兵
	public static final int C_OPCODE_SELECTWARTIME = 1463;//要求選擇 變更攻城時間(but3.3C無使用)
	public static final int C_OPCODE_PUTBOWSOLDIER = 1473;//要求配置城牆上弓手
    
	
	/** 已不使用*/
	public static final int S_OPCODE_EXP = 1000001; // 經驗值更新
	public static final int S_OPCODE_FIX_WEAPON_MENU = 1000002; // 修理武器清單
	public static final int S_OPCODE_WARTIME = 1000003; // 設定圍成時間 (已不使用)
	public static final int S_OPCODE_RESTART = 1000004; // 強制重新選擇角色
	public static final int S_OPCODE_BLUEMESSAGE = 1000005; // 紅色訊息
	public static final int C_OPCODE_RETURNTOLOGIN = 1000006;//要求回到選人畫面
	public static final int S_OPCODE_SYSMSG = 1000007; // 伺服器訊息
	public static final int S_OPCODE_TELEPORTLOCK = 1000008; // 進入傳送點-傳送鎖定

}