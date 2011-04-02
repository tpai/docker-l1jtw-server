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
     * @3.3C Taiwan Server
     * <b>2010.12.22 Lin.bin
     */
	/**3.3C ClientPacket*/
	public static final int C_OPCODE_EXIT_GHOST = 0;//要求退出觀看模式
	public static final int C_OPCODE_RETURNTOLOGIN = 1;//要求回到選人畫面
	public static final int C_OPCODE_LOGINTOSERVER = 5;//要求進入遊戲
	public static final int C_OPCODE_HIRESOLDIER = 7;//要求僱傭傭兵列表(購買)
	public static final int C_OPCODE_BOOKMARKDELETE = 8;//要求刪除記憶座標
	public static final int C_OPCODE_DROPITEM = 10;//要求丟棄物品
	public static final int C_OPCODE_RESULT = 11;//要求列表物品取得 /3.3C領取寵物
	public static final int C_OPCODE_SELECTTARGET = 13;//要求攻擊指定物件(寵物&召喚)
	public static final int C_OPCODE_COMMONCLICK = 14 ;//要求下一步 ( 公告資訊 )
	public static final int C_OPCODE_SETCASTLESECURITY = 15;//要求設置治安管理
	public static final int C_OPCODE_CLAN = 16;////要求血盟數據(例如盟標)
	public static final int C_OPCODE_FIX_WEAPON_LIST = 18;//要求維修物品清單
	public static final int C_OPCODE_USESKILL = 19;//要求使用技能
	public static final int C_OPCODE_TRADEADDCANCEL = 21;//要求取消交易(個人商店)
	public static final int C_OPCODE_CHANGEPASS = 22;//要求變更賬號密碼(登陸界面)
	public static final int C_OPCODE_DEPOSIT = 24;//要求存入資金
	public static final int C_OPCODE_TRADE = 25;//要求交易(個人)
	public static final int C_OPCODE_MOVE_CHECK = 26;//人物移動回碩檢測 <= 3.3C added
	public static final int C_OPCODE_ENTERPORTAL = 27;//要求傳送 (進入地監)
	public static final int C_OPCODE_DRAWAL = 28;//要求領出資金
	public static final int C_OPCODE_SECOND_PLEDGE = 31;//第二次要求查詢血盟成員
	public static final int C_OPCODE_RANK = 31;//要求給予角色血盟階級
	public static final int C_OPCODE_TRADEADDOK = 32;//要求完成交易(個人)
	public static final int C_OPCODE_PLEDGE = 33;//要求查詢血盟成員
	public static final int C_OPCODE_QUITGAME = 35;//要求離開遊戲
	public static final int C_OPCODE_BANCLAN = 36;//要求驅逐人物離開血盟
	public static final int C_OPCODE_WAREHOUSELOCK = 37;//要求變更倉庫密碼 && 送出倉庫密碼
	public static final int C_OPCODE_TITLE = 39;//要求賦予封號
	public static final int C_OPCODE_PICKUPITEM = 41;//要求撿取物品
	public static final int C_OPCODE_CHARRESET = 42;//要求重置人物點數
	public static final int C_OPCODE_NEWCHAR = 43;//要求創造角色 
	public static final int C_OPCODE_DOOR = 44;//要求開關門
	public static final int C_OPCODE_PETMENU = 45;//要求寵物回報選單
	public static final int C_OPCODE_CLIENTVERSION = 46; //要求登入測試 (接收伺服器版本)
	public static final int C_OPCODE_CREATECLAN = 48;//要求創立血盟
	public static final int C_OPCODE_CHANGECHAR = 50;//要求切換角色
	public static final int C_OPCODE_USEITEM = 51;//要求使用物品
	public static final int C_OPCODE_SKILLBUYOK = 52;//要求學習魔法 完成
	public static final int C_OPCODE_UNKOWN1 = 53;//用戶端自動請求在線公告
	public static final int C_OPCODE_NPCTALK = 55;//要求物件對話視窗
	public static final int C_OPCODE_TELEPORT = 56;//要求傳送 更新周圍物件 (無動畫傳送後)
	public static final int C_OPCODE_SHIP = 58;//要求下船
	public static final int C_OPCODE_CHANGEWARTIME = 102;//修正城堡總管全部功能
	public static final int C_OPCODE_USEPETITEM = 60;//要求使用寵物裝備
	public static final int C_OPCODE_SKILLBUY = 63;//要求學習魔法(金幣)
	public static final int C_OPCODE_ADDBUDDY = 64;//要求新增好友
	public static final int C_OPCODE_BOARDWRITE = 65;//要求寫入公佈欄訊息
	public static final int C_OPCODE_BOARDNEXT = 66;//要求下一頁 (公佈欄)
	public static final int C_OPCODE_FISHCLICK = 67;//要求釣魚收竿
	public static final int C_OPCODE_LEAVECLANE = 69;//要求脫離血盟
	public static final int C_OPCODE_LOGINTOSERVEROK = 70;//登入伺服器OK
	public static final int C_OPCODE_BUDDYLIST = 71;//要求查詢朋友名單
	public static final int C_OPCODE_MOVECHAR = 73;//要求角色移動
	public static final int C_OPCODE_ATTR = 74;//要求點選項目的結果
	public static final int C_OPCODE_BOARDDELETE = 75;//要求刪除公佈欄內容
	public static final int C_OPCODE_DELEXCLUDE = 76;//要求使用開啟名單(拒絕指定人物訊息)
	public static final int C_OPCODE_EXCLUDE = 76;//要求使用拒絕名單(開啟指定人物訊息)
	public static final int C_OPCODE_CHATGLOBAL = 77;//要求使用廣播聊天頻道
	public static final int C_OPCODE_PROPOSE = 78;//要求結婚
	public static final int C_OPCODE_TRADEADDITEM = 79;//要求交易(添加物品)
	public static final int C_OPCODE_CASTLESECURITY = 81;//要求治安管理  OK
	public static final int C_OPCODE_SHOP = 82;//要求開個人商店
	public static final int C_OPCODE_CHAT = 83;//要求使用一般聊天頻道
	public static final int C_OPCODE_PUTSOLDIER = 84;//要求配置已僱用士兵
	public static final int C_OPCODE_LEAVEPARTY = 85;//要求脫離隊伍
	public static final int C_OPCODE_PARTYLIST = 86;//要求查看隊伍
	public static final int C_OPCODE_SENDLOCATION = 87; // 要求傳送位置(3.3C 新增)
	public static final int C_OPCODE_BOARDREAD = 88;//要求閱讀佈告單個欄訊息
	public static final int C_OPCODE_CALL = 89;//要求召喚到身邊(gm)
	public static final int C_OPCODE_WAR = 91;//要求宣戰
	public static final int C_OPCODE_CHECKPK = 92;//要求查詢PK次數
	public static final int C_OPCODE_CHANGEHEADING = 93;//要求改變角色面向
	public static final int C_OPCODE_AMOUNT = 94;//要求確定數量選取
	public static final int C_OPCODE_WHO = 95;//要求查詢遊戲人數
	public static final int C_OPCODE_FIGHT = 96;//要求決鬥
	public static final int C_OPCODE_NPCACTION = 97;//要求物件對話視窗結果
	public static final int C_OPCODE_CHARACTERCONFIG = 100;//要求紀錄快速鍵
	public static final int C_OPCODE_ATTACK = 101;//要求角色攻擊
	public static final int C_OPCODE_SELECTWARTIME = 102;//要求選擇 變更攻城時間(but3.3C無使用)
	public static final int C_OPCODE_BOARD = 103;//要求讀取公佈欄
	public static final int C_OPCODE_PRIVATESHOPLIST = 104;//要求個人商店 （物品列表）
	public static final int C_OPCODE_LOGINPACKET = 105;//要求登入伺服器
	public static final int C_OPCODE_SELECTLIST = 106;//要求物品維修
	public static final int C_OPCODE_MAIL = 107;//要求打開郵箱
	public static final int C_OPCODE_EXTCOMMAND = 108;//要求角色表情動作
	public static final int C_OPCODE_DELETECHAR = 110;//要求刪除角色
	public static final int C_OPCODE_DELBUDDY = 112;//要求刪除好友
	public static final int C_OPCODE_ARROWATTACK = 113;//要求使用遠距武器
	public static final int C_OPCODE_EMBLEM = 114;//要求上傳盟標
	public static final int C_OPCODE_BANPARTY = 115;//要求踢出隊伍
	public static final int C_OPCODE_CHATWHISPER = 116;//要求使用密語聊天頻道
	public static final int C_OPCODE_SMS = 117;//要求寄送簡訊
	public static final int C_OPCODE_PUTHIRESOLDIER = 118;//要求配置已僱傭傭兵 OK
	public static final int C_OPCODE_BOOKMARK = 119;//要求增加記憶座標
	public static final int C_OPCODE_PUTBOWSOLDIER = 120;//要求配置城牆上弓手
	public static final int C_OPCODE_KEEPALIVE = 121;//要求更新時間
	public static final int C_OPCODE_TAXRATE = 122;//要求稅收設定封包
	public static final int C_OPCODE_GIVEITEM = 124;//要求給予物品
	public static final int C_OPCODE_JOINCLAN = 125;//要求加入血盟
	public static final int C_OPCODE_DELETEINVENTORYITEM = 126;//要求刪除物品
	public static final int C_OPCODE_RESTART = 127;//要求死亡後重新開始
	public static final int C_OPCODE_CREATEPARTY = 130;//要求邀請加入隊伍(要求創立隊伍)
	public static final int C_OPCODE_CAHTPARTY = 131;//要求人物移出隊伍
	
	/**3.2C ServerPacket*/
	public static final int S_OPCODE_COMMONNEWS2 = 0;
	public static final int S_OPCODE_USEMAP = 71;
	public static final int S_LETTER = 90;
	
	/**3.3C ServerPacket*/
	public static final int S_OPCODE_BLUEMESSAGE = 0;//藍色訊息 (使用String-h.tbl)
	public static final int S_OPCODE_BLESSOFEVA = 1;//效果圖示 (水底呼吸)
	public static final int S_OPCODE_NPCSHOUT = 3;//非玩家聊天頻道 (一般 & 大喊 )NPC
	public static final int S_OPCODE_RESURRECTION = 4;//物件復活
	public static final int S_OPCODE_BOARDREAD = 5;//佈告欄(訊息閱讀)
	public static final int S_OPCODE_CASTLEMASTER = 6;//角色皇冠 3.3C
	public static final int S_OPCODE_FIX_WEAPON_MENU = 7;//修理武器清單 (3.3C新增)
	public static final int S_OPCODE_SELECTLIST = 7;//損壞武器名單
	public static final int S_OPCODE_ADDSKILL = 8;//增加魔法進魔法名單
	public static final int S_OPCODE_CHARVISUALUPDATE = 9;//物件動作種類 (長時間)
	public static final int S_OPCODE_COMMONNEWS = 10;//公告視窗
	public static final int S_OPCODE_CHARAMOUNT = 11;//角色列表
	public static final int S_OPCODE_PARALYSIS = 12;//魔法效果 : 詛咒類 {編號 }麻痺,癱瘓
	public static final int S_OPCODE_REDMESSAGE = 13; //畫面正中出現紅色/新增未使用
	public static final int S_OPCODE_INPUTAMOUNT = 14;//拍賣公告欄選取金幣數量 (選取物品數量)
	public static final int S_OPCODE_SKILLSOUNDGFX = 15;//產生動畫 [物件]
	public static final int S_OPCODE_IDENTIFYDESC = 16;//物品資訊訊息 {使用String-h.tbl}
	public static final int S_OPCODE_EFFECTLOCATION = 18;//產生動畫 [地點]
	public static final int S_OPCODE_MAIL = 19;//郵件封包
	public static final int S_OPCODE_SHOWRETRIEVELIST = 21;//倉庫物品名單
	public static final int S_OPCODE_HOUSELIST = 22;//血盟小屋名單
	public static final int S_OPCODE_SKILLBUY = 23;//魔法購買 (金幣)
	public static final int S_OPCODE_GLOBALCHAT = 24;//廣播聊天頻道
	public static final int S_OPCODE_SYSMSG = 24;//廣播聊天頻道/伺服器訊息(字串)
	public static final int S_OPCODE_CURSEBLIND = 25;//魔法效果 - 暗盲咒術 {編號}
	public static final int S_OPCODE_INVLIST = 26;//道具欄全物品
	public static final int S_OPCODE_CHARPACK = 27;//物件封包
	public static final int S_OPCODE_DROPITEM = 27;//丟棄物品封包
	public static final int S_OPCODE_SERVERMSG = 29;//伺服器訊息 (行數)/(行數, 附加字串 )
	public static final int S_OPCODE_NEWCHARPACK = 31;//創造角色封包
	public static final int S_OPCODE_DELSKILL = 34;//移除魔法出魔法名單
	public static final int S_OPCODE_LOGINTOGAME = 35;//進入遊戲
	public static final int S_OPCODE_WHISPERCHAT = 36;//要求使用密語聊天頻道
	public static final int S_OPCODE_DRAWAL = 37;//取出城堡寶庫金幣
	public static final int S_OPCODE_CHARLIST = 38;//角色資訊
	public static final int S_OPCODE_EMBLEM = 39;//角色盟徽
	public static final int S_OPCODE_ATTACKPACKET = 40;//物件攻擊
	public static final int S_OPCODE_SPMR = 42;//魔法攻擊力與魔法防禦力
	public static final int S_OPCODE_OWNCHARSTATUS = 43;//角色屬性與能力值
	public static final int S_OPCODE_RANGESKILLS = 44;//範圍魔法
	public static final int S_OPCODE_SHOWSHOPSELLLIST = 45;//NPC物品販賣
	public static final int S_OPCODE_INVIS = 47;//物件隱形 & 現形
	public static final int S_OPCODE_NORMALCHAT = 48;//一般聊天頻道
	public static final int S_OPCODE_SKILLHASTE = 49;//魔法|物品效果 {加速纇}
	public static final int S_OPCODE_TAXRATE = 50;//稅收設定封包
	public static final int S_OPCODE_WEATHER = 51;//遊戲天氣
	public static final int S_OPCODE_HIRESOLDIER = 52;//僱傭傭兵 傭兵名單
	public static final int S_OPCODE_WAR = 53;//血盟戰爭訊息 {編號,血盟名稱,目標血盟名稱}
	public static final int S_OPCODE_TELEPORTLOCK = 54;//人物回碩檢測  OR 傳送鎖定 (無動畫)
	public static final int S_OPCODE_PINKNAME = 55;//角色名稱變紫色
	public static final int S_OPCODE_ITEMSTATUS = 56;//物品狀態更新
	public static final int S_OPCODE_ITEMAMOUNT = 56;//物品可用次數
	public static final int S_OPCODE_PRIVATESHOPLIST = 57;//角色個人商店 {購買}
	public static final int S_OPCODE_DETELECHAROK = 58;//角色移除 [非立即]/7天後
	public static final int S_OPCODE_BOOKMARKS = 59;//角色座標名單
	public static final int S_OPCODE_INITPACKET = 60;//初始化OpCodes
	public static final int S_OPCODE_MOVEOBJECT = 62;//物件移動
	public static final int S_OPCODE_PUTSOLDIER = 63;//配置已僱用士兵
	public static final int S_OPCODE_TELEPORT = 64;//要求傳送 (有動畫)
	public static final int S_OPCODE_STRUP = 65;//力量提升封包
	public static final int S_OPCODE_LAWFUL = 66;//正義值更新
	public static final int S_OPCODE_SELECTTARGET = 67;//選擇一個目標
	public static final int S_OPCODE_ABILITY = 68;//戒指
	public static final int S_OPCODE_HPMETER = 69;//物件血條
	public static final int S_OPCODE_ATTRIBUTE = 70;//物件屬性
	public static final int S_OPCODE_SERVERVERSION = 72;//伺服器版本
	public static final int S_OPCODE_EXP = 73;//經驗值更新封包
	public static final int S_OPCODE_MPUPDATE = 74;//魔力更新
	public static final int S_OPCODE_CHANGENAME = 75;//改變物件名稱
	public static final int S_OPCODE_POLY = 76;//改變外型
	public static final int S_OPCODE_MAPID = 77;//更新角色所在的地圖
	public static final int S_OPCODE_ITEMCOLOR = 79;//物品狀態
	public static final int S_OPCODE_OWNCHARATTRDEF = 80;//角色防禦 & 屬性 更新
	public static final int S_OPCODE_PACKETBOX = 82;//角色選擇視窗/開啟拒絕名單(封包盒子)
	public static final int S_OPCODE_ACTIVESPELLS = 82;
	public static final int S_OPCODE_SKILLICONGFX = 82;
	public static final int S_OPCODE_UNKNOWN2 = 82;
	public static final int S_OPCODE_DELETEINVENTORYITEM = 83;//物品刪除
	public static final int S_OPCODE_RESTART = 84;//角色重新選擇 返回選人畫面 功能未知
	public static final int S_OPCODE_PINGTIME = 85;//Ping Time
	public static final int S_OPCODE_DEPOSIT = 86;//存入資金城堡寶庫 (2)
	public static final int S_OPCODE_TRUETARGET = 88;//魔法動畫 {精準目標}
	public static final int S_OPCODE_HOUSEMAP = 89;//血盟小屋地圖 [地點]
	public static final int S_OPCODE_CHARTITLE = 90;//角色封號
	public static final int S_OPCODE_DEXUP = 92;//敏捷提升封包
	public static final int S_OPCODE_CHANGEHEADING = 94;//物件面向
	public static final int S_OPCODE_BOARD = 96;//佈告欄 (訊息列表)
	public static final int S_OPCODE_LIQUOR = 97;//海底波紋
	public static final int S_OPCODE_TRADESTATUS = 99;//交易狀態
	public static final int S_OPCODE_SPOLY = 100;//特別變身封包
	public static final int S_OPCODE_UNDERWATER = 101;//更新角色所在的地圖 （水下）
	public static final int S_OPCODE_SKILLBRAVE = 102;//魔法|物品效果圖示 {勇敢藥水類}
	public static final int S_OPCODE_PUTHIRESOLDIER = 103;//配置傭兵
	public static final int S_OPCODE_POISON = 104 ;//魔法效果:中毒 { 編號 }
	public static final int S_OPCODE_DISCONNECT = 105;//立即中斷連線
	public static final int S_OPCODE_NEWCHARWRONG = 106;//角色創造失敗
	public static final int S_OPCODE_REMOVE_OBJECT = 107;//物件刪除
	public static final int S_OPCODE_NPC_ATTACKPACKET = 108;//NPC攻擊 用於特殊攻擊?!
	public static final int S_OPCODE_ADDITEM = 110;//物品增加封包
	public static final int S_OPCODE_TRADE = 111;//交易封包
	public static final int S_OPCODE_OWNCHARSTATUS2 = 112;//角色狀態 (2)
	public static final int S_OPCODE_SHOWHTML = 113;//產生對話視窗
	public static final int S_OPCODE_SKILLICONSHIELD = 114;//魔法效果 : 防禦纇
	public static final int S_OPCODE_DOACTIONGFX = 115;//物件動作種類 (短時間)
	public static final int S_OPCODE_TRADEADDITEM = 116;//增加交易物品封包
	public static final int S_OPCODE_YES_NO = 117;//選項封包 {Yes || No}
	public static final int S_OPCODE_HPUPDATE = 118;//體力更新
	public static final int S_OPCODE_SHOWSHOPBUYLIST = 119;//物品購買
	public static final int S_OPCODE_GAMETIME = 120;//更新目前遊戲時間 ( 遊戲時間 )
	public static final int S_OPCODE_PETCTRL = 121;//寵物控制介面移除
	public static final int S_OPCODE_CHARRESET = 121; //角色重置
	public static final int S_OPCODE_SOUND = 122;//撥放音效
	public static final int S_OPCODE_LIGHT = 123;//物件亮度
	public static final int S_OPCODE_LOGINRESULT = 124;//登入狀態
	public static final int S_OPCODE_PUTBOWSOLDIERLIST = 125;//配置牆上弓手
	public static final int S_OPCODE_WARTIME = 126;//攻城時間設定
	public static final int S_OPCODE_ITEMNAME = 127;//物品顯示名稱
    
    
	/**額外區塊(不確定||未使用)*/
    //public static final int S_OPCODE_xxxx = 33; // 似乎是顯示被馴服寵物的名稱 // ok
    //public static final int S_OPCODE_UNKNOWN1= 35;//0000: 01 03 xx xx xx..

}