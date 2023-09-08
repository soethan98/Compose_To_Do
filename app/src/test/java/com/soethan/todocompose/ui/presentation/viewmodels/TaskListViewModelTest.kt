package com.soethan.todocompose.ui.presentation.viewmodels

import androidx.compose.runtime.State
import app.cash.turbine.test
import com.soethan.todocompose.MainCoroutinesRule
import com.soethan.todocompose.data.ToDoDao
import com.soethan.todocompose.data.models.Priority
import com.soethan.todocompose.data.models.ToDoTask
import com.soethan.todocompose.data.repositories.DataStoreRepository
import com.soethan.todocompose.data.repositories.ToDoRepository
import com.soethan.todocompose.helpers.tasks
import com.soethan.todocompose.util.Resource
import com.soethan.todocompose.util.SearchAppBarState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskListViewModelTest {
    private var todoRepository: ToDoRepository = mockk()
    private var dataStoreRepository: DataStoreRepository = mockk()

    @get:Rule
    var mainCoroutineRule = MainCoroutinesRule()

    private lateinit var sut: TaskListViewModel

    @Before
    fun setUp() {
        sut = TaskListViewModel(todoRepository, dataStoreRepository)
    }


    @Test
    fun `persistSortState calls DataStoreRepository with the correct priority`() {
        val capturePriority = slot<Priority>()
        coEvery { dataStoreRepository.persistSortState(capture(capturePriority)) } returns Unit
        // Arrange: Define the priority to be tested
        val priority = Priority.HIGH

        // Act: Call the persistSortState method in the ViewModel
        sut.persistSortState(priority)

        coVerify(exactly = 1) { dataStoreRepository.persistSortState(priority) }

        capturePriority.captured.let {
            assert(it == Priority.HIGH)
        }
    }

    @Test
    fun `deleteAllTasks calls Repository's deleteAllTasks`() {
        coEvery { todoRepository.deleteAllTasks() } returns Unit
        sut.deleteAllTasks()

        coVerify(exactly = 1) { todoRepository.deleteAllTasks() }

    }


    @Test
    fun `deleteTask calls Repository's deleteTask with the correct argument`() {
        val captureTask = slot<ToDoTask>()
        // Arrange: Create a ToDoTask to delete
        val toDoTask = ToDoTask(
            id = 1,
            title = "Task to Delete",
            description = "Task Desc",
            priority = Priority.HIGH
        )

        // Mock the behavior of the Repository's deleteTask function
        coEvery { todoRepository.deleteTask(capture(captureTask)) } returns Unit

        // Act: Call the deleteTask method in the ViewModel
        sut.deleteTask(toDoTask)

        // Assert: Verify that the Repository's deleteTask function was called with the expected argument
        coVerify(exactly = 1) { todoRepository.deleteTask(toDoTask) }
        assert(captureTask.captured == toDoTask)
    }

    @Test
    fun `updateSearchTextState updates searchTextState correctly`() {
        // Arrange: Define the new state to set
        val newState = "New Search State"
        // Act: Call the updateSearchTextState method
        assert(sut.searchTextState.value.isEmpty())

        sut.updateSearchTextState(newState)
        assert(sut.searchTextState.value == newState)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test

    fun `getAllTask_post_success`() = runTest {
        every { todoRepository.getAllTasks } returns flow {
            emit(tasks)
        }
        sut.getAllTasks()
        sut.allTask.test {
            assertEquals(Resource.Loading, awaitItem())
            advanceTimeBy(1000L)
            assertEquals(Resource.Success(tasks), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllTask_post_error`() = runTest {
        val exception = Exception("Error")
        every { todoRepository.getAllTasks } throws exception

        sut.getAllTasks()
        sut.allTask.test {
            assertEquals(Resource.Loading, awaitItem())
            advanceTimeBy(1000L)
            assertEquals(Resource.Error(exception), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `readSortState_result post success`() {
        runTest {
            every { dataStoreRepository.readSortState() } returns flow { emit(Priority.HIGH.name) }
            assertEquals(sut.sortState.value, Resource.Idle)
            sut.readSortState()
            sut.sortState.test {
                assertEquals(Resource.Success(Priority.HIGH), awaitItem())
            }
        }
    }


    @Test
    fun `readSortState result post error`() {
        runTest {
            val exception = Exception("Error")
            every { dataStoreRepository.readSortState() } throws exception
            assertEquals(sut.sortState.value, Resource.Idle)
            sut.readSortState()
            sut.sortState.test {
                assertEquals(Resource.Error(exception), awaitItem())
            }

        }
    }


    @Test
    fun `correct query parameter pass to db`() {
        runTest {
            val stringSlot = slot<String>()
            coEvery { todoRepository.searchDatabase(capture(stringSlot)) } returns flow {
                emit(tasks)
            }
            sut.searchDatabase("task")
            assert(stringSlot.captured == "%task%")
        }
    }

    @Test
    fun `searchDatabase return correct task list`() {
        runTest {
            coEvery { todoRepository.searchDatabase(any()) } returns flow {
                emit(tasks)
            }
            assertEquals(sut.searchedTasks.value, Resource.Idle)

            sut.searchDatabase("task")
            sut.searchedTasks.test {
                assertEquals(Resource.Success(tasks), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            assertEquals(sut.searchAppBarState.value, SearchAppBarState.TRIGGERED)

        }
    }

    @Test
    fun `searchDatabase failed post result`() {
        runTest {
            val exception = Exception("Exception")
            coEvery { todoRepository.searchDatabase(any()) } throws exception
            assertEquals(sut.searchedTasks.value, Resource.Idle)

            sut.searchDatabase("task")
            sut.searchedTasks.test {
                assertEquals(Resource.Error(exception), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            assertEquals(sut.searchAppBarState.value, SearchAppBarState.TRIGGERED)

        }
    }

}