package com.soethan.todocompose.navigation

sealed class Screens(val route: String) {
    object List : Screens("list/{action}")
    object Detail : Screens("detail/{taskId}")
}


const val LIST_SCREEN = "list/{action}"
const val TASK_SCREEN = "task/{taskId}"

const val LIST_ARGUMENT_KEY = "action"
const val TASK_ARGUMENT_KEY = "taskId"