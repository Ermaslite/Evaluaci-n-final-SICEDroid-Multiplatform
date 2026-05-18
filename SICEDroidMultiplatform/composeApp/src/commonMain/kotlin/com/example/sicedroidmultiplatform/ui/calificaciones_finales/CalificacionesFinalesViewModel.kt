package com.example.sicedroidmultiplatform.ui.calificaciones_finales

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicedroidmultiplatform.data.model.CalificacionFinal
import com.example.sicedroidmultiplatform.data.repository.SicenetRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

sealed interface CalificacionesFinalesUiState {
    data class Success(val calificaciones: List<CalificacionFinal>) : CalificacionesFinalesUiState
    object Loading : CalificacionesFinalesUiState
    data class Error(val message: String) : CalificacionesFinalesUiState
}

class CalificacionesFinalesViewModel(private val repository: SicenetRepository) : ViewModel() {
    var uiState: CalificacionesFinalesUiState by mutableStateOf(CalificacionesFinalesUiState.Loading)
        private set

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun loadCalificacionesFinales() {
        viewModelScope.launch {
            uiState = CalificacionesFinalesUiState.Loading
            try {
                val result = repository.getCalificacionesFinales()
                if (result != null && result.contains("[")) {
                    val start = result.indexOf("[")
                    val end = result.lastIndexOf("]") + 1
                    val cleanJson = result.substring(start, end)
                    
                    val jsonArray = json.parseToJsonElement(cleanJson).jsonArray
                    val lista = jsonArray.map { element ->
                        val obj = element.jsonObject
                        CalificacionFinal(
                            materia = obj.entries.find { it.key.equals("materia", true) }?.value?.jsonPrimitive?.content ?: "Materia",
                            calificacion = obj.entries.find { it.key.equals("calif", true) }?.value?.jsonPrimitive?.content ?: "0"
                        )
                    }
                    uiState = CalificacionesFinalesUiState.Success(lista)
                } else {
                    uiState = CalificacionesFinalesUiState.Error("No se encontraron calificaciones finales")
                }
            } catch (e: Exception) {
                uiState = CalificacionesFinalesUiState.Error("Error al procesar los datos")
            }
        }
    }
}
