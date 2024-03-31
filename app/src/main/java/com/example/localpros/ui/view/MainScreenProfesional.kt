package com.example.localpros.ui.view


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.localpros.data.model.Candidatura
import com.example.localpros.data.model.Profesional

@Composable
fun MainScreenProfesional(profesional: Profesional) {
    val candidaturas = profesional.historialCandidaturas
    Scaffold(
        topBar = { TopBar() }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            WelcomeSection(userName = profesional.nombre)
            ViewAvailableOffersButton()
            MyCandidaturesList(candidaturas)
            ManageProfileSection(profesional)
            ReviewsSection()
            Footer()
        }
    }
}


@Composable
fun ViewAvailableOffersButton() {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { /* TODO: Navegar a la pantalla para ver ofertas disponibles */ }
    ) {
        Text("Ver Ofertas Disponibles")
    }
}

@Composable
fun MyCandidaturesList(candidaturas: List<Candidatura>) {
    LazyColumn {
        items(candidaturas) { candidatura ->
            CandidatureItem(candidatura)
        }
    }
}

@Composable
fun CandidatureItem(candidatura: Candidatura) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Candidatura para: ${candidatura.oferta.descripcion}")
        Text(text = "Estado: ${candidatura.estado}")
    }
}

@Composable
fun ManageProfileSection(profesional: Profesional) {
    // TODO: Implementar lógica para gestionar el perfil y la disponibilidad del profesional
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Gestionar Perfil y Disponibilidad")
    }
}


