package a.gatekeeper.model;

import java.util.Arrays;

import a.gatekeeper.util.*;

public class KeyResponseValidator
{
  // note: a response can cryptographically sound and have a public key
  //       yet still be invalid if it is not correct for the challenge

  private byte[] senderpubkey_;
  private boolean isValid_;

  public byte[] senderpubkey() { return senderpubkey_; }
  public boolean isValid() { return isValid_; }

  public KeyResponseValidator( String rsp, KeyChallenge ch )
  {
    senderpubkey_ = null;
    isValid_ = false;

    try
    {
      byte[] signature = null;

      byte[] rspBytes = Base64.decode( rsp );

      // uncompressed public key

      if ( (byte)0x04 == rspBytes[0] )
      {
        senderpubkey_ = Arrays.copyOfRange( rspBytes, 0, 65 );
        signature = Arrays.copyOfRange( rspBytes, 65, rspBytes.length );
      }

      // compressed public keys

      if ( (byte)0x02 == rspBytes[0] || (byte)0x03 == rspBytes[0] )
      {
        senderpubkey_ = Arrays.copyOfRange( rspBytes, 0, 33 );
        signature = Arrays.copyOfRange( rspBytes, 33, rspBytes.length );
      }

      // verify responder signed my public key using the private key
      // matching the provided public key

      byte[] mypub = ch.pubkey();
      byte[] h32 = SHA256.hash( mypub );

      isValid_ = new Secp256k1().verifyECDSA( signature, h32, senderpubkey_ );
    }
    catch( Exception e )
    {
      System.err.println( "KeyResponseValidator: " + e.getMessage() );
    }
  }
}
