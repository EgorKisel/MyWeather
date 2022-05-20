package com.geekbrains.myweather.model

import com.geekbrains.myweather.viewmodel.DetailsViewModel

interface DetailsRepositoryAdd {
    fun addWeather(weather: Weather)
}