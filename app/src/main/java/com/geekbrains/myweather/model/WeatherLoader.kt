package com.geekbrains.myweather.model

import android.os.Handler
import android.os.Looper
import com.geekbrains.myweather.BuildConfig
import com.geekbrains.myweather.model.dto.WeatherDTO
import com.geekbrains.myweather.utils.TIME_OUT
import com.geekbrains.myweather.utils.YANDEX_API_KEY
import com.geekbrains.myweather.viewmodel.ResponseState
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse, private val onErrorListener: OnServerResponseListener) {

    fun loadWeather(lat: Double, lon: Double){

        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection)
            .apply {
            connectTimeout = TIME_OUT
            readTimeout = TIME_OUT
            addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        }

        Thread {

            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                val serverSide = 500..599
                val clientSide = 400..499
                val responseOk = 200..299

                when (responseCode) {
                    in serverSide -> {
                        onErrorListener.onError(ResponseState.Message1("Серверная ошибка"))
                    }
                    in clientSide -> {
                        onErrorListener.onError(ResponseState.Message2("Клиентская ошибка"))
                    }
                    in responseOk -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        Handler(Looper.getMainLooper()).post {
                            onServerResponseListener.onResponse(weatherDTO)
                        }
                        onErrorListener.onError(ResponseState.Message3("Успешное соединение"))
                    }
                }
//
//                Handler(Looper.getMainLooper()).post{
//                    onServerResponseListener.onResponse(weatherDTO)
//                }
            }catch (e: Exception){
                onErrorListener.onError(ResponseState.Message1("Что-то пошло не так"))
            } finally {
                urlConnection.disconnect()
            }

        }.start()
    }
}