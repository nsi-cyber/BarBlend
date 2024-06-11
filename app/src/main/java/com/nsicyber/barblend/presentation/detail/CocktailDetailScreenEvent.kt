package com.nsicyber.barblend.presentation.detail

sealed class CocktailDetailScreenEvent {
    data class TypeSuggestion(val str: String?) : CocktailDetailScreenEvent()

    data object RemoveFromFavorites : CocktailDetailScreenEvent()

    data object AddToFavorites : CocktailDetailScreenEvent()

    data class StartPage(val id: String?) : CocktailDetailScreenEvent()

    data object OpenSuggestionBottomSheet : CocktailDetailScreenEvent()

    data object ResetBottomSheet : CocktailDetailScreenEvent()
}
