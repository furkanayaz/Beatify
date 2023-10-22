package com.fa.beatify.presentation.album_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.models.Track
import com.fa.beatify.presentation.FailureMusicCategories
import com.fa.beatify.presentation.LoadingPage

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
    val lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val tracks: State<BeatifyResponse<List<Track>>?> = viewModel.tracks.collectAsState()

    LaunchedEffect(key1 = Unit) {
        lifeCycleOwner.lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.fetchData(albumId = albumId.toInt())
        }
    }

    when (tracks.value) {
        is BeatifyResponse.Success -> SuccessAlbumDetail(
            viewModel = viewModel,
            albumDetail = tracks.value,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            artistName = artistName,
            tfSearch = tfSearch,
            albumName = albumName
        )

        is BeatifyResponse.Failure -> FailureMusicCategories(
            topPadding = topPadding, bottomPadding = bottomPadding, code = tracks.value?.code
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