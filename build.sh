#/bin/sh
find ./src/main/java -name "*.java" > sources.txt
rm -rf bin && mkdir bin
javac -cp src/main/java:lib/log4j-core-2.1.jar:lib/log4j-api-2.1.jar -d bin -proc:none @sources.txt
