package com.soethan.todocompose.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailScreen(onBack: () -> Unit) {
    Box {
        Button(onClick = {
            onBack()
        }) {
            Text(text = "Detail Page")
        }
    }
}