package com.nsicyber.barblend.presentation.favoriteDetail

sealed class FavoriteDetailScreenEvent {
    data object RemoveFromFavorites : FavoriteDetailScreenEvent()

    data class StartPage(val id: String?) : FavoriteDetailScreenEvent()

    data object ResetBottomSheet : FavoriteDetailScreenEvent()
}
