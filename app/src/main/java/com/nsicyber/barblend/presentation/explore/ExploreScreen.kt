package com.nsicyber.barblend.presentation.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.nsicyber.barblend.R
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.presentation.components.BarBlendTextStyles
import com.nsicyber.barblend.presentation.components.BaseView
import com.nsicyber.barblend.presentation.components.LatestCocktailCardView
import com.nsicyber.barblend.presentation.components.PopularCocktailCardView

@Composable
fun ExploreScreen(
    onDetail: (id: String) -> Unit,
    onRandom: () -> Unit,
    onSearch: () -> Unit,
    viewModel: ExploreScreenViewModel = hiltViewModel<ExploreScreenViewModel>(),
) {
    val cocktails by viewModel.exploreScreenState.collectAsState()
    val swipeRefreshState = remember { SwipeRefreshState(isRefreshing = false) }
    val lazyState = rememberLazyListState()

    LaunchedEffect(cocktails.data.popularCocktails) {
        if (!cocktails.data.popularCocktails.isNullOrEmpty()) lazyState.animateScrollToItem(0)
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(ExploreScreenEvent.StartPage)
    }

    BaseView(content = {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(ExploreScreenEvent.RefreshPage)
            },
        ) {
            Scaffold(floatingActionButton = {
                Button(
                    onClick = { onRandom() },
                    shape = RoundedCornerShape(99.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(id = R.string.feeling_luck_button_title),
                                fontSize = 14.sp,
                            )
                            Text(
                                text = stringResource(id = R.string.feeling_luck_button_subtitle),
                                fontSize = 14.sp,
                            )
                        }

                        Text(
                            text = "?",
                            fontSize = 32.sp,
                        )
                    }
                }
            }) { pdVal ->
                LazyColumn(
                    state = lazyState,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(pdVal),
                ) {
                    item {
                        PopularCocktailsSection(
                            popularCocktails = cocktails.data.popularCocktails,
                            onClick = { id -> onDetail(id) },
                        )
                    }

                    item {
                        SearchSection(onClick = onSearch)
                    }

                    item {
                        if (!cocktails.data.latestCocktails.isNullOrEmpty()) {
                            LatestCocktailsSectionTitle()
                        }
                    }

                    items(
                        cocktails.data.latestCocktails.orEmpty(),
                        key = { item -> item?.id.toString() },
                    ) { model ->
                        Box(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                            LatestCocktailCardView(model = model) { redirectId ->
                                onDetail(redirectId)
                            }
                        }
                    }

                    item {
                        if (cocktails.data.popularCocktails.isNullOrEmpty() && cocktails.data.latestCocktails.isNullOrEmpty()) {
                            Box(
                                modifier =
                                    Modifier.clickable {
                                        viewModel.onEvent(ExploreScreenEvent.StartPage)
                                    }.padding(24.dp),
                            ) {
                                Text(text = "Error while fetching data. Click to retry")
                            }
                        }
                    }
                }
            }
        }
    }, isLoading = cocktails.isLoading)
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
            Text(
                text = stringResource(id = R.string.popular_cocktails_title),
                style = BarBlendTextStyles.subheader,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(id = R.string.popular_cocktails_subtitle),
                style = BarBlendTextStyles.header,
                modifier = Modifier.fillMaxWidth(),
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
                contentDescription = "Search Icon",
            )
        }
    }
}

@Composable
fun LatestCocktailsSectionTitle() {
    Column(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.last_added_title),
            style = BarBlendTextStyles.subheader,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = stringResource(id = R.string.last_added_subtitle),
            style = BarBlendTextStyles.header,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
