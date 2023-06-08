package com.fa.beatify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.fa.beatify.schemes.AppColor

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
)

@Composable
fun currentColor(): AppColor = if (isSystemInDarkTheme()) NtColors else LtColors