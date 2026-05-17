package com.example.sicedroidmultiplatform.ui.calificaciones

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
import com.example.sicedroidmultiplatform.data.model.Calificacion

@Composable
fun CalificacionesScreen(viewModel: CalificacionesViewModel) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadCalificaciones()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (uiState) {
            is CalificacionesUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is CalificacionesUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(24.dp)
                )
            }
            is CalificacionesUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item { Spacer(Modifier.height(8.dp)) }
                    
                    items(uiState.calificaciones) { calificacion ->
                        CalificacionAcademicaRow(calificacion)
                    }
                    
                    item { Spacer(Modifier.height(40.dp)) }
                }
            }
        }
    }
}

@Composable
fun CalificacionAcademicaRow(calif: Calificacion) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = calif.materia,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(Modifier.height(8.dp))
        
        // Muestra una lista horizontal con cada unidad y su respectiva calificación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            calif.notas.forEach { (unidad, nota) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = unidad,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = nota,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        
        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
