package com.example.localpros.data.model

data class Particular(
    override val id: String,
    override val nombre: String,
    override val contacto: String,
    override val fotoPerfil: String,
    val historialOfertas: List<Oferta>,
    val reseñasRecibidas: List<Resena>
) : User(id, nombre, contacto, fotoPerfil)