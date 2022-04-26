package com.geekbrains.myweather.model

import com.geekbrains.myweather.model.dto.WeatherDTO

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}