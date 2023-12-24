package com.zqc.weather.ui.daily.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqc.model.BaseModel
import com.zqc.model.WeatherLoading
import com.zqc.model.WeatherUiState
import com.zqc.model.weather.DailyResponse
import com.zqc.weather.ui.daily.repository.DailyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val context: Application,
    private val dailyRepository: DailyRepository
) : ViewModel() {

    private val _dailyWeather = MutableStateFlow<WeatherUiState<BaseModel<DailyResponse>>>(WeatherLoading)
    val dailyWeather: StateFlow<WeatherUiState<BaseModel<DailyResponse>>>
        get() = _dailyWeather.asStateFlow()

    fun getDailyWeather(longitude: String, latitude: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dailyRepository.fetchDailyWeather(longitude, latitude)
            _dailyWeather.value = result
        }
    }

    fun insertCityInfo(address: String, longitude: String, latitude: String, callback: () -> Unit) {
        viewModelScope.launch {
            val isCity = dailyRepository.hasCity(address)
            val isCityCount = dailyRepository.hasExcessQuantity()
            if (isCity) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "当前城市或地区已添加", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (isCityCount) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "城市数量已达上限，如需添加新的城市，请先删除已有的城市或地区", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    dailyRepository.insertCityInfo(address, longitude, latitude)
                    callback()
                }
            }
        }
    }

    val cityCount = MutableStateFlow(0)

    fun cityCount() {
        viewModelScope.launch {
            cityCount.value = dailyRepository.cityCount()
        }
    }
}