package com.example.sicedroidmultiplatform.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, onLogout: () -> Unit) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (uiState) {
            is ProfileUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ProfileUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        uiState.message,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { viewModel.loadProfile() }) { Text("Reintentar") }
                }
            }
            is ProfileUiState.Success -> {
                ProfileContent(uiState.data)
            }
        }
    }
}

@Composable
fun ProfileContent(data: Map<String, String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection(data["nombre"] ?: "Estudiante", data["matricula"] ?: "")

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Trayectoria Académica")
        ProfileRow("Carrera", data["carrera"] ?: "No disponible")
        ProfileRow("Especialidad", data["especialidad"] ?: "No disponible")
        ProfileRow("Semestre Actual", "${data["semActual"] ?: "N/A"}° Semestre")

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Estatus y Créditos")
        ProfileRow("Acumulados", data["cdtosAcumulados"] ?: "0")
        ProfileRow("Actuales", data["cdtosActuales"] ?: "0")
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        ProfileRow("Situación", data["estatus"] ?: "Desconocido")
        ProfileRow("Próxima Reinscripción", data["fechaReins"] ?: "No disponible")

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun HeaderSection(nombre: String, matricula: String) {
    val inicial = if (nombre.isNotEmpty()) nombre[0].toString() else "?"

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(90.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 6.dp,
            tonalElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = inicial,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = nombre,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = matricula,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
