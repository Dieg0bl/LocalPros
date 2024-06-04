package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.Notificacion
import com.example.localpros.data.repository.RepositorioNotificaciones
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificacionViewModel @Inject constructor(
    private val repositorioNotificaciones: RepositorioNotificaciones
) : ViewModel() {

    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> get() = _notificaciones

    fun cargarNotificaciones(idUsuario: String) {
        viewModelScope.launch {
            val result = repositorioNotificaciones.listarNotificaciones(idUsuario)
            _notificaciones.value = result
        }
    }

    fun enviarNotificacion(notificacion: Notificacion) {
        viewModelScope.launch {
            repositorioNotificaciones.enviarNotificacion(notificacion)
            cargarNotificaciones(notificacion.userId)
        }
    }
}