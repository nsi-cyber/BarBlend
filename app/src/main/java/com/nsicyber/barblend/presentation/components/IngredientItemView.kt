package com.nsicyber.barblend.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.nsicyber.barblend.data.model.IngredientModel

@Composable
fun IngredientItemView(model: IngredientModel?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(4.5f),
            text =
                model?.ingredient.orEmpty(),
        )

        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            text = " - ",
        )

        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(4.5f),
            text =
                model?.measure.orEmpty(),
        )
    }
}
