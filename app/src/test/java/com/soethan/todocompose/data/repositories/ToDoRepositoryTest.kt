package com.soethan.todocompose.data.repositories

import com.soethan.todocompose.data.ToDoDao
import com.soethan.todocompose.data.models.Priority
import com.soethan.todocompose.data.models.ToDoTask
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

@ExperimentalCoroutinesApi
class ToDoRepositoryTest {


    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var toDoDao: ToDoDao

    private lateinit var sut: ToDoRepository

    @Before
    fun setup() {
        sut = ToDoRepository(toDoDao)
    }


    @Test
    fun `deleteTask deletes the task from the DAO`() = runTest {

        coEvery { toDoDao.deleteTask(any()) } returns Unit
        // Arrange
        val taskToDelete = ToDoTask(
            id = 1,
            title = "Task to delete",
            description = "Description",
            priority = Priority.MEDIUM
        )

        // Act
        sut.deleteTask(taskToDelete)

        // Assert

        coVerify(exactly = 1) {
            sut
                .deleteTask(taskToDelete)

        }
    }


    @Test
    fun `addTask inserts a task into the DAO`() = runTest {
        val capturedParameter = slot<ToDoTask>()
        // Arrange: Create a ToDoTask to add
        val taskToAdd =
            ToDoTask(id = 1, title = "Test Task", description = "Test Description", Priority.LOW)

        // Mock the behavior of the toDoDao's addTask function
        coEvery { toDoDao.addTask(capture(capturedParameter)) } returns Unit

        // Act: Call the addTask function in the repository
        sut.addTask(taskToAdd)

        // Assert: Verify that the toDoDao's addTask function was called with the expected argument
        coEvery { toDoDao.addTask(taskToAdd) }

        capturedParameter.captured.let {
            assertEquals(it.title, "Test Task")
            assertEquals(it.priority, Priority.LOW)
            assertEquals(it.description, "Test Description")
        }
    }


    @Test
    fun `updateTask calls the updateTask function in the DAO with the correct task`() = runTest {

        coEvery { toDoDao.updateTask(any()) } returns Unit

        // Arrange: Create a ToDoTask to update
        val taskToUpdate = ToDoTask(
            id = 1,
            title = "Updated Task",
            description = "Updated Description",
            priority = Priority.MEDIUM
        )

        // Act: Call the updateTask function in the repository
        sut.updateTask(taskToUpdate)

        // Assert: Verify that the updateTask function in the DAO was called with the expected argument
        coVerify { toDoDao.updateTask(taskToUpdate) }
    }


    @Test
    fun `getSelectedTask returns a Flow with the correct task`() = runTest {
        val taskId = 1
        val task = ToDoTask(
            id = 1,
            title = "Selected Task",
            description = "Task Description",
            priority = Priority.MEDIUM
        )

        val taskFlow = flowOf(task)
        coEvery { toDoDao.getSelectedTask(taskId) } returns taskFlow

        val resultFlow = sut.getSelectedTask(1).first()
        assert(resultFlow.title == task.title)

    }


    @Test
    fun `getAllTasks returns a Flow with a list of tasks`() = runTest {
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
                priority = Priority.MEDIUM
            )
        )
        val flow = flowOf(tasks)

        coEvery { toDoDao.getAllTasks() } returns flow

        // Act: Access the getAllTasks property
        val result = sut.getAllTasks.first()
        assert(result.size == tasks.size)
        assert(result[0].title == tasks[0].title)

    }


    @Test
    fun `sortByLowPriority returns a Flow with a list of tasks`() = runTest {
        // Arrange: Create a list of tasks and a Flow of that list
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
        val flow = flowOf(tasks.sortedByDescending {
            it.priority.ordinal
        })


        // Mock the behavior of the toDoDao's sortByLowPriority function
        coEvery { toDoDao.sortByLowPriority() } returns flow

        val resultFlow = sut
            .sortByLowPriority

        val result = resultFlow.first()
        assertEquals(result[0].id, tasks[1].id)
        assertEquals(result[1].id, tasks[0].id)
        assertEquals(result[2].id, tasks[2].id)
    }


    @Test
    fun `sortByHighPriority returns a Flow with a list of tasks`() = runTest {
        val tasks = listOf(
            ToDoTask(
                id = 1,
                title = "Task 1",
                description = "Task 1 Description",
                priority = Priority.LOW
            ),
            ToDoTask(
                id = 2,
                title = "Task 2",
                description = "Task 1 Description",
                priority = Priority.HIGH
            ),
            ToDoTask(
                id = 3,
                title = "Task 3",
                description = "Task 3 Description",
                priority = Priority.MEDIUM
            )
        )
        val flow = flowOf(tasks.sortedBy {
            it.priority.ordinal
        })


        // Mock the behavior of the toDoDao's sortByLowPriority function
        coEvery { toDoDao.sortByHighPriority() } returns flow

        val resultFlow = sut
            .sortByHighPriority
        val result = resultFlow.first()
        assertEquals(result[0].id, tasks[1].id)
        assertEquals(result[1].id, tasks[2].id)
        assertEquals(result[2].id, tasks[0].id)
    }

}