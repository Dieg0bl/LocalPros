package com.example.localpros.ui.view.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.GeocodingResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPicker(
    initialLocation: com.google.android.gms.maps.model.LatLng,
    onLocationSelected: (com.google.android.gms.maps.model.LatLng) -> Unit,
    modifier: Modifier = Modifier,
    showSearchBar: Boolean = true,
    geoApiContext: GeoApiContext
) {
    var searchText by remember { mutableStateOf("") }
    var triggerSearch by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(initialLocation, 14f, 0f, 0f)
    }

    Column(modifier = modifier) {
        if (showSearchBar) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Buscar ubicación") },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(onClick = { triggerSearch = !triggerSearch }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )
        }

        // Monitor changes in triggerSearch to perform the search
        LaunchedEffect(triggerSearch, searchText) {
            if (searchText.isNotBlank()) {
                val newLocation = searchLocation(searchText, geoApiContext)
                newLocation?.let {
                    cameraPositionState.position = CameraPosition(it, 14f, 0f, 0f)
                    onLocationSelected(it)
                }
            }
        }

        GoogleMap(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                onLocationSelected(latLng)
                cameraPositionState.position = CameraPosition(latLng, 14f, 0f, 0f)
            }
        )
    }
}

suspend fun searchLocation(query: String, context: GeoApiContext): com.google.android.gms.maps.model.LatLng? {
    return try {
        val results: Array<GeocodingResult> = GeocodingApi.geocode(context, query).await()
        if (results.isNotEmpty()) {
            com.google.android.gms.maps.model.LatLng(
                results[0].geometry.location.lat,
                results[0].geometry.location.lng
            )
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}