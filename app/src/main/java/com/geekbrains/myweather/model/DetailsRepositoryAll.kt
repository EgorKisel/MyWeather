package com.geekbrains.myweather.model

import com.geekbrains.myweather.viewmodel.DetailsViewModel
import com.geekbrains.myweather.viewmodel.HistoryViewModel

interface DetailsRepositoryAll {
    fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll)
}