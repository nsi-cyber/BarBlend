package com.nsicyber.barblend.presentation.randomCocktail

import androidx.compose.runtime.Immutable
import com.nsicyber.barblend.data.model.CocktailModel

data class RandomDetailScreenState(
    val data: RandomDetailScreenData = RandomDetailScreenData(),
    val isLoading: Boolean = true,
    val bottomSheetData: BottomSheetData = BottomSheetData(),
)

@Immutable
data class RandomDetailScreenData(
    val cocktailDetail: CocktailModel? = null,
    val isFavorite: Boolean? = null,
)

data class BottomSheetData(
    var suggestion: String? = null,
    val text: String? = null,
    val bottomSheetState: RandomBottomSheetState = RandomBottomSheetState.onDismiss,
)

enum class RandomBottomSheetState { onInput, onDismiss, onMessage }
