package com.zqc.model.weather.air

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * 墨迹天气生活指数Bean
 */
@JsonClass(generateAdapter = true)
data class LiveIndexResponse(val code: Int, val data: DataEntity, val msg: String) {

    data class DataEntity(val city: CityDesc, val liveIndex: Map<String, List<LiveIndex>>) {

        data class CityDesc(
            val cityId: Int,
            @Json(name = "counname") val counName: String,
            @Json(name = "ianatimezone") val ianaTimeZone: String,
            val name: String,
            @Json(name = "pname") val pName: String,
            @Json(name = "secondaryname") val secondaryName: String,
            val timezone: String
        )

        data class LiveIndex(
            val code: Int,
            val day: String,
            val desc: String,
            val level: String,
            val name: String,
            val status: String,
            @Json(name = "updatetime") val updateTime: Date
        )

    }

}