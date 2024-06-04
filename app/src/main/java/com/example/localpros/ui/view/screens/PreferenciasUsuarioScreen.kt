package com.example.localpros.ui.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.localpros.data.model.*
import com.example.localpros.data.model.enums.Rol
import com.example.localpros.ui.view.composables.ErrorText
import com.example.localpros.ui.viewModel.UsuarioViewModel
import com.example.localpros.utils.validarCIF
import com.google.maps.GeoApiContext

@Composable
fun PreferenciasUsuarioScreen(
    preferenciasUsuario: PreferenciasYAjustesUsuario,
    contextoGeoApi: GeoApiContext,
    vistaModeloUsuario: UsuarioViewModel = viewModel()
) {
    var rolesUsuario by remember { mutableStateOf(preferenciasUsuario.roles) }
    var cif by remember { mutableStateOf(preferenciasUsuario.cif ?: "") }
    var notificacionesNuevasOfertas by remember { mutableStateOf(preferenciasUsuario.notificacionesNuevasOfertas) }
    var notificacionesCandidaturas by remember { mutableStateOf(preferenciasUsuario.notificacionesCandidaturas) }
    var notificacionesMensajes by remember { mutableStateOf(preferenciasUsuario.notificacionesMensajes) }
    var visibilidadPerfil by remember { mutableStateOf(preferenciasUsuario.visibilidadPerfil) }
    var estadoCuenta by remember { mutableStateOf(preferenciasUsuario.estadoCuenta) }
    var compartirInformacionContacto by remember { mutableStateOf(preferenciasUsuario.compartirInformacionContacto) }
    var ubicacionSeleccionada by remember { mutableStateOf(preferenciasUsuario.ubicacion) }
    var mensajeError by remember { mutableStateOf("") }
    var mostrarDialogoSolicitudRol by remember { mutableStateOf(false) }
    val alcanceCorrutina = rememberCoroutineScope()

    LaunchedEffect(preferenciasUsuario.idUsuario) {
        vistaModeloUsuario.obtenerRolesUsuario(preferenciasUsuario.idUsuario ?: "") { roles ->
            rolesUsuario = roles.toSet()
        }
    }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Preferencias del Usuario", style = MaterialTheme.typography.titleLarge)

        // Sección General
        Text("General", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Notificaciones", style = MaterialTheme.typography.titleMedium)
        PreferenciaSwitch(
            "Nuevas ofertas disponibles en tu zona de trabajo",
            notificacionesNuevasOfertas
        ) { notificacionesNuevasOfertas = it }
        PreferenciaSwitch(
            "Candidaturas recibidas para tus ofertas",
            notificacionesCandidaturas
        ) { notificacionesCandidaturas = it }
        PreferenciaSwitch(
            "Mensajes y comunicaciones",
            notificacionesMensajes
        ) { notificacionesMensajes = it }

        Spacer(modifier = Modifier.height(8.dp))

        // Sección Particular / Profesional
        Text("Particular / Profesional", style = MaterialTheme.typography.titleMedium)
        GrupoRadioButton(rolesUsuario.firstOrNull() ?: Rol.PARTICULAR) { rolSeleccionado ->
            rolesUsuario = setOf(rolSeleccionado)
        }

        if (rolesUsuario.contains(Rol.PROFESIONAL)) {
            TextField(
                value = cif,
                onValueChange = { cif = it },
                label = { Text("CIF de la empresa") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Zona de trabajo", style = MaterialTheme.typography.titleMedium)
            // Implementa tu LocationPicker aquí

            Spacer(modifier = Modifier.height(8.dp))

            Text("Disponibilidad", style = MaterialTheme.typography.titleMedium)
            // Implementa tu DateTimeRangePicker aquí
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sección Privacidad
        Text("Privacidad", style = MaterialTheme.typography.titleMedium)
        PreferenciaSwitch(
            "Visibilidad del perfil (público/privado)",
            visibilidadPerfil
        ) { visibilidadPerfil = it }
        PreferenciaSwitch("Desactivar / activar cuenta", estadoCuenta) { estadoCuenta = it }
        PreferenciaSwitch(
            "Compartir información de contacto solo con ofertas aceptadas",
            compartirInformacionContacto
        ) { compartirInformacionContacto = it }

        Spacer(modifier = Modifier.height(16.dp))

        if (mensajeError.isNotEmpty()) {
            ErrorText(mensajeError)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Lógica para eliminar usuario
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar Usuario")
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
                            // Lógica para activar el rol de profesional
                            vistaModeloUsuario.activarRolProfesional(
                                preferenciasUsuario.idUsuario ?: "", cif
                            ) { resultado ->
                                when (resultado) {
                                    is ObtencionDatos.Exitosa -> {
                                        if (resultado.datos) {
                                            mostrarDialogoSolicitudRol = false
                                        } else {
                                            mensajeError = "Error al activar el rol de profesional"
                                        }
                                    }
                                    is ObtencionDatos.Error -> {
                                        mensajeError = "Error al activar el rol de profesional: ${resultado.excepcion.message}"
                                    }
                                    else -> {
                                        // Otra lógica si es necesario
                                    }
                                }
                            }
                        } else {
                            // Mostrar mensaje de error
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
    }
}

@Composable
fun PreferenciaSwitch(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun GrupoRadioButton(
    selectedRole: Rol,
    onRoleSelected: (Rol) -> Unit
) {
    Column {
        Rol.values().forEach { role ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRoleSelected(role) }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = selectedRole == role,
                    onClick = { onRoleSelected(role) }
                )
                Text(
                    text = role.name,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
