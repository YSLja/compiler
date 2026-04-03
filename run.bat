@echo off
REM Windows runner
REM Usage:  run.bat           -> runs test.g
REM         run.bat myfile.g  -> runs myfile.g

set CP=lib\antlr-4.13.2-complete.jar;lib\prog1.jar;lib\prog2_withoutAbsyn.jar;bin

REM Compile only the source files we own (Absyn + Typecheck).
REM The Parse package (lexer/parser) is pre-built inside prog2_withoutAbsyn.jar.
dir /s /b Absyn\*.java Typecheck\*.java > sources.txt
javac -cp "%CP%" -d bin @sources.txt
del sources.txt

if "%~1"=="" (
    java -cp "%CP%" Typecheck.Main test.g
) else (
    java -cp "%CP%" Typecheck.Main %1
)
