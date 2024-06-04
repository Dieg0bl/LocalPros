package com.example.localpros.ui.navigation

import com.example.localpros.data.model.enums.Rol

sealed class PantallasApp(val ruta: String) {
    object IniciarSesion : PantallasApp("iniciar_sesion")
    object Registro : PantallasApp("registro")
    object ResetearContrasena : PantallasApp("resetear_contrasena")
    object SeleccionRol : PantallasApp("seleccion_rol")
    object PantallaPrincipalUsuario : PantallasApp("pantalla_principal_usuario/{rolUsuario}") {
        fun createRoute(rolUsuario: Rol) = "pantalla_principal_usuario/${rolUsuario.name}"
    }
}
