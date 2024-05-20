package com.example.localpros.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.localpros.data.model.*
import com.example.localpros.ui.view.composables.IndicadoresDesempenoSection
import com.example.localpros.ui.viewModel.UserViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenUser(
    userId: String,
    navController: NavController,
    userViewModel: UserViewModel,
    userRole: UserRole
) {
    LaunchedEffect(userId) {
        println("Cargando datos para el usuario: $userId, rol: $userRole")
        userViewModel.loadUserData(userId, userRole)
    }

    val userState by userViewModel.userData.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (userRole == UserRole.Profesional) "Perfil Profesional" else "Perfil Particular") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    if (userRole == UserRole.Profesional) {
                        IconButton(onClick = { /* Implement settings action */ }) { Icon(Icons.Filled.Settings, contentDescription = "Ajustes") }
                        IconButton(onClick = { /* Implement help action */ }) { Icon(Icons.Filled.Info, contentDescription = "Ayuda") }
                        IconButton(onClick = { /* Implement logout action */ }) { Icon(Icons.Filled.ExitToApp, contentDescription = "Salir") }
                    }
                }
            )
        },
        content = {
            when (userState) {
                is DataState.Loading -> {
                    println("Cargando datos para el usuario: $userId")
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
                is DataState.Success -> {
                    println("Datos cargados con éxito para el usuario: $userId")
                    val userData = (userState as DataState.Success<Usuario>).data
                    if (userData is Profesional && userRole == UserRole.Profesional) {
                        MainContentProfesional(userData, userViewModel)
                    } else if (userData is Particular && userRole == UserRole.Particular) {
                        MainContentParticular(userData, userViewModel)
                    } else {
                        println("Tipo de datos incorrecto para el usuario: $userId, rol: $userRole")
                        Text("Datos del usuario no disponibles o incorrectos", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                is DataState.Error -> {
                    println("Error al cargar datos para el usuario: $userId, Error: ${(userState as DataState.Error).exception.localizedMessage}")
                    Text("Error: ${(userState as DataState.Error).exception.localizedMessage}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    )
}

@Composable
fun MainContentProfesional(profesional: Profesional, userViewModel: UserViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bienvenido, ${profesional.nombre}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text("Tu información de contacto: ${profesional.contacto}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Text("Historial de candidaturas:", style = MaterialTheme.typography.titleMedium)

        if (profesional.historialCandidaturas.isNotEmpty()) {
            profesional.historialCandidaturas.forEach { (candidaturaId, _) ->
                var candidaturaState by remember { mutableStateOf<DataState<Candidatura>>(DataState.Loading) }

                LaunchedEffect(candidaturaId) {
                    userViewModel.loadCandidaturaData(candidaturaId)
                    userViewModel.candidaturaData.collect { result ->
                        if (result is DataState.Success) {
                            candidaturaState = result as DataState<Candidatura>
                        }
                    }
                }

                when (candidaturaState) {
                    is DataState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is DataState.Success -> {
                        Text("Candidatura cargada con éxito")
                    }
                    is DataState.Error -> {
                        Text("Error al cargar la candidatura: ${(candidaturaState as DataState.Error).exception.localizedMessage}")
                    }
                }
            }
        } else {
            Text("No hay historial de candidaturas", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun MainContentParticular(particular: Particular, userViewModel: UserViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bienvenido, ${particular.nombre}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text("Tu información de contacto: ${particular.contacto}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Text("Historial de ofertas:", style = MaterialTheme.typography.titleMedium)

        if (particular.historialOfertas.isNotEmpty()) {
            particular.historialOfertas.forEach { (ofertaId, _) ->
                var ofertaState by remember { mutableStateOf<DataState<Oferta>>(DataState.Loading) }

                LaunchedEffect(ofertaId) {
                    userViewModel.loadOfertaData(ofertaId)
                    userViewModel.ofertaData.collect { result ->
                        if (result is DataState.Success) {
                            ofertaState = result as DataState<Oferta>
                        }
                    }
                }

                when (ofertaState) {
                    is DataState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is DataState.Success -> {
                        Text("Oferta cargada con éxito")
                    }
                    is DataState.Error -> {
                        Text("Error al cargar la oferta: ${(ofertaState as DataState.Error).exception.localizedMessage}")
                    }
                }
            }
        } else {
            Text("No hay historial de ofertas", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
