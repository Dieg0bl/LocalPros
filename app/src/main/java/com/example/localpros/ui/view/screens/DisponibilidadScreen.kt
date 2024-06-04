package com.example.localpros.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.localpros.data.model.DisponibilidadTemporal
import com.example.localpros.data.model.PosicionYRadio
import com.example.localpros.ui.view.composables.BotonPersonalizado
import com.example.localpros.ui.view.composables.IndicadorDisponibilidad
import com.example.localpros.ui.view.composables.IndicadorUbicacionYRadio
import com.example.localpros.ui.viewModel.DisponibilidadViewModel
import com.google.maps.GeoApiContext
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime

@Composable
fun DisponibilidadScreen(
    geoApiContext: GeoApiContext,
    userId: String,
    esProfesional: Boolean, // Agregar este parámetro
    disponibilidadViewModel: DisponibilidadViewModel = hiltViewModel(),
) {
    var disponibilidadInicio by remember { mutableStateOf(LocalDateTime.now()) }
    var disponibilidadFin by remember { mutableStateOf(LocalDateTime.now().plusHours(1)) }
    var location by remember { mutableStateOf(PosicionYRadio(0.0, 0.0, 10.0)) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Selecciona la disponibilidad de inicio y fin", style = MaterialTheme.typography.bodyLarge)
        IndicadorDisponibilidad(
            fechaHoraInicio = disponibilidadInicio,
            fechaHoraFin = disponibilidadFin,
            onFechaHoraInicioChange = { newStart ->
                disponibilidadInicio = newStart
            },
            onFechaHoraFinChange = { newEnd ->
                disponibilidadFin = newEnd
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Duración total: ${formatDuration(Duration.between(disponibilidadInicio, disponibilidadFin))}", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona tu ubicación de trabajo", style = MaterialTheme.typography.bodyLarge)
        IndicadorUbicacionYRadio(
            ubicacionInicial = location,
            onUbicacionSeleccionada = { newLocation ->
                location = newLocation
            },
            geoApiContext = geoApiContext,
            esProfesional = esProfesional
        )

        Spacer(modifier = Modifier.height(16.dp))

        BotonPersonalizado(
            text = "Guardar Disponibilidad",
            onClick = {
                coroutineScope.launch {
                    disponibilidadViewModel.agregarDisponibilidad(
                        userId,
                        DisponibilidadTemporal(
                            inicio = disponibilidadInicio,
                            fin = disponibilidadFin
                        )
                    )
                }
            }
        )
    }
}

fun formatDuration(duration: Duration): String {
    val days = duration.toDays()
    val hours = duration.toHours() % 24
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60
    return "$days días, $hours horas, $minutes minutos, $seconds segundos"
}
