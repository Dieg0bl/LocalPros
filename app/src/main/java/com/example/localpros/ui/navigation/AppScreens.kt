package com.example.localpros.ui.navigation
sealed class AppScreens(val route: String) {
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object ResetPasswordScreen : AppScreens("reset_password_screen")
    object MainScreen : AppScreens("main_screen")

}
