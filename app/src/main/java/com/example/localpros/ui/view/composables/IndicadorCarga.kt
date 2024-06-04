package com.example.localpros.ui.view.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun IndicadorCarga() {
    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
}
