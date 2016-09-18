package a.gatekeeper.model;

import java.awt.image.*;
import java.io.File;
import javax.imageio.*;

public class RPiCamera
{
  public RPiCamera() {}

  public BufferedImage takePicture() throws Exception
  {
    try
    {
      return ImageIO.read( new File(IMAGE_FILE) );
    }
    catch( Exception e )
    {
      return null;
    }
  }

  public static final String IMAGE_FILE = "/mnt/ramdisk/image.png";
}
