#!/bin/bash

JLIB="-Djava.library.path=./lib/armv7l:/opt/vc/lib"

CLPTH="-cp ./java:./lib/gatekeeper.jar:./lib/core-3.2.1.jar:./lib/javase-3.2.1.jar:./lib/hsqldb.jar:/opt/pi4j/lib/pi4j-core.jar:/opt/pi4j/lib/pi4j-device.jar:/opt/pi4j/lib/pi4j-gpio-extension.jar:/opt/pi4j/lib/pi4j-service.jar"

java $JLIB $CLPTH Main
