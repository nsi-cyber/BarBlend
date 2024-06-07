package com.nsicyber.barblend.presentation.favoriteDetail


sealed class FavoriteDetailScreenEvent {
    data object RemoveFromFavorites : FavoriteDetailScreenEvent()
    data object ResetBottomSheet : FavoriteDetailScreenEvent()
}