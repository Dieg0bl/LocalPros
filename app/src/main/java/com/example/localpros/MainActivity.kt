// MainActivity.kt
package com.example.localpros

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.localpros.ui.navigation.AppNavigation
import com.example.localpros.ui.navigation.AppScreens
import com.example.localpros.ui.theme.LocalProsTheme
import com.example.localpros.ui.viewModel.AuthViewModel
import com.example.localpros.ui.viewModel.UserViewModel
import com.example.localpros.data.model.UserPreferences
import com.example.localpros.data.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalProsTheme {
                MainContent(sharedPreferences)
            }
        }
    }
}

@Composable
fun MainContent(sharedPreferences: SharedPreferences) {
    val navController = rememberNavController()
    val shouldKeepSignedIn = remember { sharedPreferences.getBoolean("keep_signed_in", false) }
    val firebaseAuth = remember { FirebaseAuth.getInstance() }
    val userPreferences = remember { UserPreferences(sharedPreferences) }
    val authViewModel: AuthViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (shouldKeepSignedIn && firebaseAuth.currentUser != null) {
            val startDestination = AppScreens.MainUserScreen.createRoute(UserRole.valueOf(
                userPreferences.userRole.toString()
            ))
            LaunchedEffect(Unit) {
                navController.navigate(startDestination) {
                    popUpTo(0) { inclusive = true }
                }
            }
        } else {
            AppNavigation(navController, userPreferences, authViewModel, userViewModel)
        }
    }
}
