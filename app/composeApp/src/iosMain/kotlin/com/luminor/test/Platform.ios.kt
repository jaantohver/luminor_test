package com.luminor.test

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun showToast(message: String) {
    // In a real iOS app, you'd show a UIAlertController or a custom Toast.
    // For now, we print to console or you can implement a simple alert.
    println("Toast: $message")
}