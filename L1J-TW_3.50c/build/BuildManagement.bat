@ECHO OFF
TITLE Lineage Java Server Build Management
SETLOCAL ENABLEDELAYEDEXPANSION

REM =================================== 路徑設定 =====================================
CD %~p0
CD ..

REM ================================================== 設定參數 ==================================================
SET ANT_BINARY=build\ant\bin\ant
SET BASE_PATH=..

REM ================================================== 顯示功能選項 ==================================================
MODE CON COLS=80 LINES=26
:init
SET ARGS=
CLS
ECHO ============================== L1J 建置管理系統 ==============================
ECHO.                                                                 Design by Neo
ECHO.
ECHO 請選擇要使用的功能：
ECHO.
ECHO  1. 一般建置
ECHO     完整的錯誤訊息資訊, 開發時較易找出錯誤原因.
ECHO.
ECHO  2. 最小建置 (無除錯資訊)
ECHO     最小化編譯後的核心程序大小, 便於網路傳輸.
ECHO.
ECHO.
ECHO.
ECHO.
ECHO.
ECHO.
ECHO.
ECHO.
ECHO.
ECHO.
ECHO.
ECHO.
ECHO. 0. 結束本程式
ECHO.
ECHO.
SET /P ARGS=請輸入編號來選擇功能： 
CLS
IF "%ARGS%"=="" GOTO ERR_NO_ARGS
IF %ARGS%==1 GOTO opt1
IF %ARGS%==2 GOTO opt2
IF %ARGS%==0 GOTO exit
GOTO ERR_NO_ARGS

REM ================================================== 功能選項 ==================================================
:opt1
START %ANT_BINARY% all
GOTO init

:opt2
START %ANT_BINARY% mini
GOTO init

REM ================================================== 錯誤處理 ==================================================
:ERR_NO_ARGS
CLS
ECHO 沒有這個選項 %ARGS%
ECHO.
PAUSE
GOTO init

REM ================================================== 結束程式 ==================================================
:exit
EXIT