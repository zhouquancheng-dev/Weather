package com.zqc.model.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 实况预报数据base
 */
@JsonClass(generateAdapter = true)
data class RealtimeResponse(val realtime: Realtime) {

    /**
     * @param status 状态
     * @param temperature 地表 2 米气温
     * @param humidity  地表 2 米湿度相对湿度(%)
     * @param skyCon 天气现象
     * @param visibility 地表水平能见度
     * @param wind 地表风速风向
     * @param pressure 地面气压
     * @param apparentTemperature 体感温度
     * @param airQuality 空气质量
     * @param lifeIndex 生活指数自然语言描述
     */
    data class Realtime(
        val status: String,
        val temperature: Float,
        val humidity: Float,
        @Json(name = "skycon") val skyCon: String,
        val visibility: Float,
        val wind: Wind,
        val pressure: Float,
        @Json(name = "apparent_temperature") val apparentTemperature: Float,
        @Json(name = "air_quality") val airQuality: AirQuality,
        @Json(name = "life_index") val lifeIndex: LifeIndex
    ) {

        /**
         * @param speed 风速
         * @param direction 风向
         */
        data class Wind(val speed: Float, val direction: Float)

        /**
         * @param aqi 国标AQI
         * @param description 描述
         */
        data class AirQuality(val aqi: AQI, val description: AqiDesc) {
            /**
             * @param chn 国标aqi
             */
            data class AQI(val chn: Int)

            /**
             * @param chn 国标
             */
            data class AqiDesc(val chn: String)
        }

        /**
         * @param ultraviolet 紫外线指数
         * @param comfort 舒适度指数
         */
        data class LifeIndex(val ultraviolet: LifeIndexDesc, val comfort: LifeIndexDesc) {
            /**
             * @param index 等级
             * @param desc 自然语言描述
             */
            data class LifeIndexDesc(val index: Float, val desc: String)
        }
    }
}
