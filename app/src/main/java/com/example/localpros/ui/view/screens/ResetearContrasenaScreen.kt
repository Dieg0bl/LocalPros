package com.example.localpros.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.example.localpros.ui.view.composables.BotonPersonalizado
import com.example.localpros.ui.view.composables.TextFieldPersonalizado


@Composable
fun ResetearContrasenaScreen(
    navController: NavHostController
){
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Recuperar Contraseña", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldPersonalizado(
            value = email,
            onValueChange = { newEmail -> email = newEmail },
            label = "Correo electrónico",
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardOptions.Default
        )

        Spacer(modifier = Modifier.height(16.dp))

        BotonPersonalizado(
            text = "Enviar",
            onClick = {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            message = "Se ha enviado un correo para restablecer tu contraseña."
                            isError = false
                        } else {
                            message = task.exception?.message ?: "Error desconocido"
                            isError = true
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (message.isNotEmpty()) {
            Text(text = message, color = if (isError) Color.Red else Color.Green)
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Volver al inicio de sesión")
        }
    }
}
