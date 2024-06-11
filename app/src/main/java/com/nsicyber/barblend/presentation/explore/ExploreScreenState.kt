package com.nsicyber.barblend.presentation.explore

import androidx.compose.runtime.Immutable
import com.nsicyber.barblend.data.model.CocktailModel

data class ExploreScreenState(
    val data: ExploreScreenData = ExploreScreenData(),
    val isLoading: Boolean = true,
)

@Immutable
data class ExploreScreenData(
    val popularCocktails: List<CocktailModel?>? = null,
    val latestCocktails: List<CocktailModel?>? = null,
)

fun ExploreScreenData?.isContent(): Boolean {
    return this?.popularCocktails != null && this.latestCocktails != null
}
