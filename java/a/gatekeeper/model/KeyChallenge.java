package a.gatekeeper.model;

import java.security.SecureRandom;
import a.gatekeeper.util.*;

// Protocol: the challenge is a valid 65-byte Secp256k1 public key
// (includes the prepended 0x04 byte) expressed in Base64 plus
// 64-byte signature of public key
public class KeyChallenge
{
  public static KeyChallenge newChallenge()
  {
    KeyChallenge result = null;
    byte[] privkey = new byte[32];

    try
    {
      SecureRandom.getInstance( "SHA1PRNG" ).nextBytes( privkey );

      Secp256k1 curve = new Secp256k1();

      if ( !curve.privateKeyIsValid(privkey) )
        throw new Exception( "invalid private key" );

      byte[] rawpubkey = curve.publicKeyCreate( privkey );
      byte[] sig = curve.signECDSA( SHA256.hash(rawpubkey), privkey );

      if (curve.verifyECDSA(sig, SHA256.hash(rawpubkey), rawpubkey))
        result = new KeyChallenge( rawpubkey, sig );
    }
    catch( Exception e )
    {
      System.err.println( "Challenge.newChallenge: " + e.getMessage() );
      return null;
    }

    return result;
  }

  public KeyChallenge( byte[] pubkey, byte[] sig ) throws Exception
  {
    if (null != pubkey && 64 == pubkey.length)
      pubkey = ByteOps.prepend( (byte)0x04, pubkey );

    if (null == pubkey)
      throw new Exception( "Challenge: invalid pubkey" );

    if (null == sig || 0 == sig.length)
      throw new Exception( "Challenge: invalid sig" );

    pubkey_ = pubkey;
    sig_ = sig;
  }

  public static KeyChallenge parse( String b64 ) throws Exception
  {
    byte[] raw = Base64.decode( b64 );

    byte[] key = ((byte)0x02 == raw[0] || (byte)0x03 == raw[0])
                 ? java.util.Arrays.copyOfRange( raw, 0, 33 )
                 : java.util.Arrays.copyOfRange( raw, 0, 65 );

    byte[] sig =
      java.util.Arrays.copyOfRange( raw,
                                    raw.length - key.length + 1,
                                    32 );

    return new KeyChallenge( key, sig );
  }

  public String toBase64()
  {
    try
    {
      return Base64.encode( toBytes() );
    }
    catch( Exception e )
    {
      System.err.println( "Challenge.toBase64: " + e.getMessage() );
    }

    return null;
  }

  public byte[] toBytes()
  {
    return ByteOps.concat( pubkey_, sig_ );
  }

  public byte[] pubkey() { return pubkey_; }
  public byte[] sig() { return sig_; }

  private byte[] pubkey_;
  private byte[] sig_;

  public static void main( String[] args )
  {
    String chB64 = "BOX3MR5UYW6RQG8PtU/153J64adLst5e3Ab0AuQyxn7MgmVgncTOF+QX2B82zDTOdaA1UhCjG2feaYA6v06IB/AwRAIgcwKDDz1WjN/nZhx+lDxgIFCt64Sukg209IdaR9rVCjECIEv7wt1Qkl65sdhpamqtkbFjCJpbNHwqiKQop5SqB8tO";

    String resp = "BBA9GPU9Rr2FicQ1Agk5i3L+t6O6/MGC5jjy7sqxm5u2pexpQw3FChKfbXwMJSlqRvshwohK1fT27RRdYduEy88wRQIhALru1iB1U6gCb93F2R7KSThQhHzDEIZAziLHhLDtK+SXAiAMLHcPGWzLUQwlcrNqHLDfHbUgJSPGbeE7t8ZUIPpVPg==";

    try
    {
      KeyChallenge kc = KeyChallenge.parse( chB64 );
      KeyResponseValidator vd = new KeyResponseValidator( resp, kc );

      if (vd.isValid())
        System.out.println( "KeyChallenge: PASS" );
      else
        System.out.println( "KeyChallenge: FAIL" );
    }
    catch( Exception e )
    {
      System.out.println( "KeyChallenge: " + e.getMessage() );
    }
  }
}
