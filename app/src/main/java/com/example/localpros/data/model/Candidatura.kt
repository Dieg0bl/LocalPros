package com.example.localpros.data.model

data class Candidatura(
    val id: String = "",
    val profesional: Profesional,
    val oferta: Oferta,
    val propuestaEconomica: Double = 0.0,
    val cartaPresentacion: String = "",
    val estado: EstadoCandidatura = EstadoCandidatura.ABIERTA
)