package com.fa.beatify.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingPage(
    controller: Int,
    topPadding: Dp,
    bottomPadding: Dp,
    oddPaddingValues: PaddingValues?,
    evenPaddingValues: PaddingValues?
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0.0f, targetValue = 1000.0f, animationSpec = infiniteRepeatable(
            animation = tween(delayMillis = 0, durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val shimmerBrush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    val cellCount: Int = when (controller) {
        0, 3, 4 -> 1
        1, 2 -> 2
        else -> 0
    }

    val bgImageHeight: Dp = (LocalConfiguration.current.screenHeightDp / 4).dp
    val itemShape = RoundedCornerShape(size = 10.0.dp)

    LazyVerticalGrid(modifier = Modifier
        .fillMaxSize()
        .padding(top = topPadding, bottom = (bottomPadding.value + 15.0).dp),
        columns = GridCells.Fixed(count = cellCount),
        content = {

            if (controller == 3) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = bgImageHeight)
                            .background(brush = shimmerBrush)
                    )
                }
            }

            items(count = 10) { pos: Int ->
                if (controller in 1..2) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(ratio = 1.0f)
                            .padding(if (pos % 2 == 0) oddPaddingValues!! else evenPaddingValues!!) // padding values object not null already when position 2
                            .clip(shape = itemShape)
                            .background(brush = shimmerBrush)
                    )
                }else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 100.0.dp)
                            .padding(top = 15.0.dp, start = 15.0.dp, end = 15.0.dp)
                            .clip(shape = itemShape)
                            .background(brush = shimmerBrush)
                    )
                }
            }
        })
}