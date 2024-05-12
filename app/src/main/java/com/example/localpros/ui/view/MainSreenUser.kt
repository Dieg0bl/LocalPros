package com.example.localpros.ui.view


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.localpros.data.model.DataState
import com.example.localpros.data.model.Particular
import com.example.localpros.data.model.Profesional
import com.example.localpros.data.model.UserRole
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
    // LaunchedEffect para cargar los datos al abrir la vista
    LaunchedEffect(key1 = userId) {
        userViewModel.loadUserData(userId, userRole)
    }

    // Obteniendo el estado del usuario desde el ViewModel
    val userState = userViewModel.userData.collectAsState()

    // Scaffold para la estructura de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (userRole == UserRole.Profesional) "Perfil Profesional" else "Perfil Particular") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Regresar")
                    }
                },
                actions = {
                    if (userRole == UserRole.Profesional) {
                        IconButton(onClick = { /* Implement settings action */ }) { Icon(Icons.Filled.Settings, "Ajustes") }
                        IconButton(onClick = { /* Implement help action */ }) { Icon(Icons.Filled.Info, "Ayuda") }
                        IconButton(onClick = { /* Implement logout action */ }) { Icon(Icons.Filled.ExitToApp, "Salir") }
                    }
                }
            )
        }
    ) {
        // Manejo de los estados de carga, éxito y error
        when (val currentState = userState.value) {
            is DataState.Loading -> CircularProgressIndicator()
            is DataState.Success -> currentState.data?.let { user ->
                if (user is Profesional && userRole == UserRole.Profesional) {
                    MainContentProfesional(user)
                } else if (user is Particular && userRole == UserRole.Particular) {
                    MainContentParticular(user)
                } else {
                    Text("Datos del usuario no disponibles o incorrectos", style = MaterialTheme.typography.bodyLarge)
                }
            }
            is DataState.Error -> Text(currentState.exception.message ?: "Error desconocido", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun MainContentProfesional(profesional: Profesional) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bienvenido, ${profesional.nombre}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text("Tu información de contacto: ${profesional.contacto}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Text("Historial de candidaturas:", style = MaterialTheme.typography.titleMedium)
        if (profesional.historialCandidaturas.isNotEmpty()) {
            profesional.historialCandidaturas.forEach { candidatura ->
                Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Oferta: ${candidatura.oferta.descripcion}", style = MaterialTheme.typography.bodyMedium)
                        Text("Estado: ${candidatura.estado}", style = MaterialTheme.typography.bodySmall)
                        Text("Propuesta Económica: ${candidatura.propuestaEconomica}€", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        } else {
            Text("No hay candidaturas activas actualmente.", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(16.dp))
        IndicadoresDesempenoSection(profesional.indicadoresDesempeno)
    }
}

@Composable
fun MainContentParticular(particular: Particular) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bienvenido, ${particular.nombre}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text("Contacto: ${particular.contacto}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Text("Ofertas Publicadas:", style = MaterialTheme.typography.titleMedium)
        if (particular.historialOfertas.isNotEmpty()) {
            particular.historialOfertas.forEach { oferta ->
                Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Descripción: ${oferta.descripcion}", style = MaterialTheme.typography.bodyMedium)
                        Text("Ubicación: ${oferta.ubicacion}", style = MaterialTheme.typography.bodySmall)
                        Text("Presupuesto: ${oferta.presupuestoDisponible}€", style = MaterialTheme.typography.bodySmall)
                        Text("Estado: ${oferta.estado}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        } else {
            Text("No has publicado ofertas aún.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

