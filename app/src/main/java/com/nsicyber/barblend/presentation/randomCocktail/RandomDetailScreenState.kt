package com.nsicyber.barblend.presentation.randomCocktail

import androidx.compose.runtime.Immutable
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.presentation.detail.BottomSheetData

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
