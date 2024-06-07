package com.nsicyber.barblend.presentation.detail


sealed class CocktailDetailScreenEvent {
    data class TypeSuggestion(var str: String?) : CocktailDetailScreenEvent()
    data object RemoveFromFavorites : CocktailDetailScreenEvent()
    data object AddToFavorites : CocktailDetailScreenEvent()
    data object OpenSuggestionBottomSheet : CocktailDetailScreenEvent()
    data object ResetBottomSheet : CocktailDetailScreenEvent()
}