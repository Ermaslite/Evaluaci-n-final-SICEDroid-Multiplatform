package com.example.sicedroidmultiplatform

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {
    var matricula by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    val tipoUsuario = "Alumno"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SICEDroid",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 32.dp),
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = matricula,
            onValueChange = { matricula = it },
            label = { Text("Matrícula") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = contrasenia,
            onValueChange = { contrasenia = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            singleLine = true
        )

        Button(
            onClick = {
                // Aquí se conectará con SoapRequestBuilder más adelante
                println("Login: $matricula, Tipo: $tipoUsuario")
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Entrar")
        }
    }
}
