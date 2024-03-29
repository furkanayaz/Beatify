package com.fa.beatify.presentation.activity

import com.fa.beatify.presentation.ui.theme.currentColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.fa.beatify.presentation.album_detail.AlbumDetail
import com.fa.beatify.presentation.artist_detail.ArtistDetail
import com.fa.beatify.presentation.artists.Artist
import com.fa.beatify.utils.constants.controller.MusicController
import com.fa.beatify.domain.models.PlayMusic
import com.fa.beatify.presentation.music_categories.MusicCategories
import com.fa.beatify.presentation.music_likes.Likes
import com.fa.beatify.R
import com.fa.beatify.utils.constants.controller.ClearController
import com.fa.beatify.presentation.album_detail.AlbumDetailVM
import com.fa.beatify.presentation.artist_detail.ArtistDetailVM
import com.fa.beatify.presentation.artists.ArtistsVM
import com.fa.beatify.presentation.music_categories.MusicCategoriesVM
import com.fa.beatify.presentation.music_likes.LikesVM
import com.fa.beatify.presentation.ui.theme.BeatifyTheme
import com.fa.beatify.utils.NavUtility
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.CircleShape
import com.fa.beatify.presentation.ui.theme.Transparent
import com.fa.beatify.presentation.ui.theme.White
import com.fa.beatify.presentation.ui.theme.Black
import com.fa.beatify.utils.constants.controller.MusicController.trackingController
import com.fa.beatify.utils.constants.controller.BottomBarController.SELECT_LIKES
import com.fa.beatify.utils.constants.controller.BottomBarController.SELECT_CATEGORIES

class MainActivity : ComponentActivity(), AndroidScopeComponent {
    override val scope: Scope by activityScope()

    private val mainActivityVM by viewModel<MainActivityVM>()

