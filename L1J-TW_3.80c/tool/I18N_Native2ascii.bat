title L1J-TW 多國語系
@echo off
goto Manu

::選單
:Manu
echo L1J-TW 多國語系 Internationalization
echo ┌─────────────────┐
echo │           多國語系轉換　　　　　 │
echo └─────────────────┘
echo ---------------------------------------
echo (1) 轉換 zh_TW
echo (2) 轉換 jp_JP
echo (3) 轉換 zh_TW + 轉換 jp_JP
echo (4) 離開
echo ---------------------------------------
echo 請選擇:
set /p num=執行:
echo 選擇了%num%
goto go%num%




::============================以下是資料處理================================

::轉換 繁體中文
::●請注意路徑●●請修改成自己的Jdk/bin底下的native2ascii.exe路徑●
::zh_TW
:go1
"C:\Program Files\Java\jdk1.6.0_23\bin\native2ascii.exe" -encoding UTF-8 ../data/language/messages_zh_TW.txt ../data/language/messages_zh_TW.properties
goto Manu



::轉換 日文
::●請注意路徑●●請修改成自己的java/bin底下的native2ascii.exe路徑●
::jp_JP
:go2
"C:\Program Files\Java\jdk1.6.0_23\bin\native2ascii.exe" -encoding UTF-8 ../data/language/messages_ja_JP.txt ../data/language/messages_ja_JP.properties
goto Manu

::轉換 繁體中文 + 日文
::●請注意路徑●●請修改成自己的java/bin底下的native2ascii.exe路徑●
::zh_TW + jp_JP
:go3
"C:\Program Files\Java\jdk1.6.0_23\bin\native2ascii.exe" -encoding UTF-8 ../data/language/messages_zh_TW.txt ../data/language/messages_zh_TW.properties
"C:\Program Files\Java\jdk1.6.0_23\bin\native2ascii.exe" -encoding UTF-8 ../data/language/messages_ja_JP.txt ../data/language/messages_ja_JP.properties
goto Manu

::離開
:go4
exit


