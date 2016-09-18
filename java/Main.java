import a.gatekeeper.controller.*;
import a.gatekeeper.model.*;
import a.gatekeeper.view.*;

public class Main
{
  public static void main( String[] args ) throws Exception
  {
    // model
    GateLock lock = new GateLock();
    RPiCamera cam = new RPiCamera();

    // view
    ChallengePanel cp = ChallengePanel.makeUI();

    // controller
    Controller ctrl =
      new Controller( lock, cam, cp.qr(), cp.camView(), cp.statView() );

    try
    {
      ctrl.control(); // blocks, UI thread and others execute in background
    }
    catch( Exception e )
    {
      System.err.println( "Main.main: " + e.getMessage() );
      e.printStackTrace();
      System.exit( 1 );
    }

    System.exit( 0 );
  }
}
