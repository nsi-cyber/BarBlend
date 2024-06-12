package com.nsicyber.barblend.presentation.favoriteList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsicyber.barblend.R
import com.nsicyber.barblend.presentation.components.BarBlendTextStyles
import com.nsicyber.barblend.presentation.components.BaseView
import com.nsicyber.barblend.presentation.components.HeaderSubHeaderTitleView
import com.nsicyber.barblend.presentation.search.SearchCocktailCardView

@Composable
fun FavoriteListScreen(
    onDetail: (id: String) -> Unit,
    viewModel: FavoriteListScreenViewModel = hiltViewModel<FavoriteListScreenViewModel>(),
) {
    val cocktails by viewModel.favoriteListScreenState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(FavoriteListScreenEvent.StartPage)
    }

    BaseView(content = {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
                HeaderSubHeaderTitleView(
                    subheader = stringResource(id = R.string.favorite_title),
                    header = stringResource(id = R.string.favorite_subtitle),
                )
            }
            if (cocktails.data.favoriteCocktails?.isEmpty() == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_favorite_title),
                            style = BarBlendTextStyles.header,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = stringResource(id = R.string.no_favorite_subtitle),
                            style = BarBlendTextStyles.body,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    items(
                        cocktails.data.favoriteCocktails.orEmpty(),
                        key = { item -> item?.id.toString() },
                    ) { model ->
                        SearchCocktailCardView(model = model) {
                            onDetail(it)
                        }
                    }
                }
            }
        }
    }, isLoading = cocktails.isLoading)
}
