package com.example.localpros.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localpros.data.model.ObtencionDatos
import com.example.localpros.data.model.PosicionYRadio
import com.example.localpros.data.model.enums.Rol
import com.example.localpros.data.model.usuarioModel.Usuario
import com.example.localpros.data.repository.RepositorioUsuarios
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val repositorioUsuarios: RepositorioUsuarios
) : ViewModel() {

    private val _estadoUsuario = MutableStateFlow<ObtencionDatos<Usuario?>>(ObtencionDatos.Cargando)
    val estadoUsuario: StateFlow<ObtencionDatos<Usuario?>> get() = _estadoUsuario

    private val _estadoAsignacionRol = MutableStateFlow<ObtencionDatos<Boolean>>(ObtencionDatos.Cargando)
    val estadoAsignacionRol: StateFlow<ObtencionDatos<Boolean>> get() = _estadoAsignacionRol

    private val _estadoActualizacion = MutableStateFlow<ObtencionDatos<Boolean>>(ObtencionDatos.Cargando)
    val estadoActualizacion: StateFlow<ObtencionDatos<Boolean>> get() = _estadoActualizacion

    fun cargarUsuario(userId: String) {
        _estadoUsuario.value = ObtencionDatos.Cargando
        repositorioUsuarios.obtenerUsuarioPorId(userId) { usuario ->
            if (usuario != null) {
                _estadoUsuario.value = ObtencionDatos.Exitosa(usuario)
            } else {
                _estadoUsuario.value = ObtencionDatos.Error(Exception("Usuario no encontrado."))
            }
        }
    }

    fun activarRolParticular(userId: String, callback: (ObtencionDatos<Boolean>) -> Unit) {
        viewModelScope.launch {
            _estadoAsignacionRol.value = ObtencionDatos.Cargando
            repositorioUsuarios.asignarRol(userId, Rol.PARTICULAR) { estado ->
                _estadoAsignacionRol.value = estado
                callback(estado)
            }
        }
    }

    fun activarRolProfesional(userId: String, cif: String, callback: (ObtencionDatos<Boolean>) -> Unit) {
        viewModelScope.launch {
            _estadoAsignacionRol.value = ObtencionDatos.Cargando
            repositorioUsuarios.activarRolProfesional(userId, cif) { resultado ->
                _estadoAsignacionRol.value = resultado
                callback(resultado)
            }
        }
    }

    fun removerRol(userId: String, rol: Rol, callback: (ObtencionDatos<Boolean>) -> Unit) {
        viewModelScope.launch {
            repositorioUsuarios.eliminarRol(userId, rol) { resultado ->
                _estadoAsignacionRol.value = resultado
                callback(resultado)
            }
        }
    }

    fun desactivarRol(userId: String, rol: Rol, callback: (ObtencionDatos<Boolean>) -> Unit) {
        viewModelScope.launch {
            repositorioUsuarios.desactivarRol(userId, rol) { resultado ->
                _estadoAsignacionRol.value = resultado
                callback(resultado)
            }
        }
    }

    fun actualizarUbicacionUsuario(userId: String, nuevaUbicacion: PosicionYRadio) {
        viewModelScope.launch {
            repositorioUsuarios.actualizarUbicacionUsuario(userId, nuevaUbicacion) { resultado ->
                _estadoActualizacion.value = resultado
            }
        }
    }

    fun obtenerRolesUsuario(userId: String, callback: (Set<Rol>) -> Unit) {
        viewModelScope.launch {
            repositorioUsuarios.obtenerRolesUsuario(userId) { roles: Set<Rol> ->
                callback(roles)
            }
        }
    }

    fun asignarRol(idUsuario: String, rol: Rol, any: Any) {

    }
}
