package a.gatekeeper.view;

import java.awt.*;
import java.util.Date;
import javax.swing.*;

public class StatusView extends JPanel implements Runnable
{
  // BLACK -  initial and after other states have displayed a while
  // GREEN -  valid response, keyholder allowed, lock should be open
  // ORANGE - valid response, keyholder disallowed, access denied
  // RED   -  invalid response, access denied
  // WHITE -  system is in 'toggle access' mode - next public key
  //          scanned will be learned or forgotten

  public enum Status { BLACK, GREEN, ORANGE, RED, WHITE };

  public StatusView()
  {
    endTime_ = 0L;
    new Thread(this).start();
  }

  public Dimension getPreferredSize()
  {
    return new Dimension( 770, 20 );
  }

  // set status to newStatus for a few seconds
  public void pulse( Status newStatus )
  {
    synchronized(this)
    {
      status_ = newStatus;
      endTime_ = new Date().getTime() + DURATION_MSEC;
    }
  }

  public void run()
  {
    while (true)
    {
      long now = new Date().getTime();

      Color col = Color.BLACK;

      synchronized(this)
      {
        if ( now > endTime_ )
        {
          status_ = Status.BLACK;
        }
        else
          switch ( status_ )
          {
            case BLACK:  col = Color.BLACK; break;
            case GREEN:  col = Color.GREEN; break;
            case ORANGE: col = Color.ORANGE.darker(); break;
            case RED:    col = Color.RED;   break;
            case WHITE:  col = Color.WHITE; break;

            default:
              System.err.println( "Status: unrecognized state " );
          }
      }

      try
      {
        final Color toSet = col;

        SwingUtilities.invokeLater( new Runnable() {
          public void run() {
            if (toSet != Color.BLACK)
              System.out.println( "setting color: " + toSet );
            setBackground( toSet );
            repaint();
          }
        } );

        Thread.currentThread().sleep( TPOLLINTVL );
      }
      catch (Exception e)
      {
        System.err.println( "Status.run: " + e.getMessage() );
        System.exit(1);
      }

    } // end forever loop
  }

  public static final long DURATION_MSEC = 5000L; // 5 sec
  private static final long TPOLLINTVL = 1000L; // thread poll interval
  private long endTime_; // time at which status pulse should end
  private Status status_;
}
