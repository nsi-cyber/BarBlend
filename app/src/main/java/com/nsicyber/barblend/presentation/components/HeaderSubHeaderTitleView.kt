package com.nsicyber.barblend.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HeaderSubHeaderTitleView(
    subheader: String,
    header: String,
) {
    Text(
        text = header,
        style = BarBlendTextStyles.header,
        modifier = Modifier.fillMaxWidth(),
    )
    Text(
        text = subheader,
        style = BarBlendTextStyles.subheader,
        modifier = Modifier.fillMaxWidth(),
    )
}
