package com.geekbrains.myweather.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastDTO(
    val date: String,
    val dateTs: Int,
    val moonCode: Int,
    val moonText: String,
    val partDTOS: List<PartDTO>,
    val sunrise: String,
    val sunset: String,
    val week: Int
): Parcelable