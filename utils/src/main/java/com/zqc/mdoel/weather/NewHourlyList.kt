package com.zqc.mdoel.weather

import com.zqc.model.weather.HourlyResponse
import com.zqc.model.weather.RealtimeResponse
import java.util.*

fun getNewTemperatureList(
    hourly: HourlyResponse.Hourly,
    realtime: RealtimeResponse.Realtime,
    currentDate: Date = Date(System.currentTimeMillis())
): List<HourlyResponse.Hourly.Temperature> {
    val currentTemp = HourlyResponse.Hourly.Temperature(currentDate, realtime.temperature)
    return listOf(hourly.temperature[0], currentTemp) + hourly.temperature.drop(1)
}

fun getNewSkyList(
    hourly: HourlyResponse.Hourly,
    realtime: RealtimeResponse.Realtime,
    currentDate: Date = Date(System.currentTimeMillis())
): List<HourlyResponse.Hourly.SkyCon> {
    val currentSky = HourlyResponse.Hourly.SkyCon(currentDate, realtime.skyCon)
    return listOf(hourly.skyCon[0], currentSky) + hourly.skyCon.drop(1)
}

fun getNewWindList(
    hourly: HourlyResponse.Hourly,
    realtime: RealtimeResponse.Realtime,
    currentDate: Date = Date(System.currentTimeMillis())
): List<HourlyResponse.Hourly.Wind> {
    val currentWind = HourlyResponse.Hourly.Wind(currentDate, realtime.wind.speed, realtime.wind.direction)
    return listOf(hourly.wind[0], currentWind) + hourly.wind.drop(1)
}

fun getNewAqiList(
    hourly: HourlyResponse.Hourly,
    realtime: RealtimeResponse.Realtime,
    currentDate: Date = Date(System.currentTimeMillis())
): List<HourlyResponse.Hourly.AirQuality.AQI> {
    val currentAqiDesc = HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(realtime.airQuality.aqi.chn)
    val currentAqi = HourlyResponse.Hourly.AirQuality.AQI(currentDate, currentAqiDesc)
    return listOf(hourly.airQuality.aqi[0], currentAqi) + hourly.airQuality.aqi.drop(1)
}
