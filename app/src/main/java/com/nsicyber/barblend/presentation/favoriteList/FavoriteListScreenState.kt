package com.nsicyber.barblend.presentation.favoriteList

import androidx.compose.runtime.Immutable
import com.nsicyber.barblend.data.model.CocktailModel

data class FavoriteListScreenState(
    val data: FavoriteListScreenData = FavoriteListScreenData(),
    val isLoading: Boolean = false,
)

@Immutable
data class FavoriteListScreenData(
    val favoriteCocktails: List<CocktailModel?>? = null,
)
