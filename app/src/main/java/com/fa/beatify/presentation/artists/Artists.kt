package com.fa.beatify.presentation.artists

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.fa.beatify.data.response.Response
import com.fa.beatify.domain.models.Artist
import com.fa.beatify.presentation.FailurePage
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
    val lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val artists: State<Response<List<Artist>>?> = viewModel.artists.collectAsState()

    LaunchedEffect(key1 = Unit) {
        lifeCycleOwner.lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.fetchData(genreId = genreId.toInt())
        }
    }

    val oddPaddingValues =
        PaddingValues(top = 7.5.dp, bottom = 7.5.dp, start = 15.0.dp, end = 7.5.dp)
    val evenPaddingValues =
        PaddingValues(top = 7.5.dp, bottom = 7.5.dp, end = 15.0.dp, start = 7.5.dp)

    when (artists.value) {
        is Response.Success -> SuccessArtist(
            artistDto = artists.value,
            navController = navController,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            oddPaddingValues = oddPaddingValues,
            evenPaddingValues = evenPaddingValues,
            tfSearch = tfSearch
        )

        is Response.Failure -> FailurePage(
            topPadding = topPadding, bottomPadding = bottomPadding
        )

        is Response.Loading -> LoadingPage(
            controller = 2,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            oddPaddingValues = oddPaddingValues,
            evenPaddingValues = evenPaddingValues
        )

        else -> FailurePage(
            topPadding = topPadding, bottomPadding = bottomPadding
        )
    }

}