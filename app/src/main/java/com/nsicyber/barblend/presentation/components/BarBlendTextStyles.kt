package com.nsicyber.barblend.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object BarBlendTextStyles {
    val header =
        TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )
    val subheader =
        TextStyle(
            textAlign = TextAlign.Start,
            color = Color.Gray,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
        )
    val body =
        TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
        )
}
