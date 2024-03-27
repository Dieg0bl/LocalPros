package com.example.localpros.data.model

data class Particular(
    val id: String,
    val nombre: String,
    val contacto: String,
    val fotoPerfil: String,
    val historialOfertas: List<Oferta>,
    val reseñasRecibidas: List<Resena>
)