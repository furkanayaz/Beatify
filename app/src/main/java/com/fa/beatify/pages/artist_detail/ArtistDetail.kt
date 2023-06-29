package com.fa.beatify.pages.artist_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.models.Album
import com.fa.beatify.pages.FailureMusicCategories
import com.fa.beatify.pages.LoadingPage

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
    val artistDetail: State<BeatifyResponse<Album>?> = viewModel.albumModel.observeAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchData(artistId = artistId.toInt())
    }

    when (artistDetail.value) {
        is BeatifyResponse.Success -> SuccessArtistDetail(
            viewModel = viewModel,
            artistDetail = artistDetail.value,
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