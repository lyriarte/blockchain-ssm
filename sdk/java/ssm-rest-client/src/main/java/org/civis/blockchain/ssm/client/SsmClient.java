package org.civis.blockchain.ssm.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import org.civis.blockchain.ssm.client.Utils.JsonUtils;
import org.civis.blockchain.ssm.client.command.*;
import org.civis.blockchain.ssm.client.domain.*;
import org.civis.blockchain.ssm.client.repository.CoopRepository;
import org.civis.blockchain.ssm.client.repository.InvokeReturn;
import org.civis.blockchain.ssm.client.repository.RepositoryFactory;
import okhttp3.ResponseBody;
import org.civis.blockchain.ssm.client.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

    public static SsmClient fromConfig(SsmClientConfig config) throws IOException {
        RepositoryFactory factory = new RepositoryFactory(config.getBaseUrl());
        return new SsmClient(factory.buildCoopRepository());
    }

    private final CoopRepository coopRepository;

    public SsmClient(CoopRepository coopRepository) {
        this.coopRepository = coopRepository;
    }

    public CompletableFuture<InvokeReturn> registerUser(Signer signer, Agent agent) throws Exception {
        RegisterCommand cmd = new RegisterCommand(signer, agent);
        return invoke(cmd);
    }

    public CompletableFuture<InvokeReturn> create(Signer signer, Ssm ssm) throws Exception {
        CreateCommand cmd = new CreateCommand(signer, ssm);
        return invoke(cmd);
    }

    public CompletableFuture<InvokeReturn> start(Signer signer, Session session) throws Exception {
        StartCommand cmd = new StartCommand(signer, session);
        return invoke(cmd);
    }

    public CompletableFuture<InvokeReturn> perform(Signer signer, String action, Context context) throws Exception {
        PerformCommand cmd = new PerformCommand(signer, action, context);
        return invoke(cmd);
    }

    public CompletableFuture<List<String>> listAdmins() {
        AdminQuery query = new AdminQuery();
        ;
        return list(query, String.class);
    }

    public CompletableFuture<Optional<Agent>> getAdmin(String username) {
        AdminQuery query = new AdminQuery();
        return query(username, query, Agent.class);
    }

    public CompletableFuture<List<String>> listAgent() {
        AgentQuery query = new AgentQuery();
        return list(query, String.class);
    }

    public CompletableFuture<Optional<Agent>> getAgent(String agentName)  {
        AgentQuery query = new AgentQuery();
        return query(agentName, query, Agent.class);
    }

    public CompletableFuture<List<String>> listSsm() {
        SsmQuery query = new SsmQuery();
        return list(query, String.class);
    }

    public CompletableFuture<Optional<Ssm>> getSsm(String name) {
        SsmQuery query = new SsmQuery();
        return query(name, query, Ssm.class);
    }

    public CompletableFuture<Optional<SessionState>> getSession(String sessionId) {
        SessionQuery query = new SessionQuery();
        return query(sessionId, query, SessionState.class);
    }

    public CompletableFuture<List<String>> listSession() {
        SessionQuery query = new SessionQuery();
        return list(query, String.class);
    }

    private <T> CompletableFuture<Optional<T>> query(String value, HasGet query, Class<T> clazz) {
        InvokeArgs args = query.queryArgs(value);
        return query(clazz, args);
    }

    private <T> CompletableFuture<List<T>> list(HasList query, Class<T> clazz) {
        InvokeArgs args = query.listArgs();
        CompletableFuture<ResponseBody> request = coopRepository.command("query", args.getFcn(), args.getArgs());
        return request.thenApply(toCompetableObjects(clazz));
    }

    private <T> CompletableFuture<Optional<T>> query(Class<T> clazz, InvokeArgs args) {
        CompletableFuture<ResponseBody> request = coopRepository.command("query", args.getFcn(), args.getArgs());
        return request.thenApply(toCompetableObject(clazz));
    }

    private CompletableFuture<InvokeReturn> invoke(Command cmd) throws Exception {
        InvokeArgs invokeArgs = cmd.invoke();
        logger.info("Send to the blockchain command[{}] with args:{}", cmd.getCommandName(), invokeArgs);
        return coopRepository.command("invoke",invokeArgs.getFcn(), invokeArgs.getArgs())
                .thenApply(toCompetableObject(InvokeReturn.class))
                .thenApply(opt -> opt.get());
    }

    public static <T> Function<ResponseBody, List<T>> toCompetableObjects(Class<T> clazz) {
        TypeReference<List<T>> type = new TypeReference<List<T>>(){};
        return value -> {
            try {
                return JsonUtils.toObject(value.string(), type);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        };
    }

    public static <T> Function<ResponseBody, Optional<T>> toCompetableObject(Class<T> clazz) {
        return value -> {
            try {
                String respnse = value.string();
                if(Strings.isNullOrEmpty(respnse)){
                    return Optional.empty();
                }
                return  Optional.of(JsonUtils.toObject(respnse, clazz));
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        };
    }

}
