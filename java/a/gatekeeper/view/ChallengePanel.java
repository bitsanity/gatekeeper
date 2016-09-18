package a.gatekeeper.view;

import java.awt.*;
import javax.swing.*;

public class ChallengePanel extends JPanel
{
  public static ChallengePanel makeUI()
  {
    ChallengePanel result = null;

    JFrame jf = new JFrame( "Gatekeeper" );

    jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

    jf.getContentPane().add( result = new ChallengePanel(),
                             BorderLayout.CENTER );

    //jf.setSize( 800, 480 );
    jf.setExtendedState( Frame.MAXIMIZED_BOTH );
    jf.pack();
    jf.setVisible( true );

    return result;
  }

  public QRView qr() { return qr_; }
  public CameraView camView() { return cam_; }
  public StatusView statView() { return stat_; }

  private ChallengePanel()
  {
    setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
    setBackground( Color.BLACK );
    setLayout( new BorderLayout() );

    qr_ = new QRView();
    cam_ = new CameraView();
    stat_ = new StatusView();

    Box bx = Box.createHorizontalBox();
    bx.add( qr_ );
    bx.add( Box.createRigidArea(new Dimension(5,5)) );
    bx.add( cam_ );
    add( bx, BorderLayout.CENTER );

    add( stat_, BorderLayout.SOUTH );
  }

  private QRView qr_ = null;
  private CameraView cam_ = null;
  private StatusView stat_ = null;
}
