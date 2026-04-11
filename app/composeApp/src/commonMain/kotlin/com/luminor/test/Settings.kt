package com.luminor.test

interface Settings {
    fun putString(key: String, value: String)
    fun getString(key: String): String?
    fun remove(key: String)
}

expect class SettingsFactory {
    fun createSettings(): Settings
}
