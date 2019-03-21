package org.civis.blockchain.ssm.client.fabric;

import org.civis.blockchain.ssm.client.command.InvokeArgs;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CoopRepository {

    @GET("/")
    CompletableFuture<ResponseBody> command(
            @Query("cmd") String cmd,
            @Query("fcn") String fcn,
            @Query("args") List<String> args
    );

//    @GET("v2")
//    CompletableFuture<ResponseBody> query(
//            @Query("fcn") String fcn,
//            @Query("args") List<String> args
//    );
//
//    @POST("v2")
//    CompletableFuture<ResponseBody> invoke(
//            @Body InvokeArgs args
//    );

}
