#!/usr/bin/python

import picamera
import time

camera = picamera.PiCamera()

try:
  camera.resolution = (1000,1000)
  camera.rotation = 270

  while (True):
    camera.capture( '/mnt/ramdisk/image.png', 'png' )
    time.sleep( 0.30 )

finally:
  camera.close()

