package com.nsicyber.barblend.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nsicyber.barblend.data.model.CocktailModel

@Composable
fun PopularCocktailCardView(
    model: CocktailModel, onClick: (cocktailId: String) -> Unit = {}
) {


    Box(
        modifier = Modifier
            .clickable { onClick(model.id) }
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .width(208.dp)
            .padding(4.dp)

    ) {
        Column {
            AsyncImage(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = model.image,
                contentDescription = ""
            )
            Text(
                text = model.title.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = model.ingredients?.size.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "Ingredient",
                )
            }
            LazyHorizontalStaggeredGrid(
                modifier = Modifier.height(72.dp),
                rows = StaggeredGridCells.Fixed(2)
            ) {
                items(model.tags?.size ?: 0) { index ->
                    KeywordCardItem(model.tags?.get(index))
                }

            }

        }
    }


}

@Composable
fun KeywordCardItem(
    data: String? = null,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(99.dp))
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.onPrimaryContainer),
    ) {
        Text(
            text = data.orEmpty(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onPrimary, textAlign = TextAlign.Center
        )
    }
}


@Composable
fun LatestCocktailCardView(
    model: CocktailModel, onClick: (cocktailId: String) -> Unit = {}
) {


    Box(
        modifier = Modifier.clickable { onClick(model.id) }
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp)

    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = model.image,
                contentDescription = ""
            )
            Column(modifier=Modifier.fillMaxHeight()) {
                Text(maxLines = 2,
                    text = model.title.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.fillMaxSize(0f))
                Row(modifier = Modifier,verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = model.ingredients?.size.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = "Ingredient",
                    )
                }
            }


        }
    }


}



@Composable
fun SearchCocktailCardView(
    model: CocktailModel, onClick: (cocktailId: String) -> Unit = {}
) {


    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .clickable { onClick(model.id) }
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp)

    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = model.image,
                contentDescription = ""
            )
            Column(modifier=Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(maxLines = 2,
                    text = model.title.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
                }


        }
    }


}