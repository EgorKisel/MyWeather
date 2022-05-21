package com.geekbrains.myweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather.model.*

class DetailsViewModel(private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
                       private val repository: DetailsRepositoryOne = DetailsRepositoryOneRetrofit2Impl(),
                       private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryOneRoomImpl()
) :ViewModel() {
    fun getLiveData() = liveData
    fun getWeather(city: City){
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(city, object : Callback{
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                repositoryAdd.addWeather(weather)
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