package com.example.localpros.ui.view.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorText(localizedMessage: String?) {
    localizedMessage?.let {
        Text(text = it, color = Color.Red)
    }
}