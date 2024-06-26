package com.nsicyber.barblend.presentation.search

import androidx.compose.runtime.Immutable
import com.nsicyber.barblend.data.model.CocktailModel

data class SearchScreenState(
    val data: SearchScreenData = SearchScreenData(),
    val isSearchLoading: Boolean = false,
    val isPageLoading: Boolean = false,
)

@Immutable
data class SearchScreenData(
    val recentCocktails: List<CocktailModel?>? = null,
    val searchCocktails: List<CocktailModel?>? = null,
)
