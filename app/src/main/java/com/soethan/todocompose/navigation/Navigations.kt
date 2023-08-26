package com.soethan.todocompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.soethan.todocompose.ui.presentation.DetailScreen
import com.soethan.todocompose.ui.presentation.ListScreen

@Composable
fun ToDoNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screens.List.route) {
        composable(Screens.List.route) {
            ListScreen(onNavigateToDetail = {
                navController.navigate("${Screens.Detail.route}/$it")
            })
        }
        composable(
            Screens.Detail.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType; nullable = false },
            )
        ) {
            DetailScreen {
                navController.popBackStack()
            }
        }
    }
}