package com.example.sicedroidmultiplatform.ui.carga

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sicedroidmultiplatform.data.model.Materia

@Composable
fun CargaScreen(viewModel: CargaViewModel) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadCarga()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        when (uiState) {
            is CargaUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is CargaUiState.Error -> {
                Text(
                    text = uiState.message, 
                    color = MaterialTheme.colorScheme.error, 
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is CargaUiState.Success -> {
                val dias = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado")
                
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { Spacer(Modifier.height(16.dp)) }
                    
                    dias.forEach { dia ->
                        val materiasDelDia = filtrarMateriasPorDia(uiState.materias, dia)
                        
                        if (dia != "Sabado" || materiasDelDia.isNotEmpty()) {
                            item { DayHeader(dia) }
                            
                            if (materiasDelDia.isEmpty()) {
                                item { 
                                    Text(
                                        "Sin actividad escolar",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                                    )
                                }
                            } else {
                                items(materiasDelDia) { materia ->
                                    MateriaAgendaItem(materia, dia)
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(40.dp)) }
                }
            }
        }
    }
}

@Composable
fun DayHeader(dia: String) {
    val nombreDia = when(dia) {
        "Miercoles" -> "Miércoles"
        "Sabado" -> "Sábado"
        else -> dia
    }
    Text(
        text = nombreDia.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
    )
}

@Composable
fun MateriaAgendaItem(materia: Materia, dia: String) {
    val infoDia = when (dia) {
        "Lunes" -> materia.lunes
        "Martes" -> materia.martes
        "Miercoles" -> materia.miercoles
        "Jueves" -> materia.jueves
        "Viernes" -> materia.viernes
        else -> materia.sabado
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(55.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.small)
        )
        
        Spacer(Modifier.width(16.dp))
        
        Column {
            Text(
                text = materia.nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = infoDia,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = "Prof. ${materia.docente}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

fun filtrarMateriasPorDia(materias: List<Materia>, dia: String): List<Materia> {
    return materias.filter {
        val valor = when (dia) {
            "Lunes" -> it.lunes
            "Martes" -> it.martes
            "Miercoles" -> it.miercoles
            "Jueves" -> it.jueves
            "Viernes" -> it.viernes
            else -> it.sabado
        }
        valor.isNotBlank() && valor != "null" && valor != " "
    }.sortedBy { 
        val h = when (dia) {
            "Lunes" -> it.lunes
            "Martes" -> it.martes
            "Miercoles" -> it.miercoles
            "Jueves" -> it.jueves
            "Viernes" -> it.viernes
            else -> it.sabado
        }
        h
    }
}
