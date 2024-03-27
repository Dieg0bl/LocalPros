package com.example.localpros.data.model

data class Resena(
    val id: String,
    val autor: String,
    val destinatario: String,
    val texto: String,
    val puntuacion: Int,
    val ofertaRelacionada: Oferta
)