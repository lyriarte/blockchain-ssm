package org.civis.blockchain.ssm.client.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Strings;
import org.bouncycastle.crypto.CryptoException;
import org.civis.blockchain.ssm.client.crypto.RSACipher;

import java.beans.Transient;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public interface HasPrivateMessage<T> {

    Map<String, String> getPrivate();
    @JsonInclude(NON_NULL)
    T setPrivate(Map<String, String> privateMessage);

    @Transient
    default void addPrivateMessage(String val, Agent agent) throws CryptoException {
        addPrivateMessage(val, agent.getName(), agent.getPubAsKey());
    }

    @Transient
    default void addPrivateMessage(String val, String name, PublicKey publicKey) throws CryptoException {
        if (getPrivate() == null) {
            setPrivate(new HashMap<>());
        }
        String encrypted = RSACipher.encrypt(val.getBytes(), publicKey);
        getPrivate().put(name, encrypted);
    }

    @Transient
    default String getPrivateMessage(String name, PrivateKey privateKey) throws CryptoException {
        if (getPrivate() == null) {
            return null;
        }
        String value = getPrivate().get(name);
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return RSACipher.decrypt(value, privateKey);
    }

    @Transient
    default String getPrivateMessage(Signer signer) throws CryptoException {
        return getPrivateMessage(signer.getName(), signer.getPair().getPrivate());
    }
}
