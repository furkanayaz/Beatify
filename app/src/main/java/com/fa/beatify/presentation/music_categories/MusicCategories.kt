package com.fa.beatify.presentation.music_categories

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
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.models.Genre
import com.fa.beatify.presentation.FailureMusicCategories
import com.fa.beatify.presentation.LoadingPage

@Composable
fun MusicCategories(
    viewModel: MusicCategoriesVM,
    navController: NavHostController,
    topPadding: Dp,
    bottomPadding: Dp,
    tfSearch: MutableState<String>
) {
    val lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val genres: State<BeatifyResponse<List<Genre>>?> = viewModel.genres.collectAsState()

    LaunchedEffect(key1 = Unit) {
        lifeCycleOwner.lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.fetchData()
        }
    }

    val oddPaddingValues =
        PaddingValues(top = 7.5.dp, bottom = 7.5.dp, start = 15.0.dp, end = 7.5.dp)
    val evenPaddingValues =
        PaddingValues(top = 7.5.dp, bottom = 7.5.dp, end = 15.0.dp, start = 7.5.dp)

    when (genres.value) {
        is BeatifyResponse.Success -> SuccessMusicCategories(
            genres = genres.value,
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
            controller = 1,
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