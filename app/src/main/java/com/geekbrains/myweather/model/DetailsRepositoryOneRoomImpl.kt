package com.geekbrains.myweather.model

import com.geekbrains.myweather.MyApp
import com.geekbrains.myweather.utils.convertHistoryEntityToWeather
import com.geekbrains.myweather.utils.convertWeatherToEntity
import com.geekbrains.myweather.viewmodel.DetailsViewModel
import com.geekbrains.myweather.viewmodel.HistoryViewModel

class DetailsRepositoryOneRoomImpl : DetailsRepositoryOne, DetailsRepositoryAll, DetailsRepositoryAdd {
    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDao().getAll()))
    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val list = convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryForCity(city.name))
        callback.onResponse(list.last())
    }

    override fun addWeather(weather: Weather) {
        MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))
    }

}