package com.nsicyber.barblend.presentation.randomCocktail

sealed class RandomDetailScreenEvent {
    data class TypeSuggestion(var str: String?) : RandomDetailScreenEvent()

    data object RemoveFromFavorites : RandomDetailScreenEvent()

    data object StartScreen : RandomDetailScreenEvent()

    data object AddToFavorites : RandomDetailScreenEvent()

    data object OpenSuggestionBottomSheet : RandomDetailScreenEvent()

    data object ResetBottomSheet : RandomDetailScreenEvent()
}
