package com.fa.beatify.presentation.music_likes

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
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
import coil.compose.AsyncImage
import com.fa.beatify.R
import com.fa.beatify.utils.constants.ListState
import com.fa.beatify.utils.constants.controller.MusicController
import com.fa.beatify.data.models.Like
import com.fa.beatify.domain.models.PlayMusic
import com.fa.beatify.services.MusicService
import com.fa.beatify.presentation.ui.theme.CustomGradient
import com.fa.beatify.presentation.ui.theme.LtPrimary
import com.fa.beatify.presentation.ui.theme.Transparent
import com.fa.beatify.presentation.ui.theme.currentColor
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import com.fa.beatify.utils.repos.SearchRepo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun Likes(
    viewModel: LikesVM,
    topPadding: Dp,
    bottomPadding: Dp,
    tfSearch: MutableState<String>
) {
    val context: Context = LocalContext.current
    val configuration: Configuration = LocalConfiguration.current
    val listState: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = ListState.LIKES_STATE
    )

    LaunchedEffect(key1 = listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(timeoutMillis = 500L) // 500 millis içerisinde herhangi bir değişim olmazsa, collect eder.
            .collectLatest { index: Int ->
                ListState.LIKES_STATE = index
            }
    }

    val musicServiceService = Intent(context, MusicService::class.java)

    val connObserver: State<Connection.Status> = NetworkConnection(context = context).observe().collectAsState(initial = Connection.Status.Unavailable)

    val likesData: State<List<Like>> = viewModel.likesData.collectAsState(initial = emptyList())

    MusicController.likeList = likesData.value

    val playingController: State<Boolean> =
        MusicController.trackingController.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = currentColor().screenBg)
    ) {

        MusicController.likeList.apply {
            val rowShape = RoundedCornerShape(size = 10.0.dp)
            val gradientColors: Brush = Brush.horizontalGradient(
                colors = CustomGradient
            )

            if (this.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = topPadding, bottom = bottomPadding)
                        .background(color = currentColor().screenBg), state = listState
                ) {
                    val likeList: List<Like> = this@apply.filter {
                        SearchRepo(model = it, searchedText = tfSearch.value)::search.invoke()
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 15.0.dp, start = 15.0.dp, end = 15.0.dp)
                                .clip(shape = rowShape)
                                .background(color = currentColor().gridArtistBg)
                                .border(width = 1.5.dp, brush = gradientColors, shape = rowShape)
                                .clickable {
                                    if (connObserver.value == Connection.Status.Available) {
                                        MusicController.playingController.value = false
                                        MusicController.playMusic = PlayMusic(
                                            artistName = likeModel.artistName,
                                            albumName = likeModel.albumName,
                                            musicName = likeModel.musicName,
                                            musicImage = likeModel.musicImage,
                                            musicDuration = likeModel.musicDuration
                                        )

                                        if (playingController.value) {
                                            context.stopService(musicServiceService)
                                        }
                                        context.startService(
                                            musicServiceService.putExtra(
                                                "url", likeModel.musicPreview
                                            )
                                        )
                                    } else {
                                        Toast
                                            .makeText(
                                                context,
                                                context.getString(R.string.play_problem),
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
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
                                        color = currentColor().text,
                                        fontSize = 14.0.sp,
                                        fontFamily = FontFamily(
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
                                        color = currentColor().text,
                                        fontSize = 12.0.sp,
                                        fontFamily = FontFamily(
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
                            ), color = currentColor().text
                        )
                    )
                }
            }
        }

    }
}