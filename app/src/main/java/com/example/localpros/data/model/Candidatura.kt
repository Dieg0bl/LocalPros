package com.example.localpros.data.model

data class Candidatura(
    val id: String,
    val profesional: Profesional,
    val oferta: Oferta,
    val propuestaEconomica: Double,
    val cartaPresentacion: String,
    val estado: EstadoCandidatura
)