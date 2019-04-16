package org.civis.blockchain.ssm.client.Utils;

import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.Reader;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyPairReader {

    public static KeyPair loadKeyPair(String filename) throws Exception {
        PrivateKey priv = loadPrivateKey(filename);
        PublicKey pub = loadPublicKey(filename);
        return new KeyPair(pub, priv);
    }

    public static PrivateKey loadPrivateKey(String filename) throws Exception {
        PemObject pem = getPemObject(filename);
        RSAPrivateKey key = RSAPrivateKey.getInstance(pem.getContent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateCrtKeySpec privSpec = new RSAPrivateCrtKeySpec(key.getModulus(), key.getPublicExponent(), key.getPrivateExponent(), key.getPrime1(), key.getPrime2(), key.getExponent1(), key.getExponent2(), key.getCoefficient());
        return kf.generatePrivate(privSpec);
    }

    public static PublicKey loadPublicKey(String filename) throws Exception {
        if(!filename.endsWith(".pub")) {
            filename = filename.concat(".pub");
        }
        PemObject pem = getPemObject(filename);
        byte[] pubKeyBytes = pem.getContent();

        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(pubSpec);
        return pubKey;
    }

    public static KeyPair generateRSAKey() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }

    private static PemObject getPemObject(String filename) throws IOException {
        Reader reader = FileUtils.getReader(filename);
        PemReader rpem = new PemReader(reader);
        return rpem.readPemObject();
    }

}
