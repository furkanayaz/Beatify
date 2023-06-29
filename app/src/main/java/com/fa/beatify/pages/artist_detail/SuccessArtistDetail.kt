package com.fa.beatify.pages.artist_detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fa.beatify.R
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.constants.controller.ListState
import com.fa.beatify.constants.utils.ImageConstants
import com.fa.beatify.models.Album
import com.fa.beatify.models.AlbumModel
import com.fa.beatify.ui.theme.CustomGradient
import com.fa.beatify.ui.theme.Transparent
import com.fa.beatify.ui.theme.currentColor
import com.fa.beatify.utils.NavUtility

@Composable
fun SuccessArtistDetail(
    viewModel: ArtistDetailVM,
    artistDetail: BeatifyResponse<Album>?,
    navController: NavHostController,
    topPadding: Dp,
    bottomPadding: Dp,
    artistName: String,
    tfSearch: MutableState<String>
) {
    val configuration: Configuration = LocalConfiguration.current
    val listState: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = ListState.ARTIST_DETAIL_STATE
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding, bottom = bottomPadding)
            .background(color = currentColor().screenBg),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        artistDetail?.data?.data?.let { albumModelList ->
            val rowShape = RoundedCornerShape(size = 10.0.dp)
            val gradientColors: Brush = Brush.horizontalGradient(
                colors = CustomGradient
            )

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = (configuration.screenHeightDp / 4).dp)
                    .padding(bottom = 15.0.dp),
                contentScale = ContentScale.FillBounds,
                model = ImageConstants.ARTIST_IMAGE,
                contentDescription = stringResource(id = R.string.music_image)
            )

            val filteredAlbumModel = albumModelList.filter { albumModel: AlbumModel ->
                albumModel.title!!.lowercase().contains(tfSearch.value.lowercase())
            }

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .background(color = Transparent),
                state = listState,
                content = {
                    items(count = filteredAlbumModel.count(), itemContent = { pos: Int ->
                        val album: AlbumModel = filteredAlbumModel[pos]

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = 100.0.dp)
                                .padding(bottom = 15.0.dp, start = 15.0.dp, end = 15.0.dp)
                                .clip(shape = rowShape)
                                .background(color = currentColor().gridArtistBg)
                                .border(width = 1.5.dp, brush = gradientColors, shape = rowShape)
                                .clickable {
                                    navController
                                        .navigate(
                                            route = NavUtility.AlbumDetail.withSourceArgs(
                                                artistName,
                                                album.id.toString(),
                                                album.title!!.replace("/", "-")
                                            )
                                        )
                                        .also {
                                            ListState.ARTIST_DETAIL_STATE =
                                                listState.firstVisibleItemIndex
                                        }
                                },
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier.weight(weight = 0.3f),
                                contentScale = ContentScale.Crop,
                                model = album.cover,
                                contentDescription = album.title
                            )
                            Column(
                                modifier = Modifier
                                    .background(color = Transparent)
                                    .weight(weight = 0.7f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    modifier = Modifier
                                        .width(width = (configuration.screenWidthDp / 2).dp)
                                        .padding(start = 16.0.dp),
                                    textAlign = TextAlign.Start,
                                    maxLines = 1,
                                    text = album.title!!,
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
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.0.dp, start = 16.0.dp),
                                    textAlign = TextAlign.Start,
                                    text = viewModel.getReleaseDate(releaseDate = album.releaseDate!!),
                                    style = TextStyle(
                                        color = currentColor().text,
                                        fontSize = 12.0.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                resId = R.font.sofiaproregular,
                                                weight = FontWeight.Normal
                                            )
                                        )
                                    )
                                )
                            }
                        }
                    })
                })
        }
    }
}