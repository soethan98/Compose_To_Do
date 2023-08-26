package com.soethan.todocompose.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ListScreen(onNavigateToDetail: (Int) -> Unit) {
    Box {
        Button(onClick = {
            onNavigateToDetail(1)
        }) {
            Text(text = "Navigate To detail")
        }
    }
}