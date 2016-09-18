#!/bin/bash

javah -cp ../java/:. a.gatekeeper.util.Secp256k1

gcc -D __int64="long long" -c -I"$HOME/secp256k1/include" -I"$JAVAHOME/include" -I"$JAVAHOME/include/linux" -shared a_gatekeeper_util_Secp256k1.c

gcc -shared -o ../lib/armv7l/liba_gatekeeper_util_Secp256k1.so a_gatekeeper_util_Secp256k1.o $HOME/secp256k1/.libs/libsecp256k1.a

