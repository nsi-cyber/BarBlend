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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavAction
import coil.compose.AsyncImage
import com.nsicyber.barblend.R
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.presentation.components.LatestCocktailCardView
import com.nsicyber.barblend.presentation.components.PopularCocktailCardView
import com.nsicyber.barblend.presentation.navigation.BackPressHandler
import com.nsicyber.barblend.presentation.navigation.NavigationActions
import kotlinx.coroutines.launch


@Composable
fun ExploreScreen(
    navAction: NavigationActions,
    viewModel: ExploreScreenViewModel = hiltViewModel<ExploreScreenViewModel>(),
) {

    val cocktails by viewModel.exploreScreenState.collectAsState(
        initial = ExploreScreenState(isLoading = true)
    )

    LaunchedEffect(Unit) {
        viewModel.getPopularCocktails()
        viewModel.getLatestCocktails()
    }



    Box {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())

        ) {

            if (!cocktails.data.popularCocktails.isNullOrEmpty()) {

                Column(modifier = Modifier.padding(start = 16.dp, top = 32.dp, bottom = 8.dp)) {
                    Text(
                        text = "Popular",
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
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(start = 16.dp)
                ) {
                    items(cocktails.data.popularCocktails!!) {
                        PopularCocktailCardView(model = it!!) {
                            navAction.navigateToCocktailDetail(it)
                        }
                    }
                }

            }

            Text(
                text = "Search",
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp)
            )
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .clickable {
                        navAction.navigateToSearch()
                    }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Discover Drinks...",
                        color = Color.Black,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.wrapContentSize()
                    )

                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search Icon",
                    )
                }

            }


            if (!cocktails.data.latestCocktails.isNullOrEmpty()) {

                Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
                    Text(
                        text = "Last Added",
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    repeat(cocktails.data.latestCocktails!!.size) {
                        LatestCocktailCardView(model = cocktails.data.latestCocktails!!.get(it)!!) {
                            navAction.navigateToCocktailDetail(it)
                        }
                    }
                }

            }

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












