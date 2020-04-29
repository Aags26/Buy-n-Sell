package com.bphc.buyandsell;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bphc.buyandsell.Constants.BASE_URL;

public class APIClient {

    private static Retrofit retrofit = null;

    private static HttpLoggingInterceptor httpLoggingInterceptor =
            new HttpLoggingInterceptor();

    private static OkHttpClient.Builder builder = new OkHttpClient.Builder();

    private APIClient() {
    }

    public static Retrofit getRetrofitInstance() {
        if(retrofit == null) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            if(BuildConfig.DEBUG) {
                builder.addInterceptor(httpLoggingInterceptor);
            }
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();
        }
        return retrofit;
    }
}
