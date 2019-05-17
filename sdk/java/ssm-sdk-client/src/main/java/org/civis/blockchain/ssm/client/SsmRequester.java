package org.civis.blockchain.ssm.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import okhttp3.ResponseBody;
import org.civis.blockchain.ssm.client.Utils.JsonUtils;
import org.civis.blockchain.ssm.client.command.Command;
import org.civis.blockchain.ssm.client.command.InvokeArgs;
import org.civis.blockchain.ssm.client.query.HasGet;
import org.civis.blockchain.ssm.client.query.HasList;
import org.civis.blockchain.ssm.client.repository.CoopRepository;
import org.civis.blockchain.ssm.client.repository.InvokeReturn;
import org.civis.blockchain.ssm.client.repository.CommandArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

public class SsmRequester {

    public static final String QUERY = "query";
    public static final String INVOKE = "invoke";
    private final Logger logger = LoggerFactory.getLogger(SsmClient.class);

    private final CoopRepository coopRepository;

    public SsmRequester(CoopRepository coopRepository) {
        this.coopRepository = coopRepository;
    }

    public <T> CompletableFuture<Optional<T>> query(String value, HasGet query, Class<T> clazz) {
        InvokeArgs args = query.queryArgs(value);
        CompletableFuture<ResponseBody> request = coopRepository.command(QUERY, args.getFcn(), args.getArgs());

        logger.info("List the blockchain fcn[{}] with args:{}", args.getFcn(), args.getArgs());
        return request.thenApply(toCompetableObject(clazz));
    }

    public <T> CompletableFuture<List<T>> list(HasList query, Class<T> clazz) {
        InvokeArgs args = query.listArgs();
        CompletableFuture<ResponseBody> request = coopRepository.command(QUERY, args.getFcn(), args.getArgs());

        logger.info("List the blockchain fcn[{}] with args:{}", args.getFcn(), args.getArgs());
        return request.thenApply(toCompetableObjects(clazz));
    }

    public CompletableFuture<InvokeReturn> invoke(Command cmd) throws Exception {
        InvokeArgs invokeArgs = cmd.invoke();
        logger.info("Invoke the blockchain command[{}] with args:{}", cmd.getCommandName(), invokeArgs);
        return coopRepository.invoke(CommandArgs.from(INVOKE, invokeArgs))
                .thenApply(toCompetableObject(InvokeReturn.class))
                .thenApply(opt -> opt.get());
    }

    private <T> Function<ResponseBody, List<T>> toCompetableObjects(Class<T> clazz) {
        TypeReference<List<T>> type = new TypeReference<List<T>>(){};
        return value -> {
            try {
                return JsonUtils.toObject(value.string(), type);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        };
    }

    private <T> Function<ResponseBody, Optional<T>> toCompetableObject(Class<T> clazz) {
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
