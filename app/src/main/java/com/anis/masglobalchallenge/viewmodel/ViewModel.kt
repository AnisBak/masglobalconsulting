package com.anis.masglobalchallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anis.masglobalchallenge.api.ApiResponse
import com.anis.masglobalchallenge.model.Main
import com.anis.masglobalchallenge.model.Weather
import com.anis.masglobalchallenge.model.Wind
import com.anis.masglobalchallenge.repositories.WeatherRepository
import com.anis.masglobalchallenge.ui.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class WeatherViewModel  @Inject constructor(private val weatherRepository:WeatherRepository) : ViewModel(){


    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private var fetchJob: Job? = null




    fun fetchWeather(city: String="", lon:String="", lat:String="") {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val weatherApiResponse = _fetchWeather(city,lon,lat)
                _uiState.update {
                    it.copy(weather = weatherApiResponse.weather[0], main = weatherApiResponse.main,wind = weatherApiResponse.wind, message = null)
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(weather = Weather(),wind = Wind(), main= Main(), message = ex.message )
                }
            }
        }
    }
    private suspend fun _fetchWeather(city: String, lon:String, lat:String): ApiResponse {
        return weatherRepository.searchCity(city,lon,lat)
    }

}