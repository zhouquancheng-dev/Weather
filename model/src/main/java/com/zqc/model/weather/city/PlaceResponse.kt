package com.zqc.model.weather.city

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 搜索全球城市Bean
 */
@JsonClass(generateAdapter = true)
data class PlaceResponse(
    val status: String,
    val places: List<PlaceEntity>,
) {
    /**
     * @param id id
     * @param name 城市名
     * @param location 城市经纬度
     * @param address 详细地址
     * @param placeId 城市id
     */
    data class PlaceEntity(
        val id: String,
        val name: String,
        val location: LocationEntity,
        @Json(name = "formatted_address") val address: String,
        @Json(name = "place_id") val placeId: String
    )

    /**
     * @param lat 纬度latitude
     * @param lng 经度longitude
     */
    data class LocationEntity(
        val lat: String,
        val lng: String
    )
}