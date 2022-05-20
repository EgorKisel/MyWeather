package com.geekbrains.myweather.view.weatherlist

import com.geekbrains.myweather.model.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}