package com.geekbrains.myweather.model.dto

import com.geekbrains.myweather.model.dto.FactDTO
import com.geekbrains.myweather.model.dto.ForecastDTO
import com.geekbrains.myweather.model.dto.InfoDTO
import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName ("fact")
    val factDTO: FactDTO,
    @SerializedName ("forecast")
    val forecastDTO: ForecastDTO,
    @SerializedName ("info")
    val infoDTO: InfoDTO,
    @SerializedName ("now")
    val now: Int,
    @SerializedName ("now_dt")
    val nowDt: String
)