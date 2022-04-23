package com.geekbrains.myweather.viewmodel

sealed class ResponseState {
    data class Message1 (val errorText: String): ResponseState()
    data class Message2 (val errorText: String): ResponseState()
    data class Message3 (val errorText: String): ResponseState()
}