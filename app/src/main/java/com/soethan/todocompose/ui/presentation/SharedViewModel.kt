package com.soethan.todocompose.ui.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soethan.todocompose.data.models.ToDoTask
import com.soethan.todocompose.data.repositories.ToDoRepository
import com.soethan.todocompose.util.Resource
import com.soethan.todocompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {
    private val _searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchAppBarState: State<SearchAppBarState> = _searchAppBarState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    private val _allTasks = MutableStateFlow<Resource<List<ToDoTask>>>(Resource.Idle)
    val allTask: StateFlow<Resource<List<ToDoTask>>> get() = _allTasks

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            repository.getAllTasks.collect {
                _allTasks.value = Resource.Success(it)
            }
        }
    }


    fun updateSearchAppBarState(state: SearchAppBarState) {
        _searchAppBarState.value = state
    }

    fun updateSearchTextState(state: String) {
        _searchTextState.value = state
    }

}