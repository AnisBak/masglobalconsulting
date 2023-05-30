package com.anis.masglobalchallenge.api

import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY = "25143eb7f1eca4b68b7046db7949c98e"
interface RetrofitService {
    @GET("data/2.5/weather?APPID=${API_KEY}&units=metric")
    suspend fun searchCity(@Query("q") city: String,@Query("lon") lon:String,@Query("lat") lat:String): ApiResponse
}