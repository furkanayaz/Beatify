package com.fa.beatify.pages.music_categories

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
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.constants.controller.ListState
import com.fa.beatify.models.Genre
import com.fa.beatify.models.GenreModel
import com.fa.beatify.ui.theme.CustomGradient
import com.fa.beatify.ui.theme.Transparent
import com.fa.beatify.ui.theme.currentColor
import com.fa.beatify.utils.NavUtility

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
    val gridState: LazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = ListState.CATEGORIES_STATE
    )

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
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(count = 2),
                state = gridState
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
                            navController
                                .navigate(
                                    route = NavUtility.Artists.withSourceArgs(
                                        model.id.toString(), model.name!!.replace("/", " ")
                                    )
                                )
                                .also {
                                    ListState.CATEGORIES_STATE = gridState.firstVisibleItemIndex
                                }
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
                                .padding(top = 15.0.dp, bottom = 15.0.dp),
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