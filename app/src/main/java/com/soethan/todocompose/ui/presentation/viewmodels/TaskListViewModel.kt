package com.soethan.todocompose.ui.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soethan.todocompose.data.models.ToDoTask
import com.soethan.todocompose.data.repositories.ToDoRepository
import com.soethan.todocompose.util.Resource
import com.soethan.todocompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {
    private val _searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchAppBarState: State<SearchAppBarState> = _searchAppBarState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    private val _allTasks = MutableStateFlow<Resource<List<ToDoTask>>>(Resource.Idle)
    val allTask: StateFlow<Resource<List<ToDoTask>>> get() = _allTasks


    private val _searchedTasks =
        MutableStateFlow<Resource<List<ToDoTask>>>(Resource.Idle)
    val searchedTasks: StateFlow<Resource<List<ToDoTask>>> get() = _searchedTasks


    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            try {
                repository.getAllTasks.collect {
                    _allTasks.value = Resource.Success(it)
                }
            } catch (e: Exception) {
                _allTasks.value = Resource.Error(e)
            }

        }
    }


    fun searchDatabase(searchQuery: String) {
        _searchedTasks.value = Resource.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect { searchedTasks ->
                        _searchedTasks.value = Resource.Success(searchedTasks)
                    }
            }
        } catch (e: Exception) {
            _searchedTasks.value = Resource.Error(e)
        }
        _searchAppBarState.value = SearchAppBarState.TRIGGERED
    }


    fun updateSearchAppBarState(state: SearchAppBarState) {
        _searchAppBarState.value = state
    }

    fun updateSearchTextState(state: String) {
        _searchTextState.value = state
    }

}