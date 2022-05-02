package com.geekbrains.myweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather.model.City
import com.geekbrains.myweather.model.DetailsRepository
import com.geekbrains.myweather.model.DetailsRepositoryRetrofit2Impl
import com.geekbrains.myweather.model.Weather

class DetailsViewModel(private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
                       private val repository: DetailsRepository = DetailsRepositoryRetrofit2Impl()
) :ViewModel() {
    fun getLiveData() = liveData
    fun getWeather(city: City){
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(city, object : Callback{
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }

            override fun onFail() {
                liveData.postValue(DetailsState.Error(Throwable("Server error")))
            }
        })
    }

    interface Callback{
        fun onResponse(weather: Weather)
        fun onFail()
    }
}