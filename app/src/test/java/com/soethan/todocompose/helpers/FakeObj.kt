package com.soethan.todocompose.helpers

import com.soethan.todocompose.data.models.Priority
import com.soethan.todocompose.data.models.ToDoTask

val tasks = listOf(
    ToDoTask(
        id = 1,
        title = "Task 1",
        description = "Task 1 Description",
        priority = Priority.MEDIUM
    ),
    ToDoTask(
        id = 2,
        title = "Task 2",
        description = "Task 1 Description",
        priority = Priority.LOW
    ),
    ToDoTask(
        id = 3,
        title = "Task 3",
        description = "Task 3 Description",
        priority = Priority.HIGH
    )
)

val task = ToDoTask(
    id = 1,
    title = "Task 1",
    description = "Task 1 Description",
    priority = Priority.MEDIUM
);