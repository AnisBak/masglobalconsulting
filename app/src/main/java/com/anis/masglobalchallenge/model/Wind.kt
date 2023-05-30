package com.anis.masglobalchallenge.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Wind(
    val speed: String? = null,
    val deg: String? =null
)
