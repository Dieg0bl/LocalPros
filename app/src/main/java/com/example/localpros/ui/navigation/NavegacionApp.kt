package com.example.localpros.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.localpros.data.model.PreferenciasYAjustesUsuario
import com.example.localpros.data.model.enums.Rol
import com.example.localpros.ui.view.screens.*
import com.google.maps.GeoApiContext

@Composable
fun NavegacionApp(navController: NavHostController, userPreferences: PreferenciasYAjustesUsuario, geoApiContext: GeoApiContext) {
    NavHost(navController = navController, startDestination = PantallasApp.IniciarSesion.ruta) {
        composable(PantallasApp.IniciarSesion.ruta) {
            LoginScreen(navController = navController, onLoginSuccess = {
                navController.navigate(PantallasApp.SeleccionRol.ruta)
            }, userPreferences = userPreferences)
        }
        composable(PantallasApp.Registro.ruta) {
            RegistroScreen(navController = navController, geoApiContext = geoApiContext)
        }
        composable(PantallasApp.ResetearContrasena.ruta) {
            ResetearContrasenaScreen(navController = navController)
        }
        composable(PantallasApp.SeleccionRol.ruta) {
            SeleccionRolScreen(navController = navController, preferenciasUsuario = userPreferences)
        }
        composable(
            "${PantallasApp.PantallaPrincipalUsuario.ruta}/{rolUsuario}",
            arguments = listOf(navArgument("rolUsuario") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val rolUsuario = backStackEntry.arguments?.getString("rolUsuario")?.let { Rol.valueOf(it) }
            val idUsuario = userPreferences.idUsuario ?: ""
            if (rolUsuario != null) {
                when (rolUsuario) {
                    Rol.PARTICULAR -> ParticularScreen(idUsuario = idUsuario, navController = navController, geoApiContext = geoApiContext)
                    Rol.PROFESIONAL -> ProfesionalScreen(idUsuario = idUsuario, navController = navController, geoApiContext = geoApiContext)
                    else -> Text("Error: Rol no válido")
                }
            } else {
                Text("Error: No se pudo determinar el rol del usuario.")
            }
        }
    }
}


