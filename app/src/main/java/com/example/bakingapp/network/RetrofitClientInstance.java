package com.example.bakingapp.network;

import android.support.test.espresso.IdlingResource;

import com.jakewharton.espresso.OkHttp3IdlingResource;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

//    public static OkHttpClient client = new OkHttpClient();
//    public static IdlingResource resource = OkHttp3IdlingResource.create("OkHttp", client);

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient();
            IdlingResources.registerOkHttp(client);
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
