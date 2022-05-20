package com.geekbrains.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather.model.DetailsRepositoryOneRoomImpl
import com.geekbrains.myweather.model.RepositoryImpl
import com.geekbrains.myweather.model.Weather

class HistoryViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData(),
                       private val repository: DetailsRepositoryOneRoomImpl = DetailsRepositoryOneRoomImpl()
                    ) :
    ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }
    fun getAll(){
        repository.getAllWeatherDetails(object: CallbackForAll{
            override fun onResponse(listWeather: List<Weather>) {
                liveData.postValue(AppState.Success(listWeather))
            }

            override fun onFail() {
                //Not yet implemented
            }

        })
    }
    interface CallbackForAll{
        fun onResponse(listWeather: List<Weather>)
        fun onFail()
    }
}