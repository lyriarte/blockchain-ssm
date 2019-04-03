package org.civis.blockchain.ssm.client;


import org.civis.blockchain.ssm.client.domain.*;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

public class SsmClientOptionalTest {

    private static SsmClient client;

    @BeforeAll
    public static void init() throws Exception {
        client = SsmClient.fromConfigFile("ssm-client.properties");
    }

    @Test
    public void getAdminUser() throws Exception {
        CompletableFuture<Optional<Agent>> agentRet = client.getAdmin(UUID.randomUUID().toString());
        Optional<Agent> agentFormClient = agentRet.get();
        assertThat(agentFormClient).isEmpty();
    }

    @Test
    public void getAgentUser2() throws Exception {
        CompletableFuture<Optional<Agent>> agentRet = client.getAgent(UUID.randomUUID().toString());
        Optional<Agent> agentFormClient = agentRet.get();
        assertThat(agentFormClient).isEmpty();
    }

    @Test
    public void getSsm() throws Exception {
        CompletableFuture<Optional<Ssm>> ssmReq = client.getSsm(UUID.randomUUID().toString());
        Optional<Ssm> ssm = ssmReq.get();
        assertThat(ssm).isEmpty();
    }

    @Test
    public void getSession() throws Exception {
        CompletableFuture<Optional<SessionState>> ses = client.getSession(UUID.randomUUID().toString());
        Optional<SessionState> sesReq = ses.get();
        assertThat(sesReq).isEmpty();
    }

}