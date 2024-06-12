package com.nsicyber.barblend.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun KeywordCardItem(data: String? = null) {
    Box(
        modifier =
            Modifier
                .clip(RoundedCornerShape(99.dp))
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.onPrimaryContainer),
    ) {
        Text(
            text = data.orEmpty(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
        )
    }
}
