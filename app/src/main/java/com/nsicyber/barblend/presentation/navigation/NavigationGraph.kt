package com.nsicyber.barblend.presentation.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nsicyber.barblend.R
import com.nsicyber.barblend.presentation.detail.CocktailDetailScreen
import com.nsicyber.barblend.presentation.explore.ExploreScreen
import com.nsicyber.barblend.presentation.favoriteDetail.FavoriteDetailScreen
import com.nsicyber.barblend.presentation.favoriteList.FavoriteListScreen
import com.nsicyber.barblend.presentation.randomCocktail.RandomDetailScreen
import com.nsicyber.barblend.presentation.search.SearchScreen
import com.nsicyber.barblend.presentation.splash.SplashScreen

sealed class BottomNavItem(var title: String, var icon: Int, var route: String) {
    data object Explore : BottomNavItem("Explore", R.drawable.ic_menu_explore, Destination.EXPLORE)

    data object Search : BottomNavItem("Search", R.drawable.ic_menu_search, Destination.SEARCH)

    data object Favorites :
        BottomNavItem("Favorites", R.drawable.ic_menu_favorite, Destination.FAVORITES)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items =
        listOf(
            BottomNavItem.Explore,
            BottomNavItem.Search,
            BottomNavItem.Favorites,
        )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(icon = {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                )
            }, label = { Text(item.title) }, selected = currentRoute == item.route, onClick = {
                if (currentRoute != item.route) {
                    navController.navigate(item.route)
                }
            })
        }
    }
}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    enabled: Boolean = true,
    onBackPressed: () -> Unit,
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback =
        remember {
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    currentOnBackPressed()
                }
            }
        }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}

@Composable
fun NavigationGraph(
    isConnected: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destination.SPLASH,
    navActions: NavigationActions =
        remember(navController) {
            NavigationActions(navController)
        },
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val showBottomBar =
        currentDestination in
            listOf(
                Destination.EXPLORE,
                Destination.FAVORITES,
                Destination.SEARCH,
            ) && isConnected

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            ) {
                BottomNavigationBar(navController = navController)
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding),
        ) {
            composable(route = Destination.SPLASH) {
                SplashScreen(onSplashFinished = {
                    if (isConnected) {
                        navActions.navigateToExplore()
                    } else {
                        navActions.navigateToFavorites()
                    }
                })
            }

            composable(route = Destination.EXPLORE) {
                ExploreScreen(
                    onDetail = { id -> navActions.navigateToCocktailDetail(id) },
                    onRandom = { navActions.navigateToRandomDetail() },
                    onSearch = { navActions.navigateToSearch() },
                )
            }

            composable(route = Destination.SEARCH) {
                SearchScreen(onDetail = { id -> navActions.navigateToCocktailDetail(id) })
            }

            composable(route = Destination.FAVORITES) {
                FavoriteListScreen(onDetail = { id -> navActions.navigateToFavoriteDetail(id) })
            }

            composable(route = Destination.RANDOM_DETAIL) {
                RandomDetailScreen()
            }

            composable(
                route = "${Destination.COCKTAIL_DETAIL}?id={id}",
                arguments =
                    listOf(
                        navArgument("id") {
                            type = NavType.StringType
                        },
                    ),
            ) { navBackStackEntry ->
                val cocktailId = navBackStackEntry.arguments?.getString("id")
                CocktailDetailScreen(cocktailId = cocktailId)
            }

            composable(
                route = "${Destination.FAVORITE_DETAIL}?id={id}",
                arguments =
                    listOf(
                        navArgument("id") {
                            type = NavType.StringType
                        },
                    ),
            ) { navBackStackEntry ->
                val cocktailId = navBackStackEntry.arguments?.getString("id")
                FavoriteDetailScreen(navAction = navActions, cocktailId = cocktailId)
            }

            if (!isConnected && currentDestination in
                listOf(
                    Destination.EXPLORE,
                    Destination.SEARCH,
                )
            ) {
                navActions.navigateToFavorites()
            }
        }
    }
}
