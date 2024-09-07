#!/bin/bash

SOURCE_FILE="HeapSort.java"
PACKAGE_PATH="ru/nsu/lavitskaya"

javac ${PACKAGE_PATH}/${SOURCE_FILE}

javadoc -d docs ${PACKAGE_PATH}/*.java

jar cfe HeapSort.jar ru.nsu.lavitskaya.HeapSort ${PACKAGE_PATH}/*.class

java -jar HeapSort.jar