package org.civis.blockchain.ssm.client.command;

import com.google.common.collect.ImmutableMap;
import org.civis.blockchain.ssm.client.crypto.Sha256RSASigner;
import org.civis.blockchain.ssm.client.domain.Session;
import org.civis.blockchain.ssm.client.domain.Signer;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StartCommandTest {

    @Test
    public void test_execute() throws Exception {

        //    "ssm":"Car dealership",
        //    "session":"deal20181201",
        //    "public":"Used car for 100 dollars.",
        //    "roles":{
        //      "chuck":"Buyer",
        //       "sarah":"Seller"
        //    }
        Map<String, String> roles = ImmutableMap.of("chuck", "Buyer", "sarah","Seller");
        Signer signer = Signer.loadFromFile("adam", "command/adam");
        Session session = new Session("Car dealership", "deal20181201", "Used car for 100 dollars.", roles);

        InvokeArgs invokeArgs = new StartCommand(signer, session).invoke();
        invokeArgs.getArgs().forEach(System.out::println);

        String expectedJson = "{\"ssm\":\"Car dealership\",\"session\":\"deal20181201\",\"public\":\"Used car for 100 dollars.\",\"roles\":{\"chuck\":\"Buyer\",\"sarah\":\"Seller\"}}";
        assertThat(invokeArgs.getFcn()).isEqualTo("start");
        assertThat(invokeArgs.getArgs())
                .isNotEmpty()
                .containsExactly(
                        expectedJson,
                        "adam",
                        Sha256RSASigner.rsaSignAsB64(expectedJson, signer.getPair().getPrivate())
                );

    }

}