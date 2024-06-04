package com.example.localpros.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.localpros.data.model.enums.Categoria
import com.example.localpros.data.model.enums.CalidadMateriales
import com.example.localpros.data.model.enums.CalidadProcedimientos
import com.example.localpros.data.model.ObtencionDatos
import com.example.localpros.data.model.Oferta
import com.example.localpros.ui.view.composables.BotonPersonalizado
import com.example.localpros.ui.view.composables.TextFieldPersonalizado
import com.example.localpros.ui.view.composables.IndicadoresDesempeno
import com.example.localpros.ui.viewModel.OfertaViewModel
import com.example.localpros.data.model.IndicadoresDesempeno
import com.example.localpros.data.model.enums.EstadoOferta
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Composable
fun CrearOfertaScreen(
    idUsuario: String,
    navController: NavHostController,
    ofertaViewModel: OfertaViewModel = hiltViewModel(),
    onCreateOfferSuccess: () -> Unit = {
        navController.navigateUp()
    }
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var calidadMateriales by remember { mutableStateOf<CalidadMateriales?>(null) }
    var calidadProcedimientos by remember { mutableStateOf<CalidadProcedimientos?>(null) }
    var categoriaSeleccionada by remember { mutableStateOf<Categoria?>(null) }
    var tiempoEstimado by remember { mutableStateOf("") }
    var inicioEjecucion by remember { mutableStateOf(LocalDateTime.now()) }
    var finalizacionEjecucion by remember { mutableStateOf(LocalDateTime.now().plusHours(1)) }
    val createOfferState by ofertaViewModel.estadoOferta.collectAsState()
    var categoriaDialogOpen by remember { mutableStateOf(false) }
    var calidadMaterialesDialogOpen by remember { mutableStateOf(false) }
    var calidadProcedimientosDialogOpen by remember { mutableStateOf(false) }
    val indicadoresDesempeno = remember { mutableStateOf(IndicadoresDesempeno()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Crear Oferta", style = MaterialTheme.typography.headlineLarge)

        TextFieldPersonalizado(
            value = title,
            onValueChange = { title = it },
            label = "Título",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        TextFieldPersonalizado(
            value = description,
            onValueChange = { description = it },
            label = "Descripción",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        TextFieldPersonalizado(
            value = price,
            onValueChange = { price = it },
            label = "Precio",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedButton(onClick = { calidadMaterialesDialogOpen = true }) {
            Text(text = calidadMateriales?.descripcion ?: "Seleccionar Calidad de Materiales")
        }

        if (calidadMaterialesDialogOpen) {
            Dialog(
                onDismissRequest = { calidadMaterialesDialogOpen = false },
                properties = DialogProperties()
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Seleccionar Calidad de Materiales", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        CalidadMateriales.values().forEach { calidad ->
                            TextButton(onClick = {
                                calidadMateriales = calidad
                                calidadMaterialesDialogOpen = false
                            }) {
                                Text(text = calidad.descripcion)
                            }
                        }
                    }
                }
            }
        }

        OutlinedButton(onClick = { calidadProcedimientosDialogOpen = true }) {
            Text(text = calidadProcedimientos?.descripcion ?: "Seleccionar Calidad de Procedimientos")
        }

        if (calidadProcedimientosDialogOpen) {
            Dialog(
                onDismissRequest = { calidadProcedimientosDialogOpen = false },
                properties = DialogProperties()
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Seleccionar Calidad de Procedimientos", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        CalidadProcedimientos.values().forEach { calidad ->
                            TextButton(onClick = {
                                calidadProcedimientos = calidad
                                calidadProcedimientosDialogOpen = false
                            }) {
                                Text(text = calidad.descripcion)
                            }
                        }
                    }
                }
            }
        }

        OutlinedButton(onClick = { categoriaDialogOpen = true }) {
            Text(text = categoriaSeleccionada?.nombre ?: "Seleccionar Categoría")
        }

        if (categoriaDialogOpen) {
            Dialog(
                onDismissRequest = { categoriaDialogOpen = false },
                properties = DialogProperties()
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Seleccionar Categoría", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Categoria.values().forEach { categoria ->
                            TextButton(onClick = {
                                categoriaSeleccionada = categoria
                                categoriaDialogOpen = false
                            }) {
                                Text(text = categoria.nombre)
                            }
                        }
                    }
                }
            }
        }

        TextFieldPersonalizado(
            value = tiempoEstimado,
            onValueChange = { tiempoEstimado = it },
            label = "Tiempo Estimado",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        // Campos para inicioEjecucion y finalizacionEjecucion
        TextFieldPersonalizado(
            value = inicioEjecucion.toString(),
            onValueChange = { inicioEjecucion = LocalDateTime.parse(it) },
            label = "Inicio Ejecución",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default
        )

        TextFieldPersonalizado(
            value = finalizacionEjecucion.toString(),
            onValueChange = { finalizacionEjecucion = LocalDateTime.parse(it) },
            label = "Finalización Ejecución",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default
        )

        IndicadoresDesempeno(performanceIndicators = indicadoresDesempeno.value) { newIndicators ->
            indicadoresDesempeno.value = newIndicators
        }

        BotonPersonalizado(
            text = "Crear Oferta",
            onClick = {
                if (title.isNotBlank() && description.isNotBlank() && price.isNotBlank() && categoriaSeleccionada != null && calidadMateriales != null && calidadProcedimientos != null) {
                    val oferta = Oferta(
                        idPublicador = idUsuario,
                        titulo = title,
                        descripcion = description,
                        presupuestoDisponible = price.toDouble(),
                        calidadMateriales = calidadMateriales!!,
                        calidadProcedimientos = calidadProcedimientos!!,
                        descripcionLibre = description,
                        estado = EstadoOferta.PUBLICADA,
                        categoria = categoriaSeleccionada!!,
                        tiempoEstimado = tiempoEstimado,
                        fechaBorrado = LocalDate.now().plusDays(30),
                        inicioEjecucion = inicioEjecucion,
                        finalizacionEjecucion = finalizacionEjecucion,
                        indicadoresDesempeno = indicadoresDesempeno.value
                    )
                    val nuevaOfertaRef = ofertaViewModel.crearNuevaOfertaReferencia()
                    ofertaViewModel.crearOferta(oferta, nuevaOfertaRef) { estado ->
                        when (estado) {
                            is ObtencionDatos.Exitosa<*> -> onCreateOfferSuccess()
                            is ObtencionDatos.Error -> {
                                // Manejar error aquí si es necesario
                                Text(
                                    text = "Error al crear la oferta: ${(estado as ObtencionDatos.Error).excepcion.message}",
                                    color = Color.Red
                                )
                            }
                            else -> {}
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        when (createOfferState) {
            is ObtencionDatos.Cargando -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is ObtencionDatos.Error -> {
                Text(
                    text = (createOfferState as ObtencionDatos.Error).excepcion.message ?: "Error desconocido",
                    color = Color.Red
                )
            }
            is ObtencionDatos.Exitosa -> {
                LaunchedEffect(Unit) {
                    onCreateOfferSuccess()
                }
            }
        }
    }
}

