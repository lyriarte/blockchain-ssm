package org.civis.blockchain.ssm.client.crypto;


import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import org.assertj.core.api.Assertions;
import org.bouncycastle.crypto.CryptoException;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.civis.blockchain.ssm.client.Utils.FileUtils.getFile;
import static org.civis.blockchain.ssm.client.crypto.AESCipher.secretKeyFromBase64;

class AESCipherTest {

    public static final String FILE_TO_COMMIT_TXT = "crypto/fileToCommit.txt";
    public static final String FILE_TO_COMMIT_ENCRYPTED = "crypto/fileToCommit.encrypted";

    @Test
    void encrypt() throws IOException, CryptoException {
        File fileToEncrypt = getFile(FILE_TO_COMMIT_TXT);
        File encryptedFile = File.createTempFile("enc_", "tmp");
        File encryptedFileProof = getFile(FILE_TO_COMMIT_ENCRYPTED);
        try {
            FileOutputStream os = new FileOutputStream(encryptedFile);
            SecretKey key = AESCipher.secretKeyFromBase64("+cRaRuaSK1/RObE9oEOm6Q==");
            AESCipher.encrypt(fileToEncrypt, os, key);

            boolean areFileEquals = Files.equal(encryptedFile, encryptedFileProof);
            Assertions.assertThat(areFileEquals).isTrue();
        } finally {
            encryptedFile.delete();
        }
    }

    @Test
    void decrypr() throws IOException, CryptoException {
        File encryptedFile = getFile(FILE_TO_COMMIT_ENCRYPTED);
        try {
            SecretKey key = secretKeyFromBase64("+cRaRuaSK1/RObE9oEOm6Q==");

            InputStream decryptedStream = AESCipher.decrypt(new FileInputStream(encryptedFile), key);

            String value = CharStreams.toString(new InputStreamReader(decryptedStream));
            Assertions.assertThat(value).isEqualTo("to commit");
        } finally {
            encryptedFile.delete();
        }
    }

    @Test
    void generateSecretKey() throws NoSuchAlgorithmException {
        SecretKey key = AESCipher.generateSecretKey();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        SecretKey keyBuilded = AESCipher.secretKeyFromBase64(encodedKey);
        Assertions.assertThat(key).isEqualToComparingFieldByField(keyBuilded);
    }

}