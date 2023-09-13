package com.soethan.todocompose.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.soethan.todocompose.data.models.Priority
import com.soethan.todocompose.data.models.ToDoTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ToDoDaoTest {
    private lateinit var database: ToDoDatabase
    private lateinit var toDoDao: ToDoDao


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            ToDoDatabase::class.java
        ).allowMainThreadQueries().build()
        toDoDao = database.toDoDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addTaskInsertsATaskIntoDatabase() =
        runTest {
            val taskToAdd = ToDoTask(
                id = 1,
                title = "Test Task",
                description = "Test Description",
                priority = Priority.MEDIUM
            )
            // Act: Call the addTask function
            toDoDao.addTask(taskToAdd)
            val retrievedTask =
                toDoDao.getSelectedTask(taskToAdd.id)
                    .first() // Assuming there's a getTaskById function
            assert(retrievedTask.id == taskToAdd.id)
            assert(retrievedTask.title == taskToAdd.title)
            assert(retrievedTask.description == taskToAdd.description)
            assert(retrievedTask.priority == taskToAdd.priority)

        }


    @Test
    fun deleteTaskRemovesTaskFromDatabase() = runTest {
        val taskToAdd = ToDoTask(
            id = 1,
            title = "Test Task",
            description = "Test Description",
            priority = Priority.MEDIUM
        )
        // Act: Call the addTask function
        toDoDao.addTask(taskToAdd)
        // Act: Delete the task using the deleteTask function
        toDoDao.deleteTask(taskToAdd)

        // Assert: Ensure that the task has been deleted by attempting to retrieve it
        val deletedTask = toDoDao.getSelectedTask(taskToAdd.id).first()

        // Verify that the deleted task is null
        assert(deletedTask == null)

    }


    @Test
    fun getSelectedTaskReturnsTheCorrectTaskFromFlow() = runTest {
        val taskToAdd = ToDoTask(
            id = 1,
            title = "Test Task",
            description = "Test Description",
            priority = Priority.MEDIUM
        )
        toDoDao.addTask(taskToAdd)

        // Act: Get the task using the getSelectedTask function
        val taskFlow = toDoDao.getSelectedTask(taskToAdd.id)

        // Assert: Collect the Flow and verify the emitted task
        val resultTask = taskFlow.first()
        assert(resultTask.id == taskToAdd.id)
        assert(resultTask.title == taskToAdd.title)
        assert(resultTask.description == taskToAdd.description)
    }

    @Test
    fun updateTaskUpdateTaskInDb() = runTest {
        val taskToAdd = ToDoTask(
            id = 1,
            title = "Test Task",
            description = "Test Description",
            priority = Priority.MEDIUM
        )
        toDoDao.addTask(taskToAdd)

        // Act: Update the task using the updateTask function
        val updatedTask = taskToAdd.copy(title = "Updated Title")
        toDoDao.updateTask(updatedTask)

        // Assert: Retrieve the task from the database and verify the update
        val retrievedTask =
            toDoDao.getSelectedTask(taskToAdd.id).first() // Assuming there's a getTaskById function
        assert(retrievedTask.id == updatedTask.id)
        assert(retrievedTask.title == updatedTask.title)
        assert(retrievedTask.description == updatedTask.description)


    }

    @Test
    fun deleteAllTasksDeleteAllTaskFromDatabase() = runTest {
        val task1 = ToDoTask(
            id = 1,
            title = "Task 1",
            description = "Description 1",
            priority = Priority.MEDIUM
        )
        val task2 = ToDoTask(
            id = 2,
            title = "Task 2",
            description = "Description 2",
            priority = Priority.NONE
        )
        val task3 = ToDoTask(
            id = 3,
            title = "Task 3",
            description = "Description 3",
            priority = Priority.HIGH
        )
        toDoDao.addTask(task1)
        toDoDao.addTask(task2)
        toDoDao.addTask(task3)

        // Act: Delete all tasks using the deleteAllTasks function
        toDoDao.deleteAllTasks()

        // Assert: Verify that the database is empty
        val count = toDoDao.getAllTasks().first().count()
        assert(count == 0)

    }

    @Test
    fun getAllTasksReturnTheCorrectListOfTask() = runTest {
        // Arrange: Insert some tasks into the database
        val task1 = ToDoTask(
            id = 1,
            title = "Task 1",
            description = "Description 1",
            priority = Priority.MEDIUM
        )
        val task2 = ToDoTask(
            id = 2, title = "Task 2", description = "Description 2",
            priority = Priority.MEDIUM
        )
        val task3 = ToDoTask(
            id = 3,
            title = "Task 3",
            description = "Description 3",
            priority = Priority.MEDIUM
        )
        toDoDao.addTask(task1)
        toDoDao.addTask(task2)
        toDoDao.addTask(task3)


        // Act: Get all tasks using the getAllTasks function
        val tasksFlow = toDoDao.getAllTasks()

        // Assert: Collect the Flow and verify the emitted list of tasks
        val resultTasks = tasksFlow.first()
        assert(resultTasks.size == 3)
        assert(resultTasks.contains(task1))
        assert(resultTasks.contains(task2))
        assert(resultTasks.contains(task3))
    }


    @Test
    fun searchDatabaseReturnsTheCorrectListOfTasks() = runTest {
        // Arrange: Insert some tasks into the database
        val task1 = ToDoTask(
            id = 1,
            title = "Task 1",
            description = "Description 1",
            priority = Priority.MEDIUM
        )
        val task2 = ToDoTask(
            id = 2,
            title = "Task 2",
            description = "Description 2",
            priority = Priority.MEDIUM
        )
        val task3 = ToDoTask(
            id = 3,
            title = "Task 3",
            description = "Description 3",
            priority = Priority.MEDIUM
        )
        toDoDao.addTask(task1)
        toDoDao.addTask(task2)
        toDoDao.addTask(task3)

        // Act: Search for tasks using the searchDatabase function
        val searchQuery = "Task"
        val searchResultsFlow = toDoDao.searchDatabase("%$searchQuery%")

        // Assert: Collect the Flow and verify the emitted list of tasks
        val searchResults = searchResultsFlow.first()
        assert(searchResults.size == 3)
        assert(searchResults.contains(task1))
        assert(searchResults.contains(task2))
        assert(searchResults.contains(task3))
    }


    @Test
    fun sortByLowPriorityReturnsTheCorrectListOfTasksOrderedByPriority() = runTest {
        // Arrange: Insert some tasks with different priorities into the database
        val task1 = ToDoTask(
            id = 1,
            title = "Task 1",
            description = "Description 1",
            priority = Priority.HIGH
        )
        val task2 = ToDoTask(
            id = 2,
            title = "Task 2",
            description = "Description 2",
            priority = Priority.LOW
        )
        val task3 = ToDoTask(
            id = 3,
            title = "Task 3",
            description = "Description 3",
            priority = Priority.MEDIUM
        )
        toDoDao.addTask(task1)
        toDoDao.addTask(task2)
        toDoDao.addTask(task3)

        // Act: Get tasks sorted by low priority using the sortByLowPriority function
        val sortedTasksFlow = toDoDao.sortByLowPriority()
        val sortedTasks = sortedTasksFlow.first()
        assert(sortedTasks.size == 3)
        assert(sortedTasks[0] == task2)
        assert(sortedTasks[1] == task3)
        assert(sortedTasks[2] == task1)
    }


    @Test
    fun sortByHighPriorityReturnsTheCorrectListOfTasksOrderedByPriority() = runTest {
        // Arrange: Insert some tasks with different priorities into the database
        val task1 = ToDoTask(
            id = 1,
            title = "Task 1",
            description = "Description 1",
            priority = Priority.HIGH
        )
        val task2 = ToDoTask(
            id = 2,
            title = "Task 2",
            description = "Description 2",
            priority = Priority.LOW
        )
        val task3 = ToDoTask(
            id = 3,
            title = "Task 3",
            description = "Description 3",
            priority = Priority.MEDIUM
        )
        toDoDao.addTask(task1)
        toDoDao.addTask(task2)
        toDoDao.addTask(task3)

        // Act: Get tasks sorted by low priority using the sortByLowPriority function
        val sortedTasksFlow = toDoDao.sortByHighPriority()
        val sortedTasks = sortedTasksFlow.first()
        assert(sortedTasks.size == 3)
        assert(sortedTasks[0] == task1)
        assert(sortedTasks[1] == task3)
        assert(sortedTasks[2] == task2)
    }
}