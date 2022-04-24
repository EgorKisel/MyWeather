package com.geekbrains.myweather.model

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}