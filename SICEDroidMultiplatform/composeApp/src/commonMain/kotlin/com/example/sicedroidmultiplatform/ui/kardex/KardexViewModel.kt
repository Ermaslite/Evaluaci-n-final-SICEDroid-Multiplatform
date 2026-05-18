package com.example.sicedroidmultiplatform.ui.kardex

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sicedroidmultiplatform.data.model.KardexItem
import com.example.sicedroidmultiplatform.data.repository.SicenetRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

sealed interface KardexUiState {
    data class Success(
        val groupedKardex: List<Pair<String, List<KardexItem>>>,
        val promedio: String,
        val creditosAprobados: Int,
        val materiasCursadas: Int,
        val materiasAprobadas: Int
    ) : KardexUiState
    object Loading : KardexUiState
    data class Error(val message: String) : KardexUiState
}

class KardexViewModel(private val repository: SicenetRepository) : ViewModel() {
    var uiState: KardexUiState by mutableStateOf(KardexUiState.Loading)
        private set

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun loadKardex() {
        viewModelScope.launch {
            uiState = KardexUiState.Loading
            try {
                val result = repository.getKardex()
                if (result != null && result.contains("[")) {
                    val start = result.indexOf("[")
                    val end = result.lastIndexOf("]") + 1
                    val cleanJson = result.substring(start, end)
                    
                    val jsonArray = json.parseToJsonElement(cleanJson).jsonArray
                    val items = jsonArray.mapNotNull { element ->
                        val obj = element.jsonObject
                        
                        val materia = obj.entries.find { it.key.equals("materia", true) }?.value?.jsonPrimitive?.content
                            ?: return@mapNotNull null
                            
                        KardexItem(
                            materia = materia,
                            calificacion = obj.entries.find { it.key.equals("calif", true) || it.key.equals("calificacion", true) }?.value?.jsonPrimitive?.content ?: "0",
                            creditos = obj.entries.find { it.key.equals("cdts", true) || it.key.equals("cd", true) || it.key.equals("creditos", true) }?.value?.jsonPrimitive?.content ?: "0",
                            nivel = obj.entries.find { it.key.equals("observaciones", true) || it.key.equals("nivel", true) }?.value?.jsonPrimitive?.content ?: "",
                            acreditacion = obj.entries.find { it.key.equals("acreditacion", true) }?.value?.jsonPrimitive?.content ?: "",
                            periodo = obj.entries.find { it.key.equals("periodo", true) }?.value?.jsonPrimitive?.content ?: "Periodo Desconocido"
                        )
                    }
                    
                    // Cálculos
                    val aprobadas = items.filter { (it.calificacion.toIntOrNull() ?: 0) >= 70 }
                    val creditosAprobados = aprobadas.sumOf { it.creditos.toIntOrNull() ?: 0 }
                    
                    val numericGrades = items.mapNotNull { it.calificacion.toDoubleOrNull() }
                    val promedio = if (numericGrades.isNotEmpty()) (numericGrades.sum() / numericGrades.size).toString().take(5) else "0.0"
                    
                    val grouped = items.groupBy { it.periodo }
                        .toList()
                        .sortedByDescending { it.first }
                    
                    uiState = KardexUiState.Success(
                        groupedKardex = grouped,
                        promedio = promedio,
                        creditosAprobados = creditosAprobados,
                        materiasCursadas = items.size,
                        materiasAprobadas = aprobadas.size
                    )
                } else {
                    uiState = KardexUiState.Error("No se recibieron datos")
                }
            } catch (e: Exception) {
                uiState = KardexUiState.Error("Error al procesar: ${e.message}")
            }
        }
    }
}
