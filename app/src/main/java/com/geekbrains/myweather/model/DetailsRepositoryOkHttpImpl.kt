package com.geekbrains.myweather.model

import android.view.View
import com.geekbrains.myweather.BuildConfig
import com.geekbrains.myweather.model.dto.WeatherDTO
import com.geekbrains.myweather.utils.YANDEX_API_KEY
import com.geekbrains.myweather.utils.YANDEX_DOMAIN
import com.geekbrains.myweather.utils.YANDEX_ENDPOINT
import com.geekbrains.myweather.utils.convertDtoToModel
import com.geekbrains.myweather.viewmodel.DetailsViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

import java.io.IOException

class DetailsRepositoryOkHttpImpl: DetailsRepository {
    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {

            val client = OkHttpClient()
            val builder = Request.Builder()
            builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
            builder.url("$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=${city.lat}&lon=${city.lon}")
            val request = builder.build()
            val call = client.newCall(request)
        Thread{
            val response = call.execute() // синхронно
            if (response.isSuccessful){
                val serverResponse = response.body()!!.string()
                val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                val weather = convertDtoToModel(weatherDTO)
                weather.city = city
                callback.onResponse(weather)
            }else{
                callback.onFail()
            }
        }.start()
    }
}