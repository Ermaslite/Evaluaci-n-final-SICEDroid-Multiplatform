package com.example.sicedroidmultiplatform.ui.calificaciones

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicedroidmultiplatform.data.model.Calificacion
import com.example.sicedroidmultiplatform.data.repository.SicenetRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

sealed interface CalificacionesUiState {
    data class Success(val calificaciones: List<Calificacion>) : CalificacionesUiState
    object Loading : CalificacionesUiState
    data class Error(val message: String) : CalificacionesUiState
}

class CalificacionesViewModel(private val repository: SicenetRepository) : ViewModel() {
    var uiState: CalificacionesUiState by mutableStateOf(CalificacionesUiState.Loading)
        private set

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun loadCalificaciones() {
        viewModelScope.launch {
            uiState = CalificacionesUiState.Loading
            try {
                val result = repository.getCalificaciones()
                if (result != null && result.length > 10) {
                    val jsonArray = json.parseToJsonElement(result).jsonArray
                    val lista = jsonArray.map { element ->
                        val obj = element.jsonObject
                        val materiaNombre = obj["Materia"]?.jsonPrimitive?.content ?: "Materia"
                        
                        // Extraemos y ordenamos las unidades numéricamente (1, 2, 3...)
                        val notas = obj.filter { it.key.startsWith("C") && it.key.length > 1 }
                            .mapNotNull { (key, value) ->
                                val nota = value.jsonPrimitive.content.trim()
                                if (nota.isNotBlank() && nota != "null") {
                                    key to nota
                                } else null
                            }
                            .sortedBy { (key, _) -> 
                                // Extrae el número después de la 'C' para ordenar correctamente
                                key.substring(1).toIntOrNull() ?: 0 
                            }
                            .map { (key, nota) -> 
                                // Reemplaza C por U para la vista (U1, U2...)
                                key.replace("C", "U") to nota
                            }
                        
                        Calificacion(materiaNombre, notas)
                    }
                    uiState = CalificacionesUiState.Success(lista)
                } else {
                    uiState = CalificacionesUiState.Error("No hay calificaciones disponibles")
                }
            } catch (e: Exception) {
                uiState = CalificacionesUiState.Error("Error al procesar datos")
            }
        }
    }
}
