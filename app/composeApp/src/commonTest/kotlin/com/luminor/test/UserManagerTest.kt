package com.luminor.test

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserManagerTest {
    private val settings = FakeSettings()
    private val userManager = UserManager(settings)

    @Test
    fun testRegisterUser() {
        val result = userManager.registerUser("test@email.com", "password123")
        assertTrue(result)
        assertEquals("password123", settings.getString("USER_test@email.com"))
    }

    @Test
    fun testRegisterDuplicateUser() {
        userManager.registerUser("test@email.com", "password123")
        val result = userManager.registerUser("test@email.com", "different")
        assertFalse(result)
    }

    @Test
    fun testLoginSuccess() {
        userManager.registerUser("test@email.com", "password123")
        val result = userManager.loginUser("test@email.com", "password123")
        assertTrue(result)
    }

    @Test
    fun testLoginWrongPassword() {
        userManager.registerUser("test@email.com", "password123")
        val result = userManager.loginUser("test@email.com", "wrong")
        assertFalse(result)
    }

    @Test
    fun testLoginNonExistentUser() {
        val result = userManager.loginUser("nonexistent@email.com", "any")
        assertFalse(result)
    }

    @Test
    fun testRegisterBlankFields() {
        assertFalse(userManager.registerUser("", "password"))
        assertFalse(userManager.registerUser("email@test.com", ""))
    }
}
