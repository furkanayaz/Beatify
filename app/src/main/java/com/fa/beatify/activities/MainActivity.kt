package com.fa.beatify.activities

import com.fa.beatify.ui.theme.currentColor
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fa.beatify.R
import com.fa.beatify.controllers.BottomBarController
import com.fa.beatify.ui.theme.Transparent
import com.fa.beatify.ui.theme.White
import com.fa.beatify.pages.*
import com.fa.beatify.rooms.RoomDB
import com.fa.beatify.ui.theme.BeatifyTheme
import com.fa.beatify.viewmodels.MainVM

class MainActivity : ComponentActivity() {
    private val mainVM: MainVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition(condition = { mainVM.getCondition() })
        setContent {
            BeatifyTheme(
                packageManager = packageManager,
                context = LocalContext.current,
                content = {
                    NavActivity()
                }
            )
        }
    }

    override fun onStart() {
        if (mainVM.getPlayingController().value) {
            // Bildirimi sil.
        }
        super.onStart()
    }

    override fun onStop() {
        if (mainVM.getPlayingController().value) {
            // Bildirimi başlat.
        }
        super.onStop()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavActivity() {
    val pageTitle = remember { mutableStateOf("Kategoriler") }
    val tfSearch = remember { mutableStateOf("") }
    val searchController = remember { mutableStateOf(value = false) }
    val bottomBarController = remember { mutableStateOf(value = false) }
    var selectedBottomItem by remember { mutableIntStateOf(value = 0) }

    val context = LocalContext.current

    val navController = rememberNavController()

    LaunchedEffect(key1 = true) {
        RoomDB.accessDatabase(context = context)
    }

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
                selectedBottomItem = BottomBarController.SELECT_CATEGORIES
                bottomBarController.value = false
                MusicCategories(
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
                selectedBottomItem = BottomBarController.SELECT_LIKES
                Likes(
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
                        navController = navController,
                        topPadding = values.calculateTopPadding(),
                        bottomPadding = values.calculateBottomPadding(),
                        tfSearch = tfSearch,
                        artistId = artistId
                    )
                }
            }
            composable(
                route = "album_detail/{albumId}/{albumName}",
                arguments = listOf(navArgument(name = "albumId") {
                    type = NavType.IntType
                })
            ) { stackEntry ->
                val albumId = stackEntry.arguments?.getInt("albumId")
                val albumName = stackEntry.arguments?.getString("albumName")

                if (albumId != null && albumName != null) {
                    tfSearch.value = ""
                    searchController.value = false
                    pageTitle.value = albumName
                    AlbumDetail(
                        topPadding = values.calculateTopPadding(),
                        bottomPadding = values.calculateBottomPadding(),
                        tfSearch = tfSearch,
                        albumId = albumId
                    )
                }

            }

        }
    }, bottomBar = {
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
                                text = stringResource(id = R.string.categories), style = TextStyle(
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
                            if (selectedBottomItem == BottomBarController.SELECT_CATEGORIES) {
                                Icon(
                                    painter = painterResource(id = R.drawable.headset_f),
                                    tint = currentColor().navIconFill,
                                    contentDescription = stringResource(id = R.string.categories)
                                )
                            }; if (selectedBottomItem != BottomBarController.SELECT_CATEGORIES) {
                            Icon(
                                painter = painterResource(id = R.drawable.headset),
                                tint = currentColor().navIcon,
                                contentDescription = stringResource(id = R.string.categories)
                            )
                        }
                        },
                        selected = selectedBottomItem == BottomBarController.SELECT_CATEGORIES,
                        onClick = {
                            selectedBottomItem =
                                BottomBarController.SELECT_CATEGORIES; navController.navigate(
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
                            if (selectedBottomItem == BottomBarController.SELECT_LIKES) {
                                Icon(
                                    painter = painterResource(id = R.drawable.heart_f),
                                    tint = currentColor().navIconFill,
                                    contentDescription = stringResource(id = R.string.likes)
                                )
                            }; if (selectedBottomItem != BottomBarController.SELECT_LIKES) {
                            Icon(
                                painter = painterResource(id = R.drawable.heart),
                                tint = currentColor().navIcon,
                                contentDescription = stringResource(id = R.string.likes)
                            )
                        }
                        },
                        selected = selectedBottomItem == BottomBarController.SELECT_LIKES,
                        onClick = {
                            selectedBottomItem =
                                BottomBarController.SELECT_LIKES; navController.navigate(route = "likes") {
                            popUpTo(
                                route = "likes"
                            ) { inclusive = true }
                        }
                        },
                        colors = navBarItemColors
                    )
                })
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTopBar(
    navController: NavHostController,
    pageTitle: String,
    tfSearch: MutableState<String>,
    searchController: MutableState<Boolean>
) {
    val tfFocusRequester = remember { FocusRequester() }
    val tfColors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = currentColor().textColor,
        containerColor = currentColor().searchContainer,
        focusedBorderColor = Transparent,
        unfocusedBorderColor = Transparent,
        cursorColor = currentColor().primary,
        selectionColors = TextSelectionColors(
            handleColor = currentColor().selectionHandle, backgroundColor = currentColor().selectionBg
        )
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

@Preview(showBackground = true)
@Composable
fun PreviewNavActivity() {
    NavActivity()
}