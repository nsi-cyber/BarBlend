package com.nsicyber.barblend.presentation.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsicyber.barblend.R
import com.nsicyber.barblend.presentation.explore.ExploreScreenViewModel
import kotlinx.coroutines.delay

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.presentation.components.SearchCocktailCardView
import com.nsicyber.barblend.presentation.navigation.BackPressHandler
import com.nsicyber.barblend.presentation.navigation.NavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navAction: NavigationActions,
    viewModel: SearchScreenViewModel = hiltViewModel<SearchScreenViewModel>(),
) {

    val cocktails by viewModel.searchScreenState.collectAsState(
        initial = SearchScreenState(isPageLoading = true)
    )
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
        viewModel.getRecent()
    }



Box {


        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .padding(horizontal = 16.dp)
                .shadow(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        )
        {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextField(
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .background(color = Color.White)
                            .fillMaxWidth(0.9f),
                        value = textState.value,
                        onValueChange = { value ->
                            textState.value = value
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            containerColor = Color.White,
                            disabledTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                    )


                    Image(
                        modifier = Modifier
                            .weight(0.1f)
                            .size(24.dp)
                            .clickable {
                                if (textState.value.text.isNotBlank()) textState.value =
                                    TextFieldValue()


                            },
                        painter = if (textState.value.text.isNotBlank()) painterResource(R.drawable.ic_cross)
                        else painterResource(
                            R.drawable.ic_search
                        ),
                        contentDescription = "Search Icon",
                    )
                }
                Box(Modifier.height(3.dp)) {
                    if (cocktails.isSearchLoading)
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                        )
                }

            }


        }





 if (cocktails.data.searchCocktails == null) {
//recent cocktails
            Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
                Text(
                    text = "Recently",
                    color = Color.Gray,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Viewed",
                    fontSize = 28.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (cocktails.data.recentCocktails?.isEmpty() == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Recents",
                            fontSize = 28.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "See recently viewed cocktails here.",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                        )
                    }

                }

            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cocktails.data.recentCocktails?.size ?: 0) {
                        WideMiniCocktailCardView(model = cocktails.data.recentCocktails?.get(it)) {
                            navAction.navigateToCocktailDetail(it!!)

                        }
                    }
                }
            }


        } else {
            Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
                Text(
                    text = "Search",
                    color = Color.Gray,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Results",
                    fontSize = 28.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (cocktails.data.searchCocktails?.isEmpty() == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Result",
                            fontSize = 28.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Check the spelling or try a new search.",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                        )
                    }

                }

            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cocktails.data.searchCocktails?.size ?: 0) {
                        SearchCocktailCardView(model = cocktails.data.searchCocktails?.get(it)!!) {
                            navAction.navigateToCocktailDetail(it!!)
                        }
                    }
                }
            }
        }


    }
    if (cocktails.isPageLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .alpha(0.5f), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

}


@Composable
fun WideMiniCocktailCardView(
    model: CocktailModel?, onClick: (cocktailId: String?) -> Unit = {}
) {


    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .clickable { onClick(model?.id) }
            .padding(4.dp)

    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = model?.image,
                contentDescription = ""
            )
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    maxLines = 2,
                    text = model?.title.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis
                )
            }


        }
    }


}


