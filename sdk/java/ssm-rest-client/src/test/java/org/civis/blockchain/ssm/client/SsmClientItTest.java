package org.civis.blockchain.ssm.client;


import com.google.common.collect.ImmutableMap;
import org.assertj.core.util.Lists;
import org.civis.blockchain.ssm.client.domain.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SsmClientItTest {

    private static final String NETWORK = "bclan-it/";

    private static String uuid;

    private static Signer signerAdam;
    private static Signer signerBob;
    private static Signer signerSam;

    private static Agent agentAdam;
    private static Agent agentBob;
    private static Agent agentSam;


    private static SsmClient client;
    private static String ssmName;
    private static String sessionName;
    private static Session session;

    @BeforeAll
    public static void init() throws Exception {
        signerAdam = Signer.loadFromFile("adam", NETWORK+"adam");
        signerBob = Signer.loadFromFile("bob",NETWORK+"bob");
        signerSam = Signer.loadFromFile("sam", NETWORK+"sam");

        uuid = UUID.randomUUID().toString();

        agentAdam = Agent.loadFromFile("adam", NETWORK+"adam");
        agentBob = Agent.loadFromFile("bob-"+uuid, NETWORK+"bob");
        agentSam = Agent.loadFromFile("sam-"+uuid, NETWORK+"sam");


        client = SsmClient.fromConfigFile("ssm-client.properties");

        ssmName = "CarDealership-" + uuid;
        Map<String, String> roles = ImmutableMap.of("bob", "Buyer", "sam", "Seller");
        sessionName = "deal20181201-" + uuid;
        session = new Session(ssmName, sessionName, "Used car for 100 dollars.", roles);
    }

    @Test
    @Order(5)
    public void getListAdmin() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listAdmins();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains("adam");
    }


    @Test
    @Order(10)
    public void getAdminUser() throws Exception {
        CompletableFuture<Agent> agentRet = client.getAdmin("adam");
        Agent agentFormClient = agentRet.get();
        assertThat(agentFormClient).isEqualTo(agentAdam);
    }

    @Test
    @Order(20)
    public void registerBob() throws Exception {
        CompletableFuture<String> transactionEvent = client.registerUser(signerAdam, agentBob);
        String trans = transactionEvent.get();

        assertThat(trans).isNotNull();

    }

    @Test
    @Order(30)
    public void getAgentBob() throws Exception {
        CompletableFuture<Agent> agentRet = client.getAgent(agentBob.getName());
        Agent agentFormClient = agentRet.get();
        assertThat(agentFormClient).isEqualTo(agentBob);
    }

    @Test
    @Order(40)
    public void registerSam() throws Exception {
        CompletableFuture<String> transactionEvent = client.registerUser(signerAdam, agentSam);
        String trans = transactionEvent.get();
        assertThat(trans).isNotNull();
    }

    @Test
    @Order(50)
    public void getAgentSam() throws Exception {
        CompletableFuture<Agent> agentRet = client.getAgent(agentSam.getName());
        Agent agentFormClient = agentRet.get();
        assertThat(agentFormClient).isEqualTo(agentSam);
    }

    @Test
    @Order(55)
    public void listAgent() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listAgent();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains(agentBob.getName(), agentSam.getName());
    }

    @Test
    @Order(60)
    public void createSsm() throws Exception {
        Ssm.Transition sell = new Ssm.Transition(0, 1, "Seller", "Sell");
        Ssm.Transition buy = new Ssm.Transition(1, 2, "Buyer", "Buy");
        Ssm ssm = new Ssm(ssmName, Lists.newArrayList(sell, buy));

        CompletableFuture<String> transactionEvent = client.create(signerAdam, ssm);
        String trans = transactionEvent.get();

        assertThat(trans).isNotNull();
    }

    @Test
    @Order(70)
    public void getSsm() throws Exception {
        CompletableFuture<Ssm> ssmReq = client.getSsm(ssmName);
        Ssm ssm = ssmReq.get();
        assertThat(ssm).isNotNull();
        assertThat(ssm.getName()).isEqualTo(ssmName);
    }

    @Test
    @Order(80)
    public void start() throws Exception {
        Map<String, String> roles = ImmutableMap.of("bob", "Buyer", "sam", "Seller");
        Session session = new Session(ssmName, sessionName, "Used car for 100 dollars.", roles);

        CompletableFuture<String> transactionEvent = client.start(signerAdam, session);
        String trans = transactionEvent.get();

        assertThat(trans).isNotNull();
    }

    @Test
    @Order(90)
    public void getSession() throws Exception {


        CompletableFuture<SessionState> ses = client.getSession(sessionName);
        SessionState sesReq = ses.get();

        assertThat(sesReq.getCurrent()).isEqualTo(0);
        assertThat(sesReq.getIteration()).isEqualTo(0);
        assertThat(sesReq.getOrigin()).isNull();

        assertThat(sesReq.getSsm()).isEqualTo(ssmName);
        assertThat(sesReq.getRoles()).isEqualTo(session.getRoles());
        assertThat(sesReq.getSession()).isEqualTo(session.getSession());
        assertThat(sesReq.getPublicMessage()).isEqualTo(session.getPublicMessage());

    }

    @Test
    @Order(100)
    public void performSell() throws Exception {
        Context context = new Context(sessionName, "100 dollars 1978 Camaro", 0);
        CompletableFuture<String> transactionEvent = client.perform(signerSam, "Sell", context);
        String trans = transactionEvent.get();
        assertThat(trans).isNotNull();
    }

    @Test
    @Order(110)
    public void getSessionAfterSell() throws Exception {
        Ssm.Transition sell = new Ssm.Transition(0, 1, "Seller", "Sell");
        CompletableFuture<SessionState> sesReq = client.getSession(sessionName);
        SessionState state = sesReq.get();
        SessionState stateExcpected = new SessionState(ssmName, sessionName, "100 dollars 1978 Camaro", session.getRoles(), sell, 1, 1);
        assertThat(state).isEqualTo(stateExcpected);

    }

    @Test
    @Order(120)
    public void performBuy() throws Exception {
        Context context = new Context(sessionName, "Deal !", 1);
        CompletableFuture<String> transactionEvent = client.perform(signerBob, "Buy", context);
        String trans = transactionEvent.get();
        assertThat(trans).isNotNull();
    }

    @Test
    @Order(130)
    public void getSessionAfterBuy() throws Exception {
        Ssm.Transition buy = new Ssm.Transition(1, 2, "Buyer", "Buy");
        CompletableFuture<SessionState> sesReq = client.getSession(sessionName);
        SessionState state = sesReq.get();
        SessionState stateExcpected = new SessionState(ssmName, sessionName, "Deal !", session.getRoles(), buy, 2, 2);
        assertThat(state).isEqualTo(stateExcpected);
    }

    @Test
    @Order(140)
    public void listSsm() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listSsm();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains(ssmName);
    }

    @Test
    @Order(150)
    public void listSession() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listSession();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains(sessionName);
    }


}
