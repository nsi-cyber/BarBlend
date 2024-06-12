package com.nsicyber.barblend.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nsicyber.barblend.R
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.presentation.components.BarBlendTextStyles

@Composable
fun RecentCocktailCardView(
    model: CocktailModel?,
    onClick: (cocktailId: String?) -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .clickable { onClick(model?.id) }
                .padding(4.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(
                modifier =
                    Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                model = model?.image,
                contentDescription = stringResource(id = R.string.content_desc_cocktail_image),
            )
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    maxLines = 2,
                    text = model?.title.toString(),
                    style = BarBlendTextStyles.body,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun SearchCocktailCardView(
    model: CocktailModel?,
    onClick: (cocktailId: String) -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .clickable { onClick(model?.id.toString()) }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(4.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(
                modifier =
                    Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                model = model?.image,
                contentDescription = stringResource(id = R.string.content_desc_cocktail_image),
            )
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    maxLines = 2,
                    text = model?.title.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
