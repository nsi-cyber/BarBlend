package com.nsicyber.barblend.presentation.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsicyber.barblend.R
import com.nsicyber.barblend.presentation.components.BarBlendTextStyles
import com.nsicyber.barblend.presentation.components.HeaderSubHeaderTitleView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onDetail: (id: String) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel<SearchScreenViewModel>(),
) {
    val cocktails by viewModel.searchScreenState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val textState = remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(textState.value.text) {
        if (textState.value.text.length >= 3) {
            viewModel.onEvent(SearchScreenEvent.Search(textState.value.text))
        } else if (textState.value.text.isEmpty()) {
            viewModel.onEvent(SearchScreenEvent.SetEmpty)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(SearchScreenEvent.StartPage)
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextField(
                        singleLine = true,
                        maxLines = 1,
                        modifier =
                            Modifier
                                .focusRequester(focusRequester)
                                .background(color = Color.White)
                                .fillMaxWidth(0.9f),
                        value = textState.value,
                        onValueChange = { value ->
                            textState.value = value
                        },
                        colors =
                            TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Transparent,
                                containerColor = Color.White,
                                disabledTextColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                            ),
                    )

                    Image(
                        modifier =
                            Modifier
                                .weight(0.1f)
                                .size(24.dp)
                                .clickable {
                                    if (textState.value.text.isNotBlank()) {
                                        textState.value =
                                            TextFieldValue()
                                    }
                                },
                        painter =
                            if (textState.value.text.isNotBlank()) {
                                painterResource(R.drawable.ic_cross)
                            } else {
                                painterResource(
                                    R.drawable.ic_search,
                                )
                            },
                        contentDescription = stringResource(id = R.string.ic_search_cd),
                    )
                }
                Box(Modifier.height(3.dp)) {
                    if (cocktails.isSearchLoading) {
                        LinearProgressIndicator(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(3.dp),
                        )
                    }
                }
            }
        }

        if (cocktails.data.searchCocktails == null) {
            Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
                HeaderSubHeaderTitleView(
                    subheader = stringResource(id = R.string.recent_title),
                    header = stringResource(id = R.string.recent_subtitle),
                )
            }
            if (cocktails.data.recentCocktails?.isEmpty() == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_recent_title),
                            style = BarBlendTextStyles.header,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = stringResource(id = R.string.no_recent_subtitle),
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
                        cocktails.data.recentCocktails.orEmpty(),
                        key = { item -> item?.id.toString() },
                    ) { model ->
                        RecentCocktailCardView(model = model) { redirectId ->
                            onDetail(redirectId.toString())
                        }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
                HeaderSubHeaderTitleView(
                    subheader = stringResource(id = R.string.search_title),
                    header = stringResource(id = R.string.search_subtitle),
                )
            }
            if (cocktails.data.searchCocktails?.isEmpty() == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_result_title),
                            style = BarBlendTextStyles.header,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = stringResource(id = R.string.no_result_subtitle),
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
                        items = cocktails.data.searchCocktails.orEmpty(),
                        key = { item -> item?.id.toString() },
                    ) { model ->
                        SearchCocktailCardView(model = model) { redirectId ->
                            onDetail(redirectId)
                        }
                    }
                }
            }
        }
    }
}
