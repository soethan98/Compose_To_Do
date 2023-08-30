package com.soethan.todocompose.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import com.soethan.todocompose.util.Action

@Composable
fun DetailScreen(navigateToListScreen: (Action) -> Unit) {
    Box {
        Button(onClick = {
            navigateToListScreen(Action.NO_ACTION)
        }) {
            Text(text = "Detail Page")
        }
    }
}