package com.zqc.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 彩云天气 Base bean
 * @param status 请求状态
 * @param apiVersion api版本
 * @param apiStatus
 * @param lang 返回的自然语言 zh_CN简体中文 zh_TW 繁体中文 en_US 美式英语 en_GB 英式英语 ja 日语
 * @param unit 返回的数据的单位 imperial 英制  metric 默认公制
 * @param tzShift 时区偏移秒数，默认为东八区，tzshift=28800
 * @param timezone 时区
 * @param serverTime 服务器时间戳，单位是 Unix 时间戳
 * @param location 经纬度
 * @param result 子bean
 * @param primary
 */
@JsonClass(generateAdapter = true)
data class BaseModel<T>(
    val status: String,
    @Json(name = "api_version") val apiVersion: String,
    @Json(name = "api_status") val apiStatus: String,
    val lang: String,
    val unit: String,
    @Json(name = "tzshift") val tzShift: Int,
    val timezone: String,
    @Json(name = "server_time") val serverTime: Long,
    val location: List<Float>,
    val result: T,
    val primary: Int
)