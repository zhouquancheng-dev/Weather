package com.zqc.model.weather.air

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * 墨迹天气生活指数Bean
 */
data class LiveIndexResponse(val code: Int, val data: DataEntity, val msg: String) {

    data class DataEntity(val city: CityDesc, val liveIndex: Map<String, List<LiveIndex>>) {

        data class CityDesc(
            val cityId: Int,
            @SerializedName("counname") val counName: String,
            @SerializedName("ianatimezone") val ianaTimeZone: String,
            val name: String,
            @SerializedName("pname") val pName: String,
            @SerializedName("secondaryname") val secondaryName: String,
            val timezone: String
        )

        data class LiveIndex(
            val code: Int,
            val day: String,
            val desc: String,
            val level: String,
            val name: String,
            val status: String,
            @SerializedName("updatetime") val updateTime: Date
        )

    }

}