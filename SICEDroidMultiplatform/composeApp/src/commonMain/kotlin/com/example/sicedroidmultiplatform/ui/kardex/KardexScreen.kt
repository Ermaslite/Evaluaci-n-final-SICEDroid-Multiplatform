package com.example.sicedroidmultiplatform.ui.kardex

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sicedroidmultiplatform.data.model.KardexItem

@Composable
fun KardexScreen(viewModel: KardexViewModel) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadKardex()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (uiState) {
            is KardexUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is KardexUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(24.dp)
                )
            }
            is KardexUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    item { Spacer(Modifier.height(8.dp)) }
                    
                    uiState.groupedKardex.forEach { (periodo, materias) ->
                        item {
                            PeriodoHeader(periodo)
                            KardexTableHeader()
                        }
                        items(materias) { materia ->
                            KardexRow(materia)
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }

                    item {
                        KardexSummaryFooter(uiState)
                    }
                    
                    item { Spacer(Modifier.height(40.dp)) }
                }
            }
        }
    }
}

@Composable
fun KardexSummaryFooter(state: KardexUiState.Success) {
    val totalCreditos = 260
    val porcentaje = (state.creditosAprobados.toDouble() / totalCreditos * 100).toString().take(5)

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 16.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Resumen Académico",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(12.dp))
        
        SummaryRow("Promedio general:", state.promedio)
        SummaryRow("Créditos aprobados:", "${state.creditosAprobados} ($porcentaje %) de un total de $totalCreditos")
        SummaryRow("Materias cursadas:", "${state.materiasCursadas}")
        SummaryRow("Materias aprobadas:", "${state.materiasAprobadas}")
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.weight(1f).padding(start = 16.dp))
    }
}

@Composable
fun PeriodoHeader(periodo: String) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = periodo,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun KardexTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("MATERIA", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold)
        Text("CDTS", modifier = Modifier.width(40.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
        Text("CALIF", modifier = Modifier.width(45.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.End)
    }
}

@Composable
fun KardexRow(item: KardexItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.materia,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )
            if (item.acreditacion.isNotBlank()) {
                Text(
                    text = item.acreditacion,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        
        Text(
            text = item.creditos,
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = item.calificacion,
            modifier = Modifier.width(45.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            color = if ((item.calificacion.toDoubleOrNull() ?: 0.0) >= 70.0) 
                        MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.error
        )
    }
}
