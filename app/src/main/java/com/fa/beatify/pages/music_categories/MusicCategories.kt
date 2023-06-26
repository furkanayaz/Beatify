
package com.fa.beatify.pages.music_categories

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fa.beatify.R
import com.fa.beatify.models.GenreModel
import com.fa.beatify.ui.theme.CustomGradient
import com.fa.beatify.ui.theme.Transparent
import com.fa.beatify.ui.theme.currentColor

@Composable
fun MusicCategories(viewModel: MusicCategoriesVM, navController: NavHostController, topPadding: Dp, bottomPadding: Dp, tfSearch: MutableState<String>) {
    val genreList = viewModel.genres.observeAsState()
    viewModel.allGenres()

    val itemAnimEnabled = remember {
        mutableStateOf(value = false)
    }

    val itemAnimAlpha = animateFloatAsState(targetValue = if (itemAnimEnabled.value) 1.0f else 0.0f, tween(delayMillis = 0, durationMillis = 1000))

    val oddPaddingValues = PaddingValues(top = 7.5.dp, bottom = 7.5.dp, start = 15.0.dp, end = 7.5.dp)
    val evenPaddingValues = PaddingValues(top = 7.5.dp, bottom = 7.5.dp, end = 15.0.dp, start = 7.5.dp)

    LaunchedEffect(key1 = true) {
        itemAnimEnabled.value = true
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = currentColor().screenBg)
        .padding(
            top = topPadding,
            bottom = bottomPadding
        )
    ) {
        genreList.value?.let { genreModels ->
            val rowShape = RoundedCornerShape(size = 10.0.dp)
            val gradientColors: Brush = Brush.horizontalGradient(
                colors = CustomGradient
            )

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(count = 2)
            ) {
                val tempList: List<GenreModel> = genreModels.filter { genreModel: GenreModel -> genreModel.name!!.lowercase().contains(tfSearch.value.lowercase()) }

                items(count = tempList.count()) { pos: Int ->
                    val model: GenreModel = tempList[pos]
                    Box(modifier = Modifier
                        .aspectRatio(ratio = 1.0f)
                        .padding(if (pos % 2 == 0) oddPaddingValues else evenPaddingValues)
                        .background(color = Transparent)
                        .clip(shape = rowShape)
                        .alpha(alpha = itemAnimAlpha.value)
                        .border(width = 1.5.dp, brush = gradientColors, shape = rowShape)
                        .clickable {
                            navController.navigate(
                                route = "artists/${model.id!!}/${
                                    model.name!!.replace(
                                        "/",
                                        " "
                                    )
                                }"
                            )
                        },
                        contentAlignment = Alignment.BottomCenter, content = {
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
                                    fontSize = 18.0.sp,
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.sofiaprosemibold,
                                            weight = FontWeight.SemiBold
                                        )
                                    ),
                                    color = currentColor().text
                                )
                            )
                        })
                }
            }
        }?: println("Null pointer exception...")
    }
}