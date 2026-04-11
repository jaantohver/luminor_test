package com.luminor.test

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        SettingsFactory.appContext = applicationContext
        val settings = SettingsFactory().createSettings()
        
        val savedEmail = settings.getString("USER_EMAIL")
        if (savedEmail != null) {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_EMAIL", savedEmail)
            }
            startActivity(intent)
            finish()
            return
        }

        setContent {
            App(
                settings = settings,
                onLoginSuccess = { email ->
                    settings.putString("USER_EMAIL", email)
                    val intent = Intent(this, HomeActivity::class.java).apply {
                        putExtra("USER_EMAIL", email)
                    }
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}
