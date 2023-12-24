package com.zqc.weather

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zqc.mdoel.view.AdaptationScreenHeight
import com.zqc.mdoel.view.BarsLightDarkTheme
import com.zqc.mdoel.view.GrayAppAdapter
import com.zqc.weather.ui.theme.MyWeatherTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@ExperimentalFoundationApi
@ExperimentalPermissionsApi
@FlowPreview
@ExperimentalMaterialApi
@ExperimentalTextApi
@ExperimentalMaterialNavigationApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarsLightDarkTheme()
            AdaptationScreenHeight {
                MyWeatherTheme {
                    GrayAppAdapter {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
                            WeatherApplication()
                        }
                    }
                }
            }
        }
    }
}