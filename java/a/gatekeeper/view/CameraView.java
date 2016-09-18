package a.gatekeeper.view;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class CameraView extends JPanel
{
  public CameraView()
  {
    image_ = null;
    setBorder( BorderFactory.createLineBorder(Color.BLACK) );
    setBackground( Color.black );
  }

  public Dimension getPreferredSize()
  {
    return dim_;
  }

  public void setImage( BufferedImage i )
  {
    image_ = i;
    repaint();
  }

  public void paintComponent( Graphics g )
  {
    super.paintComponent( g );

    if (null != image_)
      g.drawImage( image_,
                   0, 0,                                  // dstx1, dsty1
                   385, 385,                              // dstx2, dsty2
                   0, 0,                                  // srcx1, srcy1
                   image_.getWidth(), image_.getHeight(), // srcx2, srcy2
                   null );
  }

  private static final Dimension dim_ = new Dimension( 385, 385 );
  private BufferedImage image_;
}
