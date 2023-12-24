package com.zqc.mdoel.indicator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 自定义水平滑动的 ViewPager 指示器
 * @param pagerState pager状态
 * @param pageCount 页数
 * @param modifier Modifier
 * @param activeColor 活动页面指示器的颜色
 * @param inactiveColor 非活动页面指示器的颜色
 * @param activeSize 活动页面指示器的大小
 * @param activeSize 非活动页面指示器的大小
 * @param indicatorSpacing 指示器间隔
 */
@ExperimentalFoundationApi
@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colors.background,
    inactiveColor: Color = Color.Gray,
    activeSize: Dp = 3.dp,
    inactiveSize: Dp = activeSize,
    indicatorSpacing: Dp = 15.dp
) {
    HorizontalIndicator(
        pagerState = pagerState,
        pageCount = pageCount,
        modifier = modifier,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        activeSize = activeSize,
        inactiveSize = inactiveSize,
        indicatorSpacing = indicatorSpacing
    )
}

/**
 * 水平指示器
 */
@ExperimentalFoundationApi
@Composable
private fun HorizontalIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color,
    inactiveColor: Color,
    activeSize: Dp,
    inactiveSize: Dp,
    indicatorSpacing: Dp
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val totalIndicatorWidth =
            (pageCount * indicatorSpacing.toPx() - indicatorSpacing.toPx()) + (activeSize.toPx() * pageCount)

        val start = (canvasWidth - totalIndicatorWidth) / 2

        for (index in 0 until pageCount) {
            val indicatorColor = if (index == pagerState.currentPage) activeColor else inactiveColor
            val indicatorRadius = if (index == pagerState.currentPage) activeSize else inactiveSize

            drawCircle(
                color = indicatorColor,
                radius = indicatorRadius.toPx(),
                center = Offset(start + index * (indicatorSpacing.toPx() + activeSize.toPx()), canvasHeight / 2)
            )
        }
    }
}