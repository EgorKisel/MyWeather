package com.geekbrains.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather.model.Repository
import com.geekbrains.myweather.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData(),
private val repository: RepositoryImpl = RepositoryImpl()
                    ) :
    ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherRussia() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)

    private fun getWeather(isRussian: Boolean) {
        Thread() {
            liveData.postValue(AppState.Loading(0))
            val answer = if (!isRussian) repository.getWorldWeatherFromLocalStorage() else repository.getRussianWeatherFromLocalStorage()
            liveData.postValue(AppState.Success(answer))
        }.start()
    }
}