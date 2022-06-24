package com.doodle;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/** See <a href="https://stackoverflow.com/a/55339208/1521064">this</a>. */
public class Rsa {
  public static final String PKCS_8_PEM_HEADER = "-----BEGIN PRIVATE KEY-----";
  public static final String PKCS_8_PEM_FOOTER = "-----END PRIVATE KEY-----";

  public static PrivateKey readPkcs1PrivateKey(byte[] pkcs1Bytes) throws GeneralSecurityException {
    // We can't use Java internal APIs to parse ASN.1 structures, so we build a PKCS#8 key Java can
    // understand
    var pkcs1Length = pkcs1Bytes.length;
    var totalLength = pkcs1Length + 22;
    var pkcs8Header =
        new byte[] {
          0x30,
          (byte) 0x82,
          (byte) ((totalLength >> 8) & 0xff),
          (byte) (totalLength & 0xff), // Sequence + total length
          0x2,
          0x1,
          0x0, // Integer (0)
          0x30,
          0xD,
          0x6,
          0x9,
          0x2A,
          (byte) 0x86,
          0x48,
          (byte) 0x86,
          (byte) 0xF7,
          0xD,
          0x1,
          0x1,
          0x1,
          0x5,
          0x0, // Sequence: 1.2.840.113549.1.1.1, NULL
          0x4,
          (byte) 0x82,
          (byte) ((pkcs1Length >> 8) & 0xff),
          (byte) (pkcs1Length & 0xff) // Octet string + length
        };
    var pkcs8bytes = concat(pkcs8Header, pkcs1Bytes);
    return readPkcs8PrivateKey(pkcs8bytes);
  }

  private static PrivateKey readPkcs8PrivateKey(byte[] pkcs8Bytes) throws GeneralSecurityException {
    var keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
    var keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
    try {
      return keyFactory.generatePrivate(keySpec);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException("Unexpected key format!", e);
    }
  }

  private static byte[] concat(byte[] byteArray1, byte[] byteArray2) {
    var bytes = new byte[byteArray1.length + byteArray2.length];
    System.arraycopy(byteArray1, 0, bytes, 0, byteArray1.length);
    System.arraycopy(byteArray2, 0, bytes, byteArray1.length, byteArray2.length);
    return bytes;
  }
}
