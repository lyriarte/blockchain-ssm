package org.civis.blockchain.ssm.client.Utils;

import com.google.common.io.Resources;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.FileReader;
import java.net.URL;
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
        URL path = Resources.getResource(filename);

        FileReader reader = new FileReader(path.getFile());
        PemReader rpem = new PemReader(reader);
        PemObject pem =  rpem.readPemObject();
        RSAPrivateKey key = RSAPrivateKey.getInstance(pem.getContent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateCrtKeySpec privSpec = new RSAPrivateCrtKeySpec(key.getModulus(), key.getPublicExponent(), key.getPrivateExponent(), key.getPrime1(), key.getPrime2(), key.getExponent1(), key.getExponent2(), key.getCoefficient());
        return kf.generatePrivate(privSpec);
    }

    public static PublicKey loadPublicKey(String filename) throws Exception {
        if(!filename.endsWith(".pub")) {
            filename = filename.concat(".pub");
        }
        URL path = Resources.getResource(filename);
        FileReader reader = new FileReader(path.getFile());
        PemObject pem = new PemReader(reader).readPemObject();
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

}
