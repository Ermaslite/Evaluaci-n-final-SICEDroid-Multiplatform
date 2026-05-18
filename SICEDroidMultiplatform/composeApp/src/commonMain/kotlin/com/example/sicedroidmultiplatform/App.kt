package com.example.sicedroidmultiplatform

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sicedroidmultiplatform.data.repository.SicenetRepository
import com.example.sicedroidmultiplatform.ui.login.LoginScreen
import com.example.sicedroidmultiplatform.ui.login.LoginViewModel
import com.example.sicedroidmultiplatform.ui.profile.ProfileScreen
import com.example.sicedroidmultiplatform.ui.profile.ProfileViewModel
import com.example.sicedroidmultiplatform.ui.carga.CargaScreen
import com.example.sicedroidmultiplatform.ui.carga.CargaViewModel
import com.example.sicedroidmultiplatform.ui.calificaciones.CalificacionesScreen
import com.example.sicedroidmultiplatform.ui.calificaciones.CalificacionesViewModel
import com.example.sicedroidmultiplatform.ui.kardex.KardexScreen
import com.example.sicedroidmultiplatform.ui.kardex.KardexViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    var currentScreen by remember { mutableStateOf("login") }
    var selectedItem by remember { mutableStateOf("Perfil") }
    
    val repository = remember { SicenetRepository() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MaterialTheme(colorScheme = colorScheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            if (currentScreen == "login") {
                val loginViewModel = remember { LoginViewModel(repository) }
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = { currentScreen = "main" }
                )
            } else {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "SICE MOVIL",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                            HorizontalDivider()
                            
                            NavigationDrawerItem(
                                label = { Text("Mi Perfil") },
                                selected = selectedItem == "Perfil",
                                onClick = {
                                    selectedItem = "Perfil"
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            
                            NavigationDrawerItem(
                                label = { Text("Carga Académica") },
                                selected = selectedItem == "Carga Académica",
                                onClick = {
                                    selectedItem = "Carga Académica"
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            
                            NavigationDrawerItem(
                                label = { Text("Calificaciones") },
                                selected = selectedItem == "Calificaciones",
                                onClick = {
                                    selectedItem = "Calificaciones"
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )

                            NavigationDrawerItem(
                                label = { Text("Kardex") },
                                selected = selectedItem == "Kardex",
                                onClick = {
                                    selectedItem = "Kardex"
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )

                            Spacer(Modifier.weight(1f))
                            TextButton(
                                onClick = { 
                                    currentScreen = "login"
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(selectedItem, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                                navigationIcon = {
                                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                        Column(
                                            modifier = Modifier.size(24.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            val color = MaterialTheme.colorScheme.onSurface
                                            Box(Modifier.width(18.dp).height(2.dp).background(color))
                                            Spacer(Modifier.height(4.dp))
                                            Box(Modifier.width(18.dp).height(2.dp).background(color))
                                            Spacer(Modifier.height(4.dp))
                                            Box(Modifier.width(18.dp).height(2.dp).background(color))
                                        }
                                    }
                                }
                            )
                        }
                    ) { padding ->
                        Box(modifier = Modifier.padding(padding)) {
                            when (selectedItem) {
                                "Perfil" -> {
                                    val profileViewModel = remember { ProfileViewModel(repository) }
                                    ProfileScreen(viewModel = profileViewModel)
                                }
                                "Carga Académica" -> {
                                    val cargaViewModel = remember { CargaViewModel(repository) }
                                    CargaScreen(viewModel = cargaViewModel)
                                }
                                "Calificaciones" -> {
                                    val califViewModel = remember { CalificacionesViewModel(repository) }
                                    CalificacionesScreen(viewModel = califViewModel)
                                }
                                "Kardex" -> {
                                    val kardexViewModel = remember { KardexViewModel(repository) }
                                    KardexScreen(viewModel = kardexViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
