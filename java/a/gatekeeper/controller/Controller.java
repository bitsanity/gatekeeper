package a.gatekeeper.controller;

import java.util.Arrays;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;

import a.gatekeeper.model.*;
import a.gatekeeper.util.*;
import a.gatekeeper.view.*;

public class Controller
{
  public Controller( GateLock lock,
                     RPiCamera cam,
                     QRView qr,
                     CameraView cv,
                     StatusView sv ) throws Exception
  {
    if (null == (lock_ = lock)) throw new Exception( "Controller: lock null" );
    if (null == (cam_ = cam)) throw new Exception( "Controller: cam null" );
    if (null == (qr_ = qr)) throw new Exception( "Controller: qr null" );
    if (null == (cv_ = cv)) throw new Exception( "Controller: cv null" );
    if (null == (status_ = sv))
      throw new Exception( "Controller: status null" );

    access_ = new DemoAccessController();
    inAccessMode_ = false;
  }

  public void control() throws Exception
  {
    while (true)
    {
      KeyChallenge chall = KeyChallenge.newChallenge();
      qr_.setChallenge( chall.toBase64() );

      while (true)
      {
        BufferedImage bi = cam_.takePicture();
        if (null != bi) cv_.setImage( bi );

        String response = null;

        try
        {
          response = QR.decode( bi );
        }
        catch( com.google.zxing.NotFoundException nfe )
        {
          // normal when image does not contain a barcode
        }
        catch( Exception e )
        {
          System.err.println( "Controller.control: " + e.getMessage() );
          e.printStackTrace();
        }

        if (null != response && 0 < response.length())
        {
          KeyResponseValidator vdtr =
            new KeyResponseValidator( response, chall );

          byte[] pubkey = vdtr.senderpubkey();

          if (null != pubkey)
          {
            if ( !vdtr.isValid() )
            {
              if ( Arrays.equals(lastValidKey_, pubkey) )
                continue; // taking pictures, same challenge

              status_.pulse( StatusView.Status.RED );

              break; // next challenge
            }
            else
              lastValidKey_ = pubkey;

            // first recognized key becomes root

            if (null == access_.getAdmin())
            {
              System.out.println( "admin is now: " + HexString.encode(pubkey) );
              access_.admit( pubkey );
              break; // next challenge
            }

            if (inAccessMode_)
            {
              if ( access_.isAllowed(pubkey) )
              {
                System.out.println( "barring: " + HexString.encode(pubkey) );
                access_.bar( pubkey );
              }
              else
              {
                System.out.println( "admitting: " + HexString.encode(pubkey) );
                access_.admit( pubkey );
              }

              inAccessMode_ = false;
              break; // next challenge
            }

            if ( access_.isAdmin(pubkey) )
            {
              System.out.println( "admin: going into Access mode" );
              inAccessMode_ = true;
              status_.pulse( StatusView.Status.WHITE );
            }
            else
            {
              if ( access_.isAllowed(pubkey) )
              {
                System.out.println( "admit: " + HexString.encode(pubkey) );
                lock_.pulseOpen();
                status_.pulse( StatusView.Status.GREEN );
              }
              else
              {
                System.out.println( "sorry: " + HexString.encode(pubkey) );
                status_.pulse( StatusView.Status.ORANGE );
              }
            }

            break; // to next challenge
          }
        }
      } // end while waiting for valid response
    } // end forever loop
  } // end control

  private GateLock lock_;
  private RPiCamera cam_;
  private QRView qr_;
  private CameraView cv_;
  private StatusView status_;
  private DemoAccessController access_;

  private boolean inAccessMode_;

  private byte[] lastValidKey_;
}
