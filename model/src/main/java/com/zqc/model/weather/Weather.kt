package com.zqc.model.weather

/**
 * 综合数据bean
 * @param alert 预警数据
 * @param realtime 实时数据
 * @param hourly 小时级别数据
 * @param daily 天级别数据
 */
data class Weather(
    val alert: AlertResponse.Alert,
    val realtime: RealtimeResponse.Realtime,
    val hourly: HourlyResponse.Hourly,
    val daily: DailyResponse.Daily,
)