package com.luminor.test

import Networking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FakeSettings : Settings {
    private val data = mutableMapOf<String, String>()
    override fun putString(key: String, value: String) { data[key] = value }
    override fun getString(key: String): String? = data[key]
    override fun remove(key: String) { data.remove(key) }
}

class NetworkingTest {
    private val settings = FakeSettings()
    private val networking = Networking(settings)

    @Test
    fun testRegisterUser() {
        val result = networking.registerUser("a@a", "a")
        assertTrue(result)
        assertEquals("a", settings.getString("USER_a@a"))
    }

    @Test
    fun testRegisterDuplicateUser() {
        networking.registerUser("a@a", "a")
        val result = networking.registerUser("USER_a@a", "b")
        assertFalse(result)
        assertEquals("a", settings.getString("USER_a@a"))
    }

    @Test
    fun testLoginSuccess() {
        networking.registerUser("a@a", "a")
        val result = networking.loginUser("a@a", "a")
        assertTrue(result)
    }

    @Test
    fun testLoginWrongPassword() {
        networking.registerUser("a@a", "a")
        val result = networking.loginUser("a@a", "b")
        assertFalse(result)
    }

    @Test
    fun testLoginNonExistentUser() {
        val result = networking.loginUser("b@b", "b")
        assertFalse(result)
    }
}
