package com.example.localpros.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.localpros.ui.view.*

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        composable(AppScreens.LoginScreen.route) {
            LoginScreen(navController = navController, onLoginSuccess = {
                navController.navigate("main_screen") { popUpTo(AppScreens.LoginScreen.route) { inclusive = true } }
            })
        }
        composable(AppScreens.RegisterScreen.route) {
            RegisterScreen(navController = navController, onRegistrationSuccess = {
                navController.navigate("main_screen") { popUpTo(AppScreens.RegisterScreen.route) { inclusive = true } }
            })
        }
        composable(AppScreens.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController = navController)
        }

        composable(AppScreens.MainScreen.route) {
            MainScreen(navController = navController)
        }
    }
}
