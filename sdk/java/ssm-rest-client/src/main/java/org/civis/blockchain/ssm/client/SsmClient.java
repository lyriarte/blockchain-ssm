package org.civis.blockchain.ssm.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.civis.blockchain.ssm.client.Utils.JsonUtils;
import org.civis.blockchain.ssm.client.command.*;
import org.civis.blockchain.ssm.client.domain.*;
import org.civis.blockchain.ssm.client.fabric.CoopRepository;
import org.civis.blockchain.ssm.client.fabric.RepositoryFactory;
import okhttp3.ResponseBody;
import org.civis.blockchain.ssm.client.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

public class SsmClient {

    private final Logger logger = LoggerFactory.getLogger(SsmClient.class);

    public static SsmClient fromConfigFile(String filename) throws IOException {
        SsmClientConfig config = SsmClientConfig.fromConfigFile(filename);
        RepositoryFactory factory = new RepositoryFactory(config.getBaseUrl());

        return new SsmClient(factory.buildCoopRepository());
    }

    private final CoopRepository coopRepository;

    public SsmClient(CoopRepository coopRepository) {
        this.coopRepository = coopRepository;
    }

    public CompletableFuture<String> registerUser(Signer signer, Agent agent) throws Exception {
        RegisterCommand cmd = new RegisterCommand(signer, agent);
        return invoke(cmd);
    }

    public CompletableFuture<String> create(Signer signer, Ssm ssm) throws Exception {
        CreateCommand cmd = new CreateCommand(signer, ssm);
        return invoke(cmd);
    }

    public CompletableFuture<String> start(Signer signer, Session session) throws Exception {
        StartCommand cmd = new StartCommand(signer, session);
        return invoke(cmd);
    }

    public CompletableFuture<String> perform(Signer signer, String action, Context context) throws Exception {
        PerformCommand cmd = new PerformCommand(signer, action, context);
        return invoke(cmd);
    }

    public CompletableFuture<List<String>> listAdmins() throws Exception {
        AdminQuery query = new AdminQuery();
        ;
        return list(query, String.class);
    }

    public CompletableFuture<Agent> getAdmin(String username) throws Exception {
        AdminQuery query = new AdminQuery();
        return query(username, query, Agent.class);
    }

    public CompletableFuture<List<String>> listAgent() throws Exception {
        AgentQuery query = new AgentQuery();
        return list(query, String.class);
    }

    public CompletableFuture<Agent> getAgent(String agentName) throws Exception {
        AgentQuery query = new AgentQuery();
        return query(agentName, query, Agent.class);
    }

    public CompletableFuture<List<String>> listSsm() throws Exception {
        SsmQuery query = new SsmQuery();
        return list(query, String.class);
    }

    public CompletableFuture<Ssm> getSsm(String name) throws Exception {
        SsmQuery query = new SsmQuery();
        return query(name, query, Ssm.class);
    }

    public CompletableFuture<SessionState> getSession(String sessionId) throws Exception {
        SessionQuery query = new SessionQuery();
        return query(sessionId, query, SessionState.class);
    }

    public CompletableFuture<List<String>> listSession() throws Exception {
        SessionQuery query = new SessionQuery();
        return list(query, String.class);
    }

    private CompletableFuture<String> invoke(Command cmd) throws Exception {
        InvokeArgs invokeArgs = cmd.invoke();
        logger.info("Send to the blockchain command[{}] with args:{}", cmd.getCommandName(), invokeArgs);
        return coopRepository.command("invoke",invokeArgs.getFcn(), invokeArgs.getArgs()).thenApply(toStringObject());
    }

    private <T> CompletableFuture<T> query(String value, HasGet query, Class<T> clazz) throws Exception {
        InvokeArgs args = query.queryArgs(value);
        return query(clazz, args);
    }

    private <T> CompletableFuture<List<T>> list(HasList query, Class<T> agentClass) {
        InvokeArgs args = query.listArgs();
        return query(new TypeReference<List<T>>(){}, args);
    }

    private <T> CompletableFuture<T> query(TypeReference<T> clazz, InvokeArgs args) {
        CompletableFuture<ResponseBody> request = coopRepository.command("query", args.getFcn(), args.getArgs());
        return request.thenApply(toCompetableObject(clazz));
    }

    private <T> CompletableFuture<T> query(Class<T> clazz, InvokeArgs args) {
        CompletableFuture<ResponseBody> request = coopRepository.command("query", args.getFcn(), args.getArgs());
        return request.thenApply(toCompetableObject(clazz));
    }


    public static <T> Function<ResponseBody, T> toCompetableObject(TypeReference<T> clazz) {
        return value -> {
            try {
                return JsonUtils.toObject(value.string(), clazz);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        };
    }

    public static <T> Function<ResponseBody, T> toCompetableObject(Class<T> clazz) {
        return value -> {
            try {
                return JsonUtils.toObject(value.string(), clazz);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        };
    }

    public static Function<ResponseBody, String> toStringObject() {
        return value -> {
            try {
                return value.string();
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        };
    }
}
