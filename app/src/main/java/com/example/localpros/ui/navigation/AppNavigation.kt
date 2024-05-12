package com.example.localpros.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.localpros.data.model.UserPreferences
import com.example.localpros.ui.view.*
import com.example.localpros.ui.viewModel.UserViewModel
import com.example.localpros.data.model.UserRole
import com.example.localpros.ui.view.RoleSelectionScreen


@Composable
fun AppNavigation(navController: NavHostController, userPreferences: UserPreferences, userViewModel: UserViewModel) {
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        composable(AppScreens.LoginScreen.route) {
            LoginScreen(navController = navController, userPreferences = userPreferences) {
                navController.navigate(AppScreens.RoleSelectionScreen.route)
            }
        }

        composable(AppScreens.RegisterScreen.route) {
            RegisterScreen(navController = navController) {
                navController.navigate(AppScreens.RoleSelectionScreen.route)
            }
        }

        composable(AppScreens.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController = navController)
        }

        composable(AppScreens.RoleSelectionScreen.route) {
            RoleSelectionScreen(navController = navController)
        }

        composable(
            "${AppScreens.MainUserScreen.route}/{userRole}",
            arguments = listOf(navArgument("userRole") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val userRole = backStackEntry.arguments?.getString("userRole")?.let { UserRole.valueOf(it) }
            if (userRole != null) {
                MainScreenUser(
                    userId = userPreferences.userId,
                    navController = navController,
                    userViewModel = userViewModel,
                    userRole = userRole
                )
            }
        }
    }
}
