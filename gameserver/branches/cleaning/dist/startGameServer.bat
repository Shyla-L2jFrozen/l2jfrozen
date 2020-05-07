@echo off
title L2J-Frozen: Game Server Console
mode con: cols=140
:start

REM --- Renaming old log file
set mypath=%cd%
for /f "skip=1" %%x in ('wmic os get localdatetime') do if not defined MyDate set MyDate=%%x
set CUR_NN=%time:~3,2%
set CUR_SS=%time:~6,2%
set CUR_MS=%time:~9,2%
set today=%MyDate:~0,4%-%MyDate:~4,2%-%MyDate:~6,2%-%CUR_SS%-%CUR_MS%

set SUBFILENAME=%CUR_YYYY%%CUR_MM%%CUR_DD%-%CUR_HH%%CUR_NN%%CUR_SS%
ren "%mypath%\log\loggerOut.log" "loggerOut%today%.log"

REM -------------------------------------
REM Default parameters for a basic server.
java -Dfile.encoding=UTF8 -Xms1024m -Xmx1024m -cp lib/*;lib/l2jfrozen-game.jar com.l2jfrozen.gameserver.GameServer
REM
REM If you have a big server and lots of memory, you could experiment for example with
REM java -server -Xmx1536m -Xms1024m -Xmn512m -XX:PermSize=256m -XX:SurvivorRatio=8 -Xnoclassgc -XX:+AggressiveOpts
REM -------------------------------------

if ERRORLEVEL 7 goto telldown
if ERRORLEVEL 6 goto tellrestart
if ERRORLEVEL 5 goto taskrestart
if ERRORLEVEL 4 goto taskdown
REM 3 - abort
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:tellrestart
echo.
echo Telnet server Restart ...
echo Send you bug to : http://www.l2jfrozen.com
echo.
goto start
:taskrestart
echo.
echo Auto Task Restart ...
echo Send you bug to : http://www.l2jfrozen.com
echo.
goto start
:restart
echo.
echo Admin Restart ...
echo Send you bug to : http://www.l2jfrozen.com
echo.
goto start
:taskdown
echo .
echo Server terminated (Auto task)
echo Send you bug to : http://www.l2jfrozen.com
echo .
:telldown
echo .
echo Server terminated (Telnet)
echo Send you bug to : http://www.l2jfrozen.com
echo .
:error
echo.
echo Server terminated abnormally
echo Send you bug to : http://www.l2jfrozen.com
echo.
:end
echo.
echo server terminated
echo Send you bug to : http://www.l2jfrozen.com
echo.
:question
set choix=q
set /p choix=Restart(r) or Quit(q)
if /i %choix%==r goto start
if /i %choix%==q goto exit
:exit
exit
pause
