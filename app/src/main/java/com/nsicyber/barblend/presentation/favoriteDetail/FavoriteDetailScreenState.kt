package com.nsicyber.barblend.presentation.favoriteDetail


import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.presentation.detail.BottomSheetData


data class FavoriteDetailScreenState(
    val data: FavoriteDetailScreenData = FavoriteDetailScreenData(),
    val isLoading: Boolean = true,
    val isRemoved: Boolean = false,
    val bottomSheetData: FavoriteDetailBottomSheetData = FavoriteDetailBottomSheetData()
)

data class FavoriteDetailScreenData(
    val cocktailDetail: List<CocktailModel?>? = null,
    val isFavorite: Boolean? = true,
)


data class FavoriteDetailBottomSheetData(
    var suggestion: String? = null,
    val text: String? = null,
    val bottomSheetState: FavoriteDetailBottomSheetState = FavoriteDetailBottomSheetState.onDismiss
)

enum class FavoriteDetailBottomSheetState {  onDismiss, onMessage }


fun FavoriteDetailScreenData?.isContent(): Boolean {
    return this?.cocktailDetail != null
}