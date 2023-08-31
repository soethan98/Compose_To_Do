package com.soethan.todocompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.soethan.todocompose.ui.presentation.screens.list.ListScreen
import com.soethan.todocompose.ui.presentation.screens.task.TaskScreen

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
        ) { navBackStackEntry ->
            val taskId = navBackStackEntry.arguments!!.getInt("taskId")
            TaskScreen(taskId = taskId, onNavigateBackToList = { action ->
                navController.navigate(route = "list/${action.name}") {
                    popUpTo(Screens.List.route) { inclusive = true }
                }
            })
        }
    }
}