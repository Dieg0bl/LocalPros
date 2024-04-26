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
import com.example.localpros.data.model.Profesional
import com.example.localpros.data.model.UserRole
import com.example.localpros.ui.viewModel.UserViewModel
import androidx.navigation.NavController
import com.example.localpros.data.model.DataState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenProfesional(
    userId: String,
    navController: NavController,
    userViewModel: UserViewModel
) {

    LaunchedEffect(key1 = userId) {
        userViewModel.loadUserData(userId, UserRole.Profesional)
    }

    val profesionalState = userViewModel.profesionalData.collectAsState()

    Scaffold(
        topBar = { TopBarProfesional(navController) }
    ) {
        when (val currentState = profesionalState.value) {
            is DataState.Loading -> CircularProgressIndicator()
            is DataState.Success -> currentState.data?.let { profesional ->
                MainContentProfesional(profesional)
            } ?: Text("No se encontraron datos del usuario.", style = MaterialTheme.typography.bodyLarge)
            is DataState.Error -> Text(currentState.exception.message ?: "Error desconocido", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarProfesional(navController: NavController) {
    TopAppBar(
        title = { Text("Perfil Profesional") },
        navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Filled.ArrowBack, "Regresar") }},
        actions = {
            IconButton(onClick = { /* TODO: Ajustes */ }) { Icon(Icons.Filled.Settings, "Ajustes") }
            IconButton(onClick = { /* TODO: Ayuda */ }) { Icon(Icons.Filled.Info, "Ayuda") }
            IconButton(onClick = { /* TODO: Cerrar sesión */ }) { Icon(Icons.Filled.ExitToApp, "Salir") }
        }
    )
}

@Composable
fun MainContentProfesional(profesional: Profesional) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bienvenido, ${profesional.nombre}", style = MaterialTheme.typography.titleMedium)
    }
}
