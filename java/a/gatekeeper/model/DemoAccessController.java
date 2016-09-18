package a.gatekeeper.model;

import a.gatekeeper.util.*;

import java.sql.*;

public class DemoAccessController
{
  static
  {
    try
    {
      Class.forName( "org.hsqldb.jdbc.JDBCDriver" );
    }
    catch( Exception e )
    {
      System.err.println( "DemoAccessController: " + e.getMessage() );
      System.exit( 1 );
    }
  }

  public DemoAccessController()
  {
    try
    {
      cx_ = DriverManager.getConnection( "jdbc:hsqldb:file:gk_db",
                                         "SA", "gatekeeper" );

      Statement stmt = cx_.createStatement();
      stmt.executeUpdate( CREATE_TABLE_CMD );
      stmt.close();
    }
    catch( Exception e )
    {
      System.err.println( "DemoAccessController(): " + e.getMessage() );
      System.exit( 1 );
    }
  }

  public boolean isAllowed( byte[] pubkey )
  {
    if (!checkLen(pubkey)) return false;

    boolean result = false;
    try
    {
      String query = "SELECT COUNT(*) FROM GK_ACL WHERE PubKeyB64='" +
                     Base64.encode( pubkey ) + "'";

      Statement stmt = cx_.createStatement();
      ResultSet rs = stmt.executeQuery( query );
      rs.next();
      result = (1 == rs.getInt( 1 ));
      rs.close();
      stmt.close();
    }
    catch( Exception e )
    {
      System.err.println( "DemoAccessController(): " + e.getMessage() );
      return false;
    }

    return result;
  }

  public byte[] getAdmin()
  {
    String result = null;

    try
    {
      String query = "SELECT PubKeyB64 FROM GK_ACL WHERE IsAdmin=TRUE";

      Statement stmt = cx_.createStatement();
      ResultSet rs = stmt.executeQuery( query );
      while (rs.next())
        result = rs.getString( 1 );
      rs.close();
      stmt.close();

      if (null != result)
        return Base64.decode( result );
    }
    catch( Exception e )
    {
      System.err.println( "DemoAccessController.getAdmin(): " +
                          e.getMessage() );
    }

    return null;
  }

  public boolean isAdmin( byte[] pubkey )
  {
    if (!checkLen(pubkey)) return false;

    boolean result = false;
    try
    {
      String query = "SELECT COUNT(*) FROM GK_ACL WHERE PubKeyB64='" +
                     Base64.encode( pubkey ) + "' AND IsAdmin=TRUE";

      Statement stmt = cx_.createStatement();
      ResultSet rs = stmt.executeQuery( query );
      rs.next();
      result = (1 == rs.getInt( 1 ));
      rs.close();
      stmt.close();
    }
    catch( Exception e )
    {
      System.err.println( "DemoAccessController.isAdmin: " +
                          e.getMessage() );
      return false;
    }

    return result;
  }

  public boolean admit( byte[] pubkey )
  {
    if (!checkLen(pubkey)) return false;

    try
    {
      // first key is automatically the admin

      String query = "INSERT INTO GK_ACL (PubKeyB64,IsAdmin) VALUES ('" +
                     Base64.encode(pubkey) + "'," +
                    ((0 == count()) ? "TRUE" : "FALSE") + ")";

      Statement stmt = cx_.createStatement();
      stmt.executeUpdate( query );
      stmt.close();
    }
    catch( Exception e )
    {
      System.err.println( "DemoAccessController.admit: " +
                          e.getMessage() );
      return false;
    }

    return true;
  }

  public boolean bar( byte[] pubkey )
  {
    if (!checkLen(pubkey)) return false;

    try
    {
      String query = "DELETE FROM GK_ACL WHERE PubKeyB64='" +
                     Base64.encode(pubkey) + "'";

      Statement stmt = cx_.createStatement();
      stmt.executeUpdate( query );
      stmt.close();
    }
    catch( Exception e )
    {
      System.err.println( "DemoAccessController(): " + e.getMessage() );
      return false;
    }

    return true;
  }

  private boolean checkLen( byte[] pubkey )
  {
    if (null == pubkey || (65 != pubkey.length && 33 != pubkey.length))
    {
      System.err.println( "DemoAccessController.isAllowed: invalid key" );
      return false;
    }

    return true;
  }

  private int count()
  {
    String query = "SELECT COUNT(*) FROM GK_ACL";

    int result = 0;

    try
    {
      Statement stmt = cx_.createStatement();
      ResultSet rs = stmt.executeQuery( query );
      rs.next();
      result = rs.getInt( 1 );
      rs.close();
      stmt.close();
    }
    catch( Exception e )
    {
      System.err.println( "DemoAccessController.count: " +
                          e.getMessage() );
    }

    return result;
  }

  private Connection cx_;

  // "IF NOT EXISTS" supported since hsqldb 2.3
  private static final String CREATE_TABLE_CMD =
    "CREATE TABLE IF NOT EXISTS GK_ACL " +
    "( PubKeyB64 VARCHAR(64) PRIMARY KEY, IsAdmin BOOLEAN )";
}
