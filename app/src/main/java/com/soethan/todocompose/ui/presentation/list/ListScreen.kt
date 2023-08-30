package com.soethan.todocompose.ui.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.soethan.todocompose.R
import com.soethan.todocompose.ui.presentation.SharedViewModel
import com.soethan.todocompose.ui.theme.fabBackgroundColor
import com.soethan.todocompose.util.SearchAppBarState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListScreen(onNavigateToDetail: (Int) -> Unit, viewModel: SharedViewModel = hiltViewModel()) {

    val searchAppBarState: SearchAppBarState
            by viewModel.searchAppBarState
    val searchTextState: String by viewModel.searchTextState
    val allTasks by viewModel.allTask.collectAsState()

    Scaffold(topBar = {
        ListAppBar(
            searchAppBarState = searchAppBarState,
            onUpdateSearchAppBarState = { state -> viewModel.updateSearchAppBarState(state) },
            onSearchTextChange = { value -> viewModel.updateSearchTextState(value) },
            searchTextState = searchTextState
        )
    }, floatingActionButton = {
        ListFab(onFabClicked = onNavigateToDetail)
    }) { paddingValues ->
        ListContent(
            modifier = Modifier.padding(paddingValues),
            tasks = allTasks,
            navigateToTaskScreen = { taskId: Int ->
                onNavigateToDetail(taskId)
            }
        )
    }
}


@Composable
fun ListFab(modifier: Modifier = Modifier, onFabClicked: (taskId: Int) -> Unit) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        shape = CircleShape,
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button), tint = Color.White
        )
    }
}