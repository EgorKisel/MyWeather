package com.geekbrains.myweather.model

import com.geekbrains.myweather.viewmodel.DetailsViewModel

interface DetailsRepository {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}