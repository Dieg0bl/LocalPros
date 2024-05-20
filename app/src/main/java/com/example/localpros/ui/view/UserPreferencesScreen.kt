package com.example.localpros.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.localpros.data.model.UserPreferences
import com.example.localpros.data.model.UserRole
import com.example.localpros.ui.viewModel.UserViewModel
import com.example.localpros.ui.view.composables.DatePicker
import com.example.localpros.ui.view.composables.LocationPicker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.GeoApiContext
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPreferencesScreen(
    userPreferences: UserPreferences,
    geoApiContext: GeoApiContext,
    viewModel: UserViewModel = viewModel()
) {
    var userRole by remember { mutableStateOf(UserRole.Particular) }
    var cif by remember { mutableStateOf("") }
    var notificationsNewOffers by remember { mutableStateOf(true) }
    var notificationsCandidatures by remember { mutableStateOf(true) }
    var notificationsMessages by remember { mutableStateOf(true) }
    var profileVisibility by remember { mutableStateOf(true) }
    var accountStatus by remember { mutableStateOf(true) }
    var contactInfoSharing by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("es") }
    var serviceCategories by remember { mutableStateOf(setOf<String>()) }
    var selectedLocation by remember { mutableStateOf(LatLng(40.416775, -3.703790)) } // Coordenadas iniciales de Madrid
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Preferencias del Usuario", style = MaterialTheme.typography.titleLarge)

        // Sección General
        Text("General", style = MaterialTheme.typography.titleMedium)
        LanguageSelection(language)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Notificaciones", style = MaterialTheme.typography.titleMedium)
        SwitchPreference("Nuevas ofertas disponibles en tu zona de trabajo", notificationsNewOffers) { notificationsNewOffers = it }
        SwitchPreference("Candidaturas recibidas para tus ofertas", notificationsCandidatures) { notificationsCandidatures = it }
        SwitchPreference("Mensajes y comunicaciones", notificationsMessages) { notificationsMessages = it }

        Spacer(modifier = Modifier.height(8.dp))

        // Sección Particular / Profesional
        Text("Particular / Profesional", style = MaterialTheme.typography.titleMedium)
        RadioButtonGroup(userRole) { userRole = it }

        if (userRole == UserRole.Profesional) {
            TextField(
                value = cif,
                onValueChange = { cif = it },
                label = { Text("CIF de la empresa") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Zona de trabajo", style = MaterialTheme.typography.titleMedium)
            LocationPicker(
                initialLocation = selectedLocation,
                onLocationSelected = { selectedLocation = it },
                geoApiContext = geoApiContext
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Disponibilidad", style = MaterialTheme.typography.titleMedium)
            DatePicker(
                selectedDate = selectedDate,
                onDateChange = { selectedDate = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sección Privacidad
        Text("Privacidad", style = MaterialTheme.typography.titleMedium)
        SwitchPreference("Visibilidad del perfil (público/privado)", profileVisibility) { profileVisibility = it }
        SwitchPreference("Desactivar / activar cuenta", accountStatus) { accountStatus = it }
        SwitchPreference("Compartir información de contacto solo con ofertas aceptadas", contactInfoSharing) { contactInfoSharing = it }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.updateUserPreferences(
                        userId = userPreferences.userId,
                        userRole = userRole,
                        cif = cif,
                        notificationsNewOffers = notificationsNewOffers,
                        notificationsCandidatures = notificationsCandidatures,
                        notificationsMessages = notificationsMessages,
                        profileVisibility = profileVisibility,
                        accountStatus = accountStatus,
                        contactInfoSharing = contactInfoSharing,
                        language = language,
                        serviceCategories = serviceCategories,
                        selectedLocation = selectedLocation,
                        selectedDate = selectedDate
                    ) { result ->
                        if (result.isSuccess) {
                            // Manejo del éxito
                        } else {
                            errorMessage = result.exceptionOrNull()?.message ?: "Error al actualizar las preferencias"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar preferencias")
        }

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelection(selectedLanguage: String) {
    TextField(
        value = selectedLanguage,
        onValueChange = {},
        label = { Text("Idioma") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
    )
}

@Composable
fun SwitchPreference(label: String, state: Boolean, onStateChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = state, onCheckedChange = onStateChange)
    }
}

@Composable
fun RadioButtonGroup(selectedRole: UserRole, onRoleSelected: (UserRole) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            RadioButton(
                selected = selectedRole == UserRole.Particular,
                onClick = { onRoleSelected(UserRole.Particular) }
            )
            Text("Particular", modifier = Modifier.weight(1f))
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            RadioButton(
                selected = selectedRole == UserRole.Profesional,
                onClick = { onRoleSelected(UserRole.Profesional) }
            )
            Text("Profesional", modifier = Modifier.weight(1f))
        }
    }
}
