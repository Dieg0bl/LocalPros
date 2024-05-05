package com.example.localpros.ui.view.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QualitySlider(
    range: ClosedFloatingPointRange<Float>,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    showValueLabel: Boolean = true
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = range,
        modifier = modifier
    )
    if (showValueLabel) {
        Text("Quality: ${value.toInt()}", style = MaterialTheme.typography.bodySmall)
    }
}