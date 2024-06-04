package com.example.localpros.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.localpros.data.model.IndicadoresDesempeno
import com.example.localpros.data.model.PosicionYRadio
import com.example.localpros.data.model.PreferenciasYAjustesUsuario
import com.example.localpros.data.model.enums.Rol
import com.example.localpros.data.model.usuarioModel.DatosContacto
import com.example.localpros.data.model.usuarioModel.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AutenticacionViewModel @Inject constructor(
    private val preferenciasUsuario: PreferenciasYAjustesUsuario,
    private val auth: FirebaseAuth,
    private val db: DatabaseReference
) : ViewModel() {

    sealed class EstadoAutenticacion {
        object Inactivo : EstadoAutenticacion()
        object Cargando : EstadoAutenticacion()
        data class Exitosa(val usuario: FirebaseUser, val roles: Set<Rol>) : EstadoAutenticacion()
        data class Error(val mensaje: String) : EstadoAutenticacion()
    }

    private val _estadoAutenticacion = MutableStateFlow<EstadoAutenticacion>(EstadoAutenticacion.Inactivo)
    val estadoAutenticacion: StateFlow<EstadoAutenticacion> get() = _estadoAutenticacion

    fun iniciarSesion(correo: String, contrasena: String) {
        _estadoAutenticacion.value = EstadoAutenticacion.Cargando
        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val usuario = auth.currentUser
                    if (usuario != null) {
                        obtenerRolesUsuario(usuario)
                    } else {
                        _estadoAutenticacion.value = EstadoAutenticacion.Error("Usuario no encontrado.")
                    }
                } else {
                    _estadoAutenticacion.value = EstadoAutenticacion.Error(
                        task.exception?.localizedMessage ?: "Error desconocido."
                    )
                }
            }
    }

    private fun obtenerRolesUsuario(usuario: FirebaseUser) {
        val userId = usuario.uid
        db.child("usuarios").child(userId).child("roles").get()
            .addOnSuccessListener { snapshot ->
                try {
                    val roles = snapshot.children.mapNotNull { it.key }
                        .mapNotNull { key ->
                            try {
                                Rol.valueOf(key)
                            } catch (e: IllegalArgumentException) {
                                null
                            }
                        }.toSet()

                    if (roles.isNotEmpty()) {
                        preferenciasUsuario.roles = roles
                        _estadoAutenticacion.value = EstadoAutenticacion.Exitosa(usuario, roles)
                    } else {
                        _estadoAutenticacion.value =
                            EstadoAutenticacion.Error("Roles no válidos para el usuario.")
                    }
                } catch (e: Exception) {
                    _estadoAutenticacion.value =
                        EstadoAutenticacion.Error("Error al deserializar los roles del usuario: ${e.message}")
                }
            }
            .addOnFailureListener { exception ->
                _estadoAutenticacion.value = EstadoAutenticacion.Error(
                    exception.localizedMessage ?: "Error al obtener los roles del usuario."
                )
            }
    }

    fun registrarUsuario(
        correo: String,
        contrasena: String,
        nombreUsuario: String,
        roles: Set<Rol>,
        posicionYRadio: PosicionYRadio,
        cif: String?
    ) {
        _estadoAutenticacion.value = EstadoAutenticacion.Cargando
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val usuario = auth.currentUser
                    if (usuario != null) {
                        val nuevoUsuario = Usuario(
                            id = usuario.uid,
                            nombre = nombreUsuario,
                            datosContacto = DatosContacto(nombreUsuario, correo, "", posicionYRadio),
                            roles = roles,
                            rolEnUso = roles.firstOrNull(),
                            ubicacion = posicionYRadio,
                            cif = cif,
                            esParticular = roles.contains(Rol.PARTICULAR),
                            esProfesional = roles.contains(Rol.PROFESIONAL),
                            indicadoresDesempeno = if (roles.contains(Rol.PROFESIONAL)) IndicadoresDesempeno() else null
                        )
                        db.child("usuarios").child(usuario.uid).setValue(nuevoUsuario)
                            .addOnSuccessListener {
                                _estadoAutenticacion.value = EstadoAutenticacion.Exitosa(usuario, roles)
                            }
                            .addOnFailureListener { exception ->
                                _estadoAutenticacion.value = EstadoAutenticacion.Error(
                                    exception.localizedMessage ?: "Error al guardar usuario en la base de datos."
                                )
                            }
                    } else {
                        _estadoAutenticacion.value = EstadoAutenticacion.Error("Error al crear el usuario.")
                    }
                } else {
                    _estadoAutenticacion.value = EstadoAutenticacion.Error(
                        task.exception?.localizedMessage ?: "Error desconocido al registrar el usuario."
                    )
                }
            }
    }

    fun activarRolProfesional(idUsuario: String?, cif: String, callback: (Boolean) -> Unit) {
        if (idUsuario == null) {
            callback(false)
            return
        }

        val usuarioRef = db.child("usuarios").child(idUsuario)
        usuarioRef.child("cif").setValue(cif).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                usuarioRef.child("roles").child("profesional").setValue(true).addOnCompleteListener { roleTask ->
                    if (roleTask.isSuccessful) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
            } else {
                callback(false)
            }
        }
    }

    // Método agregado para obtener el estado de autenticación
    fun obtenerEstadoAutenticacion(): EstadoAutenticacion {
        return _estadoAutenticacion.value
    }
}
