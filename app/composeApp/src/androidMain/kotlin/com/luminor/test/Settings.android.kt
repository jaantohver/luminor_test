package com.luminor.test

import android.content.Context
import android.content.SharedPreferences

class AndroidSettings(private val sharedPreferences: SharedPreferences) : Settings {
    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}

actual class SettingsFactory {
    companion object {
        lateinit var appContext: Context
    }

    actual fun createSettings(): Settings {
        val sharedPreferences = appContext.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return AndroidSettings(sharedPreferences)
    }
}
