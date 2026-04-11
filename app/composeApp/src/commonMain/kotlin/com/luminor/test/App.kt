package com.luminor.test

import Networking
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

import test.composeapp.generated.resources.Res
import test.composeapp.generated.resources.android_mascot

enum class Screen {
    LOGIN, REGISTER, HOME
}

//Very basic email validation, not going fancy with it, but maybe TODO
fun isValidEmail(email: String): Boolean {
    return email.contains("@")
}

@Composable
@Preview
fun App(
    settings: Settings? = null,
    onLoginSuccess: ((String) -> Unit)? = null
) {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf(Screen.LOGIN) }
        var userEmail by remember { mutableStateOf("") }

        val userManager = remember {
            settings?.let { Networking(it) }
        }

        when (currentScreen) {
            Screen.LOGIN -> {
                LoginScreen(
                    onLogin = { email, password ->
                        if (!isValidEmail(email)) {
                            showToast("Is that even an email? Needs an '@' buddy! 😉")
                            "Invalid email format"
                        } else if (userManager == null || userManager.loginUser(email, password)) {
                            userEmail = email
                            if (onLoginSuccess != null) {
                                onLoginSuccess(email)
                            } else {
                                currentScreen = Screen.HOME
                            }
                            null
                        } else {
                            "Invalid email or password"
                        }
                    },
                    onNavigateToRegister = {
                        currentScreen = Screen.REGISTER
                    }
                )
            }
            Screen.REGISTER -> {
                RegisterScreen(
                    onRegister = { email, password ->
                        if (!isValidEmail(email)) {
                            showToast("Trying to register without an '@'? Nice try! 😂")
                            "Invalid email format"
                        } else if (email.isEmpty() || password.isEmpty()) {
                            "Fields cannot be empty"
                        } else if (userManager != null && !userManager.registerUser(email, password)) {
                            "User already exists"
                        } else {
                            userEmail = email
                            if (onLoginSuccess != null) {
                                onLoginSuccess(email)
                            } else {
                                currentScreen = Screen.HOME
                            }
                            null
                        }
                    },
                    onNavigateToLogin = {
                        currentScreen = Screen.LOGIN
                    }
                )
            }
            Screen.HOME -> {
                HomeScreen(
                    email = userEmail,
                    onLogout = { currentScreen = Screen.LOGIN }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLogin: (String, String) -> String?,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeContentPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Logo
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.android_mascot),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Authentication",
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it 
                errorMessage = null
            },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            isError = errorMessage != null,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it 
                errorMessage = null
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp),
            isError = errorMessage != null,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            )
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp).align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { 
                errorMessage = onLogin(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Log in", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )
            Text(
                text = "or",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Gray,
                fontSize = 14.sp
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToRegister,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF91D1E0), // Light blue from image
                contentColor = Color.White
            )
        ) {
            Text("Register", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RegisterScreen(
    onRegister: (String, String) -> String?,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeContentPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Logo
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.android_mascot),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Registration",
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it 
                errorMessage = null
            },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            isError = errorMessage != null,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it 
                errorMessage = null
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp),
            isError = errorMessage != null,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            )
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp).align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { 
                errorMessage = onRegister(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Register", fontWeight = FontWeight.Bold)
        }
    }
}
