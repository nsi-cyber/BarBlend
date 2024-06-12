package com.nsicyber.barblend.presentation.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.nsicyber.barblend.R
import com.nsicyber.barblend.presentation.components.BaseView

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
                                contentAlignment = Alignment.Center,
                                modifier =
                                    Modifier
                                        .clickable {
                                            viewModel.onEvent(ExploreScreenEvent.StartPage)
                                        }
                                        .padding(24.dp),
                            ) {
                                Text(
                                    textAlign = TextAlign.Center,
                                    text = stringResource(id = R.string.error_message_explore),
                                )
                            }
                        }
                    }
                }
            }
        }
    }, isLoading = cocktails.isLoading)
}
