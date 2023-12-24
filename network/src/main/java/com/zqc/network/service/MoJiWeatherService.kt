package com.zqc.network.service

import com.zqc.model.entity.RequestLiveIndexData
import com.zqc.model.weather.air.LiveIndexResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MoJiWeatherService {

    /**
     * @param requestData json 请求格式参数
     */
    @POST("/whapi/json/aliweather/index")
    suspend fun getLiveIndex(
        @Body requestData: RequestLiveIndexData
    ): LiveIndexResponse
}