package com.example.localpros.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.localpros.data.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, onRegistrationSuccess: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val passwordRegex = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&+=]).{6,}$")

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Registro", style = MaterialTheme.typography.bodyMedium)

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número de Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Acepto los términos y condiciones")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(
                onClick = {
                    when {
                        fullName.isBlank() -> errorMessage = "El nombre completo no puede estar vacío"
                        username.isBlank() -> errorMessage = "El nombre de usuario no puede estar vacío"
                        email.isBlank() -> errorMessage = "El correo electrónico no puede estar vacío"
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> errorMessage = "El correo electrónico no es válido"
                        phoneNumber.isBlank() -> errorMessage = "El número de teléfono no puede estar vacío"
                        address.isBlank() -> errorMessage = "La dirección no puede estar vacía"
                        password.isBlank() -> errorMessage = "La contraseña no puede estar vacía"
                        !passwordRegex.matcher(password).matches() -> errorMessage = "La contraseña debe tener al menos 6 caracteres, incluyendo un símbolo y un número"
                        password != confirmPassword -> errorMessage = "Las contraseñas no coinciden"
                        !acceptTerms -> errorMessage = "Debes aceptar los términos y condiciones"
                        else -> {
                            isLoading = true
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val userId = task.result?.user?.uid
                                        if (userId != null) {
                                            val userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId)
                                            val userRole = UserRole.Particular.name // Rol por defecto
                                            val userMap = mapOf(
                                                "nombreCompleto" to fullName,
                                                "nombreUsuario" to username,
                                                "correo" to email,
                                                "telefono" to phoneNumber,
                                                "direccion" to address,
                                                "rol" to userRole
                                            )
                                            userRef.setValue(userMap).addOnCompleteListener { roleTask ->
                                                isLoading = false
                                                if (roleTask.isSuccessful) {
                                                    onRegistrationSuccess()
                                                } else {
                                                    errorMessage = roleTask.exception?.message ?: "Error desconocido al asignar rol."
                                                }
                                            }
                                        } else {
                                            isLoading = false
                                            errorMessage = "Error al obtener el ID del usuario."
                                        }
                                    } else {
                                        isLoading = false
                                        errorMessage = task.exception?.message ?: "Error desconocido"
                                    }
                                }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Volver al inicio de sesión",
            modifier = Modifier.clickable {
                navController.navigate("login_screen") {
                    popUpTo("register_screen") { inclusive = true }
                }
            },
            style = MaterialTheme.typography.bodySmall
        )

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }
    }
}
