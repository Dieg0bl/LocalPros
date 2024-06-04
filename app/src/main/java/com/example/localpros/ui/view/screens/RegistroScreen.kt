package com.example.localpros.ui.view.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.localpros.data.model.PosicionYRadio
import com.example.localpros.data.model.enums.Rol
import com.example.localpros.ui.navigation.PantallasApp
import com.example.localpros.ui.view.composables.BotonPersonalizado
import com.example.localpros.ui.view.composables.IndicadorUbicacionYRadio
import com.example.localpros.ui.view.composables.TextFieldPersonalizado
import com.example.localpros.ui.viewModel.AutenticacionViewModel
import com.example.localpros.utils.*
import com.google.maps.GeoApiContext


@SuppressLint("UnrememberedMutableState")
@Composable
fun RegistroScreen(
    navController: NavHostController,
    autenticacionViewModel: AutenticacionViewModel = hiltViewModel(),
    geoApiContext: GeoApiContext
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var cif by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isParticular by remember { mutableStateOf(true) }
    var isProfesional by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(false) }
    var ubicacion by remember { mutableStateOf(PosicionYRadio(40.416775, -3.703790, 10.0)) }
    var showImagePicker by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val authState by autenticacionViewModel.estadoAutenticacion.collectAsState()

    // Validación del formulario
    val isFormValid by derivedStateOf {
        email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() &&
                (isParticular || (isProfesional && cif.isNotBlank() && validarCIF(cif))) &&
                password == confirmPassword && validarContrasena(password)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "LOCALPROS - Registro de Usuarios", style = MaterialTheme.typography.titleSmall)
        }

        Divider(modifier = Modifier.fillMaxWidth().padding(16.dp).size(4.dp))

        Text(text = "Foto de Pelfil (opcional)", style = MaterialTheme.typography.bodyLarge)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(240.dp)
                .background(Color.LightGray)
                .border(1.dp, Color.Gray)
                .clickable { showImagePicker = true },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Añadir foto de perfil",
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Campos de texto para nombre completo, correo y contraseñas
        TextFieldPersonalizado(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Nombre Completo",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default
        )
        TextFieldPersonalizado(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default
        )
        TextFieldPersonalizado(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default
        )
        TextFieldPersonalizado(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar Contraseña",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default
        )
        TextFieldPersonalizado(
            value = telefono,
            onValueChange = { telefono = it },
            label = "Número de teléfono (opcional)",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )

        // Datos adicionales para profesional (solo si es profesional)
        if (isProfesional) {
            TextFieldPersonalizado(
                value = cif,
                onValueChange = { cif = it },
                label = "CIF de la Empresa (obligatorio para Profesionales)",
                modifier = Modifier.fillMaxWidth(),
                inputType = KeyboardOptions.Default
            )

            // Campo de texto para buscar ubicación (obligatorio para profesionales)
            IndicadorUbicacionYRadio(
                ubicacionInicial = ubicacion,
                onUbicacionSeleccionada = { nuevaUbicacion ->
                    ubicacion = nuevaUbicacion
                },
                geoApiContext = geoApiContext,
                esProfesional = isProfesional,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isParticular, onCheckedChange = { isParticular = it })
                Text("Particular", modifier = Modifier.padding(start = 8.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isProfesional, onCheckedChange = { isProfesional = it })
                Text("Profesional", modifier = Modifier.padding(start = 8.dp))
            }
        }

        if (!isParticular && !isProfesional) {
            errorMessage = "Debe seleccionar al menos un rol."
        } else {
            errorMessage = ""
        }

        // Lanzador de actividad para seleccionar la imagen
        if (showImagePicker) {
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                selectedImageUri = uri
            }
            LaunchedEffect(Unit) {
                launcher.launch("image/*")
                showImagePicker = false
            }
        }

        BotonPersonalizado(
            text = "Registrarse",
            onClick = {
                if (isFormValid) {
                    val rolesSeleccionados = mutableSetOf<Rol>()
                    if (isParticular) rolesSeleccionados.add(Rol.PARTICULAR)
                    if (isProfesional) rolesSeleccionados.add(Rol.PROFESIONAL)
                    autenticacionViewModel.registrarUsuario(
                        email,
                        password,
                        fullName,
                        rolesSeleccionados,
                        ubicacion,
                        if (isProfesional) cif else null
                    )
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        when (authState) {
            is AutenticacionViewModel.EstadoAutenticacion.Cargando -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is AutenticacionViewModel.EstadoAutenticacion.Error -> {
                Text(
                    text = (authState as AutenticacionViewModel.EstadoAutenticacion.Error).mensaje,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is AutenticacionViewModel.EstadoAutenticacion.Exitosa -> {
                LaunchedEffect(Unit) {
                    navController.navigate(PantallasApp.IniciarSesion.ruta)
                }
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate(PantallasApp.IniciarSesion.ruta) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Volver a 'Inicio Sesión'")
        }
    }
}
