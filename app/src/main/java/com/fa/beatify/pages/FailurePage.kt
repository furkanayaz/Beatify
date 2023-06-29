package com.fa.beatify.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fa.beatify.R
import com.fa.beatify.ui.theme.currentColor

@Composable
fun FailureMusicCategories(
    topPadding: Dp, bottomPadding: Dp, code: Int? = 0
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
            modifier = Modifier.fillMaxWidth().padding(start = 8.0.dp, end = 8.0.dp),
            text = stringResource(id = if (code == 204) R.string.no_content else if (code == 404) R.string.no_album_content else R.string.no_content), style = TextStyle(
                color = currentColor().text, fontSize = 15.0.sp, fontFamily = FontFamily(
                    Font(resId = R.font.sofiaprosemibold, weight = FontWeight.SemiBold)
                ), textAlign = TextAlign.Center
            )
        )
    }
}