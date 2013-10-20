####################
 L1J-JP Telnet Tool
####################

■動作環境
PHPとMySQLが動作するWebサーバー
php.iniのdefault_charsetはShift_JISでなければいけません。（default_charset = "Shift_JIS"）
※外部からのポート23が閉じている事。

■インストールと設定
1.server.propertiesの變更
  TelnetServer = True
2.ダウンロードしたファイルを解凍しWebサーバーのフォルダにコピーします。
3.setup.phpを環境に合わせて變更します。
4.ブラウザでindex.phpにアクセスしてログインします。

■コマンドの入力
ゲーム內と同じように先頭に付加する文字列で動作が變わります。
先頭の文字列が一致しない場合は何も行いません。

【.】
acountsテーブルのaccess_levelが200の場合にTelnetコマンドを實行出來ます。
結果は「Result > 」に表示されます。
ただしglobalchatコマンドは實行出來ません。
例：
.playerid [キャラクター名]
.charstatus [objid]

【&】
全体チャットで發言出來ます。
charactersテーブルのIsGMが200だと名前が[******]となります。
例：
&こんにちは
/////google/////
■操作環境
PHP和MySQL的Web服務器運行
default_charset在php.ini中沒有除非Shift_JIS 。 （ Default_charset = “ Shift_JIS ” ）
※外部端口23可以被關閉。

■安裝和配置
變更的1.server.properties
   TelnetServer =真
2 。解壓下載的文件文件夾中的Web服務器。
變更3.setup.php您的特定環境。
4 。在您的瀏覽器來登錄訪問的index.php 。

■輸入命令
WARIMASU變化字符串附加到運作的方式同遊戲內。
可能不匹配的第一個字符串沒有採取任何行動。

[ 。 ]
表200 acounts access_level來櫻花如果Telnet命令行實。
其結果是“結果” “顯示。
然而globalchat命令行來MASEN實。
例如：
。 playerid [字符名稱]
。 charstatus [ objid ]

[ ＆ ]
來櫻花發言在整個聊天。
IsGM表的字符為200 [******]名稱。
例如：
＆喜