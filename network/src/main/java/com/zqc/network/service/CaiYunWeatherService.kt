package com.zqc.network.service

import com.zqc.model.BaseModel
import com.zqc.model.weather.Weather
import com.zqc.mdoel.weather.CAI_YUN_TOKEN
import com.zqc.model.weather.DailyResponse
import com.zqc.model.weather.HourlyResponse
import com.zqc.model.weather.RealtimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CaiYunWeatherService {
    /**
     * 获取综合数据
     * @param token 令牌
     * @param longitude 纬度
     * @param latitude 经度
     * @param alert 是否返回预警信息
     * @param dailySteps 返回未来天数
     * @param hourlySteps 返回未来小时数
     */
    @GET("/v2.6/{token}/{longitude},{latitude}/weather")
    suspend fun getWeather(
        @Path("token") token: String = CAI_YUN_TOKEN,
        @Path("longitude") longitude: String,
        @Path("latitude") latitude: String,
        @Query("alert") alert: Boolean = true,
        @Query("dailysteps") dailySteps: Int = 3,
        @Query("hourlysteps") hourlySteps: Int = 24,
    ): BaseModel<Weather>

    /**
     * 获取实时天气
     * @param token 令牌
     * @param longitude 纬度
     * @param latitude 经度
     */
    @GET("/v2.6/{token}/{longitude},{latitude}/realtime")
    suspend fun getRealtimeWeather(
        @Path("token") token: String = CAI_YUN_TOKEN,
        @Path("longitude") longitude: String,
        @Path("latitude") latitude: String
    ): BaseModel<RealtimeResponse>

    /**
     * 获取未来每日的天气
     * @param token 令牌
     * @param longitude 纬度
     * @param latitude 经度
     * @param dailySteps 返回未来天数
     * @param begin 过去一天的 Unix 时间戳
     */
    @GET("/v2.6/{token}/{longitude},{latitude}/daily")
    suspend fun getDailyWeather(
        @Path("token") token: String = CAI_YUN_TOKEN,
        @Path("longitude") longitude: String,
        @Path("latitude") latitude: String,
        @Query("dailysteps") dailySteps: Int = 16,
        @Query("begin") begin: Long
    ): BaseModel<DailyResponse>

    /**
     * 获取小时级别预报
     * @param token 令牌
     * @param longitude 纬度
     * @param latitude 经度
     * @param hourlySteps 返回未来小时数
     */
    @GET("/v2.6/{token}/{longitude},{latitude}/hourly")
    suspend fun getHourlyWeather(
        @Path("token") token: String = CAI_YUN_TOKEN,
        @Path("longitude") longitude: String,
        @Path("latitude") latitude: String,
        @Query("hourlysteps") hourlySteps: Int,
    ): BaseModel<HourlyResponse>
}