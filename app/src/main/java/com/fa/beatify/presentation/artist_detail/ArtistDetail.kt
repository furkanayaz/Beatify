package com.fa.beatify.presentation.artist_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.models.Album
import com.fa.beatify.presentation.FailureMusicCategories
import com.fa.beatify.presentation.LoadingPage

@Composable
fun ArtistDetail(
    viewModel: ArtistDetailVM,
    navController: NavHostController,
    topPadding: Dp,
    bottomPadding: Dp,
    tfSearch: MutableState<String>,
    artistId: String,
    artistName: String
) {
    val albums: State<BeatifyResponse<List<Album>>?> = viewModel.albums.observeAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchData(artistId = artistId.toInt())
    }

    when (albums.value) {
        is BeatifyResponse.Success -> SuccessArtistDetail(
            viewModel = viewModel,
            artistDetail = albums.value,
            navController = navController,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            artistName = artistName,
            tfSearch = tfSearch
        )

        is BeatifyResponse.Failure -> FailureMusicCategories(
            topPadding = topPadding, bottomPadding = bottomPadding
        )

        is BeatifyResponse.Loading -> LoadingPage(
            controller = 3,
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