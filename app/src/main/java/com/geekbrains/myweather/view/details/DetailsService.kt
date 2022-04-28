package com.geekbrains.myweather.view.details

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.geekbrains.myweather.BuildConfig
import com.geekbrains.myweather.model.OnServerResponse
import com.geekbrains.myweather.model.OnServerResponseListener
import com.geekbrains.myweather.model.dto.WeatherDTO
import com.geekbrains.myweather.utils.*
import com.geekbrains.myweather.viewmodel.ResponseState
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsService(val name: String = ""): IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {

        intent?.let {
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0)
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0)

            try {

            val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
            val uri = URL(urlText)
            val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection)
                .apply {
                    connectTimeout = TIME_OUT
                    readTimeout = TIME_OUT
                    addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                }
            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                val serverSide = 500..599
                val clientSide = 400..499
                val responseOk = 200..299

                val message = Intent (KEY_WAVE_SERVICE_BROADCAST)
                message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO)
                LocalBroadcastManager.getInstance(this).sendBroadcast(message)

                when (responseCode) {
                    in serverSide -> {
                        Log.d("@@@", "serverSide")
                    }
                    in clientSide -> {
                        Log.d("@@@", "clientSide")
                    }
                    in responseOk -> {
                        Log.d("@@@", "responseOk")

                    }
                    else -> {Log.d("@@@", "Что-то ещё")}
                }
            }catch (e: Exception){
                Log.d("@@@", "Exception")
            } finally {
                urlConnection.disconnect()
            }

                }catch (e: MalformedURLException){
                    Log.d("@@@", "MalformedURLException")
                }
        }
    }
}