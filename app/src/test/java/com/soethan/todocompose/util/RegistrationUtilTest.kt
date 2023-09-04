package com.soethan.todocompose.util

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class RegistrationUtilTest {
    @Test
    fun `empty username return false`() {
        val result = RegistrationUtil.validRegistrationInput("", "124", "124")
        assertEquals(result, false)
    }


    @Test
    fun `valid username and correctly repeated password returns true`() {
        val result = RegistrationUtil.validRegistrationInput(
            "Philipp",
            "123",
            "123"
        )
        assertTrue(result)
    }

    @Test
    fun `username already exists returns false`() {
        val result = RegistrationUtil.validRegistrationInput(
            "Carl",
            "123",
            "123"
        )
        assertFalse(result)
    }

    @Test
    fun `incorrectly confirmed password returns false`() {
        val result = RegistrationUtil.validRegistrationInput(
            "Philipp",
            "123456",
            "abcdefg"
        )
        assertFalse(result)
    }

    @Test
    fun `empty password returns false`() {
        val result = RegistrationUtil.validRegistrationInput(
            "Philipp",
            "",
            ""
        )
        assertFalse(result)
    }

    @Test
    fun `less than 2 digit password returns false`() {
        val result = RegistrationUtil.validRegistrationInput(
            "Philipp",
            "abcdefg5",
            "abcdefg5"
        )
        assertFalse(result)
    }

}