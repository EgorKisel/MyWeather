package com.geekbrains.myweather.model

data class ForecastDTO(
    val date: String,
    val dateTs: Int,
    val moonCode: Int,
    val moonText: String,
    val partDTOS: List<PartDTO>,
    val sunrise: String,
    val sunset: String,
    val week: Int
)