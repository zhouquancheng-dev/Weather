package com.zqc.mdoel.weather

import com.zqc.utils.R

/**
 * 主要天气现象的优先级：降雪 > 降雨 > 雾 > 沙尘 > 浮尘 > 雾霾 > 大风 > 阴 > 多云 > 晴
 */
fun getIconAnimation(weatherCode: String): Int {
    return when(weatherCode) {
        "CLEAR_DAY" -> R.raw.weather_clear_day
        "CLEAR_NIGHT" -> R.raw.weather_clear_night
        "PARTLY_CLOUDY_DAY" -> R.raw.weather_partly_cloudy_day
        "PARTLY_CLOUDY_NIGHT" -> R.raw.weather_partly_cloudy_night
        "CLOUDY" -> R.raw.weather_cloudy
        "WIND" -> R.raw.weather_wind
        "LIGHT_RAIN" -> if (timeCompareBefore("18:00")) R.raw.weather_rain_day else R.raw.weather_rain_night
        "MODERATE_RAIN" -> if (timeCompareBefore("18:00")) R.raw.weather_rain_day else R.raw.weather_rain_night
        "HEAVY_RAIN" -> if (timeCompareBefore("18:00")) R.raw.weather_rain_day else R.raw.weather_rain_night
        "STORM_RAIN" -> if (timeCompareBefore("18:00")) R.raw.weather_rain_day else R.raw.weather_rain_night
        "THUNDER_SHOWER" -> R.raw.weather_thunder_shower
        "LIGHT_SNOW" -> if (timeCompareBefore("18:00")) R.raw.weather_snow_day else R.raw.weather_snow_night
        "MODERATE_SNOW" -> if (timeCompareBefore("18:00")) R.raw.weather_snow_day else R.raw.weather_snow_night
        "HEAVY_SNOW" -> if (timeCompareBefore("18:00")) R.raw.weather_snow_day else R.raw.weather_snow_night
        "STORM_SNOW" -> if (timeCompareBefore("18:00")) R.raw.weather_snow_day else R.raw.weather_snow_night
        "LIGHT_HAZE" -> R.raw.weather_fog
        "MODERATE_HAZE" -> R.raw.weather_fog
        "HEAVY_HAZE" -> R.raw.weather_fog
        "FOG" -> R.raw.weather_fog
        "DUST" -> R.raw.weather_fog
        "SAND" -> R.raw.weather_fog
        else -> if (timeCompareBefore("18:00")) R.raw.weather_clear_day else R.raw.weather_clear_night
    }
}