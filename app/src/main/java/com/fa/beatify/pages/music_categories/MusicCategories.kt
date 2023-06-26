package com.fa.beatify.pages.music_categories

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fa.beatify.R
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.models.Genre
import com.fa.beatify.models.GenreModel
import com.fa.beatify.ui.theme.CustomGradient
import com.fa.beatify.ui.theme.Transparent
import com.fa.beatify.ui.theme.currentColor

@Composable
fun MusicCategories(
    viewModel: MusicCategoriesVM,
    navController: NavHostController,
    topPadding: Dp,
    bottomPadding: Dp,
    tfSearch: MutableState<String>
) {
    val genres: State<BeatifyResponse<Genre>?> = viewModel.genres.observeAsState()

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
            viewModel = viewModel, topPadding = topPadding, bottomPadding = bottomPadding
        )

        is BeatifyResponse.Loading -> LoadingMusicCategories(
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            oddPaddingValues = oddPaddingValues,
            evenPaddingValues = evenPaddingValues
        )

        else -> {}
    }
}

@Composable
fun SuccessMusicCategories(
    genres: BeatifyResponse<Genre>?,
    navController: NavHostController,
    topPadding: Dp,
    bottomPadding: Dp,
    oddPaddingValues: PaddingValues,
    evenPaddingValues: PaddingValues,
    tfSearch: MutableState<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = currentColor().screenBg)
            .padding(
                top = topPadding, bottom = bottomPadding
            )
    ) {
        genres?.data?.data.let { genreModels: ArrayList<GenreModel>? ->
            val rowShape = RoundedCornerShape(size = 10.0.dp)
            val gradientColors: Brush = Brush.horizontalGradient(
                colors = CustomGradient
            )

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(count = 2)
            ) {
                val tempList: List<GenreModel> = genreModels!!.filter { genreModel: GenreModel ->
                    genreModel.name!!.lowercase().contains(tfSearch.value.lowercase())
                }

                items(count = tempList.count()) { pos: Int ->
                    val model: GenreModel = tempList[pos]
                    Box(modifier = Modifier
                        .aspectRatio(ratio = 1.0f)
                        .padding(if (pos % 2 == 0) oddPaddingValues else evenPaddingValues)
                        .background(color = Transparent)
                        .clip(shape = rowShape)
                        .border(width = 1.5.dp, brush = gradientColors, shape = rowShape)
                        .clickable {
                            navController.navigate(
                                route = "artists/${model.id!!}/${
                                    model.name!!.replace(
                                        "/", " "
                                    )
                                }"
                            )
                        }, contentAlignment = Alignment.BottomCenter, content = {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            model = model.picture!!,
                            contentDescription = model.name!!
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = currentColor().gridCategoryBg)
                                .padding(top = 10.0.dp, bottom = 20.0.dp),
                            textAlign = TextAlign.Center,
                            text = model.name!!,
                            style = TextStyle(
                                fontSize = 18.0.sp, fontFamily = FontFamily(
                                    Font(
                                        resId = R.font.sofiaprosemibold,
                                        weight = FontWeight.SemiBold
                                    )
                                ), color = currentColor().text
                            )
                        )
                    })
                }
            }
        }
    }
}

@Composable
fun FailureMusicCategories(
    viewModel: MusicCategoriesVM, topPadding: Dp, bottomPadding: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = currentColor().screenBg)
            .padding(
                top = topPadding, bottom = bottomPadding
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.no_content), style = TextStyle(
                color = currentColor().text, fontSize = 15.0.sp, fontFamily = FontFamily(
                    Font(resId = R.font.sofiaprosemibold, weight = FontWeight.SemiBold)
                ), textAlign = TextAlign.Center
            )
        )
        Box(modifier = Modifier.clickable { viewModel.allGenres() },
            contentAlignment = Alignment.Center,
            content = {
                Text(
                    modifier = Modifier.padding(all = 15.0.dp),
                    text = stringResource(id = R.string.again),
                    style = TextStyle(
                        color = currentColor().primary, fontSize = 14.0.sp, fontFamily = FontFamily(
                            Font(resId = R.font.sofiaprolight, weight = FontWeight.Light)
                        ), textDecoration = TextDecoration.Underline
                    )
                )
            })
    }
}

@Composable
fun LoadingMusicCategories(
    topPadding: Dp,
    bottomPadding: Dp,
    oddPaddingValues: PaddingValues,
    evenPaddingValues: PaddingValues
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0.0f, targetValue = 1000.0f, animationSpec = infiniteRepeatable(
            animation = tween(delayMillis = 0, durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val shimmerBrush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    LazyVerticalGrid(modifier = Modifier.fillMaxSize().padding(top = topPadding, bottom = bottomPadding),
        columns = GridCells.Fixed(count = 2),
        content = {
            items(count = 10) { pos: Int ->
                Box(
                    modifier = Modifier
                        .aspectRatio(ratio = 1.0f)
                        .padding(if (pos % 2 == 0) oddPaddingValues else evenPaddingValues)
                        .clip(shape = RoundedCornerShape(size = 10.0.dp))
                        .background(brush = shimmerBrush)
                )
            }
        })
}