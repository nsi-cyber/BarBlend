package com.nsicyber.barblend.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nsicyber.barblend.R
import com.nsicyber.barblend.presentation.components.KeywordCardItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailScreen(
    cocktailId: String?,
    viewModel: CocktailDetailScreenViewModel = hiltViewModel<CocktailDetailScreenViewModel>(),
) {
    val cocktails by viewModel.cocktailDetailScreenState.collectAsState(
        initial = CocktailDetailScreenState(isLoading = true)
    )

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
    )

    LaunchedEffect(Unit) {
        viewModel.onEvent(CocktailDetailScreenEvent.StartPage(cocktailId))
    }

    LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
        if (bottomSheetState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded)
            viewModel.onEvent(CocktailDetailScreenEvent.ResetBottomSheet)
    }
    LaunchedEffect(cocktails) {
        when (cocktails.bottomSheetData.bottomSheetState) {
            BottomSheetState.onInput -> {

                coroutineScope.launch {
                    bottomSheetState.bottomSheetState.expand()
                }
            }

            BottomSheetState.onMessage -> {
                coroutineScope.launch {
                    bottomSheetState.bottomSheetState.expand()
                }
            }

            BottomSheetState.onDismiss -> {
                coroutineScope.launch {
                    bottomSheetState.bottomSheetState.hide()
                }
            }
        }

    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetState,
        sheetContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = cocktails.bottomSheetData.text.orEmpty(),
                    style = MaterialTheme.typography.titleMedium
                )

                if (cocktails.bottomSheetData.bottomSheetState == BottomSheetState.onInput) {
                    TextField(
                        placeholder = { Text(text = "You Can add notes") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(color = Color.White),
                        value = cocktails.bottomSheetData.suggestion.orEmpty(),
                        onValueChange = { value ->
                            viewModel.onEvent(CocktailDetailScreenEvent.TypeSuggestion(value))
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Color.White,
                            disabledTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                    )
                    Box(
                        modifier = Modifier

                            .clip(RoundedCornerShape(20.dp))

                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { viewModel.onEvent(CocktailDetailScreenEvent.AddToFavorites) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Favorite")
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        if (!cocktails.isLoading)
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(4 / 5f)
                    ) {
                        AsyncImage(
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize(),
                            model = cocktails.data.cocktailDetail?.first()?.image,
                            contentDescription = ""
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    if (cocktails.data.isFavorite == true) {
                                        viewModel.onEvent(CocktailDetailScreenEvent.RemoveFromFavorites)
                                    } else {
                                        viewModel.onEvent(CocktailDetailScreenEvent.OpenSuggestionBottomSheet)

                                    }
                                }
                                .size(64.dp)
                                .alpha(0.7f)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                colorFilter = if (cocktails.data.isFavorite == true) ColorFilter.tint(
                                    Color.Red
                                ) else null,
                                painter = painterResource(id = R.drawable.ic_menu_favorite),
                                contentDescription = ""
                            )
                        }
                    }



                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {

                        Column(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp
                            )
                        ) {
                            Text(
                                text = cocktails.data.cocktailDetail?.first()?.category.orEmpty()
                            )
                            Text(
                                text = cocktails.data.cocktailDetail?.first()?.title.orEmpty(),
                                fontSize = 28.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )

                        }

                        cocktails.data.cocktailDetail?.first()?.tags?.let {
                            Column {
                                Text(
                                    text = stringResource(id = R.string.tags_title),
                                    color = Color.Gray,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp)
                                )


                                LazyHorizontalStaggeredGrid(
                                    contentPadding = PaddingValues(start = 16.dp),
                                    modifier = Modifier.height(72.dp),
                                    rows = StaggeredGridCells.Fixed(2)
                                ) {
                                    items(
                                        cocktails.data.cocktailDetail?.first()?.tags?.size ?: 0
                                    ) { index ->
                                        KeywordCardItem(
                                            cocktails.data.cocktailDetail?.first()?.tags?.get(
                                                index
                                            )
                                        )
                                    }

                                }
                            }
                        }


                        cocktails.data.cocktailDetail?.first()?.instructions?.let {
                            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                                Text(
                                    text = stringResource(id = R.string.prepare_title),
                                    color = Color.Gray,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = stringResource(id = R.string.prepare_subtitle),
                                    fontSize = 28.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = cocktails.data.cocktailDetail?.first()?.instructions.orEmpty(),
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.fillMaxWidth()
                                )

                            }
                        }



                        cocktails.data.cocktailDetail?.first()?.glass?.let {
                            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                                Text(
                                    text = stringResource(id = R.string.glass_title),
                                    color = Color.Gray,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = cocktails.data.cocktailDetail?.first()?.glass.orEmpty(),
                                    fontSize = 28.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )


                            }
                        }


                        cocktails.data.cocktailDetail?.first()?.ingredients?.let {
                            Column(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 32.dp
                                )
                            ) {

                                Text(
                                    text = stringResource(id = R.string.ingredients_title),
                                    color = Color.Gray,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = stringResource(id = R.string.ingredients_subtitle),
                                    fontSize = 28.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))

                                Column {
                                    repeat(
                                        cocktails.data.cocktailDetail?.first()?.ingredients?.size
                                            ?: 0
                                    ) { index ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Text(
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.weight(4.5f),
                                                text = cocktails.data.cocktailDetail?.first()?.ingredients?.get(
                                                    index
                                                )?.ingredient.orEmpty()
                                            )


                                            Text(
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.weight(1f),
                                                text = " - "
                                            )

                                            Text(
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.weight(4.5f),
                                                text = cocktails.data.cocktailDetail?.first()?.ingredients?.get(
                                                    index
                                                )?.measure.orEmpty()
                                            )

                                        }
                                    }
                                }

                            }
                        }

                    }

                }

            }
        else {
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
