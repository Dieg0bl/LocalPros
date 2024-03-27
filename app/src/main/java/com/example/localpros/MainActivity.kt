package com.example.localpros

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.localpros.ui.navigation.AppNavigation
import com.example.localpros.ui.navigation.AppScreens
import com.example.localpros.ui.theme.LocalProsTheme
import com.example.localpros.ui.view.RegisterScreen
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
                val navController = rememberNavController()
                val shouldKeepSignedIn = remember { sharedPreferences.getBoolean("keep_signed_in", false) }
                val firebaseAuth = FirebaseAuth.getInstance()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (shouldKeepSignedIn && (firebaseAuth.currentUser != null)) {
                        navController.navigate(AppScreens.MainScreen.route)
                    } else {
                        AppNavigation(navController)
                    }
                }
            }
        }
    }
}

