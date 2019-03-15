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

    private static Signer signerAdrien;
    private static Signer signerChuck;
    private static Signer signerSarah;
    private static SsmClient client;
    private static String ssmName;
    private static String sessionName;
    private static Session session;

    @BeforeAll
    public static void init() throws Exception {
        signerAdrien = Signer.loadFromFile("adrien");
        signerChuck = Signer.loadFromFile("chuck");
        signerSarah = Signer.loadFromFile("sarah");
        client = SsmClient.fromConfigFile("ssm-client.properties");
        String uuid = UUID.randomUUID().toString();
        ssmName = "CarDealership-" + uuid;
        Map<String, String> roles = ImmutableMap.of("chuck", "Buyer", "sarah", "Seller");
        sessionName = "deal20181201-" + uuid;
        session = new Session(ssmName, sessionName, "Used car for 100 dollars.", roles);
    }

    @Test
    @Order(5)
    public void getListAdmin() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listAdmins();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains("adrien");
    }

    @Test
    @Order(6)
    public void listAgent() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listAgent();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains("chuck", "sarah");
    }

    @Test
    @Order(7)
    public void listSsm() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listSsm();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains("Car dealership", "Loop", "Negociation");
    }

    @Test
    @Order(8)
    public void listSession() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listSession();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains("bobloops", "carsale20190301", "deal20181201", "tomloops");
    }

    @Test
    @Order(10)
    public void getAdminUser() throws Exception {
        Agent agent = Agent.loadFromFile("adrien");
        CompletableFuture<Agent> agentRet = client.getAdmin("adrien");
        Agent agentFormClient = agentRet.get();
        assertThat(agentFormClient).isEqualTo(agent);
    }

    @Test
    @Order(20)
    public void registerChuck() throws Exception {
        Agent agent = Agent.loadFromFile("chuck");
        CompletableFuture<String> transactionEvent = client.registerUser(signerAdrien, agent);
        String trans = transactionEvent.get();

        assertThat(trans).isEqualTo("4852f7a3db59d6b1cf157c8a295162bb17c19ff6e1b8915c7f98efcb052ed878");

    }

    @Test
    @Order(30)
    public void getAgentChuck() throws Exception {
        Agent agent = Agent.loadFromFile("chuck");
        CompletableFuture<Agent> agentRet = client.getAgent("chuck");
        Agent agentFormClient = agentRet.get();
        assertThat(agentFormClient).isEqualTo(agent);
    }

    @Test
    @Order(40)
    public void registerSarah() throws Exception {
        Agent agent = Agent.loadFromFile("sarah");
        CompletableFuture<String> transactionEvent = client.registerUser(signerAdrien, agent);
        String trans = transactionEvent.get();
        assertThat(trans).isNotNull();
    }

    @Test
    @Order(50)
    public void getAgentSarah() throws Exception {
        Agent agent = Agent.loadFromFile("sarah");
        CompletableFuture<Agent> agentRet = client.getAgent("sarah");
        Agent agentFormClient = agentRet.get();
        assertThat(agentFormClient).isEqualTo(agent);
    }


    @Test
    @Order(60)
    public void createSsm() throws Exception {
        Ssm.Transition sell = new Ssm.Transition(0, 1, "Seller", "Sell");
        Ssm.Transition buy = new Ssm.Transition(1, 2, "Buyer", "Buy");
        Ssm ssm = new Ssm(ssmName, Lists.newArrayList(sell, buy));

        CompletableFuture<String> transactionEvent = client.create(signerAdrien, ssm);
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
        Map<String, String> roles = ImmutableMap.of("chuck", "Buyer", "sarah", "Seller");
        Session session = new Session(ssmName, sessionName, "Used car for 100 dollars.", roles);

        CompletableFuture<String> transactionEvent = client.start(signerAdrien, session);
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
        CompletableFuture<String> transactionEvent = client.perform(signerSarah, "Sell", context);
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
        CompletableFuture<String> transactionEvent = client.perform(signerChuck, "Buy", context);
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

}
