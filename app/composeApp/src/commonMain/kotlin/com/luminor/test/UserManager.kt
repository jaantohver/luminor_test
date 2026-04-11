package com.luminor.test

class UserManager(private val settings: Settings) {
    fun registerUser(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) return false
        if (isUserRegistered(email)) return false
        settings.putString("USER_$email", password)
        return true
    }

    fun loginUser(email: String, password: String): Boolean {
        if (!isUserRegistered(email)) return false
        val savedPassword = settings.getString("USER_$email")
        return savedPassword == password
    }

    fun isUserRegistered(email: String): Boolean {
        return settings.getString("USER_$email") != null
    }
}
