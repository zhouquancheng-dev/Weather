package com.zqc.weather.ui.daily.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.mdoel.view.isDarkMode
import com.zqc.model.weather.DailyResponse
import com.zqc.weather.ui.theme.BrokenLineColor
import com.zqc.weather.ui.theme.MyWeatherTheme
import java.util.Date

/**
 * @param dailyTemperatures List<DailyResponse.Daily.Temperature>
 * @param currentDayTemp 当天的日间温度值
 * @param currentNightTemp 当天的夜间温度值
 * @param currentIndex List<Temperature> 的 index
 */
@ExperimentalTextApi
@Composable
fun DrawDailyTemperatureLineChart(
    dailyTemperatures: List<DailyResponse.Daily.Temperature>,
    currentDayTemp: Int,
    currentNightTemp: Int,
    currentIndex: Int,
) {
    val context = LocalContext.current
    // 深色模式返回true，浅色false
    val isDark = context.isDarkMode()

    val textMeasurer = rememberTextMeasurer()

    Box(modifier = Modifier
        .width(80.dp)
        .height(180.dp)
        .padding(vertical = 20.dp)
        .drawWithCache {
            val tempsSize = dailyTemperatures.size
            val maxTemperature = dailyTemperatures.maxByOrNull { it.max }?.max?.toInt() ?: 0
            val minTemperature = dailyTemperatures.minByOrNull { it.min }?.min?.toInt() ?: 0

            val middleDayTemps = mutableListOf<Float>()
            val middleNightTemps = mutableListOf<Float>()
            for (index in 0 until tempsSize - 1) {
                middleDayTemps.add(
                    index,
                    ((dailyTemperatures[index].max.toInt() + dailyTemperatures[index + 1].max.toInt()) / 2f)
                )
                middleNightTemps.add(
                    index,
                    ((dailyTemperatures[index].min.toInt() + dailyTemperatures[index + 1].min.toInt()) / 2f)
                )
            }

            val measuredDayTempText = textMeasurer.measure(
                text = AnnotatedString("$currentDayTemp°"),
                style = TextStyle(
                    color = if (isDark) Color.White else Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            val measuredNightTempText = textMeasurer.measure(
                text = AnnotatedString("$currentNightTemp°"),
                style = TextStyle(
                    color = if (isDark) Color.White else Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            val paint1 = Paint().apply {
                color = if (isDark) Color.Black else Color.White
                style = PaintingStyle.Fill
            }

            val paint2 = Paint().apply {
                color = if (isDark) Color.White else Color.Gray
                style = PaintingStyle.Stroke
                strokeWidth = 6f.toDp().toPx()
                isAntiAlias = true
            }

            val dayPath = Path()
            val night = Path()

            onDrawBehind {
                drawDayTemperatureLine(
                    dayPath,
                    tempsSize,
                    maxTemperature,
                    minTemperature,
                    currentDayTemp,
                    currentIndex,
                    middleDayTemps
                )

                drawNightTemperatureLine(
                    night,
                    tempsSize,
                    maxTemperature,
                    minTemperature,
                    currentNightTemp,
                    currentIndex,
                    middleNightTemps
                )

                // 使用原生 Canvas 对象绘制
                drawIntoCanvas { canvas ->
                    drawTemperaturePoint(
                        canvas,
                        paint1,
                        paint2,
                        maxTemperature,
                        minTemperature,
                        currentDayTemp,
                        currentNightTemp
                    )
                }

                drawTemperatureText(
                    measuredDayTempText,
                    measuredNightTempText,
                    maxTemperature,
                    minTemperature,
                    currentDayTemp,
                    currentNightTemp
                )
            }
        }
    )
}

fun DrawScope.drawDayTemperatureLine(
    path: Path,
    tempsSize: Int,
    maxTemperature: Int,
    minTemperature: Int,
    currentDayTemp: Int,
    currentIndex: Int,
    middleDayTemps: List<Float>
) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val tempRange = -(canvasHeight / (maxTemperature - minTemperature))

    if (currentIndex == 0) {
        path.moveTo(canvasWidth / 2, (currentDayTemp - maxTemperature) * tempRange)
        path.lineTo(canvasWidth, (middleDayTemps.first() - maxTemperature) * tempRange)
    } else if (currentIndex > 0 && currentIndex < tempsSize - 1) {
        path.moveTo(0f, (middleDayTemps[currentIndex - 1] - maxTemperature) * tempRange)
        path.lineTo(canvasWidth / 2, (currentDayTemp - maxTemperature) * tempRange)
        path.lineTo(canvasWidth, (middleDayTemps[currentIndex] - maxTemperature) * tempRange)
    } else {
        path.moveTo(0f, (middleDayTemps.last() - maxTemperature) * tempRange)
        path.lineTo(canvasWidth / 2, (currentDayTemp - maxTemperature) * tempRange)
    }

    drawPath(
        path = path,
        color = BrokenLineColor,
        style = Stroke(width = 8f.toDp().toPx())
    )
}

fun DrawScope.drawNightTemperatureLine(
    path: Path,
    tempsSize: Int,
    maxTemperature: Int,
    minTemperature: Int,
    currentNightTemp: Int,
    currentIndex: Int,
    middleNightTemps: List<Float>
) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val tempRange = -(canvasHeight / (maxTemperature - minTemperature))

    if (currentIndex == 0) {
        path.moveTo(canvasWidth / 2, (currentNightTemp - maxTemperature) * tempRange)
        path.lineTo(canvasWidth, (middleNightTemps.first() - maxTemperature) * tempRange)
    } else if (currentIndex > 0 && currentIndex < tempsSize - 1) {
        path.moveTo(0f, (middleNightTemps[currentIndex - 1] - maxTemperature) * tempRange)
        path.lineTo(canvasWidth / 2, (currentNightTemp - maxTemperature) * tempRange)
        path.lineTo(canvasWidth, (middleNightTemps[currentIndex] - maxTemperature) * tempRange)
    } else {
        path.moveTo(0f, (middleNightTemps.last() - maxTemperature) * tempRange)
        path.lineTo(canvasWidth / 2, (currentNightTemp - maxTemperature) * tempRange)
    }

    drawPath(
        path = path,
        color = BrokenLineColor,
        style = Stroke(width = 8f.toDp().toPx())
    )
}

@ExperimentalTextApi
fun DrawScope.drawTemperatureText(
    dayTempText: TextLayoutResult,
    nightTempText: TextLayoutResult,
    maxTemperature: Int,
    minTemperature: Int,
    currentDayTemp: Int,
    currentNightTemp: Int
) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val tempRange = -(canvasHeight / (maxTemperature - minTemperature))

    drawText(
        textLayoutResult = dayTempText,
        topLeft = Offset(
            x = (canvasWidth / 2) - (dayTempText.size.width / 2),
            y = ((currentDayTemp - maxTemperature) * tempRange) - (dayTempText.size.height * 1.5f)
        )
    )

    drawText(
        textLayoutResult = nightTempText,
        topLeft = Offset(
            x = (canvasWidth / 2) - (nightTempText.size.width / 2),
            y = ((currentNightTemp - maxTemperature) * tempRange) + (nightTempText.size.height / 1.5f)
        )
    )
}

fun DrawScope.drawTemperaturePoint(
    canvas: Canvas,
    paint1: Paint,
    paint2: Paint,
    maxTemperature: Int,
    minTemperature: Int,
    currentDayTemp: Int,
    currentNightTemp: Int
) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val tempRange = -(canvasHeight / (maxTemperature - minTemperature))

    canvas.drawCircle(
        center = Offset(
            x = canvasWidth / 2,
            y = (currentDayTemp - maxTemperature) * tempRange
        ),
        radius = 18f.toDp().toPx(),
        paint = paint1
    )

    canvas.drawCircle(
        center = Offset(
            x = canvasWidth / 2,
            y = (currentDayTemp - maxTemperature) * tempRange
        ),
        radius = 9f.toDp().toPx(),
        paint = paint2
    )

    canvas.drawCircle(
        center = Offset(
            x = canvasWidth / 2,
            y = (currentNightTemp - maxTemperature) * tempRange
        ),
        radius = 18f.toDp().toPx(),
        paint = paint1
    )

    canvas.drawCircle(
        center = Offset(
            x = canvasWidth / 2,
            y = (currentNightTemp - maxTemperature) * tempRange
        ),
        radius = 9f.toDp().toPx(),
        paint = paint2
    )
}

@ExperimentalTextApi
@Preview(name = "15日温度趋势折线", showBackground = true,
    device = "spec:width=1440px,height=2880px,dpi=560", locale = "zh-rCN", showSystemUi = false,
    backgroundColor = 0xFFFFFFFF, fontScale = 1.0f
)
@Composable
fun DrawDailyTemperatureLineChartPreview() {
    MyWeatherTheme {
        LazyRow(modifier = Modifier.fillMaxSize().padding(vertical = 30.dp)) {
            items(1) {
                DrawDailyTemperatureLineChart(
                    dailyTemperatures = listOf(
                        DailyResponse.Daily.Temperature(
                            date = Date(),
                            max = 35f,
                            min = 25f
                        ),
                        DailyResponse.Daily.Temperature(
                            date = Date(),
                            max = 35f,
                            min = 25f
                        )
                    ),
                    currentDayTemp = 35,
                    currentNightTemp = 25,
                    currentIndex = 0
                )
            }
        }
    }
}