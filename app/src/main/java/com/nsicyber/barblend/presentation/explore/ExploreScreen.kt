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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.nsicyber.barblend.R
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.presentation.components.LatestCocktailCardView
import com.nsicyber.barblend.presentation.components.PopularCocktailCardView
import com.nsicyber.barblend.presentation.navigation.NavigationActions

@Composable
fun ExploreScreen(
    navAction: NavigationActions,
    viewModel: ExploreScreenViewModel = hiltViewModel<ExploreScreenViewModel>(),
) {
    val cocktails by viewModel.exploreScreenState.collectAsState(
        initial = ExploreScreenState(
            isLoading = true
        )
    )
    val swipeRefreshState = remember { SwipeRefreshState(isRefreshing = false) }
    val lazyState = rememberLazyListState()

    LaunchedEffect(cocktails.data.popularCocktails) {
        if (!cocktails.data.popularCocktails.isNullOrEmpty()) lazyState.animateScrollToItem(0)
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(ExploreScreenEvent.StartPage)

    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.onEvent(ExploreScreenEvent.RefreshPage)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(floatingActionButton = {
                Button(
                    onClick = { navAction.navigateToRandomDetail() },
                    shape = RoundedCornerShape(99.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(pdVal)
                ) {
                    item {
                        PopularCocktailsSection(navAction, cocktails.data.popularCocktails)
                    }

                    item {
                        SearchSection(navAction)
                    }

                    item {
                        if (!cocktails.data.latestCocktails.isNullOrEmpty())
                            LatestCocktailsSectionTitle()
                    }

                    items(
                        cocktails.data.latestCocktails.orEmpty(),
                        key = { item -> item?.id.toString() }) { model ->
                        Box(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                            LatestCocktailCardView(model = model) { redirectId ->
                                navAction.navigateToCocktailDetail(redirectId.toString())
                            }
                        }
                    }


                    item {
                        if(cocktails.data.popularCocktails.isNullOrEmpty()&&cocktails.data.latestCocktails.isNullOrEmpty())
                            Box(modifier = Modifier.clickable {
                                viewModel.onEvent(ExploreScreenEvent.StartPage)
                            }.padding(24.dp)){
                                Text(text = "Error while fetching data. Click to retry")
                            }
                    }
                }
            }


            if (cocktails.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray)
                        .alpha(0.5f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun PopularCocktailsSection(navAction: NavigationActions, popularCocktails: List<CocktailModel?>?) {
    if (!popularCocktails.isNullOrEmpty()) {
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 32.dp, bottom = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.popular_cocktails_title),
                color = Color.Gray,
                fontSize = 22.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.popular_cocktails_subtitle),
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp)
        ) {

            items(items = popularCocktails, key = { item -> item?.id.toString() }) { model ->
                PopularCocktailCardView(model = model) { redirectId ->
                    navAction.navigateToCocktailDetail(redirectId.toString())
                }
            }
        }
    }
}

@Composable
fun SearchSection(navAction: NavigationActions) {
    Text(
        text = stringResource(id = R.string.search_title),
        fontSize = 28.sp,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp)
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .height(50.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { navAction.navigateToSearch() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.search_placeholder),
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.wrapContentSize()
            )

            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search Icon"
            )
        }
    }
}

@Composable
fun LatestCocktailsSectionTitle() {
    Column(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.last_added_title),
            color = Color.Gray,
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(id = R.string.last_added_subtitle),
            fontSize = 28.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


