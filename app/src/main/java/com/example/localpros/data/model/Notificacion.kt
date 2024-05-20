package com.example.localpros.data.model

data class Notificacion(
    val id: String = "",
    val usuarioId: String = "",
    val titulo: String = "",
    val mensaje: String = "",
    val leida: Boolean = false
)