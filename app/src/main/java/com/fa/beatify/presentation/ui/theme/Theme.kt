package com.fa.beatify.presentation.ui.theme

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.fa.beatify.LtMode
import com.fa.beatify.NtMode
import com.fa.beatify.utils.AppColors
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BeatifyTheme(
    packageManager: PackageManager,
    context: Context,
    content: @Composable () -> Unit
) {
    if (isSystemInDarkTheme()) {
        packageManager.setComponentEnabledSetting(ComponentName(context, NtMode::class.java), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        packageManager.setComponentEnabledSetting(ComponentName(context, LtMode::class.java), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }else {
        packageManager.setComponentEnabledSetting(ComponentName(context, LtMode::class.java), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        packageManager.setComponentEnabledSetting(ComponentName(context, NtMode::class.java), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }

    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.apply {
        setSystemBarsColor(color = currentColor().sysBars)
        statusBarDarkContentEnabled = !isSystemInDarkTheme()
    }

    MaterialTheme(
        content = content
    )
}

private val LtColors = AppColors(
    primary = LtPrimary,
    selectionHandle = LtSelectionHandle,
    selectionBg = LtSelectionBg,
    sysBars = LtSysBars,
    screenBg = LtScreenBg,
    navBarIndicator = LtNavBarIndicator,
    navBarUnSelected = LtNavBarUnSelected,
    navBarSelected = LtNavBarSelected,
    text = LtText,
    searchPlaceText = LtSearchPlaceText,
    searchText = LtSearchText,
    searchIcon = LtSearchIcon,
    searchContainer = LtSearchContainer,
    gridCategoryBg = LtGridCategoryBg,
    gridArtistBg = LtGridArtistBg,
    navIcon = LtNavIcon,
    navIconFill = LtNavIconFill,
    icon = LtIcon
)

private val NtColors = AppColors(
    primary = NtPrimary,
    selectionHandle = NtSelectionHandle,
    selectionBg = NtSelectionBg,
    sysBars = NtSysBars,
    screenBg = NtScreenBg,
    navBarIndicator = NtNavBarIndicator,
    navBarUnSelected = NtNavBarUnSelected,
    navBarSelected = NtNavBarSelected,
    text = NtText,
    searchPlaceText = NtSearchPlaceText,
    searchText = NtSearchText,
    searchIcon = NtSearchIcon,
    searchContainer = NtSearchContainer,
    gridCategoryBg = NtGridCategoryBg,
    gridArtistBg = NtGridArtistBg,
    navIcon = NtNavIcon,
    navIconFill = NtNavIconFill,
    icon = NtIcon
)

@Composable
fun currentColor(): AppColors = if (isSystemInDarkTheme()) NtColors else LtColors