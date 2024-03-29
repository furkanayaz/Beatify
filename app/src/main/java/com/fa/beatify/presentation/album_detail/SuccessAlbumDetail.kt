package com.fa.beatify.presentation.album_detail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fa.beatify.R
import com.fa.beatify.data.response.Response
import com.fa.beatify.utils.constants.controller.MusicController
import com.fa.beatify.data.models.Like
import com.fa.beatify.domain.models.PlayMusic
import com.fa.beatify.domain.models.Track
import com.fa.beatify.services.MusicService
import com.fa.beatify.presentation.ui.theme.CustomGradient
import com.fa.beatify.presentation.ui.theme.LtPrimary
import com.fa.beatify.presentation.ui.theme.Transparent
import com.fa.beatify.presentation.ui.theme.currentColor
import com.fa.beatify.utils.repos.SearchRepo

@Composable
fun SuccessAlbumDetail(
    viewModel: AlbumDetailVM,
    albumDetail: Response<List<Track>>?,
    topPadding: Dp,
    bottomPadding: Dp,
    tfSearch: MutableState<String>,
    artistName: String,
    albumName: String,
) {
    val context: Context = LocalContext.current
    val configuration: Configuration = LocalConfiguration.current
    val musicServiceService = Intent(context, MusicService::class.java)

    val playingController: State<Boolean> =
        MusicController.trackingController.collectAsState(initial = false)

    val likeChecked: SnapshotStateList<Int> = remember {
        mutableStateListOf(-1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = currentColor().screenBg)
    ) {

        albumDetail?.data?.filter {
            SearchRepo(model = it, searchedText = tfSearch.value)::search.invoke()
        }?.let {
            MusicController.trackList = it
        }

        MusicController.trackList.let {
            val rowShape = RoundedCornerShape(size = 10.0.dp)
            val gradientColors: Brush = Brush.horizontalGradient(
                colors = CustomGradient
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
                items(count = MusicController.trackList.count()) { pos: Int ->
                    val trackModel = MusicController.trackList[pos]

                    val musicImage: String = viewModel.getImage(md5Image = trackModel.md5Image)
                    val musicDuration: String = viewModel.getDuration(durationInSeconds = trackModel.duration)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.0.dp, start = 15.0.dp, end = 15.0.dp)
                            .clip(shape = rowShape)
                            .background(color = currentColor().gridArtistBg)
                            .border(width = 1.5.dp, brush = gradientColors, shape = rowShape)
                            .clickable {
                                MusicController.playingController.value = false
                                MusicController.playMusic = PlayMusic(
                                    artistName = artistName,
                                    albumName = albumName,
                                    musicName = trackModel.title,
                                    musicImage = musicImage,
                                    musicDuration = musicDuration
                                )

                                if (playingController.value) {
                                    context.stopService(musicServiceService)
                                }
                                context.startService(
                                    musicServiceService.putExtra(
                                        "url",
                                        trackModel.preview
                                    )
                                )
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
                                text = trackModel.title,
                                style = TextStyle(
                                    color = currentColor().text, fontSize = 14.0.sp, fontFamily = FontFamily(
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
                                    color = currentColor().text, fontSize = 12.0.sp, fontFamily = FontFamily(
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
                                        like = Like(
                                            id = 0,
                                            artistName = "",
                                            albumName = "",
                                            musicId = trackModel.id,
                                            musicImage = "",
                                            musicName = "",
                                            musicDuration = "",
                                            musicPreview = ""
                                        )
                                    )
                                } else {
                                    likeChecked.add(trackModel.id); viewModel.insertLike(
                                        like = Like(
                                            id = 0,
                                            artistName = artistName,
                                            albumName = albumName,
                                            musicId = trackModel.id,
                                            musicImage = musicImage,
                                            musicName = trackModel.title,
                                            musicDuration = musicDuration,
                                            musicPreview = trackModel.preview
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