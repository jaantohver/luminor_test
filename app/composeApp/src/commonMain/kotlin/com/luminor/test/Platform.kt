package com.luminor.test

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun showToast(message: String)
