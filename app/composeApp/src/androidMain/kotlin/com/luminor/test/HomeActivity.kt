package com.luminor.test

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        val settings = SettingsFactory().createSettings()
        val email = intent.getStringExtra("USER_EMAIL") ?: settings.getString("USER_EMAIL") ?: "User"
        
        setContent {
            MaterialTheme {
                HomeScreen(
                    email = email,
                    onLogout = {
                        settings.remove("USER_EMAIL")
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}
