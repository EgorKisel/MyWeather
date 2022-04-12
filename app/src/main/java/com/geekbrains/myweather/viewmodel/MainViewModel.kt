package com.geekbrains.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeather() {
        Thread() {
            liveData.postValue(AppState.Loading(0))
            sleep(2000L)
            if ((0..10).random() > 7)
                liveData.postValue(AppState.Success(Any()))
            else
                liveData.postValue(AppState.Error(IllegalAccessException()))
        }.start()
    }
}