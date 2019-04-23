package org.civis.blockchain.ssm.client.domain;

import org.civis.blockchain.ssm.client.crypto.KeyPairReader;

import java.security.KeyPair;
import java.util.Objects;

public class Signer {
    private final String name;
    private final KeyPair pair;

    public static Signer loadFromFile(String filename) throws Exception {
        KeyPair keypair = KeyPairReader.loadKeyPair(filename);
        return new Signer(filename, keypair);
    }

    public static Signer loadFromFile(String name, String filename) throws Exception {
        KeyPair keypair = KeyPairReader.loadKeyPair(filename);
        return new Signer(name, keypair);
    }

    public Signer(String name, KeyPair pair) {
        this.name = name;
        this.pair = pair;
    }

    public String getName() {
        return name;
    }

    public KeyPair getPair() {
        return pair;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signer signer = (Signer) o;
        return name.equals(signer.name) &&
                pair.equals(signer.pair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pair);
    }
}
