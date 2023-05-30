package com.anis.masglobalchallenge.api

import com.anis.masglobalchallenge.model.Main
import com.anis.masglobalchallenge.model.Weather
import com.anis.masglobalchallenge.model.Wind
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val weather: List<Weather>,
    val wind: Wind,
    val main: Main
)
