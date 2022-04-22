package com.geekbrains.myweather.viewmodel

import com.geekbrains.myweather.model.Weather

sealed class AppError {
    data class Error1 (val errorText: String): AppError()
    data class Error2 (val errorText: String): AppError()
    data class Error3 (val errorText: String): AppError()
}