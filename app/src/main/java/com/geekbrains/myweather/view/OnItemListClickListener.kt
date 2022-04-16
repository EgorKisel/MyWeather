package com.geekbrains.myweather.view

import com.geekbrains.myweather.model.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}