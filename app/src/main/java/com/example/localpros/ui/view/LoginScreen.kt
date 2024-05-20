package com.example.localpros.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.localpros.data.model.UserPreferences
import com.example.localpros.ui.navigation.AppScreens
import com.example.localpros.ui.viewModel.AuthViewModel
import com.example.localpros.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    userPreferences: UserPreferences,
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo y Título
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Logo")
        Text(text = "Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)

        // Campos de texto
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Recordarme
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Text(text = "Recordarme")
        }

        // Botón de Iniciar Sesión
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    authViewModel.login(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión")
        }

        // Indicador de progreso
        when (authState) {
            is AuthViewModel.AuthState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is AuthViewModel.AuthState.Error -> {
                Text(
                    text = (authState as AuthViewModel.AuthState.Error).message,
                    color = Color.Red
                )
            }
            is AuthViewModel.AuthState.Success -> {
                LaunchedEffect(Unit) {
                    val role = (authState as AuthViewModel.AuthState.Success).role
                    userPreferences.userRole = role
                    onLoginSuccess()
                }
            }
            else -> {

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Enlace para olvidar contraseña
        TextButton(
            onClick = {
                navController.navigate(AppScreens.ResetPasswordScreen.route)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Olvidé mi contraseña")
        }

        // Enlace para registrarse
        TextButton(
            onClick = {
                navController.navigate(AppScreens.RegisterScreen.route)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Registrarse")
        }




        // Button(onClick = { /*todo: Implementar inicio de sesión con Google */ }) {
        //     Text("Iniciar sesión con Google")
        // }
    }
}
