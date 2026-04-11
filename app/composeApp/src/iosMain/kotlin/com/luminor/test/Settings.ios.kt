package com.luminor.test

import platform.Foundation.NSUserDefaults

class IosSettings(private val userDefaults: NSUserDefaults) : Settings {
    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, key)
    }

    override fun getString(key: String): String? {
        return userDefaults.stringForKey(key)
    }

    override fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
    }
}

actual class SettingsFactory {
    actual fun createSettings(): Settings {
        return IosSettings(NSUserDefaults.standardUserDefaults)
    }
}
