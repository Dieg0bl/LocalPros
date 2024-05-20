package com.example.localpros.data.model

data class Resena(
    val reseñaId: String = "",
    val autorId: String = "",
    val destinatarioId: String = "",
    val ofertaRelacionadaId: String = "",
    val contenidoTexto: String = "",
    val puntuacion: Int = 0
)