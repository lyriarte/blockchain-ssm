package org.civis.blockchain.ssm.client.domain;

import org.assertj.core.api.Assertions;
import org.civis.blockchain.ssm.client.SsmClientItTest;
import org.civis.blockchain.ssm.client.crypto.KeyPairReader;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HasPrivateMessageTest {

    @Test
    void shouldEncryptMessage() throws Exception {
        String sessionName = "deal20181201-" + UUID.randomUUID().toString();
        Context context = new Context(sessionName, "100 dollars 1978 Camaro", 0);
        Agent agent = Agent.loadFromFile(SsmClientItTest.USER1_NAME, SsmClientItTest.USER1_FILENAME);
        context.addPrivateMessage("Value to encrypt", agent);
        String val = context.getPrivate().get(agent.getName());
        Assertions.assertThat(val).isNotEmpty().isNotEqualTo("Value to encrypt");
    }

    @Test
    void shouldDecryptMessage() throws Exception {
        String sessionName = "deal20181201-" + UUID.randomUUID().toString();
        Context context = new Context(sessionName, "100 dollars 1978 Camaro", 0);
        Agent agent = Agent.loadFromFile(SsmClientItTest.USER1_NAME, SsmClientItTest.USER1_FILENAME);
        context.addPrivateMessage("Value to encrypt", agent);

        PrivateKey privKey = KeyPairReader.loadPrivateKey(SsmClientItTest.USER1_FILENAME);
        String val = context.getPrivateMessage(SsmClientItTest.USER1_NAME, privKey);
        Assertions.assertThat(val).isNotEmpty().isEqualTo("Value to encrypt");
    }

    @Test
    void shouldDecryptMessageWithSigner() throws Exception {
        String sessionName = "deal20181201-" + UUID.randomUUID().toString();
        Context context = new Context(sessionName, "100 dollars 1978 Camaro", 0);
        Agent agent = Agent.loadFromFile(SsmClientItTest.USER1_NAME, SsmClientItTest.USER1_FILENAME);
        context.addPrivateMessage("Value to encrypt", agent);
        Signer signerUser1 = Signer.loadFromFile(SsmClientItTest.USER1_NAME, SsmClientItTest.USER1_FILENAME);
        String val = context.getPrivateMessage(signerUser1);
        Assertions.assertThat(val).isNotEmpty().isEqualTo("Value to encrypt");
    }
}