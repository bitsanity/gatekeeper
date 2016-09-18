# gatekeeper

An implementation of a gatekeeper for Raspberry Pi.

- A subsystem of [ADILOS](https://github.com/bitsanity/ADILOS)
- uses optic code recognition to communicate with keymasters
- uses secp256k1 Elliptic Curve cryptography for challenge-response protocol

FUNCTION

1. Generate new challenge and display to world
2. Scan world for response
3. detect, validate response, determine keymaster's public key K
4. Lookup K in a local authorized keys database
4. Open lock for a few seconds
5. Repeat from step (1)

DEPENDENCIES / COMPONENTS

/mnt/ramdisk
- tmpfs, chmod 777, enough space to hold png image (4MB works)

autogen libtool make
- to compile libsecp256k1

libsecp256k1
- MIT License
- https://github.com/bitcoin/secp256k1
- compile scripts expect placed in $HOME/secp256k1

Java 1.8.0 SE
- Oracle Corporation Binary Code License Agreement
- Oracle JDK for compiling, set $JAVAHOME accordingly

Apache Ant 1.9.6
- build scripting

Raspberry Pi Camera Module
- https://www.adafruit.com/products/1367
- connect camera to dedicated camera slot
- run 'sudo raspi-config' and explicitly enable camera

picamera
- apt-get install python-picamera

fswebcam module
- if using standard USB webcam instead of RPi camera

pi4j 1.0
- GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007
- interface with the GPIO pins
- packaged install: http://pi4j.com/install.html
- install process: run 'curl -s get.pi4j.com | sudo bash'
- jar files in /opt/pi4j/lib

ZXing 3.2.1
- Apache License Version 2.0, January 2004
- library to generate/parse QR
- core-3.2.1.jar
- javase-3.2.1.jar

Hypersonic SQL DB 2.3.3
- BSD-based license: http://hsqldb.org/web/hsqlLicense.html
- hsqldb.jar
- SQL / JDBC for databased access control list

