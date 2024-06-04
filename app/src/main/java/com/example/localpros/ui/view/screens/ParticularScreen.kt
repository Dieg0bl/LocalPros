package com.example.localpros.ui.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.localpros.data.model.ObtencionDatos
import com.example.localpros.data.model.Oferta
import com.example.localpros.data.model.enums.Rol
import com.example.localpros.data.model.usuarioModel.Usuario
import com.example.localpros.ui.view.composables.ColumnaLazyPersonalizada
import com.example.localpros.ui.view.composables.ErrorText
import com.example.localpros.ui.view.composables.IndicadorCarga
import com.example.localpros.ui.view.composables.TargetaPersonalizada
import com.example.localpros.ui.viewModel.OfertaViewModel
import com.example.localpros.ui.viewModel.UsuarioViewModel
import com.google.maps.GeoApiContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticularScreen(
    idUsuario: String,
    navController: NavHostController,
    geoApiContext: GeoApiContext

){
    val userViewModel: UsuarioViewModel = hiltViewModel()
    LaunchedEffect(idUsuario) {
        userViewModel.cargarUsuario(idUsuario)
    }

    val userState by userViewModel.estadoUsuario.collectAsState()
    val roleAssignmentState by userViewModel.estadoAsignacionRol.collectAsState()
    val updateState by userViewModel.estadoActualizacion.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil Particular") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("configuracion_screen") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Configuración")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("crear_oferta_screen") },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Crear Oferta")
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFE0F7FA))
                    .padding(16.dp)
            ) {
                when (userState) {
                    is ObtencionDatos.Cargando -> IndicadorCarga()
                    is ObtencionDatos.Exitosa<*> -> {
                        val userData = (userState as ObtencionDatos.Exitosa<Usuario?>).datos
                        userData?.let {
                            ParticularContent(it, navController)
                        }
                    }
                    is ObtencionDatos.Error -> {
                        ErrorText((userState as ObtencionDatos.Error).excepcion.localizedMessage ?: "Error desconocido")
                    }
                }

                if (roleAssignmentState is ObtencionDatos.Cargando || updateState is ObtencionDatos.Cargando) {
                    ProcessingOverlay()
                }
            }
        }
    )
}

@Composable
fun ProcessingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ParticularContent(particular: Usuario, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Bienvenido, ${particular.nombre}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("buscar_profesionales_screen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar profesionales")
        }

        Spacer(Modifier.height(16.dp))

        Text("Tus ofertas publicadas:", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))

        if (particular.roles.contains(Rol.PARTICULAR)) {
            ColumnaLazyPersonalizada(elementos = particular.roles.toList()) { rol ->
                val ofertaViewModel: OfertaViewModel = hiltViewModel()
                val ofertaState by ofertaViewModel.estadoOferta.collectAsState()

                LaunchedEffect(rol) {
                    ofertaViewModel.cargarDatosOferta(rol.toString())
                }

                when (ofertaState) {
                    is ObtencionDatos.Cargando -> CircularProgressIndicator()
                    is ObtencionDatos.Exitosa<*> -> {
                        val offer = (ofertaState as ObtencionDatos.Exitosa<Oferta>).datos
                        TargetaPersonalizada(item = offer, onClick = { /* TODO */ }) {
                            ItemOferta(offer)
                        }
                    }
                    is ObtencionDatos.Error -> {
                        ErrorText((ofertaState as ObtencionDatos.Error).excepcion.localizedMessage ?: "Error desconocido")
                    }
                }
            }
        } else {
            Text("No tienes ofertas publicadas.")
        }

        Spacer(Modifier.height(16.dp))

        Text("Mis Reseñas:", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))

        if (particular.roles.contains(Rol.PARTICULAR)) {
            ColumnaLazyPersonalizada(elementos = particular.roles.toList()) { rol ->
                // TODO: Implementar reseñas
            }
        } else {
            Text("No tienes reseñas emitidas.")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("configuracion_screen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Configuración")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Acción para cerrar sesión */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar Sesión")
        }
    }
}

@Composable
fun ItemOferta(offer: Oferta) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Descripción: ${offer.descripcion}", style = MaterialTheme.typography.bodyMedium)
        Text("Ubicación: ${offer.ubicacion}", style = MaterialTheme.typography.bodySmall)
        Text("Presupuesto: ${offer.presupuestoDisponible}€", style = MaterialTheme.typography.bodySmall)
        Text("Estado: ${offer.estado}", style = MaterialTheme.typography.bodySmall)
    }
}
