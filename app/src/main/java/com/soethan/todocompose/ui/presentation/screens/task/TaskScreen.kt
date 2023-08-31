package com.soethan.todocompose.ui.presentation.screens.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.soethan.todocompose.ui.presentation.viewmodels.TaskDetailViewModel
import com.soethan.todocompose.util.Action

@Composable
fun TaskScreen(
    taskId: Int,
    onNavigateBackToList: (Action) -> Unit,
    taskDetailViewModel: TaskDetailViewModel = hiltViewModel()
) {
    
}