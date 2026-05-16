package com.example.sicedroidmultiplatform.ui.carga

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicedroidmultiplatform.data.model.Materia
import com.example.sicedroidmultiplatform.data.repository.SicenetRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

sealed interface CargaUiState {
    data class Success(val materias: List<Materia>) : CargaUiState
    object Loading : CargaUiState
    data class Error(val message: String) : CargaUiState
}

class CargaViewModel(private val repository: SicenetRepository) : ViewModel() {

    var uiState: CargaUiState by mutableStateOf(CargaUiState.Loading)
        private set

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun loadCarga() {
        viewModelScope.launch {
            uiState = CargaUiState.Loading
            try {
                val result = repository.getCargaAcademica()
                if (result != null && result.length > 20) {
                    val listaMaterias = json.decodeFromString<List<Materia>>(result)
                    uiState = CargaUiState.Success(listaMaterias)
                } else {
                    uiState = CargaUiState.Error("No se encontraron datos de carga académica")
                }
            } catch (e: Exception) {
                uiState = CargaUiState.Error("Error al procesar horario: ${e.message}")
            }
        }
    }
}
