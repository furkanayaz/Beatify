package com.fa.beatify.presentation.artist_detail

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
import androidx.navigation.NavHostController
import com.fa.beatify.data.response.Response
import com.fa.beatify.domain.models.Album
import com.fa.beatify.presentation.FailurePage
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
    val lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val albums: State<Response<List<Album>>?> = viewModel.albums.collectAsState()

    LaunchedEffect(key1 = Unit) {
        lifeCycleOwner.lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.fetchData(artistId = artistId.toInt())
        }
    }

    when (albums.value) {
        is Response.Success -> SuccessArtistDetail(
            viewModel = viewModel,
            artistDetail = albums.value,
            navController = navController,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            artistName = artistName,
            tfSearch = tfSearch
        )

        is Response.Failure -> FailurePage(
            topPadding = topPadding, bottomPadding = bottomPadding
        )

        is Response.Loading -> LoadingPage(
            controller = 3,
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            oddPaddingValues = null,
            evenPaddingValues = null
        )

        else -> FailurePage(
            topPadding = topPadding, bottomPadding = bottomPadding
        )
    }
}