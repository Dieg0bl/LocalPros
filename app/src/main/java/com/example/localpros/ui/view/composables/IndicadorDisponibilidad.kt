package com.example.localpros.ui.view.composables

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun IndicadorDisponibilidad(
    fechaHoraInicio: LocalDateTime,
    fechaHoraFin: LocalDateTime,
    onFechaHoraInicioChange: (LocalDateTime) -> Unit,
    onFechaHoraFinChange: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val formateadorFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val fechaHoraInicioString = remember(fechaHoraInicio) { fechaHoraInicio.format(formateadorFechaHora) }
    val fechaHoraFinString = remember(fechaHoraFin) { fechaHoraFin.format(formateadorFechaHora) }
    var mostrarSelectorFechaInicio by remember { mutableStateOf(false) }
    var mostrarSelectorFechaFin by remember { mutableStateOf(false) }
    var mostrarSelectorHoraInicio by remember { mutableStateOf(false) }
    var mostrarSelectorHoraFin by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    LaunchedEffect(mostrarSelectorFechaInicio) {
        if (mostrarSelectorFechaInicio) {
            mostrarDialogoSelectorFecha(context, fechaHoraInicio) { nuevaFecha ->
                onFechaHoraInicioChange(nuevaFecha.atTime(fechaHoraInicio.toLocalTime()))
                mostrarSelectorFechaInicio = false
                mostrarSelectorHoraInicio = true
            }
        }
    }

    LaunchedEffect(mostrarSelectorHoraInicio) {
        if (mostrarSelectorHoraInicio) {
            mostrarDialogoSelectorHora(context, fechaHoraInicio.toLocalTime()) { nuevaHora ->
                onFechaHoraInicioChange(fechaHoraInicio.toLocalDate().atTime(nuevaHora))
                mostrarSelectorHoraInicio = false
            }
        }
    }

    LaunchedEffect(mostrarSelectorFechaFin) {
        if (mostrarSelectorFechaFin) {
            mostrarDialogoSelectorFecha(context, fechaHoraFin) { nuevaFecha ->
                if (nuevaFecha.isAfter(fechaHoraInicio.toLocalDate()) || nuevaFecha.isEqual(fechaHoraInicio.toLocalDate())) {
                    onFechaHoraFinChange(nuevaFecha.atTime(fechaHoraFin.toLocalTime()))
                    mensajeError = ""
                } else {
                    mensajeError = "La fecha de fin no puede ser anterior a la fecha de inicio"
                }
                mostrarSelectorFechaFin = false
                mostrarSelectorHoraFin = true
            }
        }
    }

    LaunchedEffect(mostrarSelectorHoraFin) {
        if (mostrarSelectorHoraFin) {
            mostrarDialogoSelectorHora(context, fechaHoraFin.toLocalTime()) { nuevaHora ->
                if (fechaHoraFin.toLocalDate().isAfter(fechaHoraInicio.toLocalDate()) || nuevaHora.isAfter(fechaHoraInicio.toLocalTime())) {
                    onFechaHoraFinChange(fechaHoraFin.toLocalDate().atTime(nuevaHora))
                    mensajeError = ""
                } else {
                    mensajeError = "La hora de fin no puede ser anterior a la hora de inicio"
                }
                mostrarSelectorHoraFin = false
            }
        }
    }

    Column(modifier = modifier) {
        Button(
            onClick = { mostrarSelectorFechaInicio = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Seleccionar fecha y hora de inicio: $fechaHoraInicioString")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { mostrarSelectorFechaFin = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Seleccionar fecha y hora de fin: $fechaHoraFinString")
        }

        if (mensajeError.isNotEmpty()) {
            Text(text = mensajeError, color = MaterialTheme.colorScheme.error)
        }

        val duracion = Duration.between(fechaHoraInicio, fechaHoraFin)
        val dias = duracion.toDays()
        val horas = duracion.toHours() % 24
        val minutos = duracion.toMinutes() % 60
        val segundos = duracion.seconds % 60

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Duración: ${dias} días, ${horas} horas, ${minutos} minutos, ${segundos} segundos")
    }
}

private fun mostrarDialogoSelectorFecha(context: Context, dateTime: LocalDateTime, onDateSelected: (LocalDate) -> Unit) {
    val year = dateTime.year
    val month = dateTime.monthValue - 1
    val day = dateTime.dayOfMonth

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        onDateSelected(LocalDate.of(selectedYear, selectedMonth + 1, selectedDay))
    }, year, month, day).show()
}

private fun mostrarDialogoSelectorHora(context: Context, time: LocalTime, onTimeSelected: (LocalTime) -> Unit) {
    val hour = time.hour
    val minute = time.minute

    TimePickerDialog(context, { _, selectedHour, selectedMinute ->
        onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
    }, hour, minute, true).show()
}
