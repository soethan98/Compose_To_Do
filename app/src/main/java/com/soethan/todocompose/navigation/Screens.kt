package com.soethan.todocompose.navigation

sealed class Screens(val route:String){
    object List : Screens("/")
    object Detail: Screens("/detail/{id}")
}