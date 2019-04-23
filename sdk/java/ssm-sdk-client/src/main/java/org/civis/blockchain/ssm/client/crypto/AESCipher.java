package org.civis.blockchain.ssm.client.crypto;

import com.google.common.io.ByteStreams;
import org.bouncycastle.crypto.CryptoException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class AESCipher {

    private static String ALGO = "AES";

    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(ALGO);
        kg.init(new SecureRandom( new byte[] { (byte)0x00, (byte)0x01, (byte)0x02}));
        return kg.generateKey();
    }

    public static SecretKey secretKeyFromBase64(String b64Key) {
        byte[] key = Base64.getDecoder().decode(b64Key);
        return new SecretKeySpec(key, ALGO);
    }


    public static InputStream decrypt(InputStream fileInput, SecretKey key) throws CryptoException {
        try {
            return getDecryptCipher(key, fileInput);
        } catch (Exception e) {
            throw new CryptoException("Error decrypting file", e);
        }

    }

    public static void encrypt(File file, OutputStream outputStream, SecretKey key) throws CryptoException {
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
            encrypt(fileInput, outputStream, key);
        } catch (Exception e) {
            throw new CryptoException("Error encrypting file:" + file.getName(), e);
        } finally {
            if(fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private static void encrypt(InputStream fileInput, OutputStream outputStream, SecretKey key) throws CryptoException {
        OutputStream output = null;
        try {
            output =  getEncryptCipher(outputStream, key);
            ByteStreams.copy(fileInput, output);
        } catch (Exception e) {
            throw new CryptoException("Error encrypting:", e);
        } finally {
            if(output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static CipherInputStream getDecryptCipher(SecretKey key, InputStream fileInput) throws CryptoException {
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new CipherInputStream(fileInput, cipher);
        } catch (Exception e) {
            throw new CryptoException("Error decrypting", e);
        }
    }

    private static CipherOutputStream getEncryptCipher(OutputStream fileOutput, SecretKey key) throws CryptoException {
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new CipherOutputStream(fileOutput, cipher);
        } catch (Exception e) {
            throw new CryptoException("Error encrypting", e);
        }
    }
}
