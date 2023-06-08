package com.fa.beatify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.fa.beatify.schemes.AppColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BeatifyTheme(
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.apply {
        setSystemBarsColor(color = currentColor().sysBars)
        statusBarDarkContentEnabled = !isSystemInDarkTheme()
    }

    MaterialTheme(
        content = content
    )
}

private val LtColors = AppColor(
    primary = LtPrimary,
    selectionHandle = LtSelectionHandle,
    selectionBg = LtSelectionBg,
    sysBars = LtSysBars,
    screenBg = LtScreenBg,
    navBarIndicator = LtNavBarIndicator,
    navBarUnSelected = LtNavBarUnSelected,
    navBarSelected = LtNavBarSelected,
    textColor = LtTextColor,
    searchContainer = LtSearchContainer,
    gridCategoryBg = LtGridCategoryBg,
    gridArtistBg = LtGridArtistBg,
    navIcon = LtNavIcon,
    navIconFill = LtNavIconFill,
    icon = LtIcon
)

private val NtColors = AppColor(
    primary = NtPrimary,
    selectionHandle = NtSelectionHandle,
    selectionBg = NtSelectionBg,
    sysBars = NtSysBars,
    screenBg = NtScreenBg,
    navBarIndicator = NtNavBarIndicator,
    navBarUnSelected = NtNavBarUnSelected,
    navBarSelected = NtNavBarSelected,
    textColor = NtTextColor,
    searchContainer = NtSearchContainer,
    gridCategoryBg = NtGridCategoryBg,
    gridArtistBg = NtGridArtistBg,
    navIcon = NtNavIcon,
    navIconFill = NtNavIconFill,
    icon = NtIcon
)

@Composable
fun currentColor(): AppColor = if (isSystemInDarkTheme()) NtColors else LtColors