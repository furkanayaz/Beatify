package com.fa.beatify.pages

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fa.beatify.R
import com.fa.beatify.controllers.MusicController
import com.fa.beatify.services.MusicPlayer
import com.fa.beatify.ui.theme.GridStrokeColor
import com.fa.beatify.ui.theme.GridStrokeColor2
import com.fa.beatify.ui.theme.GridStrokeColor3
import com.fa.beatify.ui.theme.LtPrimary
import com.fa.beatify.ui.theme.Transparent
import com.fa.beatify.ui.theme.currentColor
import com.fa.beatify.viewmodels.LikesVM

@Composable
fun Likes(topPadding: Dp, bottomPadding: Dp, tfSearch: MutableState<String>) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val musicPlayerService = Intent(context, MusicPlayer::class.java)

    val viewModel: LikesVM = viewModel()
    val likesData = viewModel.getLikesData().observeAsState()
    val tempLikeList = likesData.value

    val playingController: State<Boolean> = MusicController.playingController.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = currentColor().screenBg)
    ) {

        tempLikeList?.let {
            val rowShape = RoundedCornerShape(size = 10.0.dp)
            val gradientColors: Brush = Brush.horizontalGradient(
                colors = listOf(
                    GridStrokeColor, GridStrokeColor2, GridStrokeColor3
                )
            )

            if (tempLikeList.isNotEmpty()) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = topPadding, bottom = bottomPadding)
                        .background(color = currentColor().screenBg)
                ) {
                    val likeList = tempLikeList.filter { likeEntities ->
                        likeEntities.musicName.lowercase().contains(tfSearch.value.lowercase())
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Transparent)
                                .padding(top = 15.0.dp, start = 15.0.dp, end = 15.0.dp)
                        )
                    }

                    items(count = likeList.count()) { pos: Int ->
                        val likeModel = likeList[pos]

                        val musicName = likeModel.musicName
                        val musicDuration = likeModel.musicDuration

                        /*if (playingController.value) {
                            AlertDialog(
                                modifier = Modifier.height(height = (configuration.screenHeightDp / 3).dp),
                                containerColor = currentColor().screenBg,
                                text = {
                                    val progressValue = remember {
                                        mutableStateOf(value = IntSize.Zero)
                                    }

                                    val progressAnim = animateFloatAsState(
                                        targetValue = if (playingController.value) progressValue.value.width.toFloat() else 0.0f,
                                        animationSpec = tween(
                                            delayMillis = 0, durationMillis = 30000
                                        )
                                    )

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(shape = RoundedCornerShape(size = 15.0.dp)),
                                            model = likeList[pos].musicImage,
                                            contentScale = ContentScale.FillBounds,
                                            contentDescription = stringResource(id = R.string.music_image)
                                        )
                                        Canvas(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                top = 24.0.dp, start = 12.0.dp, end = 12.0.dp
                                            ), onDraw = {
                                            val componentSize = size
                                            progressValue.value = IntSize(
                                                width = componentSize.width.toInt(),
                                                height = componentSize.height.toInt()
                                            )

                                            drawLine(
                                                color = White,
                                                alpha = 1.0f,
                                                strokeWidth = 50.0f,
                                                cap = StrokeCap.Round,
                                                start = Offset(x = 0.0f, y = center.y),
                                                end = Offset(
                                                    x = componentSize.width, y = center.y
                                                )
                                            )
                                            drawLine(
                                                color = LtPrimary,
                                                alpha = 1.0f,
                                                strokeWidth = 50.0f,
                                                cap = StrokeCap.Round,
                                                start = Offset(x = 0.0f, y = center.y),
                                                end = Offset(
                                                    x = progressAnim.value, y = center.y
                                                )
                                            )
                                        })
                                    }
                                },
                                onDismissRequest = {  },
                                confirmButton = {
                                    Text(
                                        modifier = Modifier
                                            .background(color = Transparent)
                                            .padding(
                                                top = 24.0.dp,
                                                start = 10.0.dp,
                                                end = 10.0.dp,
                                                bottom = 10.0.dp
                                            )
                                            .clickable {
                                                context.stopService(musicPlayerService)
                                            },
                                        text = stringResource(id = R.string.close),
                                        style = TextStyle(
                                            fontSize = 16.0.sp, fontFamily = FontFamily(
                                                Font(
                                                    resId = R.font.sofiaprosemibold,
                                                    weight = FontWeight.Medium
                                                )
                                            ), color = LtPrimary
                                        )
                                    )
                                },
                                properties = DialogProperties(
                                    decorFitsSystemWindows = true,
                                    dismissOnBackPress = false,
                                    dismissOnClickOutside = false
                                )
                            )
                        }*/

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 15.0.dp, start = 15.0.dp, end = 15.0.dp)
                                .clip(shape = rowShape)
                                .background(color = currentColor().gridArtistBg)
                                .border(width = 1.5.dp, brush = gradientColors, shape = rowShape)
                                .clickable {
                                    if (playingController.value) {
                                        context.stopService(musicPlayerService)
                                    }
                                    context.startService(musicPlayerService.putExtra("url", likeModel.musicPreview))
                                },
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier.size(size = (configuration.screenWidthDp / 5).dp),
                                model = likeModel.musicImage,
                                contentDescription = stringResource(id = R.string.music_image)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(start = 16.0.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    modifier = Modifier.width(width = (configuration.screenWidthDp / 2).dp),
                                    text = musicName,
                                    style = TextStyle(
                                        color = currentColor().textColor, fontSize = 14.0.sp, fontFamily = FontFamily(
                                            Font(
                                                resId = R.font.sofiaprosemibold,
                                                weight = FontWeight.SemiBold
                                            )
                                        )
                                    ),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                                Text(
                                    text = musicDuration, style = TextStyle(
                                        color = currentColor().textColor, fontSize = 12.0.sp, fontFamily = FontFamily(
                                            Font(
                                                resId = R.font.sofiaprosemibold,
                                                weight = FontWeight.SemiBold
                                            )
                                        )
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 16.0.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { viewModel.deleteLike(like = likeModel) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.heart_f),
                                        tint = LtPrimary,
                                        contentDescription = stringResource(id = R.string.like)
                                    )
                                }
                            }
                        }
                    }
                }

            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = currentColor().screenBg),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.0.dp, end = 15.0.dp),
                        text = stringResource(id = R.string.no_likes),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 18.0.sp, fontFamily = FontFamily(
                                Font(
                                    resId = R.font.sofiaproregular, weight = FontWeight.Medium
                                )
                            ), color = currentColor().textColor
                        )
                    )
                }
            }
        }

    }
}