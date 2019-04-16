package org.civis.blockchain.ssm.client.domain;

import java.security.KeyPair;

public class SignerAdmin extends Signer {

    public SignerAdmin(Signer signer) {
        this(signer.getName(), signer.getPair());
    }

    public SignerAdmin(String name, KeyPair pair) {
        super(name, pair);
    }
}
