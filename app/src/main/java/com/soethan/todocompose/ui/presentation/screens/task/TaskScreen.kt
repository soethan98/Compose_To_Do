package com.soethan.todocompose.ui.presentation.screens.task

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.soethan.todocompose.data.models.Priority
import com.soethan.todocompose.data.models.ToDoTask
import com.soethan.todocompose.ui.presentation.viewmodels.TaskDetailViewModel
import com.soethan.todocompose.util.Action

@Composable
fun TaskScreen(
    taskId: Int,
    onNavigateBackToList: (Action) -> Unit,
    taskDetailViewModel: TaskDetailViewModel = hiltViewModel()
) {


    val title: String by taskDetailViewModel.title
    val description: String by taskDetailViewModel.description
    val priority: Priority by taskDetailViewModel.priority
    val selectedTask by taskDetailViewModel.selectedTask.collectAsState()

    LaunchedEffect(key1 = taskId){
        taskDetailViewModel.getSelectedTask(taskId = taskId)
    }
    val context = LocalContext.current

    Scaffold(topBar = {
        TaskAppBar(selectedTask = selectedTask,
            navigateToListScreen = {action ->
            if (action == Action.NO_ACTION){
                onNavigateBackToList(action)
            }else{
                if (taskDetailViewModel.validateFields()) {
                    taskDetailViewModel.handleDatabaseActions(action = action)
                    onNavigateBackToList(action)
                } else {
                    displayToast(context = context)
                }
            }
        })
    }) { paddingValues ->
        TaskContent(
            title = title,
            onTitleChange = {
                taskDetailViewModel.updateTitle(it)
            },
            description = description,
            onDescriptionChange = {
                taskDetailViewModel.updateDesc(it)
            },
            priority = priority,
            onPrioritySelected = {
                taskDetailViewModel.updatePriority(it)
            },
            modifier = Modifier.padding(paddingValues)

        )

    }
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "Fields Empty.",
        Toast.LENGTH_SHORT
    ).show()
}