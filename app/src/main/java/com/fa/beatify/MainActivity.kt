package com.fa.beatify

import com.fa.beatify.ui.theme.currentColor
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.fa.beatify.pages.album_detail.AlbumDetail
import com.fa.beatify.pages.artist_detail.ArtistDetail
import com.fa.beatify.pages.artists.Artist
import com.fa.beatify.constants.BottomBarConstants
import com.fa.beatify.constants.MusicConstants
import com.fa.beatify.models.PlayMusic
import com.fa.beatify.pages.music_categories.MusicCategories
import com.fa.beatify.pages.music_likes.Likes
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.fa.beatify.ui.theme.Transparent
import com.fa.beatify.ui.theme.White
import com.fa.beatify.ui.theme.Black
import com.fa.beatify.constants.MusicConstants.trackingController
import com.fa.beatify.pages.album_detail.AlbumDetailVM
import com.fa.beatify.pages.artist_detail.ArtistDetailVM
import com.fa.beatify.pages.artists.ArtistsVM
import com.fa.beatify.pages.music_categories.MusicCategoriesVM
import com.fa.beatify.pages.music_likes.LikesVM
import com.fa.beatify.ui.theme.BeatifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val musicCategoriesVM: MusicCategoriesVM by viewModels()
    private val likesVM: LikesVM by viewModels()
    private val artistsVM: ArtistsVM by viewModels()
    private val artistDetailVM: ArtistDetailVM by viewModels()
    private val albumDetailVM: AlbumDetailVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            BeatifyTheme(
                packageManager = packageManager,
                context = LocalContext.current,
                content = {
                    NavActivity(
                        musicCategoriesVM = musicCategoriesVM,
                        likesVM = likesVM,
                        artistsVM = artistsVM,
                        artistDetailVM = artistDetailVM,
                        albumDetailVM = albumDetailVM
                    )
                })
        }
    }

}

