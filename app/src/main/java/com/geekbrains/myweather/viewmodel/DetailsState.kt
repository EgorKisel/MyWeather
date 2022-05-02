package com.geekbrains.myweather.viewmodel

import com.geekbrains.myweather.model.Weather

sealed class DetailsState {
    object Loading: DetailsState()
    data class Success (val weather: Weather): DetailsState()
    data class Error (val error: Throwable): DetailsState()
}