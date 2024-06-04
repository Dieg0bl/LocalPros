package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.DisponibilidadTemporal
import com.example.localpros.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarioViewModel @Inject constructor(
    private val repositorioCalendario: RepositorioCalendario
) : ViewModel() {

    private val _disponibilidad = MutableStateFlow<List<DisponibilidadTemporal>>(emptyList())
    val disponibilidad: StateFlow<List<DisponibilidadTemporal>> get() = _disponibilidad

    fun cargarDisponibilidad(idUsuario: String) {
        viewModelScope.launch {
            val result = repositorioCalendario.consultarDisponibilidad(idUsuario)
            _disponibilidad.value = result
        }
    }

    fun agregarDisponibilidad(idUsuario: String, disponibilidad: DisponibilidadTemporal) {
        viewModelScope.launch {
            repositorioCalendario.agregarDisponibilidad(idUsuario, disponibilidad)
            cargarDisponibilidad(idUsuario)
        }
    }

    fun eliminarDisponibilidad(idUsuario: String, idDisponibilidad: String) {
        viewModelScope.launch {
            repositorioCalendario.eliminarDisponibilidad(idUsuario, idDisponibilidad)
            cargarDisponibilidad(idUsuario)
        }
    }

    fun actualizarDisponibilidad(idUsuario: String, disponibilidad: DisponibilidadTemporal) {
        viewModelScope.launch {
            repositorioCalendario.actualizarDisponibilidad(idUsuario, disponibilidad)
            cargarDisponibilidad(idUsuario)
        }
    }
}