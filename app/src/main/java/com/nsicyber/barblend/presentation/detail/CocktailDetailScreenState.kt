package com.nsicyber.barblend.presentation.detail

import com.nsicyber.barblend.data.model.CocktailModel


data class CocktailDetailScreenState(
    val data: CocktailDetailScreenData = CocktailDetailScreenData(),
    val isLoading: Boolean = true,
    val bottomSheetData: BottomSheetData = BottomSheetData()
)

data class CocktailDetailScreenData(
    val cocktailDetail: List<CocktailModel?>? = null,
    val isFavorite: Boolean? = null,
)


data class BottomSheetData(
    var suggestion: String? = null,
    val text: String? = null,
    val bottomSheetState: BottomSheetState = BottomSheetState.onDismiss
)

enum class BottomSheetState { onInput, onDismiss, onMessage }


fun CocktailDetailScreenData?.isContent(): Boolean {
    return this?.cocktailDetail != null && this.isFavorite != null
}