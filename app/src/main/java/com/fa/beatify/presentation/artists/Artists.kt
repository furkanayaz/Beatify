package com.fa.beatify.presentation.artists

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.models.Artist
import com.fa.beatify.presentation.FailureMusicCategories
import com.fa.beatify.presentation.LoadingPage

@Composable
fun Artist(
    viewModel: ArtistsVM,
    navController: NavHostController,
    topPadding: Dp,
    bottomPadding: Dp,
    tfSearch: MutableState<String>,
    genreId: String
) {
    val artists: State<BeatifyResponse<List<Artist>>?> = viewModel.artists.observeAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchData(genreId = genreId.toInt())
    }

    val oddPaddingValues =
        PaddingValues(top = 7.5.dp, bottom = 7.5.dp, start = 15.0.dp, end = 7.5.dp)
    val evenPaddingValues =
        PaddingValues(top = 7.5.dp, bottom = 7.5.dp, end = 15.0.dp, start = 7.5.dp)

    when (artists.value) {
        is BeatifyResponse.Success -> SuccessArtist(
            artistDto = artists.value,
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