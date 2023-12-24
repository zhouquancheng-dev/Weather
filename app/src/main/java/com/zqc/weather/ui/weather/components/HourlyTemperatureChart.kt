package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.mdoel.view.isDarkMode
import com.zqc.mdoel.weather.getAqiInfo
import com.zqc.model.weather.HourlyResponse
import java.util.Date

@ExperimentalTextApi
@Composable
fun DrawHourlyTemperatureChart(
    hourlyTemperatures: List<HourlyResponse.Hourly.Temperature>,
    hourlyAqi: List<HourlyResponse.Hourly.AirQuality.AQI>,
    currentTemp: Int,
    currentIndex: Int,
) {
    val context = LocalContext.current
    // 深色模式返回true，浅色false
    val isDark = context.isDarkMode()

    val textMeasurer = rememberTextMeasurer()

    val tempsSize = hourlyTemperatures.size

    val colors = mutableListOf<Color>()
    if (currentIndex == 0) {
        colors.add(0, getAqiInfo(hourlyAqi[0].value.chn).color)
        colors.add(1, getAqiInfo(hourlyAqi[1].value.chn).color)
    } else if (currentIndex > 0 && currentIndex < tempsSize - 1) {
        colors.add(0, getAqiInfo(hourlyAqi[currentIndex - 1].value.chn).color)
        colors.add(1, getAqiInfo(hourlyAqi[currentIndex].value.chn).color)
        colors.add(2, getAqiInfo(hourlyAqi[currentIndex + 1].value.chn).color)
    } else {
        colors.add(0, getAqiInfo(hourlyAqi[currentIndex - 1].value.chn).color)
        colors.add(1, getAqiInfo(hourlyAqi[currentIndex].value.chn).color)
    }

    Box(
        modifier = Modifier
            .width(64.dp)
            .height(80.dp)
            .padding(vertical = 10.dp)
            .drawWithCache {
                val maxTemperature = hourlyTemperatures.maxByOrNull { it.value }?.value?.toInt() ?: 0
                val minTemperature = hourlyTemperatures.minByOrNull { it.value }?.value?.toInt() ?: 0

                val middleTemps = mutableListOf<Float>()
                for (index in 0 until tempsSize - 1) {
                    middleTemps.add(index, (hourlyTemperatures[index].value.toInt() + hourlyTemperatures[index + 1].value.toInt()) / 2f)
                }

                val measuredTempText = textMeasurer.measure(
                    text = AnnotatedString("$currentTemp°"),
                    style = TextStyle(color = if (isDark) Color.White else Color.Black, fontSize = 14.sp)
                )

                val brush = Brush.linearGradient(colors)

                val tempPath = Path()

                onDrawBehind {
                    drawHourlyTemperaturePath(
                        tempPath,
                        brush,
                        hourlyTemperatures,
                        tempsSize,
                        maxTemperature,
                        minTemperature,
                        currentTemp,
                        currentIndex,
                        middleTemps
                    )

                    drawHourlyTemperatureText(
                        measuredTempText,
                        maxTemperature,
                        minTemperature,
                        currentTemp
                    )

                    drawHourlyTemperaturePoint(
                        maxTemperature,
                        minTemperature,
                        currentTemp,
                        currentIndex
                    )
                }
            }
    )
}

