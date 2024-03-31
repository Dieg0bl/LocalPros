package com.example.localpros.data.model

data class Profesional(
    override val id: String,
    override val nombre: String,
    val contacto: String,
    override val fotoPerfil: String,
    val historialCandidaturas: List<Candidatura>,
    val reseñasRecibidas: List<Resena>
): User(id, nombre, contacto, fotoPerfil)