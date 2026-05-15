package com.example.sicedroidmultiplatform

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.sicedroidmultiplatform.data.repository.SicenetRepository
import com.example.sicedroidmultiplatform.ui.login.LoginScreen
import com.example.sicedroidmultiplatform.ui.login.LoginViewModel
import com.example.sicedroidmultiplatform.ui.profile.ProfileScreen
import com.example.sicedroidmultiplatform.ui.profile.ProfileViewModel

@Composable
fun App() {
    // Detectar si el sistema está en modo oscuro
    val darkTheme = isSystemInDarkTheme()
    
    // Definir esquemas de colores
    val colorScheme = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    // Estado de navegación simple
    var currentScreen by remember { mutableStateOf("login") }
    
    // Repositorio compartido
    val repository = remember { SicenetRepository() }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentScreen) {
                "login" -> {
                    val loginViewModel = remember { LoginViewModel(repository) }
                    LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = {
                            currentScreen = "profile"
                        }
                    )
                }
                "profile" -> {
                    val profileViewModel = remember { ProfileViewModel(repository) }
                    ProfileScreen(
                        viewModel = profileViewModel,
                        onLogout = {
                            currentScreen = "login"
                        }
                    )
                }
            }
        }
    }
}
