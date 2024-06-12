package com.nsicyber.barblend.presentation.explore

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nsicyber.barblend.R
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.presentation.components.BarBlendTextStyles
import com.nsicyber.barblend.presentation.components.HeaderSubHeaderTitleView
import com.nsicyber.barblend.presentation.components.KeywordCardItem

@Composable
fun LatestCocktailCardView(
    model: CocktailModel?,
    onClick: (cocktailId: String) -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .clickable { onClick(model?.id.toString()) }
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(4.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp)),
                model = model?.image,
                contentDescription = stringResource(id = R.string.content_desc_cocktail_image),
            )
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    maxLines = 2,
                    text = model?.title.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.fillMaxSize(0f))
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = model?.ingredients?.size.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = stringResource(id = R.string.ingredients_subtitle),
                    )
                }
            }
        }
    }
}

@Composable
fun PopularCocktailsSection(
    popularCocktails: List<CocktailModel?>?,
    onClick: (id: String) -> Unit,
) {
    if (popularCocktails?.isNotEmpty() == true) {
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 32.dp, bottom = 8.dp),
        ) {
            HeaderSubHeaderTitleView(
                subheader = stringResource(id = R.string.popular_cocktails_title),
                header = stringResource(id = R.string.popular_cocktails_subtitle),
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp),
        ) {
            items(
                items = popularCocktails.orEmpty(),
                key = { item -> item?.id.toString() },
            ) { model ->
                PopularCocktailCardView(model = model) { redirectId ->
                    onClick(redirectId)
                }
            }
        }
    }
}

@Composable
fun SearchSection(onClick: () -> Unit) {
    Text(
        text = stringResource(id = R.string.search_title),
        style = BarBlendTextStyles.header,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp),
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .padding(vertical = 16.dp)
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .clickable { onClick() },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.search_placeholder),
                style = BarBlendTextStyles.body,
                color = Color.Black,
                modifier = Modifier.wrapContentSize(),
            )

            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.ic_search_cd),
            )
        }
    }
}

@Composable
fun LatestCocktailsSectionTitle() {
    Column(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
    ) {
        HeaderSubHeaderTitleView(
            subheader = stringResource(id = R.string.last_added_title),
            header = stringResource(id = R.string.last_added_subtitle),
        )
    }
}

@Composable
fun PopularCocktailCardView(
    model: CocktailModel?,
    onClick: (cocktailId: String) -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .clickable { onClick(model?.id.toString()) }
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .width(208.dp)
                .padding(4.dp),
    ) {
        Column {
            AsyncImage(
                modifier =
                    Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                model = model?.image,
                contentDescription = stringResource(id = R.string.content_desc_cocktail_image),
            )
            Text(
                text = model?.title.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = model?.ingredients?.size.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(id = R.string.ingredients_subtitle),
                )
            }
            LazyHorizontalStaggeredGrid(
                modifier = Modifier.height(72.dp),
                rows = StaggeredGridCells.Fixed(2),
            ) {
                items(
                    items = model?.tags.orEmpty(),
                    key = { item -> item },
                ) { model ->
                    KeywordCardItem(
                        model,
                    )
                }
            }
        }
    }
}
