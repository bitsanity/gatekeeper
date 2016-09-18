package a.gatekeeper.model;

import java.awt.image.*;
import java.io.File;
import javax.imageio.*;

// interfaces to generic USB camera. On RPi must install fswebcam package
public class USBCamera
{
  public USBCamera()
  {
  }

  public BufferedImage takePicture() throws Exception
  {
    BufferedImage result = null;

    Process pb = new ProcessBuilder( "fswebcam", IMAGE_FILE ).start();

    pb.waitFor();

    int status = pb.exitValue();

    if (0 == status)
    {
      result = ImageIO.read( new File(IMAGE_FILE) );
    }
    else
      throw new Exception( "USBCamera: grab attempt returned " + status );

    return result;
  }

  private static final String IMAGE_FILE = "/mnt/ramdisk/image.jpg";
}
