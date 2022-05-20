package com.geekbrains.myweather.model

import com.geekbrains.myweather.BuildConfig
import com.geekbrains.myweather.model.dto.WeatherDTO
import com.geekbrains.myweather.utils.YANDEX_DOMAIN
import com.geekbrains.myweather.utils.convertDtoToModel
import com.geekbrains.myweather.viewmodel.DetailsViewModel
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsRepositoryOneRetrofit2Impl : DetailsRepositoryOne {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)
        //val response = weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon).execute() можно так (синхронно)
        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object : Callback<WeatherDTO>{
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            val weather = convertDtoToModel(it)
                            weather.city = city
                            callbackMy.onResponse((weather))
                        }
                    }else{
                        callbackMy.onFail()
                    }
                }
                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                   callbackMy.onFail()
                }
            })
    }
}