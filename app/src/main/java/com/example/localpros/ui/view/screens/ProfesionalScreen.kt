package com.example.localpros.ui.view.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.localpros.data.model.*
import com.example.localpros.data.model.usuarioModel.Usuario
import com.example.localpros.ui.view.composables.*
import com.example.localpros.ui.viewModel.UsuarioViewModel
import com.example.localpros.ui.viewModel.OfertaViewModel
import com.google.maps.GeoApiContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfesionalScreen(
    idUsuario: String,
    navController: NavHostController,
    geoApiContext: GeoApiContext
) {
    val userViewModel: UsuarioViewModel = hiltViewModel()
    val ofertaViewModel: OfertaViewModel = hiltViewModel()

    LaunchedEffect(idUsuario) {
        userViewModel.cargarUsuario(idUsuario)
        ofertaViewModel.listarOfertasDisponibles(idUsuario)
        ofertaViewModel.obtenerCandidaturas(idUsuario)
    }

    val userState by userViewModel.estadoUsuario.collectAsState()
    val ofertasState by ofertaViewModel.ofertasDisponibles.collectAsState()
    val candidaturasState by ofertaViewModel.candidaturas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil Profesional") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
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
                            ProfesionalContent(it, navController, geoApiContext, ofertasState, candidaturasState)
                        }
                    }
                    is ObtencionDatos.Error -> {
                        ErrorText((userState as ObtencionDatos.Error).excepcion.localizedMessage ?: "Error desconocido")
                    }
                }
            }
        }
    )
}

@Composable
fun ProfesionalContent(
    profesional: Usuario,
    navController: NavController,
    geoApiContext: GeoApiContext,
    ofertasState: ObtencionDatos<List<Oferta>>,
    candidaturasState: ObtencionDatos<List<Candidatura>>
) {
    val userViewModel: UsuarioViewModel = hiltViewModel()
    val ofertaViewModel: OfertaViewModel = hiltViewModel()

    var showMap by remember { mutableStateOf(false) }
    var ubicacion by remember { mutableStateOf(profesional.ubicacion ?: PosicionYRadio(40.416775, -3.703790, 10.0)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Bienvenido, ${profesional.nombre}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text("Tu información de contacto: ${profesional.datosContacto.email}, ${profesional.datosContacto.telefono}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))

        profesional.fotoPerfil?.let {
            Image(
                painter = rememberAsyncImagePainter(it.uri),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )
        } ?: Image(
            painter = rememberAsyncImagePainter("https://via.placeholder.com/150"),
            contentDescription = "Placeholder de perfil",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )

        Spacer(Modifier.height(16.dp))

        // Mapa para actualizar la ubicación
        Box(modifier = Modifier.fillMaxWidth().clickable { showMap = true }) {
            Text("Ubicación y Radio de Trabajo", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        }

        if (showMap) {
            Dialog(
                onDismissRequest = { showMap = false }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .border(1.dp, Color.Blue, RoundedCornerShape(16.dp))
                        .background(Color(0xFFE0F7FA), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Selecciona tu ubicación y radio de trabajo", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        IndicadorUbicacionYRadio(
                            ubicacionInicial = ubicacion,
                            onUbicacionSeleccionada = { nuevaUbicacion ->
                                ubicacion = nuevaUbicacion
                                userViewModel.actualizarUbicacionUsuario(profesional.id, nuevaUbicacion)
                                showMap = false
                            },
                            geoApiContext = geoApiContext,
                            esProfesional = profesional.esProfesional
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { showMap = false }) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Indicadores de Desempeño", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        IndicadoresDesempeno(
            performanceIndicators = profesional.indicadoresDesempeno ?: IndicadoresDesempeno(),
            onIndicatorsChange = null
        )

        Spacer(Modifier.height(16.dp))

        Text("Ofertas Disponibles", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        when (ofertasState) {
            is ObtencionDatos.Cargando -> CircularProgressIndicator()
            is ObtencionDatos.Exitosa -> {
                ofertasState.datos.forEach { oferta ->
                    OfertaCard(
                        oferta = oferta,
                        onVerDetalles = { /* Acciones al ver detalles */ },
                        onPresentarCandidatura = {
                            // Presentar candidatura lógica
                            ofertaViewModel.presentarCandidatura(oferta.id, profesional.id, "Detalles de candidatura", 1000.0)
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
            is ObtencionDatos.Error -> Text("Error al cargar ofertas: ${(ofertasState as ObtencionDatos.Error).excepcion.localizedMessage}", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        Text("Candidaturas Presentadas", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        when (candidaturasState) {
            is ObtencionDatos.Cargando -> CircularProgressIndicator()
            is ObtencionDatos.Exitosa -> {
                candidaturasState.datos.forEach { candidatura ->
                    CandidaturaCard(candidatura)
                    Spacer(Modifier.height(8.dp))
                }
            }
            is ObtencionDatos.Error -> Text("Error al cargar candidaturas: ${(candidaturasState as ObtencionDatos.Error).excepcion.localizedMessage}", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun OfertaCard(oferta: Oferta, onVerDetalles: () -> Unit, onPresentarCandidatura: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(oferta.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(oferta.descripcion, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Presupuesto: ${oferta.presupuestoDisponible}", style = MaterialTheme.typography.bodySmall)
                Button(onClick = onPresentarCandidatura) {
                    Text("Presentar Candidatura")
                }
            }
        }
    }
}

@Composable
fun CandidaturaCard(candidatura: Candidatura) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(candidatura.oferta.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(candidatura.cartaPresentacion, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Propuesta Económica: ${candidatura.propuestaEconomica}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
