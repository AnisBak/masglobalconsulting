package com.anis.masglobalchallenge.repositories

import com.anis.masglobalchallenge.api.RetrofitService
import com.anis.masglobalchallenge.api.ApiResponse
import javax.inject.Inject


class WeatherRepository @Inject constructor()  {
    @Inject
    lateinit var retrofitService: RetrofitService



    suspend fun searchCity(city: String, lon:String, lat:String): ApiResponse =
        retrofitService.searchCity(city, lon, lat)
}