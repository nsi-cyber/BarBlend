package com.nsicyber.barblend.presentation.detail

import androidx.compose.runtime.Immutable
import com.nsicyber.barblend.data.model.CocktailModel

data class CocktailDetailScreenState(
    val data: CocktailDetailScreenData = CocktailDetailScreenData(),
    val isLoading: Boolean = true,
    val bottomSheetData: BottomSheetData = BottomSheetData(),
)

@Immutable
data class CocktailDetailScreenData(
    val cocktailDetail: CocktailModel? = null,
    val isFavorite: Boolean? = null,
)

data class BottomSheetData(
    var suggestion: String? = null,
    val text: String? = null,
    val bottomSheetState: BottomSheetState = BottomSheetState.onDismiss,
)

enum class BottomSheetState { onInput, onDismiss, onMessage }
