package com.zqc.model.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestLiveIndexData(
    val lon: String,
    val lat: String,
    val token: String
)