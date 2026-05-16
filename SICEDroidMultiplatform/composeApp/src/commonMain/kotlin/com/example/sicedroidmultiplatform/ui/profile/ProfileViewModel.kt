package com.example.sicedroidmultiplatform.ui.profile

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicedroidmultiplatform.data.repository.SicenetRepository
import kotlinx.coroutines.launch

sealed interface ProfileUiState {
    data class Success(val data: Map<String, String>) : ProfileUiState
    object Loading : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

class ProfileViewModel(private val repository: SicenetRepository) : ViewModel() {

    var uiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
        private set

    fun loadProfile() {
        viewModelScope.launch {
            uiState = ProfileUiState.Loading
            try {
                val result = repository.getProfile()
                if (result != null && result.length > 50) {
                    val dataMap = mutableMapOf<String, String>()
                    val keys = listOf(
                        "nombre", "matricula", "carrera", "especialidad",
                        "semActual", "cdtosAcumulados", "cdtosActuales",
                        "estatus", "fechaReins"
                    )

                    keys.forEach { key ->
                        val pattern = "\"$key\":\"?([^\",}]+)\"?".toRegex()
                        val match = pattern.find(result)
                        dataMap[key] = match?.groupValues?.get(1) ?: "No disponible"
                    }

                    uiState = ProfileUiState.Success(dataMap)
                } else {
                    uiState = ProfileUiState.Error("Error: Respuesta del servidor inválida o sesión expirada")
                }
            } catch (e: Exception) {
                uiState = ProfileUiState.Error("Error de red: ${e.message}")
            }
        }
    }
}
