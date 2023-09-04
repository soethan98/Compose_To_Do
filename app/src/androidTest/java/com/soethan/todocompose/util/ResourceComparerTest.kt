package com.soethan.todocompose.util

import android.content.Context
import com.soethan.todocompose.R
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ResourceComparerTest {

    lateinit var sut: ResourceComparer

    @Before
    fun setUp() {
        sut = ResourceComparer()
    }

    @Test
    fun stringResourceSameAsGivenString_returnsTrue() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = sut.isEqual(
            context = context,
            stringResource = R.string.app_name,
            label = "ToDoCompose"
        )
        assertTrue(result)
    }

    @Test
    fun stringResourceDifferentAsGivenString_returnsFalse() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = sut.isEqual(context, R.string.app_name, "Hello")
        assertFalse(result)
    }


}