    private val musicCategoriesVM by viewModel<MusicCategoriesVM>()
    private val likesVM by viewModel<LikesVM>()
    private val artistsVM by viewModel<ArtistsVM>()
    private val artistDetailVM by viewModel<ArtistDetailVM>()
    private val albumDetailVM by viewModel<AlbumDetailVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            BeatifyTheme(packageManager = packageManager,
                context = LocalContext.current,
                content = {
                    NavActivity(
                        mainActivityVM = mainActivityVM,
                        musicCategoriesVM = musicCategoriesVM,
                        likesVM = likesVM,
                        artistsVM = artistsVM,
                        artistDetailVM = artistDetailVM,
                        albumDetailVM = albumDetailVM
                    )
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearConstants()
    }

    private fun clearConstants() {
        ClearController.apply {
            clearListConsts()
            clearImageConsts()
        }
    }

}

@Composable
fun NavActivity(
    mainActivityVM: MainActivityVM,
    musicCategoriesVM: MusicCategoriesVM,
    likesVM: LikesVM,
    artistsVM: ArtistsVM,
    artistDetailVM: ArtistDetailVM,
    albumDetailVM: AlbumDetailVM
) {
    val navController: NavHostController = rememberNavController()
    val trackController: State<Boolean> = trackingController.collectAsState()

    val pageTitle: MutableState<String> = remember { mutableStateOf("Kategoriler") }
    val tfSearch: MutableState<String> = remember { mutableStateOf("") }
    val searchController: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    val bottomBarController: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    var selectedBottomItem: Int by remember { mutableIntStateOf(value = 0) }

    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = White, topBar = {
        CustomTopBar(
            navController = navController,
            pageTitle = pageTitle.value,
            tfSearch = tfSearch,
            searchController = searchController
        )
    }, content = { values: PaddingValues ->
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = "musiccategories"
        ) {
            composable(route = "musiccategories") {
                tfSearch.value = ""
                searchController.value = false
                pageTitle.value = stringResource(id = R.string.categories)
                selectedBottomItem = SELECT_CATEGORIES
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
                selectedBottomItem = SELECT_LIKES
                Likes(
                    viewModel = likesVM,
                    topPadding = values.calculateTopPadding(),
                    bottomPadding = values.calculateBottomPadding(),
                    tfSearch = tfSearch
                )
            }
            composable(
                route = NavUtility.Artists.withRouteArgs("id", "name"),
                arguments = listOf(navArgument(name = "id") { type = NavType.StringType },
                    navArgument(name = "name") { type = NavType.StringType })
            ) { stackEntry ->
                val genreId: String? = stackEntry.arguments?.getString("id")
                val genreName: String = stackEntry.arguments?.getString("name") ?: ""

                genreId?.let { id: String ->
                    bottomBarController.value = true

                    tfSearch.value = ""
                    searchController.value = false
                    pageTitle.value = genreName

                    Artist(
                        viewModel = artistsVM,
                        navController = navController,
                        topPadding = values.calculateTopPadding(),
                        bottomPadding = values.calculateBottomPadding(),
                        genreId = id,
                        tfSearch = tfSearch
                    )
                }
            }
            composable(
                route = NavUtility.ArtistDetail.withRouteArgs("id", "name"),
                arguments = listOf(navArgument(name = "id") { type = NavType.StringType },
                    navArgument(name = "name") { type = NavType.StringType })
            ) { stackEntry ->
                val artistId: String? = stackEntry.arguments?.getString("id")
                val artistName: String = stackEntry.arguments?.getString("name") ?: ""

                artistId?.let { id: String ->
                    tfSearch.value = ""
                    searchController.value = false
                    pageTitle.value = artistName

                    ArtistDetail(
                        viewModel = artistDetailVM,
                        navController = navController,
                        topPadding = values.calculateTopPadding(),
                        bottomPadding = values.calculateBottomPadding(),
                        artistId = id,
                        artistName = artistName,
                        tfSearch = tfSearch
                    )
                }
            }
            composable(
                route = NavUtility.AlbumDetail.withRouteArgs("artistName", "albumId", "albumName"),
                arguments = listOf(navArgument(name = "albumId") {
                    type = NavType.StringType
                })
            ) { stackEntry ->
                val albumId: String? = stackEntry.arguments?.getString("albumId")
                val artistName: String = stackEntry.arguments?.getString("artistName") ?: ""
                val albumName: String = stackEntry.arguments?.getString("albumName") ?: ""

                albumId?.let { id: String ->
                    tfSearch.value = ""
                    searchController.value = false
                    pageTitle.value = albumName
                    AlbumDetail(
                        viewModel = albumDetailVM,
                        topPadding = values.calculateTopPadding(),
                        bottomPadding = values.calculateBottomPadding(),
                        artistName = artistName,
                        albumName = albumName,
                        albumId = id,
                        tfSearch = tfSearch
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
                        MusicController.playMusic?.let { playMusic: PlayMusic ->
                            PlayMusicInBottom(
                                viewModel = mainActivityVM,
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
                                Icon(
                                    painter = painterResource(
                                        id = mainActivityVM.getCategoryIconPair(
                                            controller = selectedBottomItem
                                        ).first
                                    ),
                                    tint = colorResource(
                                        id = mainActivityVM.getCategoryIconPair(
                                            controller = selectedBottomItem
                                        ).second
                                    ),
                                    contentDescription = stringResource(id = R.string.categories)
                                )
                            },
                            selected = selectedBottomItem == SELECT_CATEGORIES,
                            onClick = {
                                selectedBottomItem =
                                    SELECT_CATEGORIES; navController.navigate(
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
                                Icon(
                                    painter = painterResource(
                                        id = mainActivityVM.getLikeIconPair(
                                            controller = selectedBottomItem
                                        ).first
                                    ),
                                    tint = colorResource(
                                        id = mainActivityVM.getLikeIconPair(
                                            controller = selectedBottomItem
                                        ).second
                                    ),
                                    contentDescription = stringResource(id = R.string.likes)
                                )
                            },
                            selected = selectedBottomItem == SELECT_LIKES,
                            onClick = {
                                selectedBottomItem =
                                    SELECT_LIKES; navController.navigate(route = "likes") {
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
        focusedTextColor = currentColor().searchText,
        unfocusedTextColor = currentColor().searchText,
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

        if ((!searchController.value && (pageTitle != stringResource(id = R.string.categories) && pageTitle != stringResource(
                id = R.string.likes2
            )))
        ) {
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
                        tint = currentColor().searchIcon,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        tfSearch.value = ""; searchController.value = !searchController.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            tint = currentColor().searchIcon,
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
                            ), color = currentColor().searchPlaceText
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
    viewModel: MainActivityVM,
    artistName: String,
    albumName: String,
    musicName: String,
    musicImage: String
) {
    val playingController: State<Boolean> =
        MusicController.playingController.collectAsState(initial = false)

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
            if (playingController.value)
                viewModel.resumeMusic()
            else
                viewModel.pauseMusic()
        }) {
            Box(modifier = Modifier
                .clip(shape = CircleShape)
                .background(color = Black), content = {
                Icon(
                    modifier = Modifier.padding(all = 5.0.dp), painter = painterResource(
                        id = viewModel.getPlayingIcon(playingController.value)
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
                text = viewModel.getMusicTitle(albumName, musicName),
                style = TextStyle(
                    fontSize = 12.0.sp, fontFamily = FontFamily(
                        Font(resId = R.font.sofiaproregular, weight = FontWeight.Normal)
                    ), color = White
                )
            )
        }
    }
}