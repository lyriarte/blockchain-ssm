package org.civis.blockchain.ssm.client.command;

import org.civis.blockchain.ssm.client.domain.Context;
import org.civis.blockchain.ssm.client.domain.Signer;
import org.civis.blockchain.ssm.client.crypto.KeyPairReader;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.assertj.core.api.Assertions.assertThat;

public class PerformCommandTest {


    @Test
    public void test_execute() throws Exception {

        KeyPair adamPair = KeyPairReader.loadKeyPair("command/adam");

        Signer signer = new Signer("adam", adamPair);

//        "{\"session\":\"deal20181201\",\"public\":\"100 dollars 1978 Camaro\",\"iteration\":0}"
        Context context = new Context("deal20181201", "100 dollars 1978 Camaro", 0);

        InvokeArgs invokeArgs = new PerformCommand(signer, "Sell", context).invoke();
        invokeArgs.getArgs().forEach(System.out::println);

        assertThat(invokeArgs.getFcn()).isEqualTo("perform");
        assertThat(invokeArgs.getArgs())
                .isNotEmpty()
                .containsExactly(
                        "Sell",
                        "{\"session\":\"deal20181201\",\"public\":\"100 dollars 1978 Camaro\",\"iteration\":0}",
                        "adam",
                        "GOqqfALK7M682IFEBU66GG8aIevDc5mGa3W/o7UXhsvweliz/1XWxWdNOfcEvMJ9DnfhrX1Bupyo956EliiFmutcbi0Hvp+IuzgjbcKUtY2jAGKWA8QLmBeN2DGnYMgor4aJiOZKS+NnA3coNz/usHalIAzqZlihq41ZANpr1Uqjc728CNXUOVuIeRJ0oWRo8X18SO0o+VRB+/0+BzGexu6In/zcoaEjDlMBtB29U0hFgB1DX28Ek0snuZYMsacL76MpKXZjr8v2Sc1oQ63PSUSrjSmCzni0lZ28AT4VJoui2LU2FKLsCqKLKyi+pIP82a+9ahdmwIQ5r0X4dpGCIQ=="
                );

    }

}