package org.civis.blockchain.ssm.client.crypto;

import org.bouncycastle.crypto.CryptoException;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSACipher {

    private static String ALGO = "RSA";

    public static String encrypt(byte[] value, PublicKey publicKey) throws CryptoException {
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enc = cipher.doFinal(value);
            return Base64.getEncoder().encodeToString(enc);
        } catch (Exception e) {
            throw new CryptoException("Error encrypting:", e);
        }
    }

    public static String decrypt(String value, PrivateKey privateKey) throws CryptoException {
        try {
            byte[] bytVal =  Base64.getDecoder().decode(value);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] dec = cipher.doFinal(bytVal);
            return new String(dec);
        } catch (Exception e) {
            throw new CryptoException("Error decrypting:", e);
        }
    }

}
