package com.example.marvel;


import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {

    public static final String PUBLIC_API_KEY = "70c2daa74bac851c5c89faf14bff60f9";
    public static final String PRIVATE_API_KEY = "d82abd3c8cb9ae39a29ad82ce6c8a9e4f34aceda";


    private static Retrofit retrofit = null;

    static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }

    public MarvelAPI getMarvelApi() {
        return ApiCall.getClient().create(MarvelAPI.class);
    }


}
