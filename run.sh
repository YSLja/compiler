#!/bin/bash
# Mac / Linux runner
# Usage:  ./run.sh           → runs test.g
#         ./run.sh myfile.g  → runs myfile.g

CP="lib/antlr-4.13.2-complete.jar:lib/prog1.jar:lib/prog2_withoutAbsyn.jar:bin"

# Compile only the source files we own (Absyn + Typecheck).
# The Parse package (lexer/parser) is pre-built inside prog2_withoutAbsyn.jar.
find Absyn Typecheck -name "*.java" > sources.txt
javac -cp "$CP" -d bin @sources.txt
rm sources.txt

if [ "$#" -eq 0 ]; then
   java -cp "$CP" Typecheck.Main test.g
else
   java -cp "$CP" Typecheck.Main "$1"
fi