fun DrawScope.drawHourlyTemperaturePath(
    path: Path,
    brush: Brush,
    hourlyTemperatures: List<HourlyResponse.Hourly.Temperature>,
    tempsSize: Int,
    maxTemperature: Int,
    minTemperature: Int,
    currentTemp: Int,
    currentIndex: Int,
    middleTemps: List<Float>
) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val tempRange = -(canvasHeight / (maxTemperature - minTemperature))

    // 贝塞尔曲线控制点 x轴偏移量
    val offsetX = 50f.toDp().toPx()
    // 贝塞尔曲线控制点 y轴偏移量
    val offsetY = 2f.toDp().toPx()

    if (currentIndex == 0) {
        path.moveTo(0f, (currentTemp - maxTemperature) * tempRange)
        path.lineTo(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)
        if (currentTemp > hourlyTemperatures[1].value.toInt()) {
            path.moveTo(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)
            path.quadraticBezierTo(
                x1 = canvasWidth / 2 + offsetX,
                y1 = (currentTemp - maxTemperature) * tempRange + offsetY,
                x2 = canvasWidth,
                y2 = (middleTemps.first() - maxTemperature) * tempRange
            )
        } else if (currentTemp < hourlyTemperatures[1].value.toInt()) {
            path.moveTo(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)
            path.quadraticBezierTo(
                x1 = canvasWidth / 2 + offsetX,
                y1 = (currentTemp - maxTemperature) * tempRange - offsetY,
                x2 = canvasWidth,
                y2 = (middleTemps.first() - maxTemperature) * tempRange
            )
        } else {
            path.moveTo(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)
            path.lineTo(canvasWidth, (middleTemps.first() - maxTemperature) * tempRange)
        }
    } else if (currentIndex > 0 && currentIndex < tempsSize - 1) {
        if (currentTemp > hourlyTemperatures[currentIndex - 1].value.toInt()) {
            path.moveTo(0f, (middleTemps[currentIndex - 1] - maxTemperature) * tempRange)
            path.quadraticBezierTo(
                x1 = canvasWidth / 2 - offsetX,
                y1 = (currentTemp - maxTemperature) * tempRange + offsetY,
                x2 = canvasWidth / 2,
                y2 = (currentTemp - maxTemperature) * tempRange
            )
        } else if (currentTemp < hourlyTemperatures[currentIndex - 1].value.toInt()) {
            path.moveTo(0f, (middleTemps[currentIndex - 1] - maxTemperature) * tempRange)
            path.quadraticBezierTo(
                x1 = canvasWidth / 2 - offsetX,
                y1 = (currentTemp - maxTemperature) * tempRange - offsetY,
                x2 = canvasWidth / 2,
                y2 = (currentTemp - maxTemperature) * tempRange
            )
        } else {
            path.moveTo(0f, (middleTemps[currentIndex - 1] - maxTemperature) * tempRange)
            path.lineTo(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)
        }

        if (currentTemp > hourlyTemperatures[currentIndex + 1].value.toInt()) {
            path.moveTo(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)
            path.quadraticBezierTo(
                x1 = canvasWidth / 2 + offsetX,
                y1 = (currentTemp - maxTemperature) * tempRange + offsetY,
                x2 = canvasWidth,
                y2 = (middleTemps[currentIndex] - maxTemperature) * tempRange
            )
        } else if (currentTemp < hourlyTemperatures[currentIndex + 1].value.toInt()) {
            path.moveTo(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)
            path.quadraticBezierTo(
                x1 = canvasWidth / 2 + offsetX,
                y1 = (currentTemp - maxTemperature) * tempRange - offsetY,
                x2 = canvasWidth,
                y2 = (middleTemps[currentIndex] - maxTemperature) * tempRange
            )
        } else {
            path.moveTo(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)
            path.lineTo(canvasWidth, (middleTemps[currentIndex] - maxTemperature) * tempRange)
        }
    } else {
        if (currentTemp > hourlyTemperatures[currentIndex - 1].value.toInt()) {
            path.moveTo(0f, (middleTemps.last() - maxTemperature) * tempRange)
            path.quadraticBezierTo(
                x1 = canvasWidth / 2 - offsetX,
                y1 = (currentTemp - maxTemperature) * tempRange + offsetY,
                x2 = canvasWidth / 2,
                y2 = (currentTemp - maxTemperature) * tempRange
            )
        } else if (currentTemp < hourlyTemperatures[currentIndex - 1].value.toInt()) {
            path.moveTo(0f, (middleTemps.last() - maxTemperature) * tempRange)
            path.quadraticBezierTo(
                x1 = canvasWidth / 2 - offsetX,
                y1 = (currentTemp - maxTemperature) * tempRange - offsetY,
                x2 = canvasWidth / 2,
                y2 = (currentTemp - maxTemperature) * tempRange
            )
        } else {
            path.moveTo(0f, (middleTemps.last() - maxTemperature) * tempRange)
            path.lineTo(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)
        }
        path.lineTo(canvasWidth, (currentTemp - maxTemperature) * tempRange)
    }

    drawPath(
        path = path,
        brush = brush,
        style = Stroke(width = 5f.toDp().toPx())
    )
}

@ExperimentalTextApi
fun DrawScope.drawHourlyTemperatureText(
    tempText: TextLayoutResult,
    maxTemperature: Int,
    minTemperature: Int,
    currentTemp: Int
) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val tempRange = -(canvasHeight / (maxTemperature - minTemperature))

    drawText(
        textLayoutResult = tempText,
        topLeft = Offset(
            x = (canvasWidth / 2) - (tempText.size.width / 2),
            y = (currentTemp - maxTemperature) * tempRange - (tempText.size.height * 1.5f)
        )
    )
}

fun DrawScope.drawHourlyTemperaturePoint(
    maxTemperature: Int,
    minTemperature: Int,
    currentTemp: Int,
    currentIndex: Int
) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val tempRange = -(canvasHeight / (maxTemperature - minTemperature))

    if (currentIndex == 1) {
        drawPoints(
            points = arrayListOf(Offset(canvasWidth / 2, (currentTemp - maxTemperature) * tempRange)),
            pointMode = PointMode.Points,
            color = Color.Gray,
            strokeWidth = 20f.toDp().toPx(),
            cap = StrokeCap.Round
        )
    }
}

@ExperimentalTextApi
@Preview(name = "24小时温度趋势曲线", showBackground = true,
    device = "spec:width=1440px,height=2880px,dpi=560", locale = "zh-rCN", showSystemUi = false,
    backgroundColor = 0xFFFFFFFF, fontScale = 1.0f
)
@Composable
fun DrawHourlyTemperatureChartPreview() {
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(1) {
            DrawHourlyTemperatureChart(
                hourlyTemperatures = listOf(
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 30f),
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 29f),
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 28f),
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 27f),
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 25f)
                ),
                hourlyAqi = listOf(
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(50)),
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(100)),
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(150)),
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(200)),
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(300))
                ),
                currentTemp = 28,
                currentIndex = 0
            )
        }
    }
}