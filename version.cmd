@ECHO OFF
IF "%1" == "" GOTO usage
IF NOT "%2" == "" GOTO usage

SET TAG=%1

echo You are using the tag: %TAG%
pause

CALL mvn versions:set -DnewVersion=%TAG% -DgenerateBackupPoms=false -DprocessAllModules=true
GOTO end

:usage
ECHO.
ECHO usage: %0 VERSION_TAG
ECHO example (I): %0 3.0.5-SNAPSHOT
ECHO example (II): %0 4.0
ECHO.

:end
