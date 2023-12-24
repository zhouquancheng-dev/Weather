package com.zqc.weather.ui.daily

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zqc.model.WeatherLoading
import com.zqc.mdoel.lce.LcePage
import com.zqc.weather.ui.daily.components.DailyWeatherContent
import com.zqc.weather.ui.daily.viewmodel.DailyViewModel

@ExperimentalTextApi
@Composable
fun DailyWeatherPage(
    dailyViewModel: DailyViewModel,
    title: String,
    longitude: String,
    latitude: String,
    position: Int,
    onBackClick: () -> Unit,
    toWeatherClick: (Int) -> Unit,
    onRetryClick: () -> Unit,
) {
    LaunchedEffect(Unit) {
        dailyViewModel.getDailyWeather(longitude, latitude)
        if (title != "未来趋势预报") {
            dailyViewModel.cityCount()
        }
    }
    val daily by dailyViewModel.dailyWeather.collectAsStateWithLifecycle(WeatherLoading)
    val cityCount by dailyViewModel.cityCount.collectAsStateWithLifecycle(0)

    LcePage(uiState = daily, onRetryClick = { onRetryClick() }) { result ->
        DailyWeatherContent(
            daily = listOf(result.result.daily),
            title = title,
            position = position,
            onBackClick = onBackClick,
            toWeatherClick = { toWeatherClick(cityCount) }
        )
    }
}