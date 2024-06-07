package com.nsicyber.barblend.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.nsicyber.barblend.presentation.search.SearchScreen
import com.nsicyber.barblend.presentation.splash.SplashScreen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities =
        connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

sealed class BottomNavItem(var title: String, var icon: Int, var route: String) {
    data object Explore : BottomNavItem("Explore", R.drawable.ic_menu_explore, Destination.EXPLORE)
    data object Search : BottomNavItem("Search", R.drawable.ic_menu_search, Destination.SEARCH)
    data object Favorites :
        BottomNavItem("Favorites", R.drawable.ic_menu_favorite, Destination.FAVORITES)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Explore, BottomNavItem.Search, BottomNavItem.Favorites
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(icon = {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = item.icon), contentDescription = item.title
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
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
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
    context: Context,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destination.SPLASH,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    var isConnected by remember { mutableStateOf(isInternetAvailable(context)) }





    LaunchedEffect(Unit) {
        isConnected = isInternetAvailable(context)
    }

    val showBottomBar =
        currentDestination in listOf(
            Destination.EXPLORE,
            Destination.FAVORITES,
            Destination.SEARCH
        ) && isConnected

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = Destination.SPLASH) {

                SplashScreen(onSplashFinished = {
                    if (isConnected)
                        navActions.navigateToExplore()
                    else
                        navActions.navigateToFavorites()
                })

            }

            composable(route = Destination.EXPLORE) {

                ExploreScreen(navActions)

            }

            composable(route = Destination.SEARCH) {

                SearchScreen(navActions)

            }

            composable(route = Destination.FAVORITES) {

                FavoriteListScreen(navActions)


            }

            composable(
                route = "${Destination.COCKTAIL_DETAIL}?id={id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.StringType
                })
            ) { navBackStackEntry ->
                val cocktailId = navBackStackEntry.arguments?.getString("id")
                CocktailDetailScreen(cocktailId = cocktailId)
            }

            composable(
                route = "${Destination.FAVORITE_DETAIL}?id={id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.StringType
                })
            ) { navBackStackEntry ->
                val cocktailId = navBackStackEntry.arguments?.getString("id")
                FavoriteDetailScreen(navAction = navActions, cocktailId = cocktailId)
            }
        }
    }

}

