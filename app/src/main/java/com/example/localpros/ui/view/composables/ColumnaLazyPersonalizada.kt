
package com.example.localpros.ui.view.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.localpros.data.model.enums.Rol

@Composable
fun ColumnaLazyPersonalizada(
    elementos: List<Rol>,
    contenidoElemento: @Composable (Rol) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp)
    ) {
        items(elementos) { elemento ->
            contenidoElemento(elemento)
        }
    }
}