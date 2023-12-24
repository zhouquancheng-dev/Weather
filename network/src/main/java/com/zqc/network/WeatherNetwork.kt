package com.zqc.network

import com.zqc.model.entity.RequestLiveIndexData
import com.zqc.network.service.*
import javax.inject.Inject

/**
 * 彩云天气信息
 * 不使用泛型实化： private val loginService = ServiceCreator.create(XXXService::class.java)
 * 使用泛型实化： private val loginService = ServiceCreator.create<XXXService>()
 */
class WeatherNetwork @Inject constructor() {

    // 使用ServiceCreator创建一个PlaceService接口的动态代理对象
    // 搜索地址信息接口动态代理对象
    private val searchPlacesService = ServiceCreator.createCaiYunApi<SearchPlacesService>()

    // 各个天气信息接口动态代理对象
    private val caiYunWeatherService = ServiceCreator.createCaiYunApi<CaiYunWeatherService>()

    private val moJiWeatherService = ServiceCreator.createMoJiApi<MoJiWeatherService>()

    /**
     * 搜索全球城市
     * @param address 搜索地址
     */
    suspend fun searchPlace(address: String) = searchPlacesService.searchPlaces(address = address)

    /**
     * 综合
     * @param longitude 经度
     * @param latitude 纬度
     */
    suspend fun fetchWeather(longitude: String, latitude: String) =
        caiYunWeatherService.getWeather(longitude = longitude, latitude = latitude)

    /**
     * 实时
     * @param longitude 经度
     * @param latitude 纬度
     */
    suspend fun fetchRealtimeWeather(longitude: String, latitude: String) =
        caiYunWeatherService.getRealtimeWeather(longitude = longitude, latitude = latitude)

    /**
     * 每日
     * @param longitude 经度
     * @param latitude 纬度
     * @param begin 过去一天的 Unix 时间戳
     */
    suspend fun fetchDailyWeather(longitude: String, latitude: String, begin: Long) =
        caiYunWeatherService.getDailyWeather(
            longitude = longitude, latitude = latitude, begin = begin
        )

    /**
     * 每小时
     * @param longitude 经度
     * @param latitude 纬度
     * @param hourlySteps 需要返回多少小时数的信息
     */
    suspend fun fetchHourlyWeather(longitude: String, latitude: String, hourlySteps: Int) =
        caiYunWeatherService.getHourlyWeather(
            longitude = longitude, latitude = latitude, hourlySteps = hourlySteps
        )

    /**
     * 生活指数
     * @param requestData 请求json
     */
    suspend fun fetchLiveIndex(requestData: RequestLiveIndexData) =
        moJiWeatherService.getLiveIndex(requestData)
}