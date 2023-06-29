package com.fa.beatify.pages.artists

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.models.Artist
import com.fa.beatify.pages.FailureMusicCategories
import com.fa.beatify.pages.LoadingPage

@Composable
fun Artist(
    viewModel: ArtistsVM,
    navController: NavHostController,
    topPadding: Dp,
    bottomPadding: Dp,
    tfSearch: MutableState<String>,
    genreId: String
) {
    val artist: State<BeatifyResponse<Artist>?> = viewModel.artists.observeAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchData(genreId = genreId.toInt())
    }

    val oddPaddingValues =
        PaddingValues(top = 7.5.dp, bottom = 7.5.dp, start = 15.0.dp, end = 7.5.dp)
    val evenPaddingValues =
        PaddingValues(top = 7.5.dp, bottom = 7.5.dp, end = 15.0.dp, start = 7.5.dp)

    when (artist.value) {
        is BeatifyResponse.Success -> SuccessArtist(
            artist = artist.value,
            navController = navController,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            oddPaddingValues = oddPaddingValues,
            evenPaddingValues = evenPaddingValues,
            tfSearch = tfSearch
        )

        is BeatifyResponse.Failure -> FailureMusicCategories(
            topPadding = topPadding, bottomPadding = bottomPadding
        )

        is BeatifyResponse.Loading -> LoadingPage(
            controller = 2,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            oddPaddingValues = oddPaddingValues,
            evenPaddingValues = evenPaddingValues
        )

        else -> FailureMusicCategories(
            topPadding = topPadding, bottomPadding = bottomPadding
        )
    }

}