package com.example.moviesearchapp.utils;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ApiClient {
    private static final String BASE_URL = "https://www.omdbapi.com/";
    private static final String API_KEY = "5ca5f6bf";

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void get(String queryParams, Callback callback) {
        String url = BASE_URL + "?apikey=" + API_KEY + "&" + queryParams;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
