package com.nsicyber.barblend.presentation.favoriteList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsicyber.barblend.presentation.components.SearchCocktailCardView
import com.nsicyber.barblend.presentation.navigation.NavigationActions


@Composable
fun FavoriteListScreen(
    navAction: NavigationActions,
    viewModel: FavoriteListScreenViewModel = hiltViewModel<FavoriteListScreenViewModel>(),
) {
    val cocktails by viewModel.favoriteListScreenState.collectAsState(
        initial = FavoriteListScreenState(isLoading = true)
    )




    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }


    Box() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {


            Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
                Text(
                    text = "Favorite",
                    color = Color.Gray,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Cocktails",
                    fontSize = 28.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (cocktails.data.favoriteCocktails?.isEmpty() == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Favorite",
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "You can explore new cocktails to add favorite.",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Normal,
                        )
                    }

                }

            } else
            {   LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(cocktails.data.favoriteCocktails?.size ?: 0) {
                    SearchCocktailCardView(model = cocktails.data.favoriteCocktails?.get(it)!!) {
                        navAction.navigateToFavoriteDetail(it!!)
                    }
                }
            }}

        }

        if (cocktails.isLoading) {
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


