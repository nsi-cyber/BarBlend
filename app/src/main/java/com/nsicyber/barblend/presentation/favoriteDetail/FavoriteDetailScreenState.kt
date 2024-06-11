package com.nsicyber.barblend.presentation.favoriteDetail

import androidx.compose.runtime.Immutable
import com.nsicyber.barblend.data.model.CocktailModel

data class FavoriteDetailScreenState(
    val data: FavoriteDetailScreenData = FavoriteDetailScreenData(),
    val isLoading: Boolean = true,
    val isRemoved: Boolean = false,
    val bottomSheetData: FavoriteDetailBottomSheetData = FavoriteDetailBottomSheetData(),
)

@Immutable
data class FavoriteDetailScreenData(
    val cocktailDetail: CocktailModel? = null,
    val isFavorite: Boolean? = true,
)

data class FavoriteDetailBottomSheetData(
    var suggestion: String? = null,
    val text: String? = null,
    val bottomSheetState: FavoriteDetailBottomSheetState = FavoriteDetailBottomSheetState.onDismiss,
)

enum class FavoriteDetailBottomSheetState { onDismiss, onMessage }
