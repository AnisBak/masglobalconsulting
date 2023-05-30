package com.anis.masglobalchallenge.di

import com.anis.masglobalchallenge.api.ApiInterceptor
import com.anis.masglobalchallenge.api.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @Provides
    fun retrofit(): RetrofitService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }
}