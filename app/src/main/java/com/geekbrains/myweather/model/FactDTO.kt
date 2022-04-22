package com.geekbrains.myweather.model

data class FactDTO(
    val condition: String,
    val daytime: String,
    val feelsLike: Int,
    val humidity: Int,
    val icon: String,
    val obsTime: Int,
    val polar: Boolean,
    val pressureMm: Int,
    val pressurePa: Int,
    val season: String,
    val temp: Int,
    val windDir: String,
    val windGust: Double,
    val windSpeed: Double
)