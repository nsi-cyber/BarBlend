package com.nsicyber.barblend.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder


object Destination {
    const val SPLASH = "splash"
    const val EXPLORE = "explore_screen"
    const val SEARCH = "search_screen"
    const val FAVORITES = "favorites_screen"
    const val COCKTAIL_DETAIL = "cocktail_detail_screen"
    const val FAVORITE_DETAIL = "favorite_detail_screen"

}

fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = true
    }
}

class NavigationActions(private val navController: NavHostController) {

    fun navigateToFavorites() {
        navController.navigate(Destination.FAVORITES){
            popUpToTop(navController)
        }
    }

    fun navigateToSearch() {
        navController.navigate(Destination.SEARCH){
            popUpToTop(navController)
        }
    }

    fun navigateToBack() {
        navController.popBackStack()
    }

    fun navigateToExplore() {
        navController.navigate(Destination.EXPLORE) {
            popUpToTop(navController)
        }
    }

    fun navigateToCocktailDetail(id: String) {
        navController.navigate("${Destination.COCKTAIL_DETAIL}?id=$id")
    }

    fun navigateToFavoriteDetail(id: String) {
        navController.navigate("${Destination.FAVORITE_DETAIL}?id=$id")
    }


}