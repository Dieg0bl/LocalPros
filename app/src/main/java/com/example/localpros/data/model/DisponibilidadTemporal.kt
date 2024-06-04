package com.example.localpros.data.model


import com.example.localpros.data.model.enums.Disponibilidad
import org.threeten.bp.LocalDateTime


data class DisponibilidadTemporal(
    val id: String = "",
    val inicio: LocalDateTime = LocalDateTime.now(),
    val fin: LocalDateTime = LocalDateTime.now().plusHours(1),
    val estado: Disponibilidad = Disponibilidad.DISPONIBLE
)
