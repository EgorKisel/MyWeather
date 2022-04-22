package com.geekbrains.myweather.model

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.geekbrains.myweather.BuildConfig
import com.geekbrains.myweather.viewmodel.AppError
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse, private val onErrorListener: OnErrorListener) {

    fun loadWeather(lat: Double, lon: Double){

        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        //val urlText = "http://212.86.114.27/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection)
            .apply {
            connectTimeout = 1000
            readTimeout = 1000
            addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
        }

        Thread {

            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                Handler(Looper.getMainLooper()).post{
                    onServerResponseListener.onResponse(weatherDTO)
                }
            }catch (e: Exception){
                onErrorListener.onError(AppError.Error1("Что-то пошло не так"))
            } finally {
                urlConnection.disconnect()
            }

        }.start()
    }
}