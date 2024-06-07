package com.nsicyber.barblend.presentation.navigation

import androidx.navigation.NavHostController


object Destination {
    const val SPLASH = "splash"
    const val EXPLORE = "explore_screen"
    const val SEARCH = "search_screen"
    const val FAVORITES = "favorites_screen"
    const val COCKTAIL_DETAIL = "cocktail_detail_screen"
    const val FAVORITE_DETAIL = "favorite_detail_screen"

}

class NavigationActions(private val navController: NavHostController) {

    fun navigateToFavorites() {
        navController.navigate(Destination.FAVORITES)
    }

    fun navigateToSearch() {
        navController.navigate(Destination.SEARCH)
    }
    fun navigateToBack() {
        navController.popBackStack()
    }

    fun navigateToExplore() {
        navController.navigate(Destination.EXPLORE)
    }

    fun navigateToCocktailDetail(id: String) {
        navController.navigate("${Destination.COCKTAIL_DETAIL}?id=$id")
    }

    fun navigateToFavoriteDetail(id: String) {
        navController.navigate("${Destination.FAVORITE_DETAIL}?id=$id")
    }


}