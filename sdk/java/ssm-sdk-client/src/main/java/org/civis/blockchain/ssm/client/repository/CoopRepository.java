package org.civis.blockchain.ssm.client.repository;

import okhttp3.ResponseBody;
import retrofit2.http.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CoopRepository {

    @GET("/")
    CompletableFuture<ResponseBody> command(
            @Query("cmd") String cmd,
            @Query("fcn") String fcn,
            @Query("args") List<String> args
    );

    @POST("/")
    CompletableFuture<ResponseBody> invoke(
          @Body CommandArgs invokeArgs
    );

}
