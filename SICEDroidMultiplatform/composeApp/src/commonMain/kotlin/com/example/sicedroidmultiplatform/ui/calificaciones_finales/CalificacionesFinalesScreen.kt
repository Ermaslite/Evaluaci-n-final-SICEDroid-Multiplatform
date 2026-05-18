package com.example.sicedroidmultiplatform.ui.calificaciones_finales

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
import com.example.sicedroidmultiplatform.data.model.CalificacionFinal

@Composable
fun CalificacionesFinalesScreen(viewModel: CalificacionesFinalesViewModel) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadCalificacionesFinales()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (uiState) {
            is CalificacionesFinalesUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is CalificacionesFinalesUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(24.dp)
                )
            }
            is CalificacionesFinalesUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    item { 
                        Spacer(Modifier.height(16.dp))
                        FinalsHeaderRow()
                        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
                    }
                    
                    items(uiState.calificaciones) { calificacion ->
                        FinalGradeRow(calificacion)
                        HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    }
                    
                    item { Spacer(Modifier.height(40.dp)) }
                }
            }
        }
    }
}

@Composable
fun FinalsHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "MATERIA",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "CALIF",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FinalGradeRow(item: CalificacionFinal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.materia,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = item.calificacion,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = if ((item.calificacion.toDoubleOrNull() ?: 0.0) >= 70.0) 
                        MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.error
        )
    }
}
