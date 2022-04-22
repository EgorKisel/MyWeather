package com.geekbrains.myweather.model

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {

    fun loadWeather(lat: Double, lon: Double){

        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply {
            connectTimeout = 1000
            readTimeout = 1000
            addRequestProperty("X-Yandex-API-Key", "345f15dc-fd28-493d-8bfa-721e72537847")
        }

        Thread {

            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                //val result = (buffer)
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                Handler(Looper.getMainLooper()).post{
                    onServerResponseListener.onResponse(weatherDTO)
                }
            }catch (e: JsonSyntaxException){
                Log.e("", "Что-то пошло не так", e)
                e.printStackTrace()
            } finally {
                urlConnection.disconnect()
            }

        }.start()
    }
}