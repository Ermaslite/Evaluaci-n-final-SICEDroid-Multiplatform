package com.example.sicedroidmultiplatform.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
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
            value = viewModel.matricula,
            onValueChange = { viewModel.matricula = it },
            label = { Text("Matrícula") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true,
            enabled = !viewModel.isLoading
        )

        OutlinedTextField(
            value = viewModel.contrasenia,
            onValueChange = { viewModel.contrasenia = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true,
            enabled = !viewModel.isLoading
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.rememberMe,
                onCheckedChange = { viewModel.rememberMe = it },
                enabled = !viewModel.isLoading
            )
            Text(
                text = "Recordarme",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (viewModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
        }

        Button(
            onClick = {
                viewModel.performLogin(onLoginSuccess)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !viewModel.isLoading && viewModel.matricula.isNotBlank() && viewModel.contrasenia.isNotBlank()
        ) {
            Text("Entrar")
        }

        viewModel.error?.let { errorMsg ->
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
