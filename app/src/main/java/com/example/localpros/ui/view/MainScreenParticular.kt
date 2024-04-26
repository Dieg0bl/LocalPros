package com.example.localpros.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.localpros.data.model.Oferta
import com.example.localpros.data.model.Particular
import com.example.localpros.data.model.UserRole
import com.example.localpros.ui.viewModel.UserViewModel
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.localpros.data.model.DataState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenParticular(
    userId: String,
    navController: NavController,
    userViewModel: UserViewModel
) {

    LaunchedEffect(key1 = userId) {
        userViewModel.loadUserData(userId, UserRole.Particular)
    }

    val particularState = userViewModel.particularData.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil Particular") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Regresar")
                    }
                }
            )
        }
    ) {
        when (val currentState = particularState.value) {
            is DataState.Loading -> CircularProgressIndicator()
            is DataState.Success -> {
                currentState.data?.let { MainContentParticular(it) } ?: Text("No se encontraron datos del usuario.", style = MaterialTheme.typography.bodyLarge)
            }
            is DataState.Error -> {
                Text(currentState.exception.message ?: "Error desconocido", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun MainContentParticular(particular: Particular) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bienvenido, ${particular.nombre}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text("Tus ofertas publicadas:", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        if (particular.historialOfertas.isNotEmpty()) {
            ListaOfertas(particular.historialOfertas)
        } else {
            Text("No hay ofertas publicadas.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ListaOfertas(ofertas: List<Oferta>) {
    Column {
        ofertas.forEach { oferta ->
            ItemOferta(oferta)
            Divider()
        }
    }
}

@Composable
fun ItemOferta(oferta: Oferta) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Descripción: ${oferta.descripcion}", style = MaterialTheme.typography.bodyMedium)
        Text("Ubicación: ${oferta.ubicacion}", style = MaterialTheme.typography.bodySmall)
        Text("Presupuesto: ${oferta.presupuestoDisponible}€", style = MaterialTheme.typography.bodySmall)
        Text("Estado: ${oferta.estado}", style = MaterialTheme.typography.bodySmall)
    }
}