@Composable
fun NavActivity(
    musicCategoriesVM: MusicCategoriesVM,
    likesVM: LikesVM,
    artistsVM: ArtistsVM,
    artistDetailVM: ArtistDetailVM,
    albumDetailVM: AlbumDetailVM
) {
    val navController = rememberNavController()

    val pageTitle = remember { mutableStateOf("Kategoriler") }
    val tfSearch = remember { mutableStateOf("") }
    val searchController = remember { mutableStateOf(value = false) }
    val bottomBarController = remember { mutableStateOf(value = false) }
    var selectedBottomItem by remember { mutableIntStateOf(value = 0) }

    val trackController: State<Boolean> = trackingController.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = White, topBar = {
        CustomTopBar(
            navController = navController,
            pageTitle = pageTitle.value,
            tfSearch = tfSearch,
            searchController = searchController
        )
    }, content = { values ->
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = "musiccategories"
        ) {
            composable(route = "musiccategories") {
                tfSearch.value = ""
                searchController.value = false
                pageTitle.value = stringResource(id = R.string.categories)
                selectedBottomItem = BottomBarConstants.SELECT_CATEGORIES
                bottomBarController.value = false
                MusicCategories(
                    viewModel = musicCategoriesVM,
                    navController = navController,
                    topPadding = values.calculateTopPadding(),
                    bottomPadding = values.calculateBottomPadding(),
                    tfSearch = tfSearch
                )
            }
            composable(route = "likes") {
                tfSearch.value = ""
                searchController.value = false
                pageTitle.value = stringResource(id = R.string.likes2)
                selectedBottomItem = BottomBarConstants.SELECT_LIKES
                Likes(
                    viewModel = likesVM,
                    topPadding = values.calculateTopPadding(),
                    bottomPadding = values.calculateBottomPadding(),
                    tfSearch = tfSearch
                )
            }
            composable(
                route = "artists/{id}/{name}",
                arguments = listOf(navArgument(name = "id") { type = NavType.IntType },
                    navArgument(name = "name") { type = NavType.StringType })
            ) { stackEntry ->
                val genreId = stackEntry.arguments?.getInt("id")
                val genreName = stackEntry.arguments?.getString("name")

                genreId?.let { id: Int ->
                    bottomBarController.value = true

                    genreName?.let { name: String ->
                        tfSearch.value = ""
                        searchController.value = false
                        pageTitle.value = name

                        Artist(
                            viewModel = artistsVM,
                            navController = navController,
                            topPadding = values.calculateTopPadding(),
                            bottomPadding = values.calculateBottomPadding(),
                            tfSearch = tfSearch,
                            genreId = id
                        )

                    } ?: Log.e("Hata", "Değerler null içeriyor.")

                } ?: Log.e("Hata", "Değerler null içeriyor.")
            }
            composable(
                route = "artist_detail/{id}/{name}",
                arguments = listOf(navArgument(name = "id") { type = NavType.IntType },
                    navArgument(name = "name") { type = NavType.StringType })
            ) { stackEntry ->
                val artistId = stackEntry.arguments?.getInt("id")
                val artistName = stackEntry.arguments?.getString("name")

                if (artistId != null && artistName != null) {
                    tfSearch.value = ""
                    searchController.value = false
                    pageTitle.value = artistName

                    ArtistDetail(
                        viewModel = artistDetailVM,
                        navController = navController,
                        topPadding = values.calculateTopPadding(),
                        bottomPadding = values.calculateBottomPadding(),
                        tfSearch = tfSearch,
                        artistId = artistId,
                        artistName = artistName,
                    )
                }
            }
            composable(
                route = "album_detail/{artistName}/{albumId}/{albumName}",
                arguments = listOf(navArgument(name = "albumId") {
                    type = NavType.IntType
                })
            ) { stackEntry ->
                val artistName = stackEntry.arguments?.getString("artistName")
                val albumId = stackEntry.arguments?.getInt("albumId")
                val albumName = stackEntry.arguments?.getString("albumName")

                if (artistName != null && albumId != null && albumName != null) {
                    tfSearch.value = ""
                    searchController.value = false
                    pageTitle.value = albumName
                    AlbumDetail(
                        viewModel = albumDetailVM,
                        topPadding = values.calculateTopPadding(),
                        bottomPadding = values.calculateBottomPadding(),
                        tfSearch = tfSearch,
                        artistName = artistName,
                        albumName = albumName,
                        albumId = albumId
                    )
                }

            }
        }
    }, bottomBar = {
        Column {
            AnimatedVisibility(
                visible = trackController.value,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(55.0.dp),
                    contentAlignment = Alignment.Center,
                    content = {
                        MusicConstants.playMusic?.let { playMusic: PlayMusic ->
                            PlayMusicInBottom(
                                artistName = playMusic.artistName,
                                albumName = playMusic.albumName,
                                musicName = playMusic.musicName,
                                musicImage = playMusic.musicImage
                            )
                        }
                    })
            }
            AnimatedVisibility(
                visible = !bottomBarController.value, enter = fadeIn(), exit = fadeOut()
            ) {
                val navBarItemColors = NavigationBarItemDefaults.colors(
                    indicatorColor = currentColor().navBarIndicator,
                    selectedIconColor = currentColor().navBarSelected,
                    selectedTextColor = currentColor().navBarSelected,
                    unselectedIconColor = currentColor().navBarUnSelected,
                    unselectedTextColor = currentColor().navBarUnSelected
                )
                NavigationBar(modifier = Modifier.fillMaxWidth(),
                    contentColor = White,
                    containerColor = currentColor().sysBars,
                    content = {
                        NavigationBarItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.categories),
                                    style = TextStyle(
                                        fontSize = 13.0.sp, fontFamily = FontFamily(
                                            Font(
                                                resId = R.font.sofiaprosemibold,
                                                weight = FontWeight.SemiBold
                                            )
                                        )
                                    )
                                )
                            },
                            icon = {
                                if (selectedBottomItem == BottomBarConstants.SELECT_CATEGORIES) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.headset_f),
                                        tint = currentColor().navIconFill,
                                        contentDescription = stringResource(id = R.string.categories)
                                    )
                                }; if (selectedBottomItem != BottomBarConstants.SELECT_CATEGORIES) {
                                Icon(
                                    painter = painterResource(id = R.drawable.headset),
                                    tint = currentColor().navIcon,
                                    contentDescription = stringResource(id = R.string.categories)
                                )
                            }
                            },
                            selected = selectedBottomItem == BottomBarConstants.SELECT_CATEGORIES,
                            onClick = {
                                selectedBottomItem =
                                    BottomBarConstants.SELECT_CATEGORIES; navController.navigate(
                                route = "musiccategories"
                            ) { popUpTo(route = "musiccategories") { inclusive = true } }
                            },
                            colors = navBarItemColors
                        )
                        NavigationBarItem(
                            label = {
                                Text(
                                    text = stringResource(id = R.string.likes), style = TextStyle(
                                        fontSize = 13.0.sp, fontFamily = FontFamily(
                                            Font(
                                                resId = R.font.sofiaprosemibold,
                                                weight = FontWeight.SemiBold
                                            )
                                        )
                                    )
                                )
                            },
                            icon = {
                                if (selectedBottomItem == BottomBarConstants.SELECT_LIKES) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.heart_f),
                                        tint = currentColor().navIconFill,
                                        contentDescription = stringResource(id = R.string.likes)
                                    )
                                }; if (selectedBottomItem != BottomBarConstants.SELECT_LIKES) {
                                Icon(
                                    painter = painterResource(id = R.drawable.heart),
                                    tint = currentColor().navIcon,
                                    contentDescription = stringResource(id = R.string.likes)
                                )
                            }
                            },
                            selected = selectedBottomItem == BottomBarConstants.SELECT_LIKES,
                            onClick = {
                                selectedBottomItem =
                                    BottomBarConstants.SELECT_LIKES; navController.navigate(route = "likes") {
                                popUpTo(
                                    route = "likes"
                                ) { inclusive = true }
                            }
                            },
                            colors = navBarItemColors
                        )
                    })
            }
        }
    })
}

