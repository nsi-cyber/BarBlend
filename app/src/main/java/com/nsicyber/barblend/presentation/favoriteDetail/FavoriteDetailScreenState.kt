package com.nsicyber.barblend.presentation.favoriteDetail

import androidx.compose.runtime.Immutable
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.presentation.detail.BottomSheetData

data class FavoriteDetailScreenState(
    val data: FavoriteDetailScreenData = FavoriteDetailScreenData(),
    val isLoading: Boolean = true,
    val isRemoved: Boolean = false,
    val bottomSheetData: BottomSheetData = BottomSheetData(),
)

@Immutable
data class FavoriteDetailScreenData(
    val cocktailDetail: CocktailModel? = null,
    val isFavorite: Boolean? = true,
)
