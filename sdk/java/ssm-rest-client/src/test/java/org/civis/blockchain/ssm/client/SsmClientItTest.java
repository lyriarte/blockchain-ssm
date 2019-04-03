package org.civis.blockchain.ssm.client;


import com.google.common.collect.ImmutableMap;
import org.assertj.core.util.Lists;
import org.civis.blockchain.ssm.client.domain.*;
import org.civis.blockchain.ssm.client.repository.InvokeReturn;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SsmClientItTest {

    private static String uuid = UUID.randomUUID().toString();
    private static final String NETWORK = "bclan-it/";
    private static final String ADMIN_NAME = "adam";
    private static final String USER1_NAME = "bob"+"-"+uuid;
    private static final String USER2_NAME = "sam"+"-"+uuid;
    private static final String USER1_FILENAME = NETWORK+"bob";
    private static final String USER2_FILENAME = NETWORK+"sam";

//    private static final String NETWORK = "bc1/";
//    private static final String ADMIN_NAME = "adrien";
//    private static final String USER1_NAME = "chuck";
//    private static final String USER2_NAME = "sarah";
//    private static final String USER1_FILENAME = NETWORK+"chuck";
//    private static final String USER2_FILENAME = NETWORK+"sarah";

    private static Signer signerAdmin;
    private static Signer signerUser1;
    private static Signer signerUser2;

    private static Agent agentAdmin;
    private static Agent agentUser1;
    private static Agent agentUser2;

    private static SsmClient client;
    private static String ssmName;
    private static String sessionName;
    private static Session session;

    @BeforeAll
    public static void init() throws Exception {
        signerAdmin = Signer.loadFromFile(ADMIN_NAME, NETWORK+ADMIN_NAME);
        signerUser1 = Signer.loadFromFile(USER1_NAME, USER1_FILENAME);
        signerUser2 = Signer.loadFromFile(USER2_NAME, USER2_FILENAME);

        agentAdmin = Agent.loadFromFile(ADMIN_NAME, NETWORK+ADMIN_NAME);
        agentUser1 = Agent.loadFromFile(signerUser1.getName(), USER1_FILENAME);
        agentUser2 = Agent.loadFromFile(signerUser2.getName(), USER2_FILENAME);

        client = SsmClient.fromConfigFile("ssm-client.properties");

        ssmName = "CarDealership-" + uuid;
        Map<String, String> roles = ImmutableMap.of(signerUser1.getName(), "Buyer", signerUser2.getName(), "Seller");
        sessionName = "deal20181201-" + uuid;
        session = new Session(ssmName, sessionName, "Used car for 100 dollars.", roles);
    }

    @AfterEach
    public void waitBetweenTest() throws InterruptedException {
        //Node rest api return http response before the transaction had been mined
        Thread.sleep(2000);
    }

    @Test
    @Order(5)
    public void getListAdmin() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listAdmins();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains(ADMIN_NAME);
    }


    @Test
    @Order(10)
    public void getAdminUser() throws Exception {
        CompletableFuture<Optional<Agent>> agentRet = client.getAdmin(ADMIN_NAME);
        Optional<Agent> agentFormClient = agentRet.get();
        assertThat(agentFormClient.get()).isEqualTo(agentAdmin);
    }

    @Test
    @Order(20)
    public void registerUser1() throws Exception {
        CompletableFuture<InvokeReturn> transactionEvent = client.registerUser(signerAdmin, agentUser1);
        InvokeReturn trans = transactionEvent.get();

        assertThat(trans).isNotNull();
        assertThat(trans.getStatus()).isEqualTo("SUCCESS");
//        assertThat(trans.getTransactionId()).isNotNull();

    }

    @Test
    @Order(30)
    public void getAgentUser1() throws Exception {
        CompletableFuture<Optional<Agent>> agentRet = client.getAgent(agentUser1.getName());
        Optional<Agent> agentFormClient = agentRet.get();
        assertThat(agentFormClient.get()).isEqualTo(agentUser1);
    }

    @Test
    @Order(40)
    public void registerUser2() throws Exception {
        CompletableFuture<InvokeReturn> transactionEvent = client.registerUser(signerAdmin, agentUser2);
        InvokeReturn trans = transactionEvent.get();
        assertThat(trans).isNotNull();
        assertThat(trans.getStatus()).isEqualTo("SUCCESS");
//        assertThat(trans.getTransactionId()).isNotNull();
    }

    @Test
    @Order(50)
    public void getAgentUser2() throws Exception {
        CompletableFuture<Optional<Agent>> agentRet = client.getAgent(agentUser2.getName());
        Optional<Agent> agentFormClient = agentRet.get();
        assertThat(agentFormClient.get()).isEqualTo(agentUser2);
    }

    @Test
    @Order(55)
    public void listAgent() throws Exception {
        CompletableFuture<List<String>> agentRet = client.listAgent();
        List<String> agentFormClient = agentRet.get();
        assertThat(agentFormClient).contains(agentUser1.getName(), agentUser2.getName());
    }

    @Test
    @Order(60)
    public void createSsm() throws Exception {
        Ssm.Transition sell = new Ssm.Transition(0, 1, "Seller", "Sell");
        Ssm.Transition buy = new Ssm.Transition(1, 2, "Buyer", "Buy");
        Ssm ssm = new Ssm(ssmName, Lists.newArrayList(sell, buy));

        CompletableFuture<InvokeReturn> transactionEvent = client.create(signerAdmin, ssm);
        InvokeReturn trans = transactionEvent.get();

        assertThat(trans).isNotNull();
        assertThat(trans.getStatus()).isEqualTo("SUCCESS");
//        assertThat(trans.getTransactionId()).isNotNull();
    }

    @Test
    @Order(70)
    public void getSsm() throws Exception {
        CompletableFuture<Optional<Ssm>> ssmReq = client.getSsm(ssmName);
        Optional<Ssm> ssm = ssmReq.get();
        assertThat(ssm).isPresent();
        assertThat(ssm.get().getName()).isEqualTo(ssmName);
    }

    @Test
    @Order(80)
    public void start() throws Exception {
        Map<String, String> roles = ImmutableMap.of(agentUser1.getName(), "Buyer", agentUser2.getName(), "Seller");
        Session session = new Session(ssmName, sessionName, "Used car for 100 dollars.", roles);

        CompletableFuture<InvokeReturn> transactionEvent = client.start(signerAdmin, session);
        InvokeReturn trans = transactionEvent.get();

        assertThat(trans).isNotNull();
        assertThat(trans.getStatus()).isEqualTo("SUCCESS");
//        assertThat(trans.getTransactionId()).isNotNull();
    }

    @Test
    @Order(90)
    public void getSession() throws Exception {
        CompletableFuture<Optional<SessionState>> ses = client.getSession(sessionName);
        Optional<SessionState> sesReq = ses.get();

        assertThat(sesReq.get().getCurrent()).isEqualTo(0);
        assertThat(sesReq.get().getIteration()).isEqualTo(0);
        assertThat(sesReq.get().getOrigin()).isNull();

        assertThat(sesReq.get().getSsm()).isEqualTo(ssmName);
        assertThat(sesReq.get().getRoles()).isEqualTo(session.getRoles());
        assertThat(sesReq.get().getSession()).isEqualTo(session.getSession());
        assertThat(sesReq.get().getPublicMessage()).isEqualTo(session.getPublicMessage());

    }

    @Test
    @Order(100)
    public void performSell() throws Exception {
        Context context = new Context(sessionName, "100 dollars 1978 Camaro", 0);
        CompletableFuture<InvokeReturn> transactionEvent = client.perform(signerUser2, "Sell", context);
        InvokeReturn trans = transactionEvent.get();
        assertThat(trans).isNotNull();
        assertThat(trans.getStatus()).isEqualTo("SUCCESS");
//        assertThat(trans.getTransactionId()).isNotNull();
    }

    @Test
    @Order(110)
    public void getSessionAfterSell() throws Exception {
        Ssm.Transition sell = new Ssm.Transition(0, 1, "Seller", "Sell");
        CompletableFuture<Optional<SessionState>> sesReq = client.getSession(sessionName);
        Optional<SessionState> state = sesReq.get();
        SessionState stateExcpected = new SessionState(ssmName, sessionName, "100 dollars 1978 Camaro", session.getRoles(), sell, 1, 1);
        assertThat(state.get()).isEqualTo(stateExcpected);

    }

    @Test
    @Order(120)
    public void performBuy() throws Exception {
        Context context = new Context(sessionName, "Deal !", 1);
        CompletableFuture<InvokeReturn> transactionEvent = client.perform(signerUser1, "Buy", context);
        InvokeReturn trans = transactionEvent.get();
        assertThat(trans).isNotNull();
        assertThat(trans.getStatus()).isEqualTo("SUCCESS");
    }

    @Test
    @Order(130)
    public void getSessionAfterBuy() throws Exception {
        Ssm.Transition buy = new Ssm.Transition(1, 2, "Buyer", "Buy");
        CompletableFuture<Optional<SessionState>> sesReq = client.getSession(sessionName);
        Optional<SessionState> state = sesReq.get();
        SessionState stateExcpected = new SessionState(ssmName, sessionName, "Deal !", session.getRoles(), buy, 2, 2);
        assertThat(state.get()).isEqualTo(stateExcpected);
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
