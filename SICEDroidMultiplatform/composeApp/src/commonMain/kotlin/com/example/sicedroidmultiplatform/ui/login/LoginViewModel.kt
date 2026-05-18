package com.example.sicedroidmultiplatform.ui.login

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicedroidmultiplatform.data.repository.SicenetRepository
import com.russhwolf.settings.Settings
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: SicenetRepository) : ViewModel() {
    private val settings = Settings()

    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    // Campos vinculados a la UI
    var matricula by mutableStateOf(settings.getString("saved_matricula", ""))
    var contrasenia by mutableStateOf(settings.getString("saved_contrasenia", ""))
    var rememberMe by mutableStateOf(settings.getBoolean("remember_me", false))

    fun performLogin(onLoginSuccess: () -> Unit) {
        if (matricula.isBlank() || contrasenia.isBlank()) {
            error = "Por favor completa todos los campos"
            return
        }

        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val result = repository.login(matricula, contrasenia, "Alumno")
                println("RESPUESTA_SICENET: $result")

                if (result != null) {
                    // Verificamos si la respuesta indica éxito
                    val isSuccess = result.contains("\"acceso\":true", ignoreCase = true) || 
                                   result.contains("\"acceso\":\"true\"", ignoreCase = true) ||
                                   (result.length > 100 && !result.contains("error", ignoreCase = true))

                    if (isSuccess) {
                        // Guardar o limpiar credenciales según el checkbox
                        if (rememberMe) {
                            settings.putBoolean("remember_me", true)
                            settings.putString("saved_matricula", matricula)
                            settings.putString("saved_contrasenia", contrasenia)
                        } else {
                            settings.putBoolean("remember_me", false)
                            settings.remove("saved_matricula")
                            settings.remove("saved_contrasenia")
                        }
                        onLoginSuccess()
                    } else {
                        error = "Matrícula o contraseña incorrecta"
                    }
                } else {
                    error = "Sin respuesta del servidor. Revisa tu conexión."
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
                println("DEBUG_LOGIN_ERROR: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}
