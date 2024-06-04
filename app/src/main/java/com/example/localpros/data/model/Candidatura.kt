package com.example.localpros.data.model

import com.example.localpros.data.model.enums.EstadoCandidatura
import org.threeten.bp.LocalDateTime


data class Candidatura(
    val id: String = "",
    val idEditor: String = "",
    val idOfertaPretendida: String,
    val propuestaEconomica: Double = 0.0,
    val cartaPresentacion: String = "",
    val estado: EstadoCandidatura = EstadoCandidatura.ABIERTA,
    val fechaCreacion: LocalDateTime = LocalDateTime.now(),
    val fechaActualizacion: LocalDateTime = LocalDateTime.now()
)
