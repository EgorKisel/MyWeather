package com.geekbrains.myweather.model

import com.geekbrains.myweather.viewmodel.AppError

fun interface OnErrorListener {
    fun onError(error: AppError)
}