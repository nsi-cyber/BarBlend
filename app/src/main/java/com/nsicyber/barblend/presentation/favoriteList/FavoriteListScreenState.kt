package com.nsicyber.barblend.presentation.favoriteList

import com.nsicyber.barblend.data.model.CocktailModel

data class FavoriteListScreenState(
    val data: FavoriteListScreenData = FavoriteListScreenData(),
    val isLoading: Boolean = false,
)

data class FavoriteListScreenData(
    val favoriteCocktails: List<CocktailModel?>? = null,
)
