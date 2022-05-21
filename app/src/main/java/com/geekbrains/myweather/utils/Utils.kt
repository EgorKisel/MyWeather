package com.geekbrains.myweather.utils

import com.geekbrains.myweather.domain.room.HistoryEntity
import com.geekbrains.myweather.model.City
import com.geekbrains.myweather.model.Weather
import com.geekbrains.myweather.model.dto.FactDTO
import com.geekbrains.myweather.model.dto.WeatherDTO
import com.geekbrains.myweather.model.getDefaultCity

const val KEY_BUNDLE_WEATHER = "key"
const val KEY_BUNDLE_LAT = "lat"
const val KEY_BUNDLE_LON = "lon"
const val KEY_WAVE = "key_wave"
const val KEY_BUNDLE_SERVICE_MESSAGE = "key_bundle_service_message"
const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER = "weather_service_broadcast"


const val YANDEX_API_KEY = "X-Yandex-API-Key"
const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"
const val YANDEX_ENDPOINT = "v2/informers?"
const val TIME_OUT = 1000
const val KEY_WAVE_SERVICE_BROADCAST = "wave_service_broadcast"
private const val PROCESS_ERROR = "Обработка ошибки"

const val KEY_SP_NAME_1 = "fileName1"
const val KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN = "is_russian"

class Utils {
}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike, fact.icon))
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather>{
    return entityList.map{
        Weather(City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.icon)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity{
    return HistoryEntity(0, weather.city.name, weather.temperature, weather.feelsLike, weather.icon)
}