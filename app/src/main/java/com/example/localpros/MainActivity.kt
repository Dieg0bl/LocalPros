package com.example.localpros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.localpros.data.model.PreferenciasYAjustesUsuario
import com.example.localpros.ui.navigation.NavegacionApp
import com.example.localpros.ui.navigation.PantallasApp
import com.example.localpros.ui.theme.LocalProsTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.GeoApiContext
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var preferenciasYAjustesUsuario: PreferenciasYAjustesUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalProsTheme {
                val navController = rememberNavController()
                val shouldKeepSignedIn = remember { preferenciasYAjustesUsuario.estadoCuenta }
                val firebaseAuth = remember { FirebaseAuth.getInstance() }
                val geoApiContext = remember { GeoApiContext.Builder().apiKey(BuildConfig.GOOGLE_MAPS_API_KEY).build() }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    if (shouldKeepSignedIn && firebaseAuth.currentUser != null) {
                        navController.navigate(PantallasApp.SeleccionRol.ruta) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        NavegacionApp(navController = navController, userPreferences = preferenciasYAjustesUsuario, geoApiContext = geoApiContext)
                    }
                }
            }
        }
    }
}