@Composable
private fun CustomTopBar(
    navController: NavHostController,
    pageTitle: String,
    tfSearch: MutableState<String>,
    searchController: MutableState<Boolean>
) {
    val tfFocusRequester = remember { FocusRequester() }
    val containerColor = currentColor().searchContainer
    val tfColors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = currentColor().textColor,
        unfocusedTextColor = currentColor().textColor,
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        cursorColor = currentColor().primary,
        selectionColors = TextSelectionColors(
            handleColor = currentColor().selectionHandle,
            backgroundColor = currentColor().selectionBg
        ),
        focusedBorderColor = Transparent,
        unfocusedBorderColor = Transparent,
    )

    val configuration = LocalConfiguration.current

    SideEffect {
        if (searchController.value) tfFocusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = currentColor().sysBars),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if ((!searchController.value && (pageTitle != "Kategoriler" && pageTitle != "Begeniler"))) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    tint = currentColor().icon,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        }

        Box(modifier = Modifier
            .background(color = Transparent)
            .padding(start = 20.0.dp, top = 16.0.dp, bottom = 16.0.dp),
            contentAlignment = Alignment.Center,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = stringResource(id = R.string.app_name)
                )
            })

        if (!searchController.value) {
            Text(
                modifier = Modifier
                    .width(width = (configuration.screenWidthDp / 2).dp)
                    .padding(top = 16.0.dp, start = 4.0.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = pageTitle.take(n = 25),
                style = TextStyle(
                    fontSize = 16.0.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.SemiBold,
                    color = currentColor().primary
                )
            )
        }

        if (searchController.value) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester = tfFocusRequester)
                    .padding(start = 20.0.dp, end = 20.0.dp, top = 2.5.dp, bottom = 2.5.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        tint = currentColor().icon,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        tfSearch.value = ""; searchController.value = !searchController.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = stringResource(id = R.string.delete)
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_category), style = TextStyle(
                            fontSize = 14.0.sp, fontFamily = FontFamily(
                                Font(
                                    resId = R.font.sofiaproregular, weight = FontWeight.Medium
                                )
                            ), color = currentColor().textColor
                        )
                    )
                },
                value = tfSearch.value,
                onValueChange = { value: String -> tfSearch.value = value },
                colors = tfColors
            )
        }

        if (!searchController.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Transparent),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { searchController.value = !searchController.value }) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        tint = currentColor().icon,
                        contentDescription = stringResource(id = R.string.search)
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayMusicInBottom(
    artistName: String, albumName: String, musicName: String, musicImage: String
) {
    val playingController: State<Boolean> =
        MusicConstants.playingController.collectAsState(initial = false)

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .blur(radius = 10.0.dp),
        model = musicImage,
        contentScale = ContentScale.Crop,
        contentDescription = musicName
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 5.0.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            MusicConstants.apply {
                if (playingController.value) {
                    mediaPlayer?.start()
                    MusicConstants.playingController.value = false
                } else {
                    mediaPlayer?.pause()
                    MusicConstants.playingController.value = true
                }

            }
        }) {
            Box(modifier = Modifier
                .clip(shape = CircleShape)
                .background(color = Black), content = {
                Icon(
                    modifier = Modifier.padding(all = 5.0.dp), painter = painterResource(
                        id = if (playingController.value) R.drawable.play else R.drawable.pause
                    ), tint = White, contentDescription = "Play"
                )
            })
        }
        Column(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(color = Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            val textModifier = Modifier.padding(start = 16.0.dp, end = 16.0.dp)
            Text(
                modifier = textModifier.padding(top = 2.0.dp), text = artistName, style = TextStyle(
                    fontSize = 14.0.sp, fontFamily = FontFamily(
                        Font(resId = R.font.sofiaprosemibold, weight = FontWeight.SemiBold)
                    ), color = White
                )
            )
            Text(
                modifier = textModifier
                    .padding(bottom = 2.0.dp)
                    .basicMarquee(),
                text = "$albumName - $musicName",
                style = TextStyle(
                    fontSize = 12.0.sp, fontFamily = FontFamily(
                        Font(resId = R.font.sofiaproregular, weight = FontWeight.Normal)
                    ), color = White
                )
            )
        }
    }
}