package com.lavalama.tabletimegame.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lavalama.tabletimegame.presentation.screen.favorites.FavoritesGameCard
import com.lavalama.tabletimegame.presentation.screen.favorites.FavoritesGamesScreen
import com.lavalama.tabletimegame.presentation.screen.gameDetails.GameDetailsScreen
import com.lavalama.tabletimegame.presentation.screen.gamesCatalog.GamesCatalogScreen

@Composable
fun NavGraph(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.GamesCatalog.route
    ){
        composable(Screen.GamesCatalog.route) {
            GamesCatalogScreen(
                onSettingsNavigate = {
                    navController.navigate(Screen.Settings.route)
                },
                onFavoriteNavigate = {
                    navController.navigate(Screen.FavoritesGames.route)
                },
                onGameDetailsNavigate = {
                    navController.navigate(Screen.GameDetails.createRoute(it))
                }
            )
        }
        composable(Screen.GameDetails.route) {
            val gameId = Screen.GameDetails.getGameId(it.arguments)
            GameDetailsScreen(
                gameId = gameId,
                onBack = {navController.popBackStack()}
            )
        }
        composable (Screen.FavoritesGames.route){
            FavoritesGamesScreen(
                onGameClick = {gameId ->
                    navController.navigate(Screen.GameDetails.createRoute(gameId))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable (Screen.Settings.route){
            //
        }
    }
}

sealed class Screen(val route: String){
    data object GamesCatalog: Screen("games_catalog")
    data object GameDetails: Screen("game_details/{gameId}"){
        fun createRoute(gameId: Int): String{
            return "game_details/${gameId}"
        }
        fun getGameId(arguments: Bundle?): Int{
            return arguments?.getString("gameId")?.toInt() ?: 0
        }
    }
    data object FavoritesGames: Screen("games_favorites")
    data object Settings: Screen("settings")
}