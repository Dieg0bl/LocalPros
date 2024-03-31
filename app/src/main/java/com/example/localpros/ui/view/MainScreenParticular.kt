package com.example.localpros.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import com.example.localpros.data.model.Oferta
import com.example.localpros.data.model.Particular


@Composable
fun MainScreenParticular(particular: Particular) {
    val offers = particular.historialOfertas
    Scaffold(
        topBar = { TopBar() }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            WelcomeSection(userName = particular.nombre)
            PublishNewOfferButton()
            MyOffersList(offers)
            SearchProfessionals()
            ReviewsSection()
            Footer()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text("LocalPros") },
        actions = {
            IconButton(onClick = { /* TODO: Implementar ajustes de cuenta */ }) {
            }
            IconButton(onClick = { /* TODO: Implementar ayuda */ }) {
            }
            IconButton(onClick = { /* TODO: Implementar cerrar sesión */ }) {
            }
        }
    )
}

@Composable
fun WelcomeSection(userName: String) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Bienvenido, $userName", style = MaterialTheme.typography.bodySmall)
        // TODO: Obtener y mostrar resumen de ofertas activas y candidaturas recibidas
        Text(text = "Tienes X ofertas activas y Y candidaturas recibidas")
    }
}

@Composable
fun PublishNewOfferButton() {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { /* TODO: Navegar al formulario de nueva oferta */ }
    ) {
        Text("Publicar Nueva Oferta")
    }
}

@Composable
fun MyOffersList(offers: List<Oferta>) {
    LazyColumn {
        items(offers) { oferta ->
            OfferItem(oferta)
        }
    }
}

@Composable
fun OfferItem(oferta: Oferta) {
    // TODO: Personalizar la visualización de cada oferta
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Oferta: ${oferta.descripcion}")
        Text(text = "Estado: ${oferta.estado}")
        // TODO: Agregar botones o acciones para editar, eliminar o ver detalles
    }
}

@Composable
fun SearchProfessionals() {
    var searchText by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar Profesionales") },
            modifier = Modifier.fillMaxWidth()
        )
        // TODO: Agregar filtros y lógica de búsqueda
    }
}

@Composable
fun ReviewsSection() {
    // TODO: Implementar lógica para mostrar y gestionar reseñas
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Reseñas y Valoraciones")
    }
}

@Composable
fun Footer() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Políticas de Privacidad", textAlign = TextAlign.Center)
        Text("Términos de Servicio", textAlign = TextAlign.Center)
        Text("Contacto", textAlign = TextAlign.Center)
        // TODO: Agregar acciones o enlaces
    }
}

