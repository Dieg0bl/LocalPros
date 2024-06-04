package com.example.localpros.ui.view.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.localpros.R
import com.example.localpros.data.model.ObtencionDatos
import com.example.localpros.data.model.PreferenciasYAjustesUsuario
import com.example.localpros.data.model.enums.Rol
import com.example.localpros.ui.navigation.PantallasApp
import com.example.localpros.ui.viewModel.AutenticacionViewModel
import com.example.localpros.ui.viewModel.UsuarioViewModel
import com.example.localpros.utils.validarCIF

@Composable
fun SeleccionRolScreen(
    navController: NavHostController,
    preferenciasUsuario: PreferenciasYAjustesUsuario,
    autenticacionViewModel: AutenticacionViewModel = hiltViewModel()
) {
    val usuarioViewModel: UsuarioViewModel = hiltViewModel()
    val estadoAutenticacion by autenticacionViewModel.estadoAutenticacion.collectAsState()
    var mostrarDialogoSolicitudRol by remember { mutableStateOf(false) }
    var cif by remember { mutableStateOf("") }
    var mostrarOpcionesRolDialogo by remember { mutableStateOf(false) }
    var rolSeleccionado by remember { mutableStateOf<Rol?>(null) }
    var mensajeError by remember { mutableStateOf("") }
    val alcanceCorrutina = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Selecciona tu rol", style = MaterialTheme.typography.headlineLarge)

        if (estadoAutenticacion is AutenticacionViewModel.EstadoAutenticacion.Exitosa) {
            val roles = (estadoAutenticacion as AutenticacionViewModel.EstadoAutenticacion.Exitosa).roles

            Button(
                onClick = {
                    navController.navigate(PantallasApp.PantallaPrincipalUsuario.createRoute(Rol.PARTICULAR))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                rolSeleccionado = Rol.PARTICULAR
                                mostrarOpcionesRolDialogo = true
                            }
                        )
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Particular",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            Button(
                onClick = {
                    if (roles.contains(Rol.PROFESIONAL)) {
                        navController.navigate(PantallasApp.PantallaPrincipalUsuario.createRoute(Rol.PROFESIONAL))
                    } else {
                        mostrarDialogoSolicitudRol = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                rolSeleccionado = Rol.PROFESIONAL
                                mostrarOpcionesRolDialogo = true
                            }
                        )
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Profesional",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            if (mostrarOpcionesRolDialogo) {
                AlertDialog(
                    onDismissRequest = { mostrarOpcionesRolDialogo = false },
                    title = { Text("Opciones de Rol") },
                    text = {
                        Column {
                            if (rolSeleccionado != null) {
                                val rol = rolSeleccionado!!
                                if (rol == Rol.PROFESIONAL) {
                                    OutlinedTextField(
                                        value = cif,
                                        onValueChange = { cif = it },
                                        label = { Text("CIF") }
                                    )
                                }
                                Button(onClick = {
                                    if (rol == Rol.PROFESIONAL) {
                                        preferenciasUsuario.idUsuario?.let { idUsuario ->
                                            usuarioViewModel.activarRolProfesional(idUsuario,
                                                rol.toString()
                                            ) { result ->
                                                if (result is ObtencionDatos.Exitosa && result.datos == true) {
                                                    mostrarOpcionesRolDialogo = false
                                                } else {
                                                    mensajeError = (result as? ObtencionDatos.Error)?.excepcion?.localizedMessage ?: "Error al asignar rol"
                                                }
                                            }
                                        }
                                    }
                                }) {
                                    Text("Activar Rol")
                                }
                                Button(onClick = {
                                    preferenciasUsuario.idUsuario?.let { idUsuario ->
                                        usuarioViewModel.removerRol(idUsuario, rol) { result ->
                                            if (result is ObtencionDatos.Exitosa && result.datos == true) {
                                                mostrarOpcionesRolDialogo = false
                                            } else {
                                                mensajeError = (result as? ObtencionDatos.Error)?.excepcion?.localizedMessage ?: "Error al eliminar rol"
                                            }
                                        }
                                    }
                                }) {
                                    Text("Eliminar Rol")
                                }
                                Button(onClick = {
                                    preferenciasUsuario.idUsuario?.let { idUsuario ->
                                        usuarioViewModel.desactivarRol(idUsuario, rol) { result ->
                                            if (result is ObtencionDatos.Exitosa && result.datos == true) {
                                                mostrarOpcionesRolDialogo = false
                                            } else {
                                                mensajeError = (result as? ObtencionDatos.Error)?.excepcion?.localizedMessage ?: "Error al desactivar rol"
                                            }
                                        }
                                    }
                                }) {
                                    Text("Desactivar Rol Temporalmente")
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { mostrarOpcionesRolDialogo = false }) {
                            Text("Cerrar")
                        }
                    }
                )
            }

            if (mostrarDialogoSolicitudRol) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoSolicitudRol = false },
                    title = { Text("Solicitar Rol de Profesional") },
                    text = {
                        Column {
                            Text("Para activar el rol de profesional, introduzca su CIF:")
                            OutlinedTextField(
                                value = cif,
                                onValueChange = { cif = it },
                                label = { Text("CIF") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            if (validarCIF(cif)) {
                                autenticacionViewModel.activarRolProfesional(preferenciasUsuario.idUsuario, cif) { exito: Boolean ->
                                    if (exito) {
                                        mostrarDialogoSolicitudRol = false
                                    } else {
                                        mensajeError = "Error al activar rol de profesional"
                                    }
                                }
                            } else {
                                mensajeError = "CIF no válido"
                            }
                        }) {
                            Text("Solicitar Rol")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { mostrarDialogoSolicitudRol = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (mensajeError.isNotEmpty()) {
                Text(mensajeError, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
