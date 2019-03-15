package org.civis.blockchain.ssm.client.fabric;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RepositoryFactory {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private final Retrofit retrofit;

    public RepositoryFactory(String baseUrl) {
        this.retrofit = buildRetrofit(baseUrl);
    }

    private Retrofit buildRetrofit(String baseUrl) {
        OkHttpClient client = httpClient
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Content-Type", "application/json").method(original.method(), original.body());
                    return chain.proceed(requestBuilder.build());
                })
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();


        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
    }

    public CoopRepository buildCoopRepository() {
        return retrofit.create(CoopRepository.class);
    }
}
