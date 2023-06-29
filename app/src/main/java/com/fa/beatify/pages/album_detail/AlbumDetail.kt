package com.fa.beatify.pages.album_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.Dp
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.models.Track
import com.fa.beatify.pages.FailureMusicCategories
import com.fa.beatify.pages.LoadingPage

@Composable
fun AlbumDetail(
    viewModel: AlbumDetailVM,
    topPadding: Dp,
    bottomPadding: Dp,
    tfSearch: MutableState<String>,
    artistName: String,
    albumName: String,
    albumId: String
) {
    val trackList: State<BeatifyResponse<Track>?> = viewModel.trackList.observeAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchData(albumId = albumId.toInt())
    }

    when (trackList.value) {
        is BeatifyResponse.Success -> SuccessAlbumDetail(
            viewModel = viewModel,
            albumDetail = trackList.value,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            artistName = artistName,
            tfSearch = tfSearch,
            albumName = albumName
        )

        is BeatifyResponse.Failure -> FailureMusicCategories(
            topPadding = topPadding, bottomPadding = bottomPadding, code = trackList.value?.code
        )

        is BeatifyResponse.Loading -> LoadingPage(
            controller = 4,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            oddPaddingValues = null,
            evenPaddingValues = null
        )

        else -> FailureMusicCategories(
            topPadding = topPadding, bottomPadding = bottomPadding
        )
    }
}