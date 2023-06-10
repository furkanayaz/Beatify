package com.fa.beatify.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fa.beatify.R
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.models.TrackModel
import com.fa.beatify.ui.theme.GridStrokeColor
import com.fa.beatify.ui.theme.GridStrokeColor2
import com.fa.beatify.ui.theme.GridStrokeColor3
import com.fa.beatify.ui.theme.LtPrimary
import com.fa.beatify.ui.theme.Transparent
import com.fa.beatify.ui.theme.White
import com.fa.beatify.ui.theme.currentColor
import com.fa.beatify.viewmodels.AlbumDetailVM

@Composable
fun AlbumDetail(topPadding: Dp, bottomPadding: Dp, tfSearch: MutableState<String>, albumId: Int) {
    val viewModel: AlbumDetailVM = viewModel()
    viewModel.getTracks(albumId = albumId)
    val tempTrackList = viewModel.getTrackList().observeAsState()

    val playingController: State<Boolean> = viewModel.getPlayingController().collectAsState(initial = false)

    val likeChecked = remember {
        mutableStateListOf(-1)
    }

    val configuration = LocalConfiguration.current

    val trackList = tempTrackList.value?.filter { trackModel: TrackModel ->
        trackModel.title?.lowercase()!!.contains(tfSearch.value.lowercase())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = currentColor().screenBg)
    ) {
        trackList?.let { modelList ->
            val rowShape = RoundedCornerShape(size = 10.0.dp)
            val gradientColors: Brush = Brush.horizontalGradient(
                colors = listOf(
                    GridStrokeColor, GridStrokeColor2, GridStrokeColor3
                )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topPadding, bottom = bottomPadding)
                    .background(color = currentColor().screenBg)
            ) {
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Transparent)
                            .padding(top = 15.0.dp, start = 15.0.dp, end = 15.0.dp)
                    )
                }
                items(count = modelList.count()) { pos: Int ->
                    val trackModel = modelList[pos]

                    val musicImage =
                        "https://e-cdns-images.dzcdn.net/images/cover/${trackModel.md5Image}/500x500-000000-80-0-0.jpg"
                    val musicDuration =
                        viewModel.getDuration(durationInSeconds = trackModel.duration!!)

                    if (playingController.value) {
                        AlertDialog(
                            modifier = Modifier.height(height = (configuration.screenHeightDp / 3).dp),
                            containerColor = currentColor().screenBg,
                            text = {
                                val progressValue = remember {
                                    mutableStateOf(value = IntSize.Zero)
                                }

                                val progressAnim = animateFloatAsState(
                                    targetValue = if (playingController.value) progressValue.value.width.toFloat() else 0.0f,
                                    animationSpec = tween(delayMillis = 0, durationMillis = 30000)
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
                                        model = musicImage,
                                        contentScale = ContentScale.FillBounds,
                                        contentDescription = stringResource(id = R.string.music_image)
                                    )
                                    Canvas(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 24.0.dp, start = 12.0.dp, end = 12.0.dp),
                                        onDraw = {
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
                                                end = Offset(x = componentSize.width, y = center.y)
                                            )
                                            drawLine(
                                                color = LtPrimary,
                                                alpha = 1.0f,
                                                strokeWidth = 50.0f,
                                                cap = StrokeCap.Round,
                                                start = Offset(x = 0.0f, y = center.y),
                                                end = Offset(x = progressAnim.value, y = center.y)
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
                                            viewModel.stopMusic()
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
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.0.dp, start = 15.0.dp, end = 15.0.dp)
                            .clip(shape = rowShape)
                            .background(color = currentColor().gridArtistBg)
                            .border(width = 1.5.dp, brush = gradientColors, shape = rowShape)
                            .clickable {
                                viewModel.playMusic(url = trackModel.preview!!)
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AsyncImage(
                            modifier = Modifier.size(size = (configuration.screenWidthDp / 5).dp),
                            model = musicImage,
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
                                text = trackModel.title!!,
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
                            IconButton(onClick = {
                                if (likeChecked.contains(trackModel.id)) {
                                    likeChecked.remove(trackModel.id); viewModel.deleteLike(
                                        like = LikeEntities(
                                            id = 0,
                                            musicId = trackModel.id!!,
                                            musicImage = "",
                                            musicName = "",
                                            musicDuration = "",
                                            musicPreview = ""
                                        )
                                    )
                                } else {
                                    likeChecked.add(trackModel.id!!); viewModel.insertLike(
                                        like = LikeEntities(
                                            id = 0,
                                            musicId = trackModel.id!!,
                                            musicImage = musicImage,
                                            musicName = trackModel.title!!,
                                            musicDuration = musicDuration,
                                            musicPreview = trackModel.preview!!
                                        )
                                    )
                                }
                            }) {
                                Icon(
                                    painter = if (likeChecked.contains(trackModel.id)) painterResource(
                                        id = R.drawable.heart_f
                                    ) else painterResource(id = R.drawable.heart),
                                    tint = LtPrimary,
                                    contentDescription = stringResource(id = R.string.like)
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}