package com.doodle;

import java.util.Base64;

public class App {
  public static void main(String[] args) throws Exception {
    var secret = Postgres.fetchSecret().trim();
    var pkcs1 = Base64.getDecoder().decode(secret);
    var privateKey = Rsa.readPkcs1PrivateKey(pkcs1);
    var pem = Base64.getEncoder().encodeToString(privateKey.getEncoded());
    System.out.println(Rsa.PKCS_8_PEM_HEADER);
    System.out.println(pem);
    System.out.println(Rsa.PKCS_8_PEM_FOOTER);
  }
}
