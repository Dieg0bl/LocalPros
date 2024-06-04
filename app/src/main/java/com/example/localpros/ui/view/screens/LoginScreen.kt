package com.example.localpros.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.localpros.data.model.PreferenciasYAjustesUsuario
import com.example.localpros.ui.navigation.PantallasApp
import com.example.localpros.ui.view.composables.BotonPersonalizado
import com.example.localpros.ui.view.composables.TextFieldPersonalizado
import com.example.localpros.ui.viewModel.AutenticacionViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    autenticacionViewModel: AutenticacionViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    userPreferences: PreferenciasYAjustesUsuario
)  {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by autenticacionViewModel.estadoAutenticacion.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box( modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(1.dp) ){
            Text(text = "LOCALPROS - Registro de Usuarios", style = MaterialTheme.typography.titleSmall,)
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        TextFieldPersonalizado(
            value = email,
            onValueChange = { email = it },
            label = "Correo electrónico",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        TextFieldPersonalizado(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        BotonPersonalizado(
            text = "Iniciar Sesión",
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    autenticacionViewModel.iniciarSesion(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        when (authState) {
            is AutenticacionViewModel.EstadoAutenticacion.Cargando -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is AutenticacionViewModel.EstadoAutenticacion.Error -> {
                Text(
                    text = (authState as AutenticacionViewModel.EstadoAutenticacion.Error).mensaje,
                    color = Color.Red
                )
            }
            is AutenticacionViewModel.EstadoAutenticacion.Exitosa -> {
                LaunchedEffect(Unit) {
                    onLoginSuccess()
                }
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                navController.navigate(PantallasApp.ResetearContrasena.ruta)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Olvidé mi contraseña")
        }

        TextButton(
            onClick = {
                navController.navigate(PantallasApp.Registro.ruta)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Registrarse")
        }
    }
}
