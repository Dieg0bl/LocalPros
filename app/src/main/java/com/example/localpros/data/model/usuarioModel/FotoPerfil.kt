package com.example.localpros.data.model.usuarioModel

data class FotoPerfil(
    val url: String,
    val formato: String,
    val tamañoMaximo: Int = 2 * 1024 * 1024
)