package com.geekbrains.myweather.model.dto

data class PartDTO(
    val condition: String,
    val daytime: String,
    val feelsLike: Int,
    val humidity: Int,
    val icon: String,
    val partName: String,
    val polar: Boolean,
    val precMm: Int,
    val precPeriod: Int,
    val precProb: Int,
    val pressureMm: Int,
    val pressurePa: Int,
    val tempAvg: Int,
    val tempMax: Int,
    val tempMin: Int,
    val windDir: String,
    val windGust: Double,
    val windSpeed: Double
)