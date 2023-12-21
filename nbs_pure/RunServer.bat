@echo off
setlocal

: 동일한 디렉토리에 있는 첫 번째 .jar 파일 찾기
for /f %%i in ('dir /b *.jar') do set JAR_FILE=%%i

set LOG_FILE=application.log

:: 애플리케이션 중지
taskkill /f /im javaw.exe

:: 애플리케이션 재시작 (백그라운드에서, 콘솔 창 없음)
start /b javaw -jar %JAR_FILE% > %LOG_FILE% 2>&1

endlocal