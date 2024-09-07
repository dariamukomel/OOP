#!/bin/bash

SOURCE_FILE="Main.java"
PACKAGE_PATH="ru/nsu/lavitskaya"

javac ${PACKAGE_PATH}/${SOURCE_FILE}

javadoc -d docs ${PACKAGE_PATH}/*.java

jar cfe Main.jar ru.nsu.lavitskaya.Main ${PACKAGE_PATH}/*.class

java -jar Main.jar