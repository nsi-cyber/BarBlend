package com.nsicyber.barblend.presentation.randomCocktail

import com.nsicyber.barblend.data.model.CocktailModel


data class RandomDetailScreenState(
    val data: RandomDetailScreenData = RandomDetailScreenData(),
    val isLoading: Boolean = true,
    val bottomSheetData: BottomSheetData = BottomSheetData()
)

data class RandomDetailScreenData(
    val cocktailDetail: List<CocktailModel?>? = null,
    val isFavorite: Boolean? = null,
)


data class BottomSheetData(
    var suggestion: String? = null,
    val text: String? = null,
    val bottomSheetState: RandomBottomSheetState = RandomBottomSheetState.onDismiss
)

enum class RandomBottomSheetState { onInput, onDismiss, onMessage }


fun RandomDetailScreenData?.isContent(): Boolean {
    return !this?.cocktailDetail.isNullOrEmpty() && this?.isFavorite != null
}