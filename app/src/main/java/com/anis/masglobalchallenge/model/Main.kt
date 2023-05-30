package com.anis.masglobalchallenge.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Main(
    val temp: String? = null,
    val temp_min: String? = null,
    val temp_max: String? = null
)
