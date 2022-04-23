package com.geekbrains.myweather.model

import com.geekbrains.myweather.viewmodel.ResponseState

fun interface OnServerResponseListener {
    fun onError(error: ResponseState)
}