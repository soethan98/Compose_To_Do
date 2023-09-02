package com.soethan.todocompose.ui.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soethan.todocompose.data.models.Priority
import com.soethan.todocompose.data.models.ToDoTask
import com.soethan.todocompose.data.repositories.ToDoRepository
import com.soethan.todocompose.util.Action
import com.soethan.todocompose.util.Constants.MAX_TITLE_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(private val repository: ToDoRepository) :
    ViewModel() {

    private val _id: MutableState<Int> = mutableIntStateOf(0)
    val id: State<Int> = _id

    private val _title: MutableState<String> = mutableStateOf("")
    val title: State<String> get() = _title

    private val _description: MutableState<String> = mutableStateOf("")
    val description: State<String> get() = _description

    private val _priority: MutableState<Priority> = mutableStateOf(Priority.LOW)
    val priority: State<Priority> get() = _priority


    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> get() = _selectedTask




    fun getSelectedTask(taskId: Int) {
        if (taskId == -1) return
        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect { task ->
                _selectedTask.value = task
                updateTaskFields(task)
            }
        }
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            _id.value = selectedTask.id
            _title.value = selectedTask.title
            _description.value = selectedTask.description
            _priority.value = selectedTask.priority
        } else {
            _id.value = 0
            _title.value = ""
            _description.value = ""
            _priority.value = Priority.LOW
        }
    }


    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.deleteTask(toDoTask = toDoTask)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }


    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.addTask(toDoTask = toDoTask)
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.updateTask(toDoTask = toDoTask)
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTasks()
            }
            Action.UNDO -> {
                addTask()
            }
            else -> {

            }
        }
//        this.action.value = Action.NO_ACTION
    }

    fun updateTitle(value: String) {
        if (value.length < MAX_TITLE_LENGTH)
            _title.value = value
    }

    fun updateDesc(value: String) {
        _description.value = value
    }

    fun updatePriority(value: Priority) {
        _priority.value = value
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }


}