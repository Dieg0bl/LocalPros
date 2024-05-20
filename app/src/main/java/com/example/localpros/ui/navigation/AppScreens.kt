package com.example.localpros.ui.navigation

import com.example.localpros.data.model.UserRole

sealed class AppScreens(val route: String) {
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object ResetPasswordScreen : AppScreens("reset_password_screen")
    object RoleSelectionScreen : AppScreens("role_selection_screen")
    object MainUserScreen : AppScreens("main_user_screen/{userRole}") {
        fun createRoute(userRole: UserRole) = "main_user_screen/${userRole.name}"
    }
}
