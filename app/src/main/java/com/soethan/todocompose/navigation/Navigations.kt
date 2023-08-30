package com.soethan.todocompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.soethan.todocompose.ui.presentation.DetailScreen
import com.soethan.todocompose.ui.presentation.list.ListScreen

@Composable
fun ToDoNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screens.List.route) {
        composable(Screens.List.route, arguments = listOf(
            navArgument("action") {
                type = NavType.StringType
            }
        )) {
            ListScreen(onNavigateToDetail = { taskId ->
                navController.navigate("detail/$taskId")
            })
        }
        composable(
            Screens.Detail.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType; nullable = false },
            )
        ) {
            DetailScreen { action ->
                navController.navigate(route = "list/${action.name}") {
                    popUpTo(Screens.List.route) { inclusive = true }
                }
            }
        }
    }
}