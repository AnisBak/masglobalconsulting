package com.anis.masglobalchallenge.ui

import com.anis.masglobalchallenge.model.Main
import com.anis.masglobalchallenge.model.Weather
import com.anis.masglobalchallenge.model.Wind

data class UiState(
    val weather: Weather = Weather(),
    val message: String?=null,
    val wind: Wind = Wind(),
    val main: Main = Main(),
    var location: Boolean = false
)
