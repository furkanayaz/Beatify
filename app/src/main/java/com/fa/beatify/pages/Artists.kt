package com.fa.beatify.pages

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fa.beatify.R
import com.fa.beatify.controllers.ImageController
import com.fa.beatify.models.ArtistModel
import com.fa.beatify.ui.theme.GridStrokeColor
import com.fa.beatify.ui.theme.GridStrokeColor2
import com.fa.beatify.ui.theme.GridStrokeColor3
import com.fa.beatify.ui.theme.Transparent
import com.fa.beatify.ui.theme.currentColor
import com.fa.beatify.viewmodels.ArtistsVM

@Composable
fun Artist(navController: NavHostController, topPadding: Dp, bottomPadding: Dp, tfSearch: MutableState<String>, genreId: Int) {
    val viewModel: ArtistsVM = viewModel()
    val artistList = viewModel.getArtists().observeAsState()
    viewModel.allArtists(genreId = genreId)

    val oddPaddingValues = PaddingValues(top = 7.5.dp, bottom = 7.5.dp, start = 15.0.dp, end = 7.5.dp)
    val evenPaddingValues = PaddingValues(top = 7.5.dp, bottom = 7.5.dp, end = 15.0.dp, start = 7.5.dp)

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = currentColor().screenBg)
        .padding(
            top = topPadding,
            bottom = bottomPadding
        )
    ) {
        artistList.value?.let {
            val rowShape = RoundedCornerShape(size = 10.0.dp)
            val gradientColors: Brush = Brush.horizontalGradient(
                colors = listOf(
                    GridStrokeColor, GridStrokeColor2, GridStrokeColor3
                )
            )

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(count = 2)
            ) {
                val tempArtists: List<ArtistModel> = artistList.value!!.filter { artistModel: ArtistModel -> artistModel.name?.lowercase()!!.contains(tfSearch.value.lowercase()) }

                items(count = tempArtists.count()) { pos: Int ->
                    val model: ArtistModel = tempArtists[pos]

                    Box(modifier = Modifier
                        .aspectRatio(ratio = 1.0f)
                        .padding(if (pos % 2 == 0) oddPaddingValues else evenPaddingValues)
                        .background(color = Transparent)
                        .clip(shape = rowShape)
                        .border(width = 1.5.dp, brush = gradientColors, shape = rowShape)
                        .clickable {
                            ImageController.ARTIST_IMAGE = model.pictureMedium
                            navController.navigate(route = "artist_detail/${model.id}/${model.name}")
                        },
                        contentAlignment = Alignment.BottomCenter, content = {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                model = model.picture!!,
                                contentDescription = model.name
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = currentColor().gridCategoryBg)
                                    .padding(bottom = 30.0.dp),
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
                                    color = currentColor().textColor
                                )
                            )
                        })
                }
            }
        }?: println("Null pointer exception...")
    }
}