package com.example.localpros.ui.navigation
sealed class AppScreens(val route: String) {


    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object ResetPasswordScreen : AppScreens("reset_password_screen")
    object MainScreen : AppScreens("main_screen")
    object MainParticularScreen : AppScreens("main_particular_screen")
    object MainProfesionalScreen : AppScreens("main_profesional_screen")

}
