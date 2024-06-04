package com.example.localpros.ui.view.composables

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import com.example.localpros.data.model.PosicionYRadio
import com.example.localpros.utils.buscarSugerencias
import com.example.localpros.utils.buscarUbicacion
import com.example.localpros.utils.obtenerDireccion
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.GeoApiContext
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun IndicadorUbicacionYRadio(
    ubicacionInicial: PosicionYRadio,
    onUbicacionSeleccionada: (PosicionYRadio) -> Unit,
    geoApiContext: GeoApiContext,
    esProfesional: Boolean,
    modifier: Modifier = Modifier
) {
    var textoBusqueda by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf(ubicacionInicial) }
    var direccion by remember { mutableStateOf("") }
    var radioKm by remember { mutableDoubleStateOf(ubicacionInicial.radioKm) }
    var mostrarCard by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(ubicacionInicial.latitud, ubicacionInicial.longitud),
            5f // Mapa inicial alejado
        )
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location? = if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else {
            null
        }

        val latLng = location?.let { LatLng(it.latitude, it.longitude) }
            ?: LatLng(40.416775, -3.703790) // Madrid lat/lng
        ubicacion = PosicionYRadio(latLng.latitude, latLng.longitude, radioKm)
        onUbicacionSeleccionada(ubicacion)
        direccion = obtenerDireccion(context, latLng)
        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 5f)
    }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = direccion,
            onValueChange = {},
            label = { Text("Ubicación") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { mostrarCard = true },
            singleLine = true,
            trailingIcon = {
                Icon(Icons.Default.LocationOn, contentDescription = "Ubicación")
            }
        )

        if (mostrarCard) {
            Dialog(
                onDismissRequest = { mostrarCard = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .wrapContentHeight()
                        .border(1.dp, Color.Blue, RoundedCornerShape(16.dp))
                        .background(Color(0xFFE0F7FA), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .background(Color(0xFFE0F7FA), RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { mostrarCard = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Cerrar")
                            }
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            var suggestions by remember { mutableStateOf(listOf<String>()) }

                            OutlinedTextField(
                                value = textoBusqueda,
                                onValueChange = {
                                    textoBusqueda = it
                                    coroutineScope.launch {
                                        suggestions = buscarSugerencias(it, geoApiContext)
                                    }
                                },
                                label = { Text("Buscar ubicación") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                singleLine = true,
                                trailingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Search
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        coroutineScope.launch {
                                            val nuevaUbicacion = buscarUbicacion(textoBusqueda, geoApiContext)
                                            nuevaUbicacion?.let {
                                                ubicacion = PosicionYRadio(it.latitude, it.longitude, radioKm)
                                                onUbicacionSeleccionada(ubicacion)
                                                direccion = obtenerDireccion(context, it)
                                                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 5f)
                                            }
                                        }
                                    }
                                )
                            )

                            DropdownMenu(
                                expanded = suggestions.isNotEmpty(),
                                onDismissRequest = { suggestions = listOf() }
                            ) {
                                suggestions.forEach { suggestion ->
                                    DropdownMenuItem(
                                        onClick = {
                                            textoBusqueda = suggestion
                                            coroutineScope.launch {
                                                val nuevaUbicacion = buscarUbicacion(suggestion, geoApiContext)
                                                nuevaUbicacion?.let {
                                                    ubicacion = PosicionYRadio(it.latitude, it.longitude, radioKm)
                                                    onUbicacionSeleccionada(ubicacion)
                                                    direccion = obtenerDireccion(context, it)
                                                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 5f)
                                                }
                                                suggestions = listOf()
                                            }
                                        },
                                        text = { Text(text = suggestion, fontSize = 16.sp) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .height(300.dp)
                                    .fillMaxWidth()
                                    .border(1.dp, Color.Gray)
                            ) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraPositionState,
                                    onMapClick = { latLng ->
                                        ubicacion = PosicionYRadio(latLng.latitude, latLng.longitude, radioKm)
                                        onUbicacionSeleccionada(ubicacion)
                                        direccion = obtenerDireccion(context, latLng)
                                        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 5f)
                                    }
                                ) {
                                    Marker(
                                        state = MarkerState(position = LatLng(ubicacion.latitud, ubicacion.longitud)),
                                        title = "Ubicación seleccionada"
                                    )
                                    if (esProfesional) {
                                        Circle(
                                            center = LatLng(ubicacion.latitud, ubicacion.longitud),
                                            radius = radioKm * 1000,
                                            strokeColor = Color.Yellow,
                                            strokeWidth = 4f,
                                            fillColor = Color.Yellow.copy(alpha = 0.2f)
                                        )
                                    }
                                }
                            }

                            if (esProfesional) {
                                Spacer(modifier = Modifier.height(16.dp))

                                Column {
                                    Text("Área de trabajo (km): ${radioKm.toInt()} km")

                                    Slider(
                                        value = radioKm.toFloat(),
                                        onValueChange = { newValue ->
                                            radioKm = newValue.roundToInt().toDouble()
                                            ubicacion = ubicacion.copy(radioKm = radioKm)
                                            onUbicacionSeleccionada(ubicacion)
                                        },
                                        valueRange = 0f..500f,
                                        steps = 500,
                                        colors = SliderDefaults.colors(
                                            thumbColor = Color.Blue,
                                            activeTrackColor = Color.Blue, // Fondo continuo azul
                                            inactiveTrackColor = Color.LightGray // Color de la pista inactiva
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { mostrarCard = false },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Confirmar Ubicación")
                            }
                        }
                    }
                }
            }
        }
    }
}



