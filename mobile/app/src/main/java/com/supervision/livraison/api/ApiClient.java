package com.supervision.livraison.api;

import com.supervision.livraison.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton holder for the Retrofit-backed {@link ApiService}.
 */
public final class ApiClient {

    private static volatile ApiService instance;

    private ApiClient() {}

    public static ApiService get() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = build();
                }
            }
        }
        return instance;
    }

    private static ApiService build() {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient http = new OkHttpClient.Builder()
                .addInterceptor(log)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout   (15, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(http)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }
}
