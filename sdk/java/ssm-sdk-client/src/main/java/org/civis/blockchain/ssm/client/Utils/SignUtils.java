package org.civis.blockchain.ssm.client.Utils;

import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

public class SignUtils {

    public static byte[] rsaSign(String plainText, PrivateKey key) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(key);
        privateSignature.update(plainText.getBytes("UTF-8"));
        return privateSignature.sign();
    }

    public static String rsaSignAsB64(String plainText, PrivateKey key) throws Exception {
        byte[] value = rsaSign(plainText, key);
        return b64Encode(value);
    }

    public static String b64Encode(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }
}
