package com.nsicyber.barblend.presentation.favoriteList

import com.nsicyber.barblend.data.model.CocktailModel

sealed class FavoriteListScreenEvent {
    data object StartPage : FavoriteListScreenEvent()

    data class Remove(val model: CocktailModel) : FavoriteListScreenEvent()
}
