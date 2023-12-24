package com.zqc.weather.ui.weather

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zqc.model.WeatherLoading
import com.zqc.model.WeatherNoContent
import com.zqc.model.room.entity.CityInfo
import com.zqc.mdoel.lce.NoContent
import com.zqc.weather.permission.FeatureThatRequiresLocationPermissions
import com.zqc.weather.ui.weather.components.WeatherTopBar
import com.zqc.weather.ui.weather.viewmodel.WeatherViewModel

@ExperimentalFoundationApi
@ExperimentalPermissionsApi
@ExperimentalTextApi
@ExperimentalMaterialApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    resultId: Int,
    toSettingClick: () -> Unit,
    toCityManage: () -> Unit,
    toDailyClick: (name: String, longitude: String, latitude: String, index :Int) -> Unit,
    toBottomSheetClick: (String) -> Unit
) {

    val cityInfoList by weatherViewModel.cityInfoList.collectAsStateWithLifecycle(null)
    val weatherData by weatherViewModel.weatherData.collectAsStateWithLifecycle(WeatherLoading)
    val located by weatherViewModel.locate.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState { cityInfoList?.size ?: 0 }

    if (cityInfoList == null || cityInfoList.isNullOrEmpty()) {
        if (cityInfoList?.size == 0) {
            if (pagerState.currentPage == 0) {
                FeatureThatRequiresLocationPermissions(weatherViewModel = weatherViewModel)
            }
        }
        NoCityContent(
            location = located,
            toCityManage = { toCityManage() },
            toSettingClick = { toSettingClick() }
        )
    } else {
        CurrentPageEffect(
            pagerState = pagerState,
            weatherViewModel = weatherViewModel,
            cityInfoList = cityInfoList ?: emptyList()
        )
        WeatherPage(
            weatherViewModel = weatherViewModel,
            weatherData = weatherData,
            pagerState = pagerState,
            cityInfoList = cityInfoList ?: emptyList(),
            resultId = resultId,
            toSettingClick = toSettingClick,
            toCityClick = toCityManage,
            toDailyClick = { name, longitude, latitude, index ->
                toDailyClick(name, longitude, latitude, index)
            },
            onRetryClick = { weatherViewModel.getWeather(cityInfoList!![pagerState.currentPage].id) },
            toBottomSheetClick = { toBottomSheetClick(it) }
        )
    }
}

@ExperimentalFoundationApi
@Composable
private fun CurrentPageEffect(
    pagerState: PagerState,
    weatherViewModel: WeatherViewModel,
    cityInfoList: List<CityInfo>
) {
    if (cityInfoList.isNotEmpty()) {
        // 在滑动过程中不刷新新一页的内容，需要完全停止滑动才会刷新
        if (pagerState.isScrollInProgress) return
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                weatherViewModel.getWeather(cityInfoList[page].id)
            }
        }
    }
}

@Composable
private fun NoCityContent(
    location: String,
    toSettingClick: () -> Unit,
    toCityManage: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()) {
        WeatherTopBar(
            cityInfo = CityInfo(address = ""),
            iconColor = MaterialTheme.colors.onSurface,
            toCityClick = toCityManage,
            toSettingClick = toSettingClick
        )
        NoContent(weatherNoContent = WeatherNoContent(location))
    }
}