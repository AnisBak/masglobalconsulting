package com.anis.masglobalchallenge.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl newUrl = originalRequest.url().newBuilder().build();

        Request newRequest = originalRequest.newBuilder().url(newUrl).build();

        return chain.proceed(newRequest);
    }
}
