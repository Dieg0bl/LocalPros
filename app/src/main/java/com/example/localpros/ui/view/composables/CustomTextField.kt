package com.example.localpros.ui.view.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    validator: (String) -> Boolean = { true },
    inputType: KeyboardType = KeyboardType.Text,
    errorText: String = "Invalid input"
) {
    val isError = remember { mutableStateOf(false) }
    val keyboardOptions = KeyboardOptions.Default.copy(keyboardType = inputType)

    TextField(
        value = value,
        onValueChange = {
            if (validator(it)) {
                isError.value = false
                onValueChange(it)
            } else {
                isError.value = true
            }
        },
        label = { Text(label) },
        modifier = modifier,
        isError = isError.value,
        keyboardOptions = keyboardOptions
    )

    if (isError.value) {
        Text(errorText, color = Color.Red)
    }
}
