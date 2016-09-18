#!/bin/bash

JLIB="-Djava.library.path=./lib/armv7l"

CLPTH="-cp ./java:./lib/gatekeeper.jar:./lib/core-3.2.1.jar:./lib/javase-3.2.1.jar:./lib/hsqldb.jar:/opt/pi4j/lib/pi4j-core.jar:/opt/pi4j/lib/pi4j-device.jar:/opt/pi4j/lib/pi4j-gpio-extension.jar:/opt/pi4j/lib/pi4j-service.jar"

# unit tests for utils
#java $CLPTH a.gatekeeper.util.Base64
#java $CLPTH a.gatekeeper.util.ByteOps
#java $CLPTH a.gatekeeper.util.HexString
#java $CLPTH a.gatekeeper.util.QR
#java $CLPTH a.gatekeeper.util.SHA256
#java $JLIB $CLPTH a.gatekeeper.util.Secp256k1

# unit tests for model
#java $JLIB $CLPTH a.gatekeeper.model.KeyChallenge;
