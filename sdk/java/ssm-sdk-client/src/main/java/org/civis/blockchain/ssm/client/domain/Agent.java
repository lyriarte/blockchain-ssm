package org.civis.blockchain.ssm.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.civis.blockchain.ssm.client.crypto.KeyPairReader;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class Agent {

    public static Agent loadFromFile(String name) throws Exception {
        PublicKey pub = KeyPairReader.loadPublicKey(name);
        return new Agent(name, pub.getEncoded());
    }

    public static Agent loadFromFile(String name, String filename) throws Exception {
        PublicKey pub = KeyPairReader.loadPublicKey(filename);
        return new Agent(name, pub.getEncoded());
    }

    private String name;
    private byte[] pub;

    @JsonCreator
    public Agent(@JsonProperty("name") String name, @JsonProperty("pub") byte[] pub) {
        this.name = name;
        this.pub = pub;
    }

    public String getName() {
        return name;
    }

    public byte[] getPub() {
        return pub;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(name, agent.name) &&
                Arrays.equals(pub, agent.pub);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(pub);
        return result;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "name='" + name + '\'' +
                ", pub=" + Base64.getEncoder().encodeToString(pub) +
                '}';
    }
